package com.konon.libsupport.repository;

import com.konon.libsupport.domain.Feedback;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Feedback entity.
 */
@SuppressWarnings("unused")
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

}
