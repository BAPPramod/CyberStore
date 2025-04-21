package fi.haagahelia.cyberstore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String userId;
    private String username;
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;

    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}