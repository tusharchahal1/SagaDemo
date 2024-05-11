package com.saga.SagaCommonService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetails {

	private String name;
	private String cardNo;
	private String validUntilMonth;
	private String validUntilYear;
	private int cvv;
}
