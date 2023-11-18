package com.project.forestresourcesmanageapplication.models;

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
@Table(name = "administrations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class Administration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code")
	private String code;
	
	@Column(name = "name",nullable = false,length = 100)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "subordinate")
	private Administration subordinate;
	
	@ManyToOne
	@JoinColumn(name = "administrative_level_id")
	private AdministrativeLevel administrativeLevel;
}
