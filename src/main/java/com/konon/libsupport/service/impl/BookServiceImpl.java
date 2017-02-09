package com.konon.libsupport.service.impl;

import com.konon.libsupport.service.BookService;
import com.konon.libsupport.domain.Book;
import com.konon.libsupport.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Book.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService{

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Inject
    private BookRepository bookRepository;

    /**
     * Save a book.
     *
     * @param book the entity to save
     * @return the persisted entity
     */
    @Override
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);
        return bookRepository.saveAndFlush(book);
    }

    /**
     *  Get all the books.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get page of all Books");
        return bookRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        log.debug("Request to get all Books");
        List<Book> books = bookRepository.findAll();
        return checkForAvailability(books);
    }

    /**
     *  Get one book by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Book findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        Book book = bookRepository.findOneWithEagerRelationships(id);
        return book;
    }

    /**
     *  Delete the  book by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.delete(id);
    }

    @Override
    public List<Book> findByBookReservationForUser(String login) {
        List<Book> booksForUser =  bookRepository.findByBookReservationForUser(login);
        return checkForAvailability(booksForUser);
    }

    @Override
    public List<Book> checkForAvailability(List<Book> books) {
        log.debug("Checking books for availability");
        return books.stream()
            .map(book -> book.setIsAvailable(!book.getBookCopies().isEmpty()))
            .collect(Collectors.toList());
    }

}
