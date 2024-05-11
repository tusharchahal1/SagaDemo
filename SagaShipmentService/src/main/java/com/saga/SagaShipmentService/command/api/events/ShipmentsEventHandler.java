package com.saga.SagaShipmentService.command.api.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.saga.SagaCommonService.events.OrderShippedEvent;
import com.saga.SagaShipmentService.command.api.data.Shipment;
import com.saga.SagaShipmentService.command.api.data.ShipmentRepository;

@Component
public class ShipmentsEventHandler {

	private ShipmentRepository shipmentRepository;
		
	public ShipmentsEventHandler(ShipmentRepository shipmentRepository) {
		//super();
		this.shipmentRepository = shipmentRepository;
	}

	@EventHandler
	public void on(OrderShippedEvent event) {
		Shipment shipment = new Shipment();
		
		BeanUtils.copyProperties(event, shipment);
		shipmentRepository.save(shipment);
	}
}
