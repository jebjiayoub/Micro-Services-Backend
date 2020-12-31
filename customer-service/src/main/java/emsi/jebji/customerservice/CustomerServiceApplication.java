package emsi.jebji.customerservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import emsi.jebji.customerservice.dao.CustomerRepository;
import emsi.jebji.customerservice.entities.Customer;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository, RepositoryRestConfiguration restConfiguration) {
		restConfiguration.exposeIdsFor(Customer.class);
		return args -> {
			customerRepository.save(new Customer(null,"Mohamed","med@gmail.com"));
            customerRepository.save(new Customer(null,"Ayoub","ayoub@gmail.com"));
            customerRepository.save(new Customer(null,"Hamza","hamza@gmail.com"));
            customerRepository.save(new Customer(null,"Jalal","jalal@gmail.com"));
            customerRepository.findAll().forEach(
                    customer -> {
                        System.out.println(customer.toString());
                    }
            );
		};
	}
}
