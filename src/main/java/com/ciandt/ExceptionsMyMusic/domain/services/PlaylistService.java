package com.ciandt.ExceptionsMyMusic.domain.services;

import com.ciandt.ExceptionsMyMusic.application.repositories.MusicRepository;
import com.ciandt.ExceptionsMyMusic.application.repositories.PlaylistRepository;
import com.ciandt.ExceptionsMyMusic.application.repositories.UserRepository;
import com.ciandt.ExceptionsMyMusic.domain.dto.MusicDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.UserDTO;
import com.ciandt.ExceptionsMyMusic.domain.entities.Music;
import com.ciandt.ExceptionsMyMusic.domain.entities.Playlist;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.DatabaseException;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class PlaylistService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(PlaylistService.class);

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void addMusicToPlaylist(String playlistID, MusicDTO musicDTO, TokenDataDTO tokenDataDTO) {
        tokenService.validateHeader(tokenDataDTO);

        if (playlistRepository.findById(playlistID).isEmpty()) {
            LOGGER.error("Playlist not found for search performed.");
            throw new ResourceNotFoundException("Playlist not found!");
        } else {
            if (musicRepository.findById(musicDTO.getId()).isEmpty()) {
                LOGGER.error("Music not found in the playlist for the search performed");
                throw new ResourceNotFoundException("Music not found!");
            } else {
                Playlist playlist = playlistRepository.findById(playlistID).get();
                Set<Music> musics = playlist.getMusics();

                for (Music music : musics) {
                    if (music.getId().equals(musicDTO.getId())) {
                        LOGGER.error("Music already exists in the playlist");
                        throw new ResourceNotFoundException("Existing song in the playlist!");
                    }
                }
                Music musicToAdd = musicRepository.findById(musicDTO.getId()).get();
                playlist.getMusics().add(musicToAdd);
                musicToAdd.getPlaylists().add(playlist);
                LOGGER.info("Music add to playlist!");
                playlistRepository.save(playlist);
            }
        }
    }

    @Transactional
    public void addMusicToPlaylistCheckingUserType(String playlistID, String userId, MusicDTO musicDTO, TokenDataDTO tokenDataDTO){
        tokenService.validateHeader(tokenDataDTO);
        UserDTO user = new UserDTO(userRepository.findById(userId).get());
        Playlist playlist = user.getPlaylist();

        if (!playlist.getId().equals(playlistID)){
            throw new ResourceNotFoundException("This playlist does not belongs to this user");
        }

        if (user.getUserType().getDescription().equals("Comum") && playlist.getMusics().size() >= 5){
            throw new ResourceNotFoundException("You have reached the maximum number of songs in your playlist. To add more songs, subscribe to the premium plan.");
        }else {
            addMusicToPlaylist(playlistID, musicDTO, tokenDataDTO);
        }
    }

    @Transactional
    public void removeMusicToPlaylist(String playlistID, String musicID, TokenDataDTO tokenDataDTO) {
        try {
            tokenService.validateHeader(tokenDataDTO);

            Optional<Playlist> playlist = playlistRepository.findById(playlistID);
            String music = playlistRepository.findMusicByPlaylists(playlistID, musicID);
            if (playlist.isEmpty()) {
                LOGGER.error("Playlist not found for search performed.");
                throw new ResourceNotFoundException("Playlist not found!");
            } else if (music == null) {
                LOGGER.error("Music not found in the playlist for the search performed");
                throw new ResourceNotFoundException("Music not found in playlist!");
            }
            LOGGER.info("Music removed to playlist!");
            playlistRepository.removeMusicFromPlaylist(playlistID, musicID);
        } catch (DatabaseException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}