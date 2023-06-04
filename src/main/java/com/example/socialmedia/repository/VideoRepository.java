package com.example.socialmedia.repository;
import com.example.socialmedia.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByUserId(Long userId);

    Optional<Video> findByPostId(Long postId);
}

