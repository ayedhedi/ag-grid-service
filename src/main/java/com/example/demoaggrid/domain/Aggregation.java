package com.example.demoaggrid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Aggregation {
    private String name;
    private int count;
    private double min;
    private double max;
    private double avg;
    private double sum;
}
