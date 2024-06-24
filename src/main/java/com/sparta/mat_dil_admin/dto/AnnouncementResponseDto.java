package com.sparta.mat_dil_admin.dto;

import com.sparta.mat_dil_admin.entity.AnnouncementPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnnouncementResponseDto {
    private String title;
    private String contents;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public AnnouncementResponseDto(AnnouncementPost announcementPost){
        this.title = announcementPost.getTitle();
        this.contents = announcementPost.getContents();
        this.createAt = announcementPost.getCreatedAt();
        this.modifiedAt = announcementPost.getModifiedAt();
    }
}
