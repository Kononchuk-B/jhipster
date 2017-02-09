package com.konon.libsupport.domain.listener;

import com.konon.libsupport.domain.Book;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class BookEntityListener {

    @PrePersist
    @PreUpdate
    public void prePersist(Book book) {
        if(book.getIsAvailable() == null) {
            book.setIsAvailable(false);
        }
    }
}
