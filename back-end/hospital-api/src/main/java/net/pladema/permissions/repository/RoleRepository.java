package net.pladema.permissions.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.permissions.repository.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Short> {

	@Transactional(readOnly = true)
	@Query("SELECT r FROM Role r WHERE r.description = :description")
	Optional<Role> findByDescription(@Param("description") String description);

}
