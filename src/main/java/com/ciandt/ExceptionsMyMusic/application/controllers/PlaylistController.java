package com.ciandt.ExceptionsMyMusic.application.controllers;

import com.ciandt.ExceptionsMyMusic.domain.dto.Data;
import com.ciandt.ExceptionsMyMusic.domain.dto.DataDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.MusicDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.entities.Playlist;
import com.ciandt.ExceptionsMyMusic.domain.services.PlaylistService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/playlists")
public class PlaylistController {
    private static final String AUTHORIZATION_ID_HEADER = "id";
    private static final String AUTHORIZATION_TOKEN_HEADER = "token";
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(PlaylistController.class);

    @Autowired
    private PlaylistService playlistService;

    @Operation(summary = "Add music to playlist", description = "Receive a list of songs and add them to the playlist")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful Operation"),
            @ApiResponse(code = 400, message = "Playlist Does Not Exist OR Existing Song in Playlist OR Payload Body Does Not Conform to Documentation"),
    })

    @PostMapping("/{playlistId}/musicas")
    public ResponseEntity<Playlist> findMusicandArtistByName(@PathVariable(value = "playlistId") String playlistId,
                                                             @RequestBody DataDTO dataDTO,
                                                             @RequestHeader(AUTHORIZATION_ID_HEADER) String userId,
                                                             @RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token) {
        TokenDataDTO tokenDataDTO = new TokenDataDTO(new Data(userId, token));
        MusicDTO musicDTO = dataDTO.getData().get(0);
        playlistService.addMusicToPlaylist(playlistId, musicDTO, tokenDataDTO);

        LOGGER.info("Operation performed successfully!");
        return new ResponseEntity<Playlist>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{playlistId}/musicas/{musicaId}")
    @Operation(summary = "Remove music from playlist", description = "Receive a list of songs and add them to the playlist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Operation"),
            @ApiResponse(code = 400, message = "Playlist Does Not Exist OR Existing Song in Playlist OR Payload Body Does Not Conform to Documentation"),
    })
    public ResponseEntity<String> removeMusicFromPlaylist(@PathVariable(value = "playlistId") String playlistId,
                                                          @PathVariable(value = "musicaId") String musicaId,
                                                          @RequestHeader(AUTHORIZATION_ID_HEADER) String userId,
                                                          @RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token
    ) {
        TokenDataDTO tokenDataDTO = new TokenDataDTO(new Data(userId, token));
        playlistService.removeMusicToPlaylist(playlistId, musicaId, tokenDataDTO);

        return ResponseEntity.ok().body("Song successfully deleted!");
    }

    @PostMapping("/{playlistId}/{userId}/musicas")
    public ResponseEntity<Playlist> addMusicUserPlaylist(@PathVariable(value = "playlistId") String playlistId,
                                                         @PathVariable(value = "userId") String userId,
                                                         @RequestBody DataDTO dataDTO,
                                                         @RequestHeader(AUTHORIZATION_ID_HEADER) String userTokenId,
                                                         @RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token) {
        TokenDataDTO tokenDataDTO = new TokenDataDTO(new Data(userTokenId, token));
        MusicDTO musicDTO = dataDTO.getData().get(0);
        playlistService.addMusicToPlaylistCheckingUserType(playlistId, userId, musicDTO, tokenDataDTO);

        LOGGER.info("Operation performed successfully!");
        return new ResponseEntity<Playlist>(HttpStatus.CREATED);
    }
}