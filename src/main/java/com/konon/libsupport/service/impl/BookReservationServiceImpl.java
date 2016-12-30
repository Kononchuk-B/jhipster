package com.konon.libsupport.service.impl;

import com.konon.libsupport.service.BookReservationService;
import com.konon.libsupport.domain.BookReservation;
import com.konon.libsupport.repository.BookReservationRepository;
import com.konon.libsupport.service.dto.BookReservationDTO;
import com.konon.libsupport.service.mapper.BookReservationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing BookReservation.
 */
@Service
@Transactional
public class BookReservationServiceImpl implements BookReservationService{

    private final Logger log = LoggerFactory.getLogger(BookReservationServiceImpl.class);
    
    @Inject
    private BookReservationRepository bookReservationRepository;

    @Inject
    private BookReservationMapper bookReservationMapper;

    /**
     * Save a bookReservation.
     *
     * @param bookReservationDTO the entity to save
     * @return the persisted entity
     */
    public BookReservationDTO save(BookReservationDTO bookReservationDTO) {
        log.debug("Request to save BookReservation : {}", bookReservationDTO);
        BookReservation bookReservation = bookReservationMapper.bookReservationDTOToBookReservation(bookReservationDTO);
        bookReservation = bookReservationRepository.save(bookReservation);
        BookReservationDTO result = bookReservationMapper.bookReservationToBookReservationDTO(bookReservation);
        return result;
    }

    /**
     *  Get all the bookReservations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BookReservationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BookReservations");
        Page<BookReservation> result = bookReservationRepository.findAll(pageable);
        return result.map(bookReservation -> bookReservationMapper.bookReservationToBookReservationDTO(bookReservation));
    }

    /**
     *  Get one bookReservation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BookReservationDTO findOne(Long id) {
        log.debug("Request to get BookReservation : {}", id);
        BookReservation bookReservation = bookReservationRepository.findOne(id);
        BookReservationDTO bookReservationDTO = bookReservationMapper.bookReservationToBookReservationDTO(bookReservation);
        return bookReservationDTO;
    }

    /**
     *  Delete the  bookReservation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BookReservation : {}", id);
        bookReservationRepository.delete(id);
    }
}
