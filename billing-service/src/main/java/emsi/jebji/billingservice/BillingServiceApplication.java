package emsi.jebji.billingservice;

import java.util.Date;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import emsi.jebji.billingservice.dao.BillRepository;
import emsi.jebji.billingservice.dao.ProductItemRepository;
import emsi.jebji.billingservice.entities.Bill;
import emsi.jebji.billingservice.entities.ProductItem;
import emsi.jebji.billingservice.feignServices.CustomerServiceRestClient;
import emsi.jebji.billingservice.feignServices.ProductServiceRestClient;
import emsi.jebji.billingservice.model.Customer;
import emsi.jebji.billingservice.model.Product;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(BillRepository billRepository,
							ProductItemRepository productItemRepository,
							CustomerServiceRestClient customerRepository,
							ProductServiceRestClient productRepository) {
		return args -> {
			System.out.println("-------------");
			Customer customer = customerRepository.getCustommerById(1L);
			Bill bill = billRepository.save(new Bill(null, new Date(), null, customer.getId(),null));
			PagedModel<Product> productPageModel = productRepository.pageProducts(0, 5);
			productPageModel.forEach(p->{
				ProductItem productItem = new ProductItem();
				productItem.setPrice(p.getPrice());
				productItem.setQuantity(1+new Random().nextInt(100));
				productItem.setBill(bill);
				productItem.setProductID(p.getId());
				productItemRepository.save(productItem);
			});
		};
	}

}
