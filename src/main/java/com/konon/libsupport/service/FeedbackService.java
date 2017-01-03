package com.konon.libsupport.service;

import com.konon.libsupport.domain.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Feedback.
 */
public interface FeedbackService {

    /**
     * Save a feedback.
     *
     * @param feedback the entity to save
     * @return the persisted entity
     */
    Feedback save(Feedback feedback);

    /**
     *  Get all the feedbacks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Feedback> findAll(Pageable pageable);

    /**
     *  Get the "id" feedback.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Feedback findOne(Long id);

    /**
     *  Delete the "id" feedback.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
