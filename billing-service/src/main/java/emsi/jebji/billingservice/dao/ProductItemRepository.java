package emsi.jebji.billingservice.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import emsi.jebji.billingservice.entities.ProductItem;

@RepositoryRestResource
public interface ProductItemRepository extends JpaRepository<ProductItem, Long>{
	public Collection<ProductItem> findByBillId(Long id);
}
