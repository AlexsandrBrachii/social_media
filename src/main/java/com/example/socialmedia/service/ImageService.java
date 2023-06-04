package com.example.socialmedia.service;

import com.example.socialmedia.entity.Image;
import com.example.socialmedia.entity.Post;
import com.example.socialmedia.entity.User;
import com.example.socialmedia.exceptions.ImageNotFoundException;
import com.example.socialmedia.repository.ImageRepository;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
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
public class ImageService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, PostRepository postRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public static byte[] compressImage(byte[] data) {
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
            LOG.error("Cannot compress image");
        }
        System.out.println("Compressed image size = " + byteArrayOutputStream.toByteArray().length);
        return byteArrayOutputStream.toByteArray();
    }

    private static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        byte[] segment = new byte[1024];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(segment);
                byteArrayOutputStream.write(segment, 0, count);
            }
            byteArrayOutputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error(("cannot decompress image"));
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


    public Image uploadImageToProfile(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse( null);

        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(compressImage(file.getBytes()));
        image.setName(file.getName());
        LOG.info("Create image to user {}", user.getId());

        return imageRepository.save(image);
    }

    public Image getUserProfileImage(Principal principal) {
        User user = getUserByPrincipal(principal);

        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            userProfileImage.setImageBytes(decompressImage(userProfileImage.getImageBytes()));
        }
        return userProfileImage;
    }

    public Image uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(singlePostCollector());

        Image image = new Image();
        image.setPostId(post.getId());
        image.setImageBytes(compressImage(file.getBytes()));
        image.setName(file.getName());
        LOG.info("Create image to post {}", post.getId());

        return imageRepository.save(image);
    }

    public Image getPostImage(Long postId) {
        Image postImage = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Image cannot found for post" + postId));
        if (!ObjectUtils.isEmpty(postImage)) {
            postImage.setImageBytes(decompressImage((postImage.getImageBytes())));
        }
        return postImage;
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }

}

