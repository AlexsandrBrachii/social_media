package com.example.socialmedia.service;

import com.example.socialmedia.entity.Post;
import com.example.socialmedia.entity.User;
import com.example.socialmedia.entity.Video;
import com.example.socialmedia.exceptions.VideoNotFoundException;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class VideoService {
    public static final Logger LOG = LoggerFactory.getLogger(VideoService.class);


        @Autowired
        private final VideoRepository videoRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;


    public VideoService(VideoRepository videoRepository, PostRepository postRepository, UserRepository userRepository) {

            this.videoRepository = videoRepository;
            this.postRepository = postRepository;
            this.userRepository = userRepository;
        }
        public static byte[] compressVideo(byte[] data) {
            Deflater deflater = new Deflater();
            deflater.setInput(data);
            deflater.finish();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
            byte[] segment = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(segment);
                byteArrayOutputStream.write(segment, 0, count);
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                LOG.error("Cannot compress video");
            }
            System.out.println("Compressed video = " + byteArrayOutputStream.toByteArray().length);
            return byteArrayOutputStream.toByteArray();
        }

        private static byte[] decompressImage(byte[] data) {
            Inflater inflater = new Inflater();
            inflater.setInput(data);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
            byte[] segment = new byte[720];

            try {
                while (!inflater.finished()) {
                    int count = inflater.inflate(segment);
                    byteArrayOutputStream.write(segment, 0, count);
                }
                byteArrayOutputStream.close();
            } catch (IOException | DataFormatException e) {
                LOG.error(("cannot decompress video"));
            }
            return byteArrayOutputStream.toByteArray();

        }

        public <T> Collector<T, ?, T> singlePostCollector() {
            return Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> {
                        if (list.size() != 1) {
                            throw new IllegalStateException();
                        }
                        return list.get(0);
                    }
            );
    }

    public Video uploadVideoToProfile(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        Video userProfileVideo = videoRepository.findByUserId(user.getId()).orElse( null);

        if (!ObjectUtils.isEmpty(userProfileVideo)) {
            videoRepository.delete(userProfileVideo);
        }
        Video video = new Video();
        video.setUserId(user.getId());
        video.setVideoBytes(compressVideo(file.getBytes()));
        LOG.info("Upload video to user {}", user.getId());

        return videoRepository.save(video);
    }

    public Video getUserProfileVideo(Principal principal) {
        User user = getUserByPrincipal(principal);

        Video userProfileVideo = videoRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileVideo)) {
            userProfileVideo.setVideoBytes(decompressImage(userProfileVideo.getVideoBytes()));
        }
        return userProfileVideo;
    }

    public Video uploadVideoToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(singlePostCollector());

        Video video = new Video();
        video.setPostId(post.getId());
        video.setVideoBytes(compressVideo(file.getBytes()));
        video.setName(file.getName());
        LOG.info("Upload video to post {}", post.getId());

        return videoRepository.save(video);
    }

    public Video getPostVideo(Long postId) {
        Video postVideo = videoRepository.findByPostId(postId)
                .orElseThrow(() -> new VideoNotFoundException("Video cannot found for post" + postId));
        if (!ObjectUtils.isEmpty(postVideo)) {
            postVideo.setVideoBytes(decompressImage((postVideo.getVideoBytes())));
        }
        return postVideo;
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }




}

