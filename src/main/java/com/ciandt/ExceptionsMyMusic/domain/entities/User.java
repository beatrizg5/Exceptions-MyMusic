package com.ciandt.ExceptionsMyMusic.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Usuarios")
public class User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "Id")
    private String id;

    @Column(name = "Nome")
    private String name;

    @ManyToOne
    @JoinColumn(name = "TipoUsuarioId")
    private UserType userType;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "PlaylistId", referencedColumnName = "Id")
    private Playlist playlist;
}