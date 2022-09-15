package com.ciandt.ExceptionsMyMusic.domain.dto;

import com.ciandt.ExceptionsMyMusic.domain.entities.Playlist;
import com.ciandt.ExceptionsMyMusic.domain.entities.User;
import com.ciandt.ExceptionsMyMusic.domain.entities.UserType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String id;
    private String name;
    private Playlist playlist;
    private UserType userType;

    public UserDTO(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.playlist = entity.getPlaylist();
        this.userType = entity.getUserType();
    }
}