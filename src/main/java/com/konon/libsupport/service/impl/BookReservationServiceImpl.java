package com.konon.libsupport.service.impl;

import com.konon.libsupport.service.BookReservationService;
import com.konon.libsupport.domain.BookReservation;
import com.konon.libsupport.repository.BookReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing BookReservation.
 */
@Service
@Transactional
public class BookReservationServiceImpl implements BookReservationService{

    private final Logger log = LoggerFactory.getLogger(BookReservationServiceImpl.class);
    
    @Inject
    private BookReservationRepository bookReservationRepository;

    /**
     * Save a bookReservation.
     *
     * @param bookReservation the entity to save
     * @return the persisted entity
     */
    public BookReservation save(BookReservation bookReservation) {
        log.debug("Request to save BookReservation : {}", bookReservation);
        BookReservation result = bookReservationRepository.save(bookReservation);
        return result;
    }

    /**
     *  Get all the bookReservations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BookReservation> findAll(Pageable pageable) {
        log.debug("Request to get all BookReservations");
        Page<BookReservation> result = bookReservationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one bookReservation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BookReservation findOne(Long id) {
        log.debug("Request to get BookReservation : {}", id);
        BookReservation bookReservation = bookReservationRepository.findOne(id);
        return bookReservation;
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
