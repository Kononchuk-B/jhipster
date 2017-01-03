package com.konon.libsupport.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.konon.libsupport.domain.BookCopy;
import com.konon.libsupport.service.BookCopyService;
import com.konon.libsupport.web.rest.util.HeaderUtil;
import com.konon.libsupport.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BookCopy.
 */
@RestController
@RequestMapping("/api")
public class BookCopyResource {

    private final Logger log = LoggerFactory.getLogger(BookCopyResource.class);
        
    @Inject
    private BookCopyService bookCopyService;

    /**
     * POST  /book-copies : Create a new bookCopy.
     *
     * @param bookCopy the bookCopy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookCopy, or with status 400 (Bad Request) if the bookCopy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/book-copies")
    @Timed
    public ResponseEntity<BookCopy> createBookCopy(@Valid @RequestBody BookCopy bookCopy) throws URISyntaxException {
        log.debug("REST request to save BookCopy : {}", bookCopy);
        if (bookCopy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bookCopy", "idexists", "A new bookCopy cannot already have an ID")).body(null);
        }
        BookCopy result = bookCopyService.save(bookCopy);
        return ResponseEntity.created(new URI("/api/book-copies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bookCopy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /book-copies : Updates an existing bookCopy.
     *
     * @param bookCopy the bookCopy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookCopy,
     * or with status 400 (Bad Request) if the bookCopy is not valid,
     * or with status 500 (Internal Server Error) if the bookCopy couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/book-copies")
    @Timed
    public ResponseEntity<BookCopy> updateBookCopy(@Valid @RequestBody BookCopy bookCopy) throws URISyntaxException {
        log.debug("REST request to update BookCopy : {}", bookCopy);
        if (bookCopy.getId() == null) {
            return createBookCopy(bookCopy);
        }
        BookCopy result = bookCopyService.save(bookCopy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bookCopy", bookCopy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /book-copies : get all the bookCopies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookCopies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/book-copies")
    @Timed
    public ResponseEntity<List<BookCopy>> getAllBookCopies(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BookCopies");
        Page<BookCopy> page = bookCopyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/book-copies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /book-copies/:id : get the "id" bookCopy.
     *
     * @param id the id of the bookCopy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookCopy, or with status 404 (Not Found)
     */
    @GetMapping("/book-copies/{id}")
    @Timed
    public ResponseEntity<BookCopy> getBookCopy(@PathVariable Long id) {
        log.debug("REST request to get BookCopy : {}", id);
        BookCopy bookCopy = bookCopyService.findOne(id);
        return Optional.ofNullable(bookCopy)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /book-copies/:id : delete the "id" bookCopy.
     *
     * @param id the id of the bookCopy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/book-copies/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long id) {
        log.debug("REST request to delete BookCopy : {}", id);
        bookCopyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bookCopy", id.toString())).build();
    }

}
