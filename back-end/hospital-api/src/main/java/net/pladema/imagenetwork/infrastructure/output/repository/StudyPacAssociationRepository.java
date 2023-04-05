package net.pladema.imagenetwork.infrastructure.output.repository;

import net.pladema.imagenetwork.infrastructure.output.entity.StudyPacAssociation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyPacAssociationRepository extends JpaRepository<StudyPacAssociation, String> {
}
