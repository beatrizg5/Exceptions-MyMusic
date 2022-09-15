package com.ciandt.ExceptionsMyMusic.domain.services;

import com.ciandt.ExceptionsMyMusic.application.repositories.MusicRepository;
import com.ciandt.ExceptionsMyMusic.domain.dto.MusicDTO;
import com.ciandt.ExceptionsMyMusic.domain.dto.TokenDataDTO;
import com.ciandt.ExceptionsMyMusic.domain.entities.Music;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.NoContentException;
import com.ciandt.ExceptionsMyMusic.domain.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MusicService.class);

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional(readOnly = true)
    public List<MusicDTO> findByArtistOrMusic(String nome, TokenDataDTO tokenDataDTO) {
        tokenService.validateHeader(tokenDataDTO);

        if (nome.length() <= 2) {
            LOGGER.error("Filter is less than 3 characters long, so invalid search");
            throw new ResourceNotFoundException("Filter must be 3 or more characters long");
        }

        LOGGER.info("Search performed successfully, because filter has more than 2 characters");
        List<Music> list = musicRepository.findByArtistOrNameOfMusic(nome);
        if (list.isEmpty()) {
            LOGGER.error("The filter did not return data for the search performed!");
            throw new NoContentException("Data not found");
        }

        LOGGER.info("Search performed successfully, as data found in the database");
        List<MusicDTO> listDTO = list.stream().map(music -> new MusicDTO(music)).collect(Collectors.toList());

        LOGGER.info("Data returned successfully!");
        return listDTO;
    }
}