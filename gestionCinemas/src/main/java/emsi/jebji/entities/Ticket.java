package emsi.jebji.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length=75)
	private String nomClient;
	private double prix;
	//@Column(unique = true, nullable=true)
	@Column(unique = false,nullable=true)
	private Integer codePayement;
	private boolean reserve;
	@ManyToOne
	@ToString.Exclude
	private Place place;
	@ManyToOne
	@ToString.Exclude
	private Projection projection;
}