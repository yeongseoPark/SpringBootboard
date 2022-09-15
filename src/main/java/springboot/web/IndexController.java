package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import springboot.config.auth.dto.SessionUser;
import springboot.domain.posts.Posts;
import springboot.service.posts.PostsService;
import springboot.web.dto.CommentResponseDto;
import springboot.web.dto.PostsResponseDto;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index"; // 머스태치 스타터가 앞의 경로 src/main/resources/templates로, 뒤의 확장자 .mustache 로 자동 지정
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
