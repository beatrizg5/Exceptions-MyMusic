package com.ciandt.ExceptionsMyMusic.domain.services;

import com.ciandt.ExceptionsMyMusic.application.repositories.MusicRepository;
import com.ciandt.ExceptionsMyMusic.application.repositories.PlaylistRepository;
import com.ciandt.ExceptionsMyMusic.domain.dto.Data;
import com.ciandt.ExceptionsMyMusic.domain.dto.MusicDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.entities.Artist;
import com.ciandt.ExceptionsMyMusic.domain.entities.Music;
import com.ciandt.ExceptionsMyMusic.domain.entities.Playlist;
import com.ciandt.ExceptionsMyMusic.domain.entities.User;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.NoContentException;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PlaylistServiceTests {

    @Mock
    private PlaylistRepository playlistRepository;
    @Mock
    private MusicRepository musicRepository;

    @Mock
    private MusicService musicServices;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private MusicService musicService;
    @InjectMocks
    private PlaylistService playlistService;

    private TokenDataDTO tokenDataDTO;
    private String existingIdPlaylist;
    private String nonExistingIdPlaylist;
    private String nonExistingIdMusic;
    private String nonExistingMusicDTO;
    private Playlist playlist;
    private MusicDTO musicDto;
    private Artist artist;
    private Set<Music> musicList = new HashSet<>();
    private Set<Playlist> playlistMusic = new HashSet<>();
    private List<User> userList = new ArrayList<>();
    private Set<Music> musicList2 = new HashSet<>();
    private Music musicReturned;

    private User user;

    @BeforeEach
    void setUp() throws Exception {
        tokenDataDTO = new TokenDataDTO(new Data("devTest", "tokenValue"));
        existingIdPlaylist = "f7179e0b-7a00-457e-bf08-db1f96eece5e";
        nonExistingIdPlaylist = "070d9496-ae38-4587-8ca6-2ed9b36fb198";
        nonExistingIdMusic = "iuhuiahiuhaiuhaiua";
        nonExistingMusicDTO = "32844fdd-bb76-4c0a-9627-e34ddc9fd892";
        playlist = new Playlist(existingIdPlaylist);
        artist = new Artist("32844fdd-bb76-4c0a-9627-e34ddc9fd892", "The Beatles", musicList2);
        musicDto = new MusicDTO("67f5976c-eb1e-404e-8220-2c2a8a23be47", "Hippy Hippy Shake", artist);
        musicReturned = new Music("67f5976c-eb1e-404e-8220-2c2a8a23be47", "Hippy Hippy Shake", artist, playlistMusic);
        Mockito.when(playlistRepository.getById(existingIdPlaylist)).thenReturn(playlist);
        Mockito.when(musicRepository.getById(musicDto.getId())).thenReturn(musicReturned);
        Mockito.when(tokenService.generateToken(tokenDataDTO)).thenReturn("tokenValue");
        Mockito.doNothing().when(tokenService).validateHeader(tokenDataDTO);
    }

    @Test
    public void shouldSaveMusicOnPlaylist() {

        musicDto.setId("uhahuhauahuahuahua");
        playlist = new Playlist(existingIdPlaylist, musicList, user);
        Mockito.when(playlistRepository.findById(existingIdPlaylist)).thenReturn(Optional.ofNullable(playlist));
        Mockito.when(musicRepository.findById(musicDto.getId())).thenReturn(Optional.ofNullable(musicReturned));
        Mockito.when(playlistRepository.findById(existingIdPlaylist)).thenReturn(Optional.ofNullable(playlist));
        musicList = playlist.getMusics();

        for (Music music : musicList) {
            if (music.getId().equals(musicDto.getId())) {
                throw new ResourceNotFoundException("Existing song in the playlist!");
            }
        }

        Mockito.when(musicRepository.findById(musicDto.getId())).thenReturn(Optional.ofNullable(musicReturned));
        playlist.getMusics().add(musicReturned);
        musicReturned.getPlaylists().add(playlist);

        Assertions.assertDoesNotThrow(() -> {
            playlistService.addMusicToPlaylist(existingIdPlaylist, musicDto, tokenDataDTO);
            playlistRepository.save(playlist);
        });
    }

    @Test
    public void shouldReturnNotFoundExceptionWhenNotExistsPlaylistId() {
        playlist = new Playlist(nonExistingIdPlaylist, musicList, user);
        Mockito.when(playlistRepository.findById(nonExistingIdPlaylist)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.addMusicToPlaylist(nonExistingIdPlaylist, musicDto, tokenDataDTO);
        });
    }

    @Test
    public void shouldReturnNotFoundExceptionWhenNotExistsMusicDtoId() {
        playlist = new Playlist(existingIdPlaylist, musicList, user);
        Mockito.when(playlistRepository.findById(existingIdPlaylist)).thenReturn(Optional.ofNullable(playlist));
        Mockito.when(musicRepository.findById(musicDto.getId()).isEmpty()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.addMusicToPlaylist(existingIdPlaylist, musicDto, tokenDataDTO);
        });
    }

    @Test
    public void ShouldReturnMusicWhenAlreadyExistExceptionWhenMusicIsAlreadyInPlaylist() {
        playlist.getMusics().add(musicReturned);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            playlistService.addMusicToPlaylist(existingIdPlaylist, musicDto, tokenDataDTO);
        });
    }

    @Test
    void ThrowsEmptyListException_WhenFilterIs_NotFound() {
        Assertions.assertThrows(NoContentException.class, () -> musicService.findByArtistOrMusic("asdsadasdasdasdasd", tokenDataDTO));
    }

    Artist art1 = new Artist("909090xx", "The Beatles");
    Music mus1 = new Music("808080xx", "Here Comes the Sun", art1);
    Artist art2 = new Artist("707070xx", "Michael Jackson");
    Music mus2 = new Music("606060xx", "Billie Jean", art2);

    @Test
    public void shouldThrowAnExceptionWhenRemovingTheMusicIfItCanNotFindThePlaylist() throws Exception {
        String playlistId = "Playlist ID not found!";

        when(playlistRepository.findById(playlistId)).thenReturn(null);

        try {
            playlistRepository.removeMusicFromPlaylist(playlistId, mus1.getId());
        } catch (Throwable e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Playlist not found!", e.getMessage());
        }
    }

    @Test
    public void shouldThrowAnExceptionWhenRemovingTheSongIfItCanNotFindTheSongInThePlaylist() throws Exception {
        String playlistID = "a39926f4-6acb-4497-884f-d4e5296ef652";
        String musicID = null;

        try {
            playlistRepository.removeMusicFromPlaylist(playlistID, musicID);
        } catch (Throwable e) {
            assertEquals(ResourceNotFoundException.class, e.getClass());
            assertEquals("Music not found in playlist!", e.getMessage());
        }
    }

    @Test
    public void removeMusicFromPlaylist() {
        String musicId = "03c86d1e-d3a0-462e-84a9-755cfc49aab8";

        String playlist = "a39926f4-6acb-4497-884f-d4e5296ef652";

        List<MusicDTO> musicDTO = musicServices.findByArtistOrMusic(musicId, tokenDataDTO);

        String musica = "c96b8f6f-4049-4e6b-8687-82e29c05b735";
        Mockito.when(playlistRepository.findMusicByPlaylists(playlist, musica)).thenReturn(String.valueOf(Optional.of(new Playlist())));
        Mockito.when(playlistRepository.findById(playlist)).thenReturn(Optional.of(new Playlist(playlist)));
        Mockito.when(musicServices.findByArtistOrMusic("Here Comes the Sun", tokenDataDTO)).thenReturn(musicDTO);
        Playlist playlistMusica = new Playlist(playlist);
        playlistService.removeMusicToPlaylist(playlist, musica, tokenDataDTO);
        Assertions.assertNotEquals(playlistMusica, playlistRepository.findMusicByPlaylists(playlist, musicId));
    }
}