package com.konon.libsupport.repository;

import com.konon.libsupport.domain.BookCopy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BookCopy entity.
 */
@SuppressWarnings("unused")
public interface BookCopyRepository extends JpaRepository<BookCopy,Long> {

}
