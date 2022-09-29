package springboot.domain.comment.alert;

import lombok.Builder;
import springboot.domain.alert.Alert;
import springboot.domain.user.User;

public class alertResponseDto {
    private double price;
    private double percentage;

    public alertResponseDto(Alert entity) {
        this.price = entity.getPrice();
        this.percentage = entity.getPercentage();
    }
}
