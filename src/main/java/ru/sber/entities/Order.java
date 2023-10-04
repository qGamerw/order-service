package ru.sber.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.entities.enums.EStatusOrders;
import ru.sber.models.ClientOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Заказ
 */
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long clientId;

    @Column
    private Long courierId;

    @Column
    private long branchOfficeId;

    @Column(nullable = false)
    @Size(max = 20)
    private String clientName;

    @Column
    @Size(max = 100)
    private String description;

    @Column(nullable = false)
    private int clientPhoneNumber;

    @Column(nullable = false)
    private String address;

    @Column
    private String branchAddress;

    @Column(nullable = false)
    private Integer flat;

    @Column(nullable = false)
    private Integer weight;

    @Column
    private Integer frontDoor;

    @Column
    private Integer floor;

    @Column(nullable = false)
    private EStatusOrders statusOrders;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderTime;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startCookingTime;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endCookingTime;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deliveryTime;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private String refusalReason;

    public Order(ClientOrder clientOrder) {
        this.clientId = clientOrder.getClientId();
        this.clientName = clientOrder.getClientName();
        this.description = clientOrder.getDescription();
        this.clientPhoneNumber = clientOrder.getClientPhoneNumber();
        this.address = clientOrder.getAddress();
        this.flat = clientOrder.getFlat();
        this.frontDoor = clientOrder.getFrontDoor();
        this.floor = clientOrder.getFloor();

        this.orderTime = LocalDateTime.now();
        this.statusOrders = EStatusOrders.NOT_PAID;
        this.price = BigDecimal.ONE;
        this.weight = 0;
    }
}
