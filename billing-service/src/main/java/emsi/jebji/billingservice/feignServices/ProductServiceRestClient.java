package emsi.jebji.billingservice.feignServices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import emsi.jebji.billingservice.model.Product;

@FeignClient(name = "INVENTORY-SERVICE")
public interface ProductServiceRestClient {
	@GetMapping(path = "/products")
	PagedModel<Product> pageProducts(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size);
	@GetMapping(path = "/products/{id}")
	Product getProduct(@PathVariable(name="id") Long id);
}
