package ru.practicum.closed.user.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Comment;

import java.util.List;

public interface CommentStorage extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "WHERE c.event.id = :eventId " +
            "ORDER BY c.created DESC")
    List<Comment> findCommentsByEventId(Long eventId);
}
