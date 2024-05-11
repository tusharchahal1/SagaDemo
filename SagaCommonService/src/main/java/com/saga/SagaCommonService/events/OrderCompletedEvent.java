package com.saga.SagaCommonService.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCompletedEvent {

	private String orderId;
	private String orderStatus;
	
}
