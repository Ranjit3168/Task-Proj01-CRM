package com.bpl.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bpl.entity.CustomerEntity;
import com.bpl.entity.ShowRoom;
import com.bpl.service.ICustomerService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class CustomerRestController {

	@Autowired
	private ICustomerService service;
	
	@PostMapping("/registerShowroom")
	public ResponseEntity<?> registerShowRoom(@RequestBody ShowRoom showroom){
		String msg=service.saveShowRoom(showroom);
		return new ResponseEntity<>(msg,HttpStatus.CREATED);
	} 
	
	@PostMapping("/save")
	public ResponseEntity<?> registerCustomer(@RequestBody CustomerEntity entity){
		String msg=service.saveCustomer(entity);
		return new ResponseEntity<>(msg,HttpStatus.CREATED);
	} 
	
	@GetMapping("/showAll")
	public ResponseEntity<?> showAllCustomer(){
		List<CustomerEntity> listCustomer=service.getAllCustomer();
		return new ResponseEntity<>(listCustomer,HttpStatus.OK);
	} 
	
	@GetMapping("/showById/{id}")
	public ResponseEntity<?> showCustomerById(@PathVariable Long id){
		CustomerEntity customer=service.getCustomerById(id);
		return new ResponseEntity<>(customer,HttpStatus.OK);
	} 
	
	
	@PutMapping("/update")
    public ResponseEntity<?> updateTouriest(@RequestBody CustomerEntity entity)  throws Exception{
			String msg=service.updateCustomer(entity);
			return new ResponseEntity<String>(msg,HttpStatus.OK);
    }
	
	@DeleteMapping("/delete/{id}")
	    public ResponseEntity<?> deleteTouriest(@PathVariable Long id)  throws Exception{
	    
		boolean isDeleted=service.deleteCustomerById(id);
		String responseMsg="";
		if(isDeleted) {
			responseMsg="Customer is Deleted";
		}else {
			responseMsg="Customer is not Deleted";
		}
		
		return new ResponseEntity<>(responseMsg,HttpStatus.OK);
	}
	    
	
	@GetMapping("/excel")
	public void excelReport(HttpServletResponse response)  throws Exception{
		response.setContentType("application/octet-stream");
		
		String headerKey="Content-Disposition";
		String headerValue="attachment;filename=data.xls";
		response.setHeader(headerKey, headerValue);
		service.generateExcel(response);
	}
	
	@GetMapping("/pdf")
	  public void generatePdfFile(HttpServletResponse response) throws Exception {
	  
	    response.setContentType("application/pdf");
	    String headerkey = "Content-Disposition";
	    String headervalue = "attachment; filename=Report.pdf";
	    response.setHeader(headerkey, headervalue);
	    service.generatePdf(response);
	  }
	
	@GetMapping("/csv")
	@Scheduled(cron="*/10 * * * * *")
	public ResponseEntity<?>  generateCSVFile(){
		String msg=service.createCSV();
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
}
