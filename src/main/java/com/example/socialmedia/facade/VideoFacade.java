package com.example.socialmedia.facade;

import com.example.socialmedia.dto.VideoDTO;
import com.example.socialmedia.entity.Video;
import org.springframework.stereotype.Component;

@Component
public class VideoFacade {
    public VideoDTO videoToVideoDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(video.getId());

        return videoDTO;
    }
}
