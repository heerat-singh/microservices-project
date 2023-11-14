package com.heeratsingh.orderservice.service;

import com.heeratsingh.orderservice.dto.InventoryResponse;
import com.heeratsingh.orderservice.dto.OrderLineItemsDto;
import com.heeratsingh.orderservice.dto.OrderRequest;
import com.heeratsingh.orderservice.model.Order;
import com.heeratsingh.orderservice.model.OrderLineItems;
import com.heeratsingh.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCode = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        //call inventory service

        InventoryResponse[] inventoryResponses =webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder
                        .queryParam("skuCode",skuCode).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean result= Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);

        if(result){
            orderRepository.save(order);
            log.info("Order Saved in DB");
        }
        else {
            throw new IllegalArgumentException("Product not in inventory, try later");
        }
        return "Order Saved";

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
