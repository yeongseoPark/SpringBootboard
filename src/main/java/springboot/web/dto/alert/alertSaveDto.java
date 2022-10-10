package springboot.web.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.domain.alert.Alert;
import springboot.domain.user.User;

@Data
@NoArgsConstructor
public class alertSaveDto {
    private User user;
    private double price;
    private double percentage;
    private String ticker;

    @Builder
    public alertSaveDto(User user, double price, double percentage, String ticker) {
        this.user = user;
        this.price = price;
        this.percentage = percentage;
        this.ticker = ticker;
    }

    public Alert toEntity() {
        return Alert.builder()
                .user(user)
                .price(price)
                .percentage(percentage)
                .ticker(ticker)
                .build();
    }
}
