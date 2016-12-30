package com.konon.libsupport.repository;

import com.konon.libsupport.domain.BookReservation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BookReservation entity.
 */
@SuppressWarnings("unused")
public interface BookReservationRepository extends JpaRepository<BookReservation,Long> {

}
