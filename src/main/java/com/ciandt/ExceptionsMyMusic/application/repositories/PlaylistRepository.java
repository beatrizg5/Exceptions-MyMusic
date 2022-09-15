package com.ciandt.ExceptionsMyMusic.application.repositories;

import com.ciandt.ExceptionsMyMusic.domain.entities.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PlaylistRepository extends JpaRepository<Playlist, String> {

    @Query(value = "SELECT M.id FROM Musicas M LEFT JOIN PlaylistMusicas A on M.id = A.MusicaId \n " +
            "WHERE A.MusicaId = :musicId and A.PlaylistId = :playlistId", nativeQuery = true)
    public String findMusicByPlaylists(String playlistId, String musicId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM PlaylistMusicas WHERE PlaylistId = :playlistId AND musicaId = :musicId", nativeQuery = true)
    public void removeMusicFromPlaylist(String playlistId, String musicId);
}