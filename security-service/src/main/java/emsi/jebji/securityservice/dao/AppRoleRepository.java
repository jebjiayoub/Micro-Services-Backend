package emsi.jebji.securityservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import emsi.jebji.securityservice.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	AppRole findByRoleName(String rolename);
}
