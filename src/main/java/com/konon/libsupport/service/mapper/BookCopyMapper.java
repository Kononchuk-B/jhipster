package com.konon.libsupport.service.mapper;

import com.konon.libsupport.domain.*;
import com.konon.libsupport.service.dto.BookCopyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BookCopy and its DTO BookCopyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookCopyMapper {

    @Mapping(source = "book.id", target = "bookId")
    BookCopyDTO bookCopyToBookCopyDTO(BookCopy bookCopy);

    List<BookCopyDTO> bookCopiesToBookCopyDTOs(List<BookCopy> bookCopies);

    @Mapping(source = "bookId", target = "book")
    BookCopy bookCopyDTOToBookCopy(BookCopyDTO bookCopyDTO);

    List<BookCopy> bookCopyDTOsToBookCopies(List<BookCopyDTO> bookCopyDTOs);

    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
