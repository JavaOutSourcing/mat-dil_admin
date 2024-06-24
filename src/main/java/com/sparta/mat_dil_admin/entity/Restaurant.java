package com.sparta.mat_dil_admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant")
public class Restaurant extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String description;

    @Column
    private Boolean pinned;

    @Column(nullable = false)
    private Long likes = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantLike> restaurantLikes;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Order> orders;

    public void pinnedFalse(){
        this.pinned = false;
    }
    public void pinnedTrue(){
        this.pinned = true;
    }
}
