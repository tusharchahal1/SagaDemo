package com.saga.SagaOrderService.command.api.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.saga.SagaCommonService.events.OrderCompletedEvent;
import com.saga.SagaOrderService.command.api.data.Order;
import com.saga.SagaOrderService.command.api.data.OrderRepository;

@Component
public class OrderEventsHandler {

	private OrderRepository orderRepository;
	
	public OrderEventsHandler(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@EventHandler
	public void on(OrderCreatedEvent event) {
		Order order = new Order();
		BeanUtils.copyProperties(event, order);
		orderRepository.save(order);
	}
	
	@EventHandler
	public void on(OrderCompletedEvent event) {
		
		Order order = orderRepository.findById(event.getOrderId()).get();
		
		order.setOrderStatus(event.getOrderStatus());
		orderRepository.save(order);
		
	}
}
