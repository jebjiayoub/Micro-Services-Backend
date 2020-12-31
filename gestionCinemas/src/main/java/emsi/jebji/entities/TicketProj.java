package emsi.jebji.entities;

import org.springframework.data.rest.core.config.Projection; //personaliser data rest

@Projection(name = "t1", types = Ticket.class)
public interface TicketProj {
	
	public Long getId();
	public String getNomClient();
	public double  getPrix();
	public Integer getCodePayement();
	public boolean getReserve();
	public Place getPlace();

}
