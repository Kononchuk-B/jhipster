package com.konon.libsupport.web.rest;

import com.konon.libsupport.LibSupportApp;

import com.konon.libsupport.domain.BookCopy;
import com.konon.libsupport.repository.BookCopyRepository;
import com.konon.libsupport.service.BookCopyService;
import com.konon.libsupport.service.dto.BookCopyDTO;
import com.konon.libsupport.service.mapper.BookCopyMapper;

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
 * Test class for the BookCopyResource REST controller.
 *
 * @see BookCopyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibSupportApp.class)
public class BookCopyResourceIntTest {

    private static final LocalDate DEFAULT_DATE_OF_SUPPLY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_SUPPLY = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private BookCopyRepository bookCopyRepository;

    @Inject
    private BookCopyMapper bookCopyMapper;

    @Inject
    private BookCopyService bookCopyService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBookCopyMockMvc;

    private BookCopy bookCopy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookCopyResource bookCopyResource = new BookCopyResource();
        ReflectionTestUtils.setField(bookCopyResource, "bookCopyService", bookCopyService);
        this.restBookCopyMockMvc = MockMvcBuilders.standaloneSetup(bookCopyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCopy createEntity(EntityManager em) {
        BookCopy bookCopy = new BookCopy()
                .dateOfSupply(DEFAULT_DATE_OF_SUPPLY);
        return bookCopy;
    }

    @Before
    public void initTest() {
        bookCopy = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookCopy() throws Exception {
        int databaseSizeBeforeCreate = bookCopyRepository.findAll().size();

        // Create the BookCopy
        BookCopyDTO bookCopyDTO = bookCopyMapper.bookCopyToBookCopyDTO(bookCopy);

        restBookCopyMockMvc.perform(post("/api/book-copies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookCopyDTO)))
            .andExpect(status().isCreated());

        // Validate the BookCopy in the database
        List<BookCopy> bookCopyList = bookCopyRepository.findAll();
        assertThat(bookCopyList).hasSize(databaseSizeBeforeCreate + 1);
        BookCopy testBookCopy = bookCopyList.get(bookCopyList.size() - 1);
        assertThat(testBookCopy.getDateOfSupply()).isEqualTo(DEFAULT_DATE_OF_SUPPLY);
    }

    @Test
    @Transactional
    public void createBookCopyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookCopyRepository.findAll().size();

        // Create the BookCopy with an existing ID
        BookCopy existingBookCopy = new BookCopy();
        existingBookCopy.setId(1L);
        BookCopyDTO existingBookCopyDTO = bookCopyMapper.bookCopyToBookCopyDTO(existingBookCopy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookCopyMockMvc.perform(post("/api/book-copies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingBookCopyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BookCopy> bookCopyList = bookCopyRepository.findAll();
        assertThat(bookCopyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateOfSupplyIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookCopyRepository.findAll().size();
        // set the field null
        bookCopy.setDateOfSupply(null);

        // Create the BookCopy, which fails.
        BookCopyDTO bookCopyDTO = bookCopyMapper.bookCopyToBookCopyDTO(bookCopy);

        restBookCopyMockMvc.perform(post("/api/book-copies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookCopyDTO)))
            .andExpect(status().isBadRequest());

        List<BookCopy> bookCopyList = bookCopyRepository.findAll();
        assertThat(bookCopyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookCopies() throws Exception {
        // Initialize the database
        bookCopyRepository.saveAndFlush(bookCopy);

        // Get all the bookCopyList
        restBookCopyMockMvc.perform(get("/api/book-copies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookCopy.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfSupply").value(hasItem(DEFAULT_DATE_OF_SUPPLY.toString())));
    }

    @Test
    @Transactional
    public void getBookCopy() throws Exception {
        // Initialize the database
        bookCopyRepository.saveAndFlush(bookCopy);

        // Get the bookCopy
        restBookCopyMockMvc.perform(get("/api/book-copies/{id}", bookCopy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bookCopy.getId().intValue()))
            .andExpect(jsonPath("$.dateOfSupply").value(DEFAULT_DATE_OF_SUPPLY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBookCopy() throws Exception {
        // Get the bookCopy
        restBookCopyMockMvc.perform(get("/api/book-copies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookCopy() throws Exception {
        // Initialize the database
        bookCopyRepository.saveAndFlush(bookCopy);
        int databaseSizeBeforeUpdate = bookCopyRepository.findAll().size();

        // Update the bookCopy
        BookCopy updatedBookCopy = bookCopyRepository.findOne(bookCopy.getId());
        updatedBookCopy
                .dateOfSupply(UPDATED_DATE_OF_SUPPLY);
        BookCopyDTO bookCopyDTO = bookCopyMapper.bookCopyToBookCopyDTO(updatedBookCopy);

        restBookCopyMockMvc.perform(put("/api/book-copies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookCopyDTO)))
            .andExpect(status().isOk());

        // Validate the BookCopy in the database
        List<BookCopy> bookCopyList = bookCopyRepository.findAll();
        assertThat(bookCopyList).hasSize(databaseSizeBeforeUpdate);
        BookCopy testBookCopy = bookCopyList.get(bookCopyList.size() - 1);
        assertThat(testBookCopy.getDateOfSupply()).isEqualTo(UPDATED_DATE_OF_SUPPLY);
    }

    @Test
    @Transactional
    public void updateNonExistingBookCopy() throws Exception {
        int databaseSizeBeforeUpdate = bookCopyRepository.findAll().size();

        // Create the BookCopy
        BookCopyDTO bookCopyDTO = bookCopyMapper.bookCopyToBookCopyDTO(bookCopy);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookCopyMockMvc.perform(put("/api/book-copies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookCopyDTO)))
            .andExpect(status().isCreated());

        // Validate the BookCopy in the database
        List<BookCopy> bookCopyList = bookCopyRepository.findAll();
        assertThat(bookCopyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBookCopy() throws Exception {
        // Initialize the database
        bookCopyRepository.saveAndFlush(bookCopy);
        int databaseSizeBeforeDelete = bookCopyRepository.findAll().size();

        // Get the bookCopy
        restBookCopyMockMvc.perform(delete("/api/book-copies/{id}", bookCopy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BookCopy> bookCopyList = bookCopyRepository.findAll();
        assertThat(bookCopyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
