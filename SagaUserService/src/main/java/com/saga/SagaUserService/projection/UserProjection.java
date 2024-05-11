package com.saga.SagaUserService.projection;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.saga.SagaCommonService.model.CardDetails;
import com.saga.SagaCommonService.model.User;
import com.saga.SagaCommonService.queries.GetUserPaymentDetailsQuery;

@Component
public class UserProjection {

	@QueryHandler
	public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
		/*
		 * Card details must be saved in DB against User Info
		 * Get user id from 'query.getUserId()' and must fetch user
		 * details from DB. currently this is static.
		 * */
		
		CardDetails cardDetails = CardDetails.builder()
				.name("Tuhar Chahal")
				.cardNo("9899889866")
				.cvv(123)
				.validUntilMonth("01")
				.validUntilYear("2029")
				.build();
		
		return User.builder()
				.userId(query.getUserId())
				.firstName("Tushar")
				.lastName("Chahal")
				.cardDetails(cardDetails)
				.build();
	}
}
