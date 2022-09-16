package springboot.service.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.domain.comment.Comment;
import springboot.domain.comment.CommentRepository;
import springboot.domain.posts.Posts;
import springboot.domain.posts.PostsRepository;
import springboot.web.dto.comment.CommentSaveRequestDto;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;

    @Transactional
    public Long commentSave(CommentSaveRequestDto requestDto, Long id) {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다"));
        requestDto.setPosts(post);

        return commentRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id = " + id));

        commentRepository.delete(comment);
    }

}
