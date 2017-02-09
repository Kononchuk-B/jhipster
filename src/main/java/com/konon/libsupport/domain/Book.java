package com.konon.libsupport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.konon.libsupport.domain.listener.BookEntityListener;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Book.
 */
@Entity
@EntityListeners(BookEntityListener.class)
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "edition")
    private String edition;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Column(name = "year_of_publish")
    private LocalDate yearOfPublish;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private Set<BookReservation> bookReservations = new HashSet<>();

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BookCopy> bookCopies = new HashSet<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private Set<Feedback> feedbacks = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "book_genre",
               joinColumns = @JoinColumn(name="books_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="genres_id", referencedColumnName="ID"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "book_category",
               joinColumns = @JoinColumn(name="books_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="categories_id", referencedColumnName="ID"))
    private Set<Category> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Book name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEdition() {
        return edition;
    }

    public Book edition(String edition) {
        this.edition = edition;
        return this;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public Book numberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
        return this;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public LocalDate getYearOfPublish() {
        return yearOfPublish;
    }

    public Book yearOfPublish(LocalDate yearOfPublish) {
        this.yearOfPublish = yearOfPublish;
        return this;
    }

    public void setYearOfPublish(LocalDate yearOfPublish) {
        this.yearOfPublish = yearOfPublish;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public Book isAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    public Book setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }


    public Set<BookReservation> getBookReservations() {
        return bookReservations;
    }

    public Book bookReservations(Set<BookReservation> bookReservations) {
        this.bookReservations = bookReservations;
        return this;
    }

    public Book addBookReservation(BookReservation bookReservation) {
        bookReservations.add(bookReservation);
        bookReservation.setBook(this);
        return this;
    }

    public Book removeBookReservation(BookReservation bookReservation) {
        bookReservations.remove(bookReservation);
        bookReservation.setBook(null);
        return this;
    }

    public void setBookReservations(Set<BookReservation> bookReservations) {
        this.bookReservations = bookReservations;
    }

    public Set<BookCopy> getBookCopies() {
        return bookCopies;
    }

    public Book bookCopies(Set<BookCopy> bookCopies) {
        this.bookCopies = bookCopies;
        return this;
    }

    public Book addBookCopy(BookCopy bookCopy) {
        bookCopies.add(bookCopy);
        bookCopy.setBook(this);
        return this;
    }

    public Book removeBookCopy(BookCopy bookCopy) {
        bookCopies.remove(bookCopy);
        bookCopy.setBook(null);
        return this;
    }

    public void setBookCopies(Set<BookCopy> bookCopies) {
        this.bookCopies = bookCopies;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public Book feedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        return this;
    }

    public Book addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setBook(this);
        return this;
    }

    public Book removeFeedback(Feedback feedback) {
        feedbacks.remove(feedback);
        feedback.setBook(null);
        return this;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public Book genres(Set<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public Book addGenre(Genre genre) {
        genres.add(genre);
        genre.getBooks().add(this);
        return this;
    }

    public Book removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getBooks().remove(this);
        return this;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Book categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Book addCategory(Category category) {
        categories.add(category);
        category.getBooks().add(this);
        return this;
    }

    public Book removeCategory(Category category) {
        categories.remove(category);
        category.getBooks().remove(this);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        if (book.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", author='" + author + "'" +
            ", edition='" + edition + "'" +
            ", numberOfPages='" + numberOfPages + "'" +
            ", yearOfPublish='" + yearOfPublish + "'" +
            ", isAvailable='" + isAvailable + "'" +
            '}';
    }
}
