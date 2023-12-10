package com.project.forestresourcesmanageapplication.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class User {
	@Id
	@Column(name = "username")
	private String username;

	@Column(name = "first_name", length = 100)
	private String firstName;
	
	@Column(name = "last_name", length = 100)
	private String lastName;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false, length = 200)
	private String password;
	
	@Column(name = "avatar" , length = 150)
	private String avatar;

	@Column(name = "address", length = 100)
	private String address;

	@Column(name = "birth_date")
	private LocalDate birthDate;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private Role role ;
	
	@Column(name = "otp")
	private String otp;

	@Column(name = "otp_generated_time")
	private LocalDateTime otpGeneratedTime;
	
	@ManyToOne
	@JoinColumn(name = "administration_code")
	private Administration administration;
}
