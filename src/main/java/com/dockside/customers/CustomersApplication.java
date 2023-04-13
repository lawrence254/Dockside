package com.dockside.customers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@EnableDiscoveryClient
@RequiredArgsConstructor
@SpringBootApplication
public class CustomersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomersApplication.class, args);
	}

}

@RestController
class DocksideClientrestController{
	
	private DiscoveryClient discoveryClient;
	@RequestMapping("/clients/{applicationName}")
	public @ResponseBody String getClientsByApplicationName(@PathVariable String applicationName){
		return this.discoveryClient.getInstances(applicationName).get(0).getUri().toString();
	}
}
