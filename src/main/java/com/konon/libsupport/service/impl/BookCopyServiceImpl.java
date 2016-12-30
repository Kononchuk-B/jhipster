package com.konon.libsupport.service.impl;

import com.konon.libsupport.service.BookCopyService;
import com.konon.libsupport.domain.BookCopy;
import com.konon.libsupport.repository.BookCopyRepository;
import com.konon.libsupport.service.dto.BookCopyDTO;
import com.konon.libsupport.service.mapper.BookCopyMapper;
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
 * Service Implementation for managing BookCopy.
 */
@Service
@Transactional
public class BookCopyServiceImpl implements BookCopyService{

    private final Logger log = LoggerFactory.getLogger(BookCopyServiceImpl.class);
    
    @Inject
    private BookCopyRepository bookCopyRepository;

    @Inject
    private BookCopyMapper bookCopyMapper;

    /**
     * Save a bookCopy.
     *
     * @param bookCopyDTO the entity to save
     * @return the persisted entity
     */
    public BookCopyDTO save(BookCopyDTO bookCopyDTO) {
        log.debug("Request to save BookCopy : {}", bookCopyDTO);
        BookCopy bookCopy = bookCopyMapper.bookCopyDTOToBookCopy(bookCopyDTO);
        bookCopy = bookCopyRepository.save(bookCopy);
        BookCopyDTO result = bookCopyMapper.bookCopyToBookCopyDTO(bookCopy);
        return result;
    }

    /**
     *  Get all the bookCopies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BookCopyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BookCopies");
        Page<BookCopy> result = bookCopyRepository.findAll(pageable);
        return result.map(bookCopy -> bookCopyMapper.bookCopyToBookCopyDTO(bookCopy));
    }

    /**
     *  Get one bookCopy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BookCopyDTO findOne(Long id) {
        log.debug("Request to get BookCopy : {}", id);
        BookCopy bookCopy = bookCopyRepository.findOne(id);
        BookCopyDTO bookCopyDTO = bookCopyMapper.bookCopyToBookCopyDTO(bookCopy);
        return bookCopyDTO;
    }

    /**
     *  Delete the  bookCopy by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BookCopy : {}", id);
        bookCopyRepository.delete(id);
    }
}
