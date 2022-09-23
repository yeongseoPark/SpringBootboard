package springboot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot.domain.user.User;
import springboot.domain.user.UserRepository;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public User returnUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName;

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }

        int start = userName.indexOf("email")+6;
        int end = userName.indexOf(".com,")+4;
        String email = userName.substring(start, end);

        User user = userRepository.findByEmail(email).orElse(null);

        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
