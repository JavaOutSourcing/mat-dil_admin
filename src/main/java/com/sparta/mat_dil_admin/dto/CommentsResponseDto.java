package com.sparta.mat_dil_admin.dto;

import com.sparta.mat_dil_admin.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentsResponseDto {
    private String accountId;
    private String restaurantName;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public CommentsResponseDto(Comment comment){
        this.accountId = comment.getUser().getAccountId();
        this.restaurantName = comment.getRestaurant().getRestaurantName();
        this.description = comment.getDescription();
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
