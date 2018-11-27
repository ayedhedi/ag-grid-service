package com.example.demoaggrid.service;

import com.example.demoaggrid.domain.DealerAgg;
import com.example.demoaggrid.domain.StatusAgg;
import com.example.demoaggrid.domain.Trade;
import com.example.demoaggrid.domain.TradeStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    private final TradeGenerator tradeGenerator;

    private final List<Trade> trades = new ArrayList<>();
    private Map<String, DealerAgg>  aggDealers = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("Start Generations ");
        for(int i=0;i<20000;i++) {
            trades.add(tradeGenerator.generate());
        }
        List<String> dealers = trades.stream().map(Trade::getDealer).distinct().collect(Collectors.toList());
        List<TradeStatus> status = trades.stream().map(Trade::getStatus).distinct().collect(Collectors.toList());

        dealers.forEach(dealer -> {
            DealerAgg dg = new DealerAgg(dealer, new ArrayList<>());
            status.forEach(s -> {
                StatusAgg sa = new StatusAgg(s.toString(),
                        trades.stream()
                                .filter(t -> Objects.equals(t.getDealer(), dealer))
                                .filter(t -> Objects.equals(t.getStatus(), s))
                                .collect(Collectors.toList())
                        );
                dg.getStatus().add(sa);
            });
            aggDealers.put(dealer, dg);
        });
        log.info("End Generations ");
    }

    public List<Trade> findByPage(final int from, int to) {
        return trades.subList(from, to);
    }
}
