package com.example.Payment_System;


//import com.example.Payment_System.DAO.TransactionDao;
import com.example.Payment_System.Model.Transaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.Payment_System")
public class PaymentSystemApplication  {
	public static void main(String[] args) {
		SpringApplication.run(PaymentSystemApplication.class, args);
	}


	}



