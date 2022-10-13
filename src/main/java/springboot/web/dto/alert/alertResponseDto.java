package springboot.web.dto.alert;

import lombok.Builder;
import springboot.domain.alert.Alert;
import springboot.domain.user.User;

public class alertResponseDto {

    private Long id;
    private double price;
    private double percentage;
    private String ticker;

    public alertResponseDto(Alert entity) {
        this.id = entity.getId();
        this.price = entity.getPrice();
        this.percentage = entity.getPercentage();
        this.ticker = entity.getTicker();
    }
}