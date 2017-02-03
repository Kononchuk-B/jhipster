package com.konon.libsupport.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.konon.libsupport.config.JHipsterProperties;
import com.konon.libsupport.domain.BookReservation;
import com.konon.libsupport.domain.Feedback;
import com.konon.libsupport.repository.BookReservationRepository;
import com.konon.libsupport.security.AuthoritiesConstants;
import com.konon.libsupport.security.SecurityUtils;
import com.konon.libsupport.service.BookReservationService;
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
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BookReservation.
 */
@RestController
@RequestMapping("/api")
public class BookReservationResource {

    private final Logger log = LoggerFactory.getLogger(BookReservationResource.class);

    @Inject
    private BookReservationService bookReservationService;

    @Inject
    private BookReservationRepository bookReservationRepository;

    /**
     * POST  /book-reservations : Create a new bookReservation.
     *
     * @param bookReservation the bookReservation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookReservation, or with status 400 (Bad Request) if the bookReservation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/book-reservations")
    @Timed
    public ResponseEntity<BookReservation> createBookReservation(@Valid @RequestBody BookReservation bookReservation) throws URISyntaxException {
        log.debug("REST request to save BookReservation : {}", bookReservation);
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            throw new AccessDeniedException(ErrorConstants.ERR_ACCESS_DENIED);
        }
        if (bookReservation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bookReservation", "idexists", "A new bookReservation cannot already have an ID")).body(null);
        }
        BookReservation result = bookReservationService.save(bookReservation);
        return ResponseEntity.created(new URI("/api/book-reservations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bookReservation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /book-reservations : Updates an existing bookReservation.
     *
     * @param bookReservation the bookReservation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookReservation,
     * or with status 400 (Bad Request) if the bookReservation is not valid,
     * or with status 500 (Internal Server Error) if the bookReservation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/book-reservations")
    @Timed
    public ResponseEntity<BookReservation> updateBookReservation(@Valid @RequestBody BookReservation bookReservation) throws URISyntaxException {
        log.debug("REST request to update BookReservation : {}", bookReservation);
        if (bookReservation.getId() == null) {
            return createBookReservation(bookReservation);
        }
        String reservationLogin = bookReservation.getUser().getLogin();
        return Optional.of(bookReservation)
            .filter(result -> reservationLogin.equals(SecurityUtils.getCurrentUserLogin()))
            .map(result -> {
                bookReservationService.save(result);
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("bookReservation", bookReservation.getId().toString()))
                    .body(result);
            })
            .orElseThrow(() -> new AccessDeniedException(ErrorConstants.ERR_ACCESS_DENIED));
    }

    /**
     * GET  /book-reservations : get all the bookReservations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bookReservations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/book-reservations")
    @Timed
    public ResponseEntity<List<BookReservation>> getAllBookReservations(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BookReservations");
        Page<BookReservation> page;
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = bookReservationService.findAll(pageable);
        } else {
            page = new PageImpl<>(bookReservationRepository.findByUserIsCurrentUser());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/book-reservations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /book-reservations/:id : get the "id" bookReservation.
     *
     * @param id the id of the bookReservation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookReservation, or with status 404 (Not Found)
     */
    @GetMapping("/book-reservations/{id}")
    @Timed
    public ResponseEntity<BookReservation> getBookReservation(@PathVariable Long id) {
        log.debug("REST request to get BookReservation : {}", id);
        BookReservation bookReservation = bookReservationService.findOne(id);
        return Optional.ofNullable(bookReservation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /book-reservations/:id : delete the "id" bookReservation.
     *
     * @param id the id of the bookReservation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/book-reservations/{id}")
    @Timed
    public ResponseEntity<Void> deleteBookReservation(@PathVariable Long id) {
        log.debug("REST request to delete BookReservation : {}", id);
        BookReservation currentReservation = bookReservationService.findOne(id);
        String reservationLogin = currentReservation.getUser().getLogin();
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) || reservationLogin.equals(SecurityUtils.getCurrentUserLogin())) {
            bookReservationService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bookReservation", id.toString())).build();
        } else {
            throw new AccessDeniedException(ErrorConstants.ERR_ACCESS_DENIED);
        }
    }

}
