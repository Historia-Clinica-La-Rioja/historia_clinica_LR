package net.pladema.hsi.extensions.infrastructure.repository;

import net.pladema.hsi.extensions.infrastructure.repository.entities.ExtensionDefinitionPath;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WcDefinitionPathsRepository extends JpaRepository<ExtensionDefinitionPath, Short> {
}
