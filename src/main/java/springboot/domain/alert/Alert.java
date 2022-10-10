package springboot.domain.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private double price;

    @Column
    @NotNull
    private String ticker;

    @Column
    private double percentage;

    @Builder
    public Alert(User user, double price, double percentage) {
        this.user = user;
        this.price = price;
        this.percentage = percentage;
    }
}
