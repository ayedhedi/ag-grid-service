package com.example.demoaggrid.service;

import com.example.demoaggrid.domain.Trade;
import com.example.demoaggrid.domain.TradeStatus;
import lombok.AllArgsConstructor;
import org.jfairy.Fairy;
import org.jfairy.producer.person.Person;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TradeGenerator {

    public static final DecimalFormat df = new DecimalFormat("#.0000");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private List<String> legalFunds = new ArrayList<>();
    private List<String> dealers = new ArrayList<>();
    private List<String> branches = new ArrayList<>();
    private List<String> reps = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();
    private Map<String, List<String>> subFunds = new HashMap<>();
    private Map<String, List<String>> shareClasses = new HashMap<>();

    private final Fairy fairy = Fairy.create();

    @PostConstruct
    public void init(){
        generateData();
    }

    public Trade generate(){
        final Client client = clients.get((int)(Math.random()*clients.size()));
        final String lf = legalFunds.get((int)(Math.random()*legalFunds.size()));
        final String sf = subFunds.get(lf).get((int)(Math.random()*subFunds.get(lf).size()));
        final String sc = shareClasses.get(sf).get((int)(Math.random()*shareClasses.get(sf).size()));

        return Trade.builder()
                .tradeId((long)(Math.random()*100000000))
                .accountId(client.accountId)
                .accountNumber(client.accountNum)
                .blockedDate((Math.random() > 0.5 ? sdf.format(fairy.person().dateOfBirth().toDate()) : null))
                .branch(branches.get((int)(Math.random()*10)))
                .ccy(getCcy())
                .clientNumber(client.client)
                .dealer(dealers.get((int)(Math.random()*dealers.size())))
                .legalFund(lf)
                .price(Float.valueOf(df.format(Math.random()*1000).replace(',', '.')))
                .quantity(Float.valueOf(df.format(Math.random()*100).replace(',', '.')))
                .representative(reps.get((int)(Math.random()*reps.size())))
                .shareClassCode(sc)
                .status(TradeStatus.values()[(int)(Math.random()*TradeStatus.values().length)])
                .subFund(sf)
                .valueEur(Float.valueOf(df.format(Math.random()*100000).replace(',', '.')))
                .valueUsrCcy(Float.valueOf(df.format(Math.random()*100000).replace(',', '.')))
                .build();
    }

    private String getCcy() {
        return Arrays.asList("EUR", "USD", "CAD", "GBP", "JPY").get((int)(Math.random()*5));
    }

    private void generateData() {
        for(int i=0;i<1000;i++){
            clients.add(new Client(
                    fairy.person().firstName(),
                    fairy.person().nationalIdentificationNumber(),
                    fairy.person().nationalIdentityCardNumber()));
        }
        for(int i=0;i<100;i++){
            dealers.add(fairy.person().firstName());
        }
        for(int i=0;i<10;i++){
            branches.add(fairy.person().firstName());
        }
        for(int i=0;i<50;i++){
            reps.add(fairy.person().firstName());
        }
        for(int i=0;i<1000;i++) {
            final String f = fairy.person().getCompany().name();
            legalFunds.add(f);
            final List<String> sf = new ArrayList<>();
            subFunds.put(f, sf);
            int nbSf = (int)(Math.random()*3) + 1;
            while(sf.size()<nbSf) {
                final String sub = fairy.person().getCompany().name();
                sf.add(sub);
                final List<String> ss = new ArrayList<>();
                shareClasses.put(sub, ss);
                int nbSs = (int)(Math.random()*3) + 1;
                while (ss.size()<nbSs) {
                    final String sha = fairy.person().getCompany().name();
                    ss.add(sha);
                }
            }
        }
    }

    @AllArgsConstructor
    private class Client {
        private String client;
        private String accountId;
        private String accountNum;
    }
}
