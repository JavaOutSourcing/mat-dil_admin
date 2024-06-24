package com.sparta.mat_dil_admin.entity;

import com.sparta.mat_dil_admin.dto.AnnouncementRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementPost extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public AnnouncementPost(AnnouncementRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents =requestDto.getContents();
    }
}
