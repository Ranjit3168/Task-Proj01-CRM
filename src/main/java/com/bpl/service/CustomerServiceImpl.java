package com.bpl.service;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.bpl.entity.ShowRoom;
import com.bpl.entity.CustomerEntity;
import com.bpl.repository.IShowRoomRepository;
import com.bpl.repository.ICustomerRepository;
import com.bpl.utils.EmailUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerRepository customerRepo;
	
	@Autowired
	private IShowRoomRepository showroomRepo;
	
	@Autowired
	private EmailUtils emailUtils;

	@Override
	public String saveShowRoom(ShowRoom showroom) {
		Integer idVal=showroomRepo.save(showroom).getShowroomId();	
		return "Company Saved With Id Value"+idVal;
	}


	@Override
	public String saveCustomer(CustomerEntity entity) {
		Long idVal=customerRepo.save(entity).getCustomerId();	
		String subject="Hello";
		String fileName="EMAIL-BODY.txt";
		String body=readEmailBody(entity.getFullName(),fileName );
		emailUtils.sendEmail(entity.getEmail(), subject, body);
		return "Customer Saved With Id"+idVal;
	}

	@Override
	public List<CustomerEntity> getAllCustomer() {
		List<CustomerEntity> listCustomer=customerRepo.findAll();
		return listCustomer;
	}

	@Override
	public CustomerEntity getCustomerById(Long Id) {
		Optional<CustomerEntity> opt=customerRepo.findById(Id);
		if(opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	@Override
	public String updateCustomer(CustomerEntity entity) {
		Optional<CustomerEntity> opt = customerRepo.findById(entity.getCustomerId());
		if(opt.isPresent()) {
			customerRepo.save(entity);
			return "Customer Data Is Updated having Customer Id "+entity.getCustomerId();
		}
		return "Customer Not Found";
	}

	@Override
	public boolean deleteCustomerById(Long Id) {
		
		boolean status=false;
		try {
			customerRepo.deleteById(Id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public void generateExcel(HttpServletResponse  response) throws Exception{
		List<CustomerEntity> entities=customerRepo.findAll();
		
		HSSFWorkbook workbook =new  HSSFWorkbook();
		HSSFSheet sheet=workbook.createSheet();
		HSSFRow headerRow=sheet.createRow(0);
		
		headerRow.createCell(0).setCellValue("CUSTOMER_ID");
		headerRow.createCell(1).setCellValue("FULLNAME");
		headerRow.createCell(2).setCellValue("EMAIL");
		headerRow.createCell(3).setCellValue("Mobile");
		headerRow.createCell(4).setCellValue("ADDRESS");
		headerRow.createCell(5).setCellValue("PURCHASE_DATE");
		int i=1;
		for(CustomerEntity entity:entities) {
			
			HSSFRow dataRow=sheet.createRow(i);
			dataRow.createCell(0).setCellValue(entity.getCustomerId());
			dataRow.createCell(1).setCellValue(entity.getFullName());
			dataRow.createCell(2).setCellValue(entity.getEmail());
			dataRow.createCell(3).setCellValue(entity.getMobile());
			dataRow.createCell(4).setCellValue(entity.getAddress());
			dataRow.createCell(5).setCellValue(entity.getCreateDate());
			i++;
		};
		               ServletOutputStream  outputStream = response.getOutputStream();
		               workbook.write(outputStream);
		               workbook.close();
		               outputStream.close();
	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
		List<CustomerEntity> entities=customerRepo.findAll();
	    // Creating the Object of Document
	    Document document = new Document(PageSize.A4);
	    // Getting instance of PdfWriter
	    PdfWriter.getInstance(document, response.getOutputStream());
	    // Opening the created document to change it
	    document.open();
	    // Creating font
	    // Setting font style and size
	    Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    fontTiltle.setSize(20);
	    // Creating paragraph
	    Paragraph paragraph1 = new Paragraph("Search Reports", fontTiltle);
	    // Aligning the paragraph in the document
	    paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
	    // Adding the created paragraph in the document
	    document.add(paragraph1);
	    // Creating a table of the 4 columns
	    PdfPTable table = new PdfPTable(6);
	    // Setting width of the table, its columns and spacing
	    table.setWidthPercentage(100f);
	    table.setWidths(new int[] {3,3,3,3,3,3});
	    table.setSpacingBefore(5);
	    // Create Table Cells for the table header
	    PdfPCell cell = new PdfPCell();
	    // Setting the background color and padding of the table cell
	    cell.setBackgroundColor(Color.BLUE);
	    cell.setPadding(5);
	    // Creating font
	    // Setting font style and size
	    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
	    font.setColor(Color.WHITE);
	    // Adding headings in the created table cell or  header
	    // Adding Cell to table
	    cell.setPhrase(new Phrase("CUSTOMER_ID", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("FULLNAME", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("EMAIL", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("MOBILE", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("ADDRESS", font));
	    table.addCell(cell);
	    cell.setPhrase(new Phrase("PURCHASE_DATE", font));
	    table.addCell(cell);
	    // Iterating the list of students
	    for (CustomerEntity entity:entities) {    
	      table.addCell(String.valueOf(entity.getCustomerId()));
	      table.addCell(entity.getFullName());
	      table.addCell(entity.getEmail());
	      table.addCell(String.valueOf(entity.getMobile()));
	      table.addCell(entity.getAddress());
	      table.addCell(String.valueOf(entity.getCreateDate()));

	    }
	    // Adding the created table to the document
	    document.add(table);
	    // Closing the document
	    document.close();
	  }
	
 private String readEmailBody(String fullName,String fileName) {
		
	
		String mailBody=null;
		
		//using try with resource 
		try (
				FileReader reader=new FileReader(fileName);
				BufferedReader br=new BufferedReader(reader);
				)
		{
			StringBuilder builder=new StringBuilder();
			String line=br.readLine();
			while(line != null) {
				builder.append(line);
				line=br.readLine();
			}
			mailBody=builder.toString();
			mailBody=mailBody.replace("{FULLNAME}", fullName);
		} catch (Exception e) {
			log.error("Exception Occured", e);
			//e.printStackTrace();
		}
		return  mailBody;
	}


	@Override
	public String createCSV() {
	
		  List<CustomerEntity> custEntityList = customerRepo.findAll();
	    
		  String path="D:\\JRTP\\csv.txt";
		  File file=new File(path);
		  
		  try {
			  
			  FileWriter opFile=new FileWriter(file);
			  CSVWriter writer=new CSVWriter(opFile);
			  
			  List<String[]> data=new ArrayList<String[]>();
			  data.add(new String[] {"CUSTOMER_ID","FULLNAME","MOBILE","EMAIL","ADDRESS","PURCHASE_DATE"});
			  
			  for(CustomerEntity entity : custEntityList) {
				  data.add(new String[] {String.valueOf(entity.getCustomerId()),entity.getFullName(),String.valueOf(entity.getMobile()),
						  String.valueOf(entity.getEmail()),entity.getAddress(),String.valueOf(entity.getCreateDate()) });
			  }
			  
			  writer.writeAll(data);
           writer.close();			  
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
		  return "File Created";
	}

 
 
}
