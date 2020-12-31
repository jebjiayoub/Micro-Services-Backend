package emsi.jebji.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import emsi.jebji.entities.Projection;
import emsi.jebji.entities.Ticket;

@RepositoryRestResource
@CrossOrigin("*")
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	public Page<Ticket> findByProjection(Projection p, Pageable page);
	public Ticket findOneById(Long id);
}
