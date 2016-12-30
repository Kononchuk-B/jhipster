package com.konon.libsupport.service.impl;

import com.konon.libsupport.service.FeedbackService;
import com.konon.libsupport.domain.Feedback;
import com.konon.libsupport.repository.FeedbackRepository;
import com.konon.libsupport.service.dto.FeedbackDTO;
import com.konon.libsupport.service.mapper.FeedbackMapper;
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
 * Service Implementation for managing Feedback.
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService{

    private final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    
    @Inject
    private FeedbackRepository feedbackRepository;

    @Inject
    private FeedbackMapper feedbackMapper;

    /**
     * Save a feedback.
     *
     * @param feedbackDTO the entity to save
     * @return the persisted entity
     */
    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        log.debug("Request to save Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.feedbackDTOToFeedback(feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        FeedbackDTO result = feedbackMapper.feedbackToFeedbackDTO(feedback);
        return result;
    }

    /**
     *  Get all the feedbacks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<FeedbackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Feedbacks");
        Page<Feedback> result = feedbackRepository.findAll(pageable);
        return result.map(feedback -> feedbackMapper.feedbackToFeedbackDTO(feedback));
    }

    /**
     *  Get one feedback by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FeedbackDTO findOne(Long id) {
        log.debug("Request to get Feedback : {}", id);
        Feedback feedback = feedbackRepository.findOne(id);
        FeedbackDTO feedbackDTO = feedbackMapper.feedbackToFeedbackDTO(feedback);
        return feedbackDTO;
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
