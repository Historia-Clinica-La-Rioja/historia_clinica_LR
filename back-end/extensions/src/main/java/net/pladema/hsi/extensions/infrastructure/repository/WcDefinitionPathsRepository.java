package net.pladema.hsi.extensions.infrastructure.repository;

import net.pladema.hsi.extensions.infrastructure.repository.entities.ExtensionDefinitionPath;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WcDefinitionPathsRepository extends JpaRepository<ExtensionDefinitionPath, Short> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(e.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM ExtensionDefinitionPath e " +
			"WHERE e.name = :name")
	boolean existsExtensionByName(@Param("name") String name);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(e.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM ExtensionDefinitionPath e " +
			"WHERE e.path = :path")
	boolean existsExtensionByPath(@Param("path") String path);

}
