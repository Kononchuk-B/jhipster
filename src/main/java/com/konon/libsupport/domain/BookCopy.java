package com.konon.libsupport.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A BookCopy.
 */
@Entity
@Table(name = "book_copy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BookCopy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date_of_supply", nullable = false)
    private LocalDate dateOfSupply;

    @ManyToOne
    @NotNull
    private Book book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfSupply() {
        return dateOfSupply;
    }

    public BookCopy dateOfSupply(LocalDate dateOfSupply) {
        this.dateOfSupply = dateOfSupply;
        return this;
    }

    public void setDateOfSupply(LocalDate dateOfSupply) {
        this.dateOfSupply = dateOfSupply;
    }

    public Book getBook() {
        return book;
    }

    public BookCopy book(Book book) {
        this.book = book;
        return this;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookCopy bookCopy = (BookCopy) o;
        if (bookCopy.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bookCopy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookCopy{" +
            "id=" + id +
            ", dateOfSupply='" + dateOfSupply + "'" +
            '}';
    }
}
