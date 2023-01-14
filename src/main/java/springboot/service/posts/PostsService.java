package springboot.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.posts.Posts;
import springboot.domain.posts.PostsRepository;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;
import springboot.service.UserDetailService;
import springboot.web.dto.posts.PostsListResponseDto;
import springboot.web.dto.posts.PostsResponseDto;
import springboot.web.dto.posts.PostsSaveRequestDto;
import springboot.web.dto.posts.PostsUpdateRequestDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    private final UserDetailService userDetailService;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        User user = userDetailService.returnUser();
        requestDto.setUser(user);

        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow
                (() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));
        postsRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        return new PostsResponseDto(entity);
    }

    /* 페이징 1 */
    @Transactional(readOnly = true)
    public Page<Posts> findAllDesc(Pageable pageable) {
        return postsRepository.findAllDesc(pageable);
    }
}
