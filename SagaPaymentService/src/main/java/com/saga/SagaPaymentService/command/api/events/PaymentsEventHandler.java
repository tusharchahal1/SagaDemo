package com.saga.SagaPaymentService.command.api.events;

import java.util.Date;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.saga.SagaCommonService.events.PaymentProcessEvent;
import com.saga.SagaPaymentService.command.api.data.Payment;
import com.saga.SagaPaymentService.command.api.data.PaymentRepository;

@Component
public class PaymentsEventHandler {

	private PaymentRepository paymentRepository;
	
	public PaymentsEventHandler(PaymentRepository paymentRepository) {
		//super();
		this.paymentRepository = paymentRepository;
	}

	@EventHandler
	public void on(PaymentProcessEvent event) {
		Payment payment = Payment.builder()
				.paymentId(event.getPaymentId())
				.orderId(event.getOrderId())
				.paymentStatus("COMPLETED")
				.timestamp(new Date())
				.build();
		
		paymentRepository.save(payment);
	}
}
