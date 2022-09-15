package com.ciandt.ExceptionsMyMusic.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "TipoUsuario")
public class UserType {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "Id")
    private String Id;

    @Column(name = "Descricao")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "userType")
    private Set<User> users = new HashSet<>();
}