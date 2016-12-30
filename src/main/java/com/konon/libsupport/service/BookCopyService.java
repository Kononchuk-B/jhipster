package com.konon.libsupport.service;

import com.konon.libsupport.service.dto.BookCopyDTO;
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
     * @param bookCopyDTO the entity to save
     * @return the persisted entity
     */
    BookCopyDTO save(BookCopyDTO bookCopyDTO);

    /**
     *  Get all the bookCopies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BookCopyDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bookCopy.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BookCopyDTO findOne(Long id);

    /**
     *  Delete the "id" bookCopy.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
