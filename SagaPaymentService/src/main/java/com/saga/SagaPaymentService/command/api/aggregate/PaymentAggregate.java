package com.saga.SagaPaymentService.command.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.saga.SagaCommonService.command.ValidatePaymentCommand;
import com.saga.SagaCommonService.events.PaymentProcessEvent;
import com.saga.SagaCommonService.model.CardDetails;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class PaymentAggregate {

	@AggregateIdentifier
	private String paymentId;
	private String orderId;
	private String paymentStatus;
	//private CardDetails cardDetails;
	
	public PaymentAggregate() { }
	
	@CommandHandler
	public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
		
		//Validate Payment Details
		//On Payment Success, Publish Payment Process Event
		log.info("Executing ValidatePaymentCommand for OrderID: {} "
				+ "and PaymentId: {}", validatePaymentCommand.getOrderId(),
				validatePaymentCommand.getPaymentId());
		
		PaymentProcessEvent paymentProcessEvent 
			= new PaymentProcessEvent(validatePaymentCommand.getPaymentId(),
					validatePaymentCommand.getOrderId());
		
		AggregateLifecycle.apply(paymentProcessEvent);
		log.info("PaymentProcessEvent Applied");
				
	}
	
	@EventSourcingHandler
	public void on(PaymentProcessEvent event){
		this.paymentId = event.getPaymentId();
		this.orderId = event.getOrderId();
		
	}
	
	
}
