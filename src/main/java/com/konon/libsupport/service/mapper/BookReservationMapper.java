package com.konon.libsupport.service.mapper;

import com.konon.libsupport.domain.*;
import com.konon.libsupport.service.dto.BookReservationDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BookReservation and its DTO BookReservationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BookReservationMapper {

    @Mapping(source = "book.id", target = "bookId")
    BookReservationDTO bookReservationToBookReservationDTO(BookReservation bookReservation);

    List<BookReservationDTO> bookReservationsToBookReservationDTOs(List<BookReservation> bookReservations);

    @Mapping(source = "bookId", target = "book")
    BookReservation bookReservationDTOToBookReservation(BookReservationDTO bookReservationDTO);

    List<BookReservation> bookReservationDTOsToBookReservations(List<BookReservationDTO> bookReservationDTOs);

    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
