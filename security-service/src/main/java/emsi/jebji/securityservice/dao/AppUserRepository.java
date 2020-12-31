package emsi.jebji.securityservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import emsi.jebji.securityservice.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{
	AppUser findByUsername(String username);
}
