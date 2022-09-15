package com.ciandt.ExceptionsMyMusic.applications.controller;

import com.ciandt.ExceptionsMyMusic.application.controllers.PlaylistController;
import com.ciandt.ExceptionsMyMusic.domain.dto.*;
import com.ciandt.ExceptionsMyMusic.domain.entities.Artist;
import com.ciandt.ExceptionsMyMusic.domain.entities.Music;
import com.ciandt.ExceptionsMyMusic.domain.entities.Playlist;
import com.ciandt.ExceptionsMyMusic.domain.entities.User;
import com.ciandt.ExceptionsMyMusic.domain.services.PlaylistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaylistController.class)
@AutoConfigureMockMvc
public class PlaylistControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlaylistService service;

    private TokenDataDTO tokenDataDTO;

    private List<User> userList = new ArrayList<>();

    private PlaylistDTO playlistDTO;

    private Playlist playlist;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlaylistController playlistController;

    @Autowired
    private MockMvc mvc;

    @Test
    public void insertShouldReturnPlaylistCreated() throws Exception {
        Artist artist = new Artist("32844fdd-bb76-4c0a-9627-e34ddc9fd892", "The Beatles");
        Music music = new Music("03c86d1e-d3a0-462e-84a9-755cfc49aab8", "Reminiscing");
        MusicDTO musicDTO = new MusicDTO("hjhsksoisois", "hoiuaouaoau", artist);

        tokenDataDTO = new TokenDataDTO(new Data("devTest", "tokenValue"));

        List<MusicDTO> listMusic = new ArrayList<>();
        listMusic.add(musicDTO);
        DataDTO dataDTO = new DataDTO(listMusic);

        Mockito.doNothing().when(service).addMusicToPlaylist("a39926f4-6acb-4497-884f-d4e5296ef652", musicDTO, tokenDataDTO);

        String jsonBody = objectMapper.writeValueAsString(dataDTO);
        ResultActions result =
                mockMvc.perform(post("/playlists/a39926f4-6acb-4497-884f-d4e5296ef652/musicas")
                        .content(jsonBody)
                        .header("Id", "devTest")
                        .header("token", "tokenValue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated());
    }

    @Test
    public void findByPlaylistIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String nonExistingId = "jhsoisois";

        ResultActions result =
                mockMvc.perform(get("/{playlistId}/musicas", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByMusicIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String nonExistingId = "jhsoisois";

        ResultActions result =
                mockMvc.perform(get("/{playlistId}/musicas", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    void test_removerMusica() throws Exception {

        tokenDataDTO = new TokenDataDTO(new Data("devTest", "tokenValue"));

        String playlist = "92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed";
        String musica = "c96b8f6f-4049-4e6b-8687-82e29c05b735";

        service.removeMusicToPlaylist(playlist, musica, tokenDataDTO);

        ResultActions result =
                mockMvc.perform(delete("/playlists/92d8123f-e9f6-4806-8e0e-1c6a5d46f2ed/musicas/c96b8f6f-4049-4e6b-8687-82e29c05b73")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Id", "devTest")
                        .header("token", "tokenValue")
                        .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }
}