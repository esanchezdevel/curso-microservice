package com.paymentchain.customer.controller;

import java.util.List;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.Collections;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerRestController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private final WebClient.Builder webClientBuilder;
	
	@Autowired
    RestTemplate restTemplate;

	public CustomerRestController(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}

	// define timeout
	TcpClient tcpClient = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
			.doOnConnected(connection -> {
				connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
				connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
			});

	@GetMapping("/full")
	public Customer get(@RequestParam String code) {
		Customer customer = customerRepository.findByCode(code);
		List<CustomerProduct> products = customer.getProducts();
		products.forEach(dto -> {
			String productName = getProductName(dto.getProductId());
			dto.setProductName(productName);
		});
		//customer.setTransactions(getTransacctions(customer.getIban()));
		return customer;
	}

	private <T> List<T> getTransacctions(String accountIban) {
		WebClient client = webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
				.baseUrl("http://localhost:8083/transactions")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultUriVariables(Collections.singletonMap("url", "http://localhost:8083/transactios")).build();
		List<Object> block = client.method(HttpMethod.GET)
				.uri(uriBuilder -> uriBuilder.path("/transactions").queryParam("ibanAccount", accountIban).build())
				.retrieve().bodyToFlux(Object.class).collectList().block();
		List<T> name = (List<T>) block;
		return name;
	}

	private String getProductName(long id) {		
//		WebClient client = WebClient.builder()
//				  .baseUrl("http://businessdomain-products")
//				  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
//				  .build();
//		JsonNode response = client.method(HttpMethod.GET).uri("/products/" + id).retrieve().bodyToMono(JsonNode.class).block();
//		String name = response.get("name").asText();
		
		
		//Is needed to use restTemplate for make the http request using eureka, because wit WebClient 
		//is not detecting the alias domain of the url. 
		String url = "http://businessdomain-products/products/" + id;
		String res = restTemplate.getForObject(url, String.class);
		
		JSONParser parser = new JSONParser();
		String name = "";
		try {
			JSONObject json = (JSONObject) parser.parse(res);
			name = (String) json.get("name");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return name;
	}

	@GetMapping("/list")
	public List<Customer> list() {
		return customerRepository.findAll();
	}

	@GetMapping("/{id}")
	public Customer get(@PathVariable long id) {
		Customer customer = customerRepository.findById(id).get();
		return customer;
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id, @RequestBody Customer input) {
		return null;
	}

	@PostMapping("/create")
	public ResponseEntity<?> post(@RequestBody Customer input) {
		input.getProducts().forEach(x -> x.setCustomer(input));
		Customer save = customerRepository.save(input);
		return ResponseEntity.ok(save);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		return null;
	}
}
