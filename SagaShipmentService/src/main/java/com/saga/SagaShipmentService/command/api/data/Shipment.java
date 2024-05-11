package com.saga.SagaShipmentService.command.api.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Shipment {

	@Id
	private String shipmentId;
	private String orderId;
	private String ShipmentStatus;
}
