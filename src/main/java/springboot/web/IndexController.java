package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import springboot.config.auth.dto.SessionUser;
import springboot.domain.posts.Posts;
import springboot.service.posts.PostsService;
import springboot.web.dto.comment.CommentResponseDto;
import springboot.web.dto.posts.PostsResponseDto;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    /* 페이징 1 */
    @GetMapping("/")
    public String index(Model model, @PageableDefault(size = 3)Pageable pageable) {
        Page<Posts> list = postsService.findAllDesc(pageable);

        model.addAttribute("posts", list);
        model.addAttribute("prev", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", list.hasNext());
        model.addAttribute("hasPrev", list.hasPrevious());

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("loginUserName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String PostsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/{id}")
    public String PostsInfo(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        List<CommentResponseDto> comments = dto.getComments();

        if (comments != null) {
            model.addAttribute("comment",comments);
        }

        model.addAttribute("post",dto);
        return "posts";
    }
}
