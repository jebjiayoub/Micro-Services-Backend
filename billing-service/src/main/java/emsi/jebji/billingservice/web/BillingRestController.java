package emsi.jebji.billingservice.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import emsi.jebji.billingservice.dao.BillRepository;
import emsi.jebji.billingservice.entities.Bill;
import emsi.jebji.billingservice.feignServices.CustomerServiceRestClient;
import emsi.jebji.billingservice.feignServices.ProductServiceRestClient;

@RestController
public class BillingRestController {
	
	private BillRepository billRepository;
	private CustomerServiceRestClient customerRepository;
	private  ProductServiceRestClient productRepository;
	
	
	
	public BillingRestController(BillRepository billRepository, 
								 CustomerServiceRestClient customerRepository, 
								 ProductServiceRestClient productRepository) {
		super();
		this.billRepository = billRepository;
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
	}



	@GetMapping(path = "/fullBill/{id}")
	public Bill getBill(@PathVariable(name = "id") Long id) {
		Bill bill = billRepository.findById(id).get();
		bill.setCustomer(customerRepository.getCustommerById(bill.getCustomerID()));
		bill.getProductItems().forEach(pi->{
			pi.setProduct(productRepository.getProduct(pi.getProductID()));
		});
		return bill;
	}
}
