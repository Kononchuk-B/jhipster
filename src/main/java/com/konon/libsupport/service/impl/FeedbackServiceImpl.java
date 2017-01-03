package com.konon.libsupport.service.impl;

import com.konon.libsupport.service.FeedbackService;
import com.konon.libsupport.domain.Feedback;
import com.konon.libsupport.repository.FeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Feedback.
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService{

    private final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    
    @Inject
    private FeedbackRepository feedbackRepository;

    /**
     * Save a feedback.
     *
     * @param feedback the entity to save
     * @return the persisted entity
     */
    public Feedback save(Feedback feedback) {
        log.debug("Request to save Feedback : {}", feedback);
        Feedback result = feedbackRepository.save(feedback);
        return result;
    }

    /**
     *  Get all the feedbacks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Feedback> findAll(Pageable pageable) {
        log.debug("Request to get all Feedbacks");
        Page<Feedback> result = feedbackRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one feedback by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Feedback findOne(Long id) {
        log.debug("Request to get Feedback : {}", id);
        Feedback feedback = feedbackRepository.findOne(id);
        return feedback;
    }

    /**
     *  Delete the  feedback by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Feedback : {}", id);
        feedbackRepository.delete(id);
    }
}
