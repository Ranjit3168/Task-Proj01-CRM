package com.bpl.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="SHOWROOM_TABLE")
@Data
public class ShowRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer showroomId;
	private String showroomName;
	private String showroomAddress;

	
	@OneToMany(targetEntity = CustomerEntity.class,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "showroom_Id",referencedColumnName = "showroomId")
	private List<CustomerEntity> cust;
}
