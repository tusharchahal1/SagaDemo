package com.saga.SagaPaymentService.command.api.data;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

	@Id
	private String paymentId;
	private String orderId;
	private Date timestamp;
	private String paymentStatus;
	
}
