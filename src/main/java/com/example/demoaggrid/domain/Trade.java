package com.example.demoaggrid.domain;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Builder
@ToString
public class Trade {
    private long tradeId;
    private String accountId;
    private String accountNumber;
    private String shareClassCode;
    private float quantity;
    private float valueUsrCcy;
    private String dealer;
    private float price;
    private String clientNumber;
    private String legalFund;
    private float valueEur;
    private String representative;
    private String blockedDate;
    private String ccy;
    private String subFund;
    private TradeStatus status;
    private String branch;
}
