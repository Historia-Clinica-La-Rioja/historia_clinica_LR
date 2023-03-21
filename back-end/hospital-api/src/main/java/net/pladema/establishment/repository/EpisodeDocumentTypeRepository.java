package net.pladema.establishment.repository;

import net.pladema.staff.repository.entity.EpisodeDocumentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeDocumentTypeRepository extends JpaRepository<EpisodeDocumentType, Integer> {
}
