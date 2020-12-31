package emsi.jebji.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import emsi.jebji.entities.Cinema;
import emsi.jebji.entities.Salle;

@RepositoryRestResource
@CrossOrigin("*")
public interface SalleRepository extends JpaRepository<Salle, Long> {
	public List<Salle> findByCinema(Cinema c);
	public Salle findOneById(Long id);
}
