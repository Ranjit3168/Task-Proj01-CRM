package com.bpl.service;

import java.util.List;


import com.bpl.entity.ShowRoom;
import com.bpl.entity.CustomerEntity;

import jakarta.servlet.http.HttpServletResponse;

public interface ICustomerService {

	public  String saveShowRoom(ShowRoom company);
	public  String saveCustomer(CustomerEntity entity);
	public List<CustomerEntity> getAllCustomer();
	public CustomerEntity getCustomerById(Long Id);
	public String updateCustomer(CustomerEntity entity);
	public boolean deleteCustomerById(Long id);
	public void generateExcel(HttpServletResponse response) throws Exception;
	public void generatePdf(HttpServletResponse response) throws Exception;
	public String createCSV();
	
}
