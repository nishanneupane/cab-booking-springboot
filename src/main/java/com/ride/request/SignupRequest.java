package com.ride.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

	private String fullName;

	@NotBlank(message = "email is required")
	@Email(message = "Email should be valid")
	private String email;

	private String password;

	private String mobile;

}
