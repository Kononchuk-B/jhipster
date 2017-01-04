package com.konon.libsupport.web.rest;

import com.konon.libsupport.LibSupportApp;

import com.konon.libsupport.domain.Genre;
import com.konon.libsupport.repository.GenreRepository;
import com.konon.libsupport.service.GenreService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GenreResource REST controller.
 *
 * @see GenreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibSupportApp.class)
public class GenreResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private GenreService genreService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGenreMockMvc;

    private Genre genre;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GenreResource genreResource = new GenreResource();
        ReflectionTestUtils.setField(genreResource, "genreService", genreService);
        this.restGenreMockMvc = MockMvcBuilders.standaloneSetup(genreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createEntity(EntityManager em) {
        Genre genre = new Genre()
                .type(DEFAULT_TYPE);
        return genre;
    }

    @Before
    public void initTest() {
        genre = createEntity(em);
    }

    @Test
    @Transactional
    public void createGenre() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // Create the Genre

        restGenreMockMvc.perform(post("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate + 1);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createGenreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // Create the Genre with an existing ID
        Genre existingGenre = new Genre();
        existingGenre.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenreMockMvc.perform(post("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingGenre)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGenres() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList
        restGenreMockMvc.perform(get("/api/genres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(genre.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGenre() throws Exception {
        // Get the genre
        restGenreMockMvc.perform(get("/api/genres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGenre() throws Exception {
        // Initialize the database
        genreService.save(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre
        Genre updatedGenre = genreRepository.findOne(genre.getId());
        updatedGenre
                .type(UPDATED_TYPE);

        restGenreMockMvc.perform(put("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGenre)))
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Create the Genre

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGenreMockMvc.perform(put("/api/genres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGenre() throws Exception {
        // Initialize the database
        genreService.save(genre);

        int databaseSizeBeforeDelete = genreRepository.findAll().size();

        // Get the genre
        restGenreMockMvc.perform(delete("/api/genres/{id}", genre.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
