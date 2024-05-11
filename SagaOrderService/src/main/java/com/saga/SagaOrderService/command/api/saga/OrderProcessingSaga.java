package com.saga.SagaOrderService.command.api.saga;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.saga.SagaCommonService.command.CompleteOrderCommand;
import com.saga.SagaCommonService.command.ShipOrderCommand;
import com.saga.SagaCommonService.command.ValidatePaymentCommand;
import com.saga.SagaCommonService.events.OrderCompletedEvent;
import com.saga.SagaCommonService.events.OrderShippedEvent;
import com.saga.SagaCommonService.events.PaymentProcessEvent;
import com.saga.SagaCommonService.model.CardDetails;
import com.saga.SagaCommonService.model.User;
import com.saga.SagaCommonService.queries.GetUserPaymentDetailsQuery;
import com.saga.SagaOrderService.command.api.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderProcessingSaga {
	
	@Autowired
	private transient CommandGateway commandGateway;
	
	@Autowired
	private transient QueryGateway queryGateway;
	
	/*
	 * @Autowired public OrderProcessingSaga(CommandGateway commandGateway,
	 * QueryGateway queryGateway) { //super(); this.commandGateway = commandGateway;
	 * this.queryGateway = queryGateway;
	 }*/
	
	public OrderProcessingSaga() {}

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	private void handle(OrderCreatedEvent event) {
		log.info("OrderCreatedEvent in Saga for Order Id: {} ", event.getOrderId());
		
		GetUserPaymentDetailsQuery getUserPaymentDetailsQuery
		 = new GetUserPaymentDetailsQuery(event.getUserId());
		
		User user = null;
		
		try {
			user = queryGateway.query(getUserPaymentDetailsQuery, 
					ResponseTypes.instanceOf(User.class)).join();
		}catch (Exception e) {
			log.error(e.getMessage());
			//Start the compensating transaction
		}

		CardDetails cardDetails = CardDetails.builder()
				.name("Tuhar Chahal")
				.cardNo("9899889866")
				.cvv(123)
				.validUntilMonth("01")
				.validUntilYear("2029")
				.build();

				
		ValidatePaymentCommand validatePaymentCommand
				= ValidatePaymentCommand
				.builder()
				.cardDetails(cardDetails)
				.orderId(event.getOrderId())
				.paymentId(UUID.randomUUID().toString())
				.build();
		
		commandGateway.sendAndWait(validatePaymentCommand);
		
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	private void handle(PaymentProcessEvent event) {
		
		log.info("PaymentProcessEvent in Saga for Order Id: {} ", event.getOrderId());
		
		try {
			
			ShipOrderCommand shipOrderCommand
			= ShipOrderCommand.builder()
			.shipmentId(UUID.randomUUID().toString())
			.orderId(event.getOrderId())
			.build();
			commandGateway.send(shipOrderCommand);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			//Start the compensating transaction
		}
		
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderShippedEvent event) {
		log.info("OrderShippedEvent in Saga for Order Id: {} ", event.getOrderId());
		
		CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
				.orderId(event.getOrderId())
				.orderStatus("APPROVED")
				.build();
		
		commandGateway.send(completeOrderCommand);
	}
	
	@SagaEventHandler(associationProperty = "orderId")
	@EndSaga
	public void handle(OrderCompletedEvent event) {
		
		log.info("OrderCompletedEvent in Saga for Order Id: {} ", event.getOrderId());
	}

}
