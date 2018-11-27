package com.example.demoaggrid.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.demoaggrid.domain.*;
import com.example.demoaggrid.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin("*")
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/trade/{id}")
    public Trade getTrade(
            @PathVariable final long id
    ) throws Exception {
        Optional<Trade> op = tradeService.getTrades()
                .stream().filter(t -> t.getTradeId() == id).findFirst();
        if (op.isPresent()) {
            return op.get();
        }else {
            throw new Exception("Not Found");
        }
    }

    @GetMapping("/trades")
    public List<Trade> getTrades(
            @RequestParam(required = false) int from,
            @RequestParam(required = false) int to,
            @RequestParam(required = false) final String branch,
            @RequestParam(required = false) final String subFund,
            @RequestParam(required = false) final String status,
            @RequestParam(required = false) final String ccy,
            @RequestParam(required = false) final String representative,
            @RequestParam(required = false) final String legalFund,
            @RequestParam(required = false) final String clientNumber,
            @RequestParam(required = false) final String dealer,
            @RequestParam(required = false) final String shareClassCode,
            @RequestParam(required = false) final String accountNumber,
            @RequestParam(required = false) final String accountId
    ){
        //apply filters
        List<Trade> trades = filters(tradeService.getTrades(), branch, subFund, status, ccy, representative, legalFund,
                clientNumber, dealer, shareClassCode, accountNumber, accountId);

        if (to >= trades.size()) {
            to = trades.size();
            if (from > to) {
                from = to - 1;
            }
        }

        return trades.subList(from, to);
    }

    @GetMapping("/trades/size")
    public int getTradesSize(
            @RequestParam(required = false) final String branch,
            @RequestParam(required = false) final String subFund,
            @RequestParam(required = false) final String status,
            @RequestParam(required = false) final String ccy,
            @RequestParam(required = false) final String representative,
            @RequestParam(required = false) final String legalFund,
            @RequestParam(required = false) final String clientNumber,
            @RequestParam(required = false) final String dealer,
            @RequestParam(required = false) final String shareClassCode,
            @RequestParam(required = false) final String accountNumber,
            @RequestParam(required = false) final String accountId
    ){
        //apply filters
        return filters(tradeService.getTrades(), branch, subFund, status, ccy, representative, legalFund,
                clientNumber, dealer, shareClassCode, accountNumber, accountId)
                .size();
    }


    @GetMapping("/aggregation/{entity}")
    public List<Aggregation> getAggregation(
            @PathVariable final String entity,
            @RequestParam(required = false) final String branch,
            @RequestParam(required = false) final String subFund,
            @RequestParam(required = false) final String status,
            @RequestParam(required = false) final String ccy,
            @RequestParam(required = false) final String representative,
            @RequestParam(required = false) final String legalFund,
            @RequestParam(required = false) final String clientNumber,
            @RequestParam(required = false) final String dealer,
            @RequestParam(required = false) final String shareClassCode,
            @RequestParam(required = false) final String accountNumber,
            @RequestParam(required = false) final String accountId
    ) throws Exception {
        //apply filters
        List<Trade> trades = filters(tradeService.getTrades(), branch, subFund, status, ccy, representative, legalFund,
                clientNumber, dealer, shareClassCode, accountNumber, accountId);

        Map<String, List<Trade>> map;
        switch (entity) {
            case "branch":
                 map = trades.stream().collect(Collectors.groupingBy(Trade::getBranch));
                 break;
            case "subFund":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getSubFund));
                break;
            case "status":
                map = trades.stream().collect(Collectors.groupingBy(o -> o.getStatus().name()));
                break;
            case "ccy":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getCcy));
                break;
            case "representative":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getRepresentative));
                break;
            case "legalFund":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getLegalFund));
                break;
            case "clientNumber":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getClientNumber));
                break;
            case "dealer":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getDealer));
                break;
            case "shareClassCode":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getShareClassCode));
                break;
            case "accountNumber":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getAccountNumber));
                break;
            case "accountId":
                map = trades.stream().collect(Collectors.groupingBy(Trade::getAccountId));
                break;
            default:
                throw new Exception("Invalid Aggregation "+entity);
        }

        return map.keySet().stream().map(k -> {
            List<Trade> ts = map.get(k);
            double sum = 0, min = Float.MAX_VALUE, max = 0, avr = 0;
            for(Trade t:ts) {
                sum += t.getValueUsrCcy();
                min = min > t.getValueUsrCcy() ? t.getValueUsrCcy() : min;
                max = max < t.getValueUsrCcy() ? t.getValueUsrCcy() : max;
            }
            avr = sum / ts.size();
            return new Aggregation(k, ts.size(), min, max, avr, sum);
        }).collect(Collectors.toList());
    }


    private List<Trade> filters(
            final List<Trade> trades,
            final String branch,
            final String subFund,
            final String status,
            final String ccy,
            final String representative,
            final String legalFund,
            final String clientNumber,
            final String dealer,
            final String shareClassCode,
            final String accountNumber,
            final String accountId
    ) {
        return trades.stream()
                .filter(trade -> branch == null || branch.equals(trade.getBranch()))
                .filter(trade -> subFund == null || subFund.equals(trade.getSubFund()))
                .filter(trade -> status == null || status.equalsIgnoreCase(trade.getStatus().name()))
                .filter(trade -> ccy == null || ccy.equals(trade.getCcy()))
                .filter(trade -> representative == null || representative.equals(trade.getRepresentative()))
                .filter(trade -> legalFund == null || legalFund.equals(trade.getLegalFund()))
                .filter(trade -> clientNumber == null || clientNumber.equals(trade.getClientNumber()))
                .filter(trade -> dealer == null || dealer.equals(trade.getDealer()))
                .filter(trade -> shareClassCode == null || shareClassCode.equals(trade.getShareClassCode()))
                .filter(trade -> accountNumber == null || accountNumber.equals(trade.getAccountNumber()))
                .filter(trade -> accountId == null || accountId.equals(trade.getAccountId()))
                .collect(Collectors.toList());
    }

}
