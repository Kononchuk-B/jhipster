package com.konon.libsupport.service.mapper;

import com.konon.libsupport.domain.*;
import com.konon.libsupport.service.dto.FeedbackDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Feedback and its DTO FeedbackDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FeedbackMapper {

    @Mapping(source = "book.id", target = "bookId")
    FeedbackDTO feedbackToFeedbackDTO(Feedback feedback);

    List<FeedbackDTO> feedbacksToFeedbackDTOs(List<Feedback> feedbacks);

    @Mapping(source = "bookId", target = "book")
    Feedback feedbackDTOToFeedback(FeedbackDTO feedbackDTO);

    List<Feedback> feedbackDTOsToFeedbacks(List<FeedbackDTO> feedbackDTOs);

    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
