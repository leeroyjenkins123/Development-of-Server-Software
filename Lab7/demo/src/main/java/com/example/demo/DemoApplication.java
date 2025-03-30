package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

//drop table addresses, cashes,checks,credits,customers,databasechangelog,databasechangeloglock,items,measurements,order_details,orders,payments,quantities,weights;