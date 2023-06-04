package com.example.socialmedia.entity;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import javax.persistence.*;

@Data
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] videoBytes;

    @JsonIgnore
    private Long userId;

    @JsonIgnore
    private Long postId;

}












