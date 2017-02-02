package com.konon.libsupport.repository;

import com.konon.libsupport.domain.Book;

import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Book entity.
 */
@SuppressWarnings("unused")
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select distinct book from Book book left join fetch book.genres left join fetch book.categories")
    List<Book> findAllWithEagerRelationships();

    @Query("select book from Book book left join fetch book.genres left join fetch book.categories where book.id =:id")
    Book findOneWithEagerRelationships(@Param("id") Long id);

    @Query(value = "select * from book inner join book_reservation on book.id = book_reservation.book_id inner join " +
        "jhi_user on book_reservation.user_id = jhi_user.id where jhi_user.login =:login", nativeQuery = true)
    List<Book> findByBookReservationForUser(@Param("login") String login);

}
