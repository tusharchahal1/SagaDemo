package com.saga.SagaShipmentService.command.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.saga.SagaCommonService.command.ShipOrderCommand;
import com.saga.SagaCommonService.events.OrderShippedEvent;

@Aggregate
public class ShipmentAggregate {

	@AggregateIdentifier
	private String shipmentId;
	private String orderId;
	private String ShipmentStatus;
	
	public ShipmentAggregate() {
		//super();
	}
	
	@CommandHandler
	public ShipmentAggregate(ShipOrderCommand shipOrderCommand) {
		//super();
		//Validate the command
		//Publish the order shipment event
		OrderShippedEvent orderShippedEvent
			= OrderShippedEvent.builder()
			.orderId(shipOrderCommand.getOrderId())
			.shipmentId(shipOrderCommand.getShipmentId())
			.ShipmentStatus("COMPLETED")
			.build();
		
		AggregateLifecycle.apply(orderShippedEvent);
		
	}
	
	@EventSourcingHandler
	public void on(OrderShippedEvent event) {
		this.orderId = event.getOrderId();
		this.shipmentId = event.getShipmentId();
		this.ShipmentStatus = event.getShipmentStatus();
		
	}
	
}
