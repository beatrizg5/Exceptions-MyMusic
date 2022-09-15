package com.ciandt.ExceptionsMyMusic.domain.dto;

import com.ciandt.ExceptionsMyMusic.domain.entities.Artist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistDTO {
    private String id;
    private String name;

    public ArtistDTO(Artist entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}