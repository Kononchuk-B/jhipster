package com.konon.libsupport.service;

import com.konon.libsupport.service.dto.BookReservationDTO;
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
     * @param bookReservationDTO the entity to save
     * @return the persisted entity
     */
    BookReservationDTO save(BookReservationDTO bookReservationDTO);

    /**
     *  Get all the bookReservations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BookReservationDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bookReservation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BookReservationDTO findOne(Long id);

    /**
     *  Delete the "id" bookReservation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
