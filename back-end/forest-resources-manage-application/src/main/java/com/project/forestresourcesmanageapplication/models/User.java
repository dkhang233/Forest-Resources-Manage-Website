package com.project.forestresourcesmanageapplication.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "username")
	private String username;

	@Column(name = "first_name", length = 100)
	private String firstName;
	
	@Column(name = "last_name", length = 100)
	private String lastName;

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
	private Role role ;
	
	@ManyToOne
	@JoinColumn(name = "administrative_code")
	private Administrative administrative;
}
