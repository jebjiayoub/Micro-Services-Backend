package emsi.jebji.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import emsi.jebji.dao.CinemaRepository;
import emsi.jebji.dao.FilmRepository;
import emsi.jebji.dao.PlaceRepository;
import emsi.jebji.dao.ProjectionRepository;
import emsi.jebji.dao.SalleRepository;
import emsi.jebji.dao.SeanceRepository;
import emsi.jebji.dao.TicketRepository;
import emsi.jebji.dao.VilleRepository;
import emsi.jebji.entities.Cinema;
import emsi.jebji.entities.Film;
import emsi.jebji.entities.Place;
import emsi.jebji.entities.Projection;
import emsi.jebji.entities.Salle;
import emsi.jebji.entities.Seance;
import emsi.jebji.entities.Ticket;
import emsi.jebji.entities.Ville;

@CrossOrigin("*")
@Controller
public class CinemaController {

	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private PlaceRepository placeRepository;
	
	
	@GetMapping(path = "/index")
	public String index() {
		return "index";
	}

	@GetMapping(path = "/cinemas")
	public String cinemas() {
		return "cinemas";
	}

	@GetMapping(path = "/formfilms")
	public String formFilms(Model model) {
		model.addAttribute("film", new Film());
		return "formfilms";
	}

	@PostMapping(path = "/saveFilm")
	public String saveFilm(Model model, @Valid Film film, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "formfilms";
		filmRepository.save(film);
		model.addAttribute("film", film);
		return "confirmation";
	}

	@GetMapping(path = "/films")
	public String patients(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		Page<Film> films = filmRepository.findAll(PageRequest.of(page, size));
		model.addAttribute("films", films.getContent());
		model.addAttribute("pages", new int[films.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		return "films";
	}

	@GetMapping(path = "/deleteFilm")
	public String deleteFilm(Long id, int page, int size) {
		filmRepository.deleteById(id);
		return "redirect:/films?page=" + page + "&size=" + size;
	}

	@GetMapping(path = "/projections")
	public String projections(Model model, @RequestParam(name = "idville", defaultValue = "") Long idville,
			@RequestParam(name = "idcinema", defaultValue = "") Long idcinema,
			@RequestParam(name = "idsalle", defaultValue = "") Long idsalle) {

		if (idville != null) {
			Ville v = villeRepository.findOneById(idville);
			List<Cinema> cinemas = cinemaRepository.findByVille(v);
			model.addAttribute("cinemas", cinemas);
		}

		if (idcinema != null) {
			Cinema c = cinemaRepository.findOneById(idcinema);
			List<Salle> salles = salleRepository.findByCinema(c);
			model.addAttribute("salles", salles);
		}

		if (idsalle != null) {
			Salle s = salleRepository.findOneById(idsalle);
			List<Projection> projections = projectionRepository.findBySalle(s);
			model.addAttribute("projections", projections);
		}

		model.addAttribute("idville", idville);
		model.addAttribute("idcinema", idcinema);
		model.addAttribute("idsalle", idsalle);

		List<Ville> villes = villeRepository.findAll();
		model.addAttribute("villes", villes);
		return "projections";
	}

	@GetMapping(path = "/deleteProjection")
	public String deleteProjection(Long id, Long idv, Long idc, Long ids) {
		projectionRepository.deleteById(id);
		return "redirect:/projections?idville=" + idv + "&idcinema=" + idc + "&idsalle=" + ids;
	}

	@GetMapping(path = "/addProjection")
	public String addProjection(Model model, Long ids, Long idf) {
		List<Seance> seances = seanceRepository.findAll();
		List<Film> films = filmRepository.findAll();
		Projection p = new Projection();
		// p.setSalle();
		model.addAttribute("sal", salleRepository.findOneById(ids));
		model.addAttribute("fil", filmRepository.findOneById(idf));
		model.addAttribute("projection", p);
		model.addAttribute("seances", seances);
		model.addAttribute("films", films);
		return "addProjection";
	}

	@GetMapping(path = "/editProjection")
	public String editProjection(Model model, Long id, Long ids, Long idf) {
		List<Seance> seances = seanceRepository.findAll();
		Projection p = projectionRepository.findById(id).get();
		model.addAttribute("projection", p);
		model.addAttribute("sal", salleRepository.findOneById(ids));
		model.addAttribute("fil", filmRepository.findOneById(idf));
		model.addAttribute("seances", seances);
		return "addProjection";
	}

	@PostMapping(path = "/saveProjection")
	public String saveProjection(Model model, @Valid Projection projection, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "addProjection";
		
		Salle s = salleRepository.findOneById(projection.getSalle().getId());
		List<Place> places = placeRepository.findBySalle(s);
		
		projectionRepository.save(projection);
		
		for(Place p : places) {
			Ticket t = new Ticket();
			t.setPlace(p);
			t.setPrix(projection.getPrix());
			t.setReserve(false);
			t.setProjection(projection);
			ticketRepository.save(t);
		}
		
		
		model.addAttribute("projection", projection);
		return "confirmation";
	}

	@GetMapping(path = "/tickets")
	public String tickets(Model model, Long idp, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "8") int size,
			@RequestParam(name = "idtick", defaultValue = "0") Long idtick) {
		

		if(idtick!=0) {
			System.out.println("ID Ticket : "+idtick);
			Ticket ti = ticketRepository.findOneById(idtick);
			System.out.println(ti);
			model.addAttribute("ticket", ti);
		}
		else {
			Long i = (long) 1;
			Ticket tii = ticketRepository.findOneById(i);
			model.addAttribute("ticket", tii);
		}
		//Projection p = projectionRepository.findOneById(idp);
		Projection p = projectionRepository.findOneById(idp);
		Page<Ticket> tickets = ticketRepository.findByProjection(p, PageRequest.of(page, size));
		model.addAttribute("idprojection", idp);
		model.addAttribute("tickets", tickets);
		model.addAttribute("page", page);
		model.addAttribute("films", tickets.getContent());
		model.addAttribute("pages", new int[tickets.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("idtick", idtick);
		model.addAttribute("size", size);
		return "tickets";
	}
	
	@GetMapping(path = "/editTickets")
	public String editTickets(Model model, Long idp, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "8") int size,
			@RequestParam(name = "idtick", defaultValue = "0") Long idtick) {
		Ticket t = ticketRepository.findById(idtick).get();
		//ticketRepository.findOneById(idtick)
		Projection p = projectionRepository.findOneById(idp);
		model.addAttribute("ticket", t);
		Page<Ticket> tickets = ticketRepository.findByProjection(p, PageRequest.of(page, size));
		model.addAttribute("idprojection", idp);
		model.addAttribute("tickets", tickets);
		model.addAttribute("page", page);
		model.addAttribute("films", tickets.getContent());
		model.addAttribute("pages", new int[tickets.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("idtick", idtick);
		model.addAttribute("size", size);
		return "tickets";
	}
	
	@GetMapping(path = "/saveTicket")
	public String saveTicket(Model model, Long idp, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "8") int size,
			@RequestParam(name = "idtick", defaultValue = "0") Long idtick,@Valid Ticket ticket, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "tickets";
		
		ticketRepository.save(ticket);
		
		model.addAttribute("ticket", ticket);
		return "confirmation";
	}


}
