package com.example.socialmedia.service;


import com.example.socialmedia.dto.PostDTO;
import com.example.socialmedia.entity.Image;
import com.example.socialmedia.entity.Post;
import com.example.socialmedia.entity.User;
import com.example.socialmedia.entity.Video;
import com.example.socialmedia.exceptions.PostNotFoundException;
import com.example.socialmedia.repository.ImageRepository;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;

    @Autowired

    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository, VideoRepository videoRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.videoRepository = videoRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreateDate();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post not found for username:" + user.getEmail()));
    }

    public List<Post> getAllPostsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setTitle(postDTO.getTitle());
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setLikes(0);

        LOG.info("Create new post for user: {}", user.getEmail());
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<Image> image = imageRepository.findByPostId(post.getId());
        Optional<Video> video = videoRepository.findByPostId(post.getId());
        postRepository.delete(post);
        image.ifPresent(imageRepository::delete);
        video.ifPresent(videoRepository::delete);
    }

    public Post setLikeToPost(long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        Optional<String> userLikedPost = post.getLikedUsers()
                .stream()
                .filter(u -> u.equals(username)).findAny();

        // если пользовать есть в списке "лайкнувших"
        if (userLikedPost.isPresent()) {
            // dislike
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }

        return postRepository.save(post);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}





