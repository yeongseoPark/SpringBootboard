package springboot.web;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.raw.Mod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import springboot.config.auth.dto.SessionUser;
import springboot.service.posts.PostsService;
import springboot.web.dto.posts.PostsResponseDto;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class PostsController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/posts/update/{id}") // 수정화면 이동
    public String postsUpdate(@PathVariable Long id, Model model)  {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        PostsResponseDto dto = postsService.findById(id);

        if (! (user.getEmail().trim().equals(dto.getAuthor().getEmail().trim()))) {
            throw new IllegalArgumentException("Only the writer of the post can access");
        }

        model.addAttribute("post", dto);
        return "posts-update";
    }
}
