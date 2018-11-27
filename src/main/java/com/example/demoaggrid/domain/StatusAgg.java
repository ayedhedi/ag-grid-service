package com.example.demoaggrid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StatusAgg {
    private String status;
    private List<Trade> trades;
}
