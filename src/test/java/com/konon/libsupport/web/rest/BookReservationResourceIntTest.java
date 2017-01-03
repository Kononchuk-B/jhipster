package com.konon.libsupport.web.rest;

import com.konon.libsupport.LibSupportApp;

import com.konon.libsupport.domain.BookReservation;
import com.konon.libsupport.repository.BookReservationRepository;
import com.konon.libsupport.service.BookReservationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookReservationResource REST controller.
 *
 * @see BookReservationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibSupportApp.class)
public class BookReservationResourceIntTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private BookReservationRepository bookReservationRepository;

    @Inject
    private BookReservationService bookReservationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBookReservationMockMvc;

    private BookReservation bookReservation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookReservationResource bookReservationResource = new BookReservationResource();
        ReflectionTestUtils.setField(bookReservationResource, "bookReservationService", bookReservationService);
        this.restBookReservationMockMvc = MockMvcBuilders.standaloneSetup(bookReservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookReservation createEntity(EntityManager em) {
        BookReservation bookReservation = new BookReservation()
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE);
        return bookReservation;
    }

    @Before
    public void initTest() {
        bookReservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookReservation() throws Exception {
        int databaseSizeBeforeCreate = bookReservationRepository.findAll().size();

        // Create the BookReservation

        restBookReservationMockMvc.perform(post("/api/book-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookReservation)))
            .andExpect(status().isCreated());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeCreate + 1);
        BookReservation testBookReservation = bookReservationList.get(bookReservationList.size() - 1);
        assertThat(testBookReservation.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testBookReservation.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createBookReservationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookReservationRepository.findAll().size();

        // Create the BookReservation with an existing ID
        BookReservation existingBookReservation = new BookReservation();
        existingBookReservation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookReservationMockMvc.perform(post("/api/book-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBookReservation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookReservationRepository.findAll().size();
        // set the field null
        bookReservation.setStartDate(null);

        // Create the BookReservation, which fails.

        restBookReservationMockMvc.perform(post("/api/book-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookReservation)))
            .andExpect(status().isBadRequest());

        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookReservationRepository.findAll().size();
        // set the field null
        bookReservation.setEndDate(null);

        // Create the BookReservation, which fails.

        restBookReservationMockMvc.perform(post("/api/book-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookReservation)))
            .andExpect(status().isBadRequest());

        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookReservations() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get all the bookReservationList
        restBookReservationMockMvc.perform(get("/api/book-reservations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookReservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getBookReservation() throws Exception {
        // Initialize the database
        bookReservationRepository.saveAndFlush(bookReservation);

        // Get the bookReservation
        restBookReservationMockMvc.perform(get("/api/book-reservations/{id}", bookReservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookReservation.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBookReservation() throws Exception {
        // Get the bookReservation
        restBookReservationMockMvc.perform(get("/api/book-reservations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookReservation() throws Exception {
        // Initialize the database
        bookReservationService.save(bookReservation);

        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();

        // Update the bookReservation
        BookReservation updatedBookReservation = bookReservationRepository.findOne(bookReservation.getId());
        updatedBookReservation
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE);

        restBookReservationMockMvc.perform(put("/api/book-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBookReservation)))
            .andExpect(status().isOk());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate);
        BookReservation testBookReservation = bookReservationList.get(bookReservationList.size() - 1);
        assertThat(testBookReservation.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testBookReservation.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBookReservation() throws Exception {
        int databaseSizeBeforeUpdate = bookReservationRepository.findAll().size();

        // Create the BookReservation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookReservationMockMvc.perform(put("/api/book-reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookReservation)))
            .andExpect(status().isCreated());

        // Validate the BookReservation in the database
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBookReservation() throws Exception {
        // Initialize the database
        bookReservationService.save(bookReservation);

        int databaseSizeBeforeDelete = bookReservationRepository.findAll().size();

        // Get the bookReservation
        restBookReservationMockMvc.perform(delete("/api/book-reservations/{id}", bookReservation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BookReservation> bookReservationList = bookReservationRepository.findAll();
        assertThat(bookReservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
