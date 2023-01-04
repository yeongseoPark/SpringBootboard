package springboot.web.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.domain.alert.Alert;
import springboot.domain.alert.AlertType;
import springboot.domain.user.User;

@Data
@NoArgsConstructor
public class alertSaveDto {
    private User user;
    private AlertType alertType;
    private double price;
    private double percentage;
    private String ticker;

    @Builder
    public alertSaveDto(User user, double price, double percentage, String ticker, String alertType) {
        this.alertType = AlertType.valueOf(alertType);
        this.user = user;
        this.price = price;
        this.percentage = percentage;
        this.ticker = ticker;
    }

    public Alert toEntity() {
        System.out.println(alertType.getKey());
        return Alert.builder()
                .user(user)
                .price(price)
                .percentage(percentage)
                .ticker(ticker)
                .alertType(alertType)
                .build();
    }
}
