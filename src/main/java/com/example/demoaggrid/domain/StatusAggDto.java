package com.example.demoaggrid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusAggDto {
    private String status;
    private int trades;
}
