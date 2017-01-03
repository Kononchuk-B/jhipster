package com.konon.libsupport.service;

import com.konon.libsupport.domain.BookCopy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing BookCopy.
 */
public interface BookCopyService {

    /**
     * Save a bookCopy.
     *
     * @param bookCopy the entity to save
     * @return the persisted entity
     */
    BookCopy save(BookCopy bookCopy);

    /**
     *  Get all the bookCopies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BookCopy> findAll(Pageable pageable);

    /**
     *  Get the "id" bookCopy.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BookCopy findOne(Long id);

    /**
     *  Delete the "id" bookCopy.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
