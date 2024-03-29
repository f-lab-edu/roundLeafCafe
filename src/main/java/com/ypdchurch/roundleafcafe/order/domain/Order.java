package com.ypdchurch.roundleafcafe.order.domain;

import com.ypdchurch.roundleafcafe.common.domain.BaseEntity;
import com.ypdchurch.roundleafcafe.member.domain.Member;
import com.ypdchurch.roundleafcafe.order.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
//@SuperBuilder
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", updatable = false)
    private Long id;

    @NotNull
    @Column(name = "member_id", updatable = false)
    private Long memberId;

    @NotNull
    @Column(name = "basket_id")
    private Long basketId;

    @NotNull
    @Min(value = 0, message = "총 가격은 0원 이상이어야합니다. 입력된 금액 : ${totalPrice}")
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "requests")
    private String requests;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this.of(this);
    }

    public Order orderAccept() {
        this.orderStatus = OrderStatus.ORDER_ACCEPTED;
        return this;
    }

    private Order of(Order order) {
        return Order.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .orderStatus(order.getOrderStatus())
                .requests(order.getRequests())
                .basketId(order.getBasketId())
                .build();
    }

}
