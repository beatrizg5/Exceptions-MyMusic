package com.ciandt.ExceptionsMyMusic.application.controllers;

import com.ciandt.ExceptionsMyMusic.domain.dto.Data;
import com.ciandt.ExceptionsMyMusic.domain.dto.DataDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.MusicDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.services.MusicService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MusicController {
    private static final String AUTHORIZATION_ID_HEADER = "id";
    private static final String AUTHORIZATION_TOKEN_HEADER = "token";
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MusicController.class);

    @Autowired
    private MusicService musicService;

    @Operation(summary = "Search songs by artist name or song name", description = "Search by artist name or song name. Filter search is not case sensitive. The filter must be at least 3 characters long.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Operation"),
            @ApiResponse(code = 204, message = "No results"),
            @ApiResponse(code = 400, message = "Not enough characters")
    })
    @GetMapping(value = "/musicas")
    public ResponseEntity<?> findMusicandArtistByName(@RequestParam(value = "filter") String id,
                                                      @RequestHeader(AUTHORIZATION_ID_HEADER) String userId,
                                                      @RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token) {
        TokenDataDTO tokenDataDTO = new TokenDataDTO(new Data(userId, token));
        List<MusicDTO> dtoMusic = musicService.findByArtistOrMusic(id, tokenDataDTO);

        LOGGER.info("Operation performed successfully!");
        return ResponseEntity.ok().body(new DataDTO(dtoMusic));
    }
}
