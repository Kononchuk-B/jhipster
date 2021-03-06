package com.konon.libsupport.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.konon.libsupport.domain.Book;
import com.konon.libsupport.security.AuthoritiesConstants;
import com.konon.libsupport.security.SecurityUtils;
import com.konon.libsupport.service.BookService;
import com.konon.libsupport.web.rest.errors.ErrorConstants;
import com.konon.libsupport.web.rest.util.HeaderUtil;
import com.konon.libsupport.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Book.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    @Inject
    private BookService bookService;

    /**
     * POST  /books : Create a new book.
     *
     * @param book the book to create
     * @return the ResponseEntity with status 201 (Created) and with body the new book, or with status 400 (Bad Request) if the book has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/books")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save Book : {}", book);
        if (book.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("book", "idexists", "A new book cannot already have an ID")).body(null);
        }
        Book result = bookService.save(book);
        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("book", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /books : Updates an existing book.
     *
     * @param book the book to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated book,
     * or with status 400 (Bad Request) if the book is not valid,
     * or with status 500 (Internal Server Error) if the book couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/books")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to update Book : {}", book);
        if (book.getId() == null) {
            return createBook(book);
        }
        Book result = bookService.save(book);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("book", book.getId().toString()))
            .body(result);
    }

    /**
     * GET  /books : get all the books.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of books in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/books")
    @Timed
    public ResponseEntity<List<Book>> getAllBooks(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Books");
        Page<Book> page = new PageImpl<>(bookService.checkAllForAvailability(), pageable, bookService.checkAllForAvailability().size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/books");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /books/:id : get the "id" book.
     *
     * @param id the id of the book to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the book, or with status 404 (Not Found)
     */
    @GetMapping("/books/{id}")
    @Timed
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        Book book = bookService.findOne(id);
        return Optional.ofNullable(book)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /books/:id : delete the "id" book.
     *
     * @param id the id of the book to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/books/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("book", id.toString())).build();
    }

    @GetMapping("/my-books/{login}")
    @Timed
    public ResponseEntity<List<Book>> getAllReadedBooks(@PathVariable String login)
        throws URISyntaxException {
        log.debug("REST request to get a list of readed Books for current user");
        if(!SecurityUtils.getCurrentUserLogin().equals(login)) {
            throw new AccessDeniedException(ErrorConstants.ERR_ACCESS_DENIED);
        }
        List<Book> books = bookService.findByBookReservationForUser(login)
            .stream()
            .distinct()
            .collect(Collectors.toList());
        Page<Book> page = new PageImpl<>(books);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/my-books/{login}");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
