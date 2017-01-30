package com.konon.libsupport.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "stars", nullable = false)
    private Integer stars;

    @NotNull
    @Column(name = "date_of_publish", nullable = false)
    private LocalDate dateOfPublish;

    @Size(min = 10, max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne
    @NotNull
    private Book book;

    @ManyToOne
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStars() {
        return stars;
    }

    public Feedback stars(Integer stars) {
        this.stars = stars;
        return this;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public LocalDate getDateOfPublish() {
        return dateOfPublish;
    }

    public Feedback dateOfPublish(LocalDate dateOfPublish) {
        this.dateOfPublish = dateOfPublish;
        return this;
    }

    public void setDateOfPublish(LocalDate dateOfPublish) {
        this.dateOfPublish = dateOfPublish;
    }

    public String getDescription() {
        return description;
    }

    public Feedback description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Book getBook() {
        return book;
    }

    public Feedback book(Book book) {
        this.book = book;
        return this;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public Feedback user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feedback feedback = (Feedback) o;
        if (feedback.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, feedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + id +
            ", stars='" + stars + "'" +
            ", dateOfPublish='" + dateOfPublish + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
