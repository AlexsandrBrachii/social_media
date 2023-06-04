package com.example.socialmedia.entity;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import javax.persistence.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;

    @JsonIgnore
    private Long userId;

    @JsonIgnore
    private Long postId;
}


