package com.konon.libsupport.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the BookCopy entity.
 */
public class BookCopyDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateOfSupply;


    private Long bookId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDateOfSupply() {
        return dateOfSupply;
    }

    public void setDateOfSupply(LocalDate dateOfSupply) {
        this.dateOfSupply = dateOfSupply;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookCopyDTO bookCopyDTO = (BookCopyDTO) o;

        if ( ! Objects.equals(id, bookCopyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BookCopyDTO{" +
            "id=" + id +
            ", dateOfSupply='" + dateOfSupply + "'" +
            '}';
    }
}
