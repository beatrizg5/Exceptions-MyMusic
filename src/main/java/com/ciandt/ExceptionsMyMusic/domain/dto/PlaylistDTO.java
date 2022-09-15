package com.ciandt.ExceptionsMyMusic.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlaylistDTO {
    private String id;

    public <E> PlaylistDTO(String id, ArrayList<E> es) {
    }
}