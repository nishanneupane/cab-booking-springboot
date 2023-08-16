package com.ride.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
	
	private String paymentId;
	private PaymentStatus paymentStatus;
	private String raxorPaymentLinkId;
	private String razorPaymentLinkReferenceId;
	private String raxorPaymentLinkStatus;
	private String razorPaymentId;
	

}
