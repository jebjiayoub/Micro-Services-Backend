package emsi.jebji.billingservice.feignServices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import emsi.jebji.billingservice.model.Customer;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerServiceRestClient {
	@GetMapping(path = "/customers/{id}")
	public Customer getCustommerById(@PathVariable(name="id") Long id);
}
