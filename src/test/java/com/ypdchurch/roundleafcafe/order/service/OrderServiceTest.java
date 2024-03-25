package com.ypdchurch.roundleafcafe.order.service;

import com.ypdchurch.roundleafcafe.order.domain.Order;
import com.ypdchurch.roundleafcafe.order.enums.OrderStatus;
import com.ypdchurch.roundleafcafe.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 상태 접수 성공 테스트")
    void acceptedOrderStatusSuccessTest() {
        //given
        Order order = Order.builder()
                .id(1L)
                .basketId(1L)
                .orderStatus(OrderStatus.ORDER_START)
                .totalPrice(new BigDecimal("245000"))
                .memberId(1L)
                .build();
        //when
        Order changeStatusOrder = order.accepted();
        when(orderRepository.save(changeStatusOrder)).thenReturn(changeStatusOrder);
        Order savedOrder = orderService.save(changeStatusOrder);
        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.ORDER_ACCEPTED);
    }

    @Test
    @DisplayName("주문 상태 접수 확인 성공 테스트")
    void confirmedOrderStatusSuccessTest() {
        //given
        Order order = Order.builder()
                .id(1L)
                .basketId(1L)
                .orderStatus(OrderStatus.ORDER_ACCEPTED)
                .totalPrice(new BigDecimal("245000"))
                .memberId(1L)
                .build();
        //when
        Order changeStatusOrder = order.confirmed();
        when(orderRepository.save(changeStatusOrder)).thenReturn(changeStatusOrder);

        Order savedOrder = orderService.save(changeStatusOrder);
        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.ORDER_CONFIRMED);
    }

    @Test
    @DisplayName("주문 상태 요리중 성공 테스트")
    void cookingOrderStatusTest() {
        //given
        Order order = Order.builder()
                .id(1L)
                .basketId(1L)
                .orderStatus(OrderStatus.ORDER_CONFIRMED)
                .totalPrice(new BigDecimal("245000"))
                .memberId(1L)
                .build();
        //when
        Order changeStatusOrder = order.confirmed();
        when(orderRepository.save(changeStatusOrder)).thenReturn(changeStatusOrder);

        Order savedOrder = orderService.save(changeStatusOrder);
        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.ORDER_CONFIRMED);
    }

    @Test
    @DisplayName("주문 상태 메뉴준비완료 성공 테스트")
    void menuAlreadyCompleteOrderStatusTest() {
        //given
        Order order = Order.builder()
                .id(1L)
                .basketId(1L)
                .orderStatus(OrderStatus.COOKING)
                .totalPrice(new BigDecimal("245000"))
                .memberId(1L)
                .build();
        //when
        Order changeStatusOrder = order.menuAlready();
        when(orderRepository.save(changeStatusOrder)).thenReturn(changeStatusOrder);

        Order savedOrder = orderService.save(changeStatusOrder);
        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.MENU_ALREADY_COMPLETE);
    }

    @Test
    @DisplayName("주문 상태 가져가기 완료 성공 테스트")
    void pickupCompleteOrderStatusTest() {
        //given
        Order order = Order.builder()
                .id(1L)
                .basketId(1L)
                .orderStatus(OrderStatus.MENU_ALREADY_COMPLETE)
                .totalPrice(new BigDecimal("245000"))
                .memberId(1L)
                .build();
        //when
        Order changeStatusOrder = order.pickup();
        when(orderRepository.save(changeStatusOrder)).thenReturn(changeStatusOrder);

        Order savedOrder = orderService.save(changeStatusOrder);
        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.PICK_UP_COMPLETE);
    }

    @Test
    @DisplayName("주문 상태 모두완료 성공 테스트")
    void wholeCompleteOrderStatusTest() {
        //given
        Order order = Order.builder()
                .id(1L)
                .basketId(1L)
                .orderStatus(OrderStatus.PICK_UP_COMPLETE)
                .totalPrice(new BigDecimal("245000"))
                .memberId(1L)
                .build();
        //when
        Order changeStatusOrder = order.wholeCompete();
        when(orderRepository.save(changeStatusOrder)).thenReturn(changeStatusOrder);

        Order savedOrder = orderService.save(changeStatusOrder);
        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.WHOLE_COMPLETE);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.INCLUDE, value = OrderStatus.class, names = {"ORDER_START",
            "ORDER_ACCEPTED", "ORDER_CONFIRMED", "COOKING",
            "MENU_ALREADY_COMPLETE", "PICK_UP_COMPLETE", "WHOLE_COMPLETE"})
    @DisplayName("주문 상태 취소 성공 테스트")
    void canceledOrderStatusTest(OrderStatus inputStatus) {
        //given
        Order order = Order.builder()
                .id(1L)
                .basketId(1L)
                .orderStatus(inputStatus)
                .totalPrice(new BigDecimal("245000"))
                .memberId(1L)
                .build();
        //when
        Order changeStatusOrder = order.cancel();
        when(orderRepository.save(changeStatusOrder)).thenReturn(changeStatusOrder);

        Order savedOrder = orderService.save(changeStatusOrder);
        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }
}