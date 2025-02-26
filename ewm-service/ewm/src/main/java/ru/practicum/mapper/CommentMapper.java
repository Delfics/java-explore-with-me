package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.CommentDtoRequired;
import ru.practicum.model.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(CommentDto dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        if (dto.getEvent() != null) {
            comment.setEventId(dto.getEvent().getId());
        }
        comment.setText(dto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setUser(UserMapper.toUser(dto.getAuthor()));
        comment.setEvent(EventMapper.toEvent(dto.getEvent()));
        return comment;
    }

    public CommentDtoRequired toDto(Comment comment) {
        CommentDtoRequired dto = new CommentDtoRequired();
        dto.setId(comment.getId());
        if (comment.getEventId() != null) {
            dto.setEventId(comment.getEvent().getId());
        }
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getUser().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}
