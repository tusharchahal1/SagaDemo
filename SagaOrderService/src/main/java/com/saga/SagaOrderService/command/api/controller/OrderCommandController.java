package com.saga.SagaOrderService.command.api.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saga.SagaOrderService.command.api.command.CreateOrderCommand;
import com.saga.SagaOrderService.command.api.model.OrderRestModel;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

	private CommandGateway commandGateway;
	public OrderCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createOrder(@RequestBody OrderRestModel orderRestModel) {
		
		String orderId = UUID.randomUUID().toString();
		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
				.orderId(orderId)
				.addressId(orderRestModel.getAddressId())
				.orderStatus("CREATED")
				.productId(orderRestModel.getProductId())
				.quantity(orderRestModel.getQuantity())
				.userId(orderRestModel.getUserId())
				.build();
		
		commandGateway.sendAndWait(createOrderCommand);
		
		return "Order Created";
	}
	
}
