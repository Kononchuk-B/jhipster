package com.konon.libsupport.service;

import com.konon.libsupport.domain.BookReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing BookReservation.
 */
public interface BookReservationService {

    /**
     * Save a bookReservation.
     *
     * @param bookReservation the entity to save
     * @return the persisted entity
     */
    BookReservation save(BookReservation bookReservation);

    /**
     *  Get all the bookReservations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BookReservation> findAll(Pageable pageable);

    /**
     *  Get the "id" bookReservation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BookReservation findOne(Long id);

    /**
     *  Delete the "id" bookReservation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
