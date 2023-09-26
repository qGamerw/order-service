package ru.sber.orderservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sber.orderservice.entities.enums.EStatusOrders;

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
    private long courierId;

    @Column
    private long branchOfficeId;


    @Column(nullable = false)
    @Size(max = 20)
    private String clientName;

    @Column
    @Size(max = 20)
    private String description;

    @Column(nullable = false)
    @Size(max = 10)
    private int clientPhoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer flat;

    @Column
    private Integer frontDoor;

    @Column
    private Integer floor;

    @Column(nullable = false)
    private EStatusOrders statusOrders;

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

    @Column
    private BigDecimal latitude;

    @Column
    private BigDecimal longitude;

}
