package springboot.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springboot.service.posts.PostsService;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        return "index"; // 머스태치 스타터가 앞의 경로 src/main/resources/templates로, 뒤의 확장자 .mustache 로 자동 지정
    }

    @GetMapping("/posts/save")
    public String PostsSave() {
        return "posts-save";
    }
}
