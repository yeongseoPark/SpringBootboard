package springboot.domain.alert;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertType {
    Upper_break("u_break"),
    Lower_break("l_break"),
    Upper_percent("u_percent"),
    Lower_percent("l_percent");

    private final String key;
}

