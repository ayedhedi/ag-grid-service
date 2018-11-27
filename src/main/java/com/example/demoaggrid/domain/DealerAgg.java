package com.example.demoaggrid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DealerAgg {
    private String dealer;
    private List<StatusAgg> status;
}
