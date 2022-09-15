package com.ciandt.ExceptionsMyMusic.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Data {
    private String name;
    private String token;

    public Data(String name) {
        this.name = name;
    }
}