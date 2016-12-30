package com.konon.libsupport.service.mapper;

import com.konon.libsupport.domain.*;
import com.konon.libsupport.service.dto.BookDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Book and its DTO BookDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookMapper {

    BookDTO bookToBookDTO(Book book);

    List<BookDTO> booksToBookDTOs(List<Book> books);

    @Mapping(target = "bookReservations", ignore = true)
    @Mapping(target = "bookCopies", ignore = true)
    @Mapping(target = "feedbacks", ignore = true)
    Book bookDTOToBook(BookDTO bookDTO);

    List<Book> bookDTOsToBooks(List<BookDTO> bookDTOs);
}
