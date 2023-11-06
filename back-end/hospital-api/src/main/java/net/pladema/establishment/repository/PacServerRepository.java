package net.pladema.establishment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.pladema.establishment.repository.entity.PacServer;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PacServerRepository extends JpaRepository<PacServer, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT pas "+
			"FROM PacServer AS pas " +
			"WHERE pas.active IS TRUE")
	List<PacServer> getAllActive();
}
