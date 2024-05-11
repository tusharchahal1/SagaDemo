package com.saga.SagaOrderService.command.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.saga.SagaCommonService.command.CompleteOrderCommand;
import com.saga.SagaCommonService.events.OrderCompletedEvent;
import com.saga.SagaOrderService.command.api.command.CreateOrderCommand;
import com.saga.SagaOrderService.command.api.events.OrderCreatedEvent;

@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;
	private String productId;
	private String userId;
	private String addressId;
	private Integer quantity;
	private String orderStatus;
	
	public OrderAggregate() {}
	
	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) {
		
		//Validate the Command
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
		AggregateLifecycle.apply(orderCreatedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderCreatedEvent event) {
		this.orderStatus = event.getOrderStatus();
		this.addressId = event.getAddressId();
		this.orderId = event.getOrderId();
		this.productId = event.getProductId();
		this.userId = event.getUserId();
		this.quantity = event.getQuantity();
		
	}
	
	@CommandHandler
	public void handle(CompleteOrderCommand completeOrderCommand) {
		
		//validate the command
		//Publish Order Complete Event
		OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder()
				.orderId(completeOrderCommand.getOrderId())
				.orderStatus(completeOrderCommand.getOrderStatus())
				.build();
		AggregateLifecycle.apply(orderCompletedEvent);
	}
	
	@EventSourcingHandler
	public void on(OrderCompletedEvent event) {
		this.orderStatus = event.getOrderStatus();
		
	}
	
}
