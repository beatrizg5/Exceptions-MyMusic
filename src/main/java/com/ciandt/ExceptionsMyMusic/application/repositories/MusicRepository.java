package com.ciandt.ExceptionsMyMusic.application.repositories;

import com.ciandt.ExceptionsMyMusic.domain.entities.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, String> {

    @Query(nativeQuery = true, value = "SELECT DISTINCT * FROM Musicas M INNER JOIN Artistas A ON A.id =  " +
            "M.ArtistaId WHERE Upper(A.nome) Like Upper ('%' || :name || '%') " +
            "OR Upper(M.nome) like Upper('%' || :name || '%') ORDER BY M.nome, A.nome")
    List<Music> findByArtistOrNameOfMusic(String name);
}