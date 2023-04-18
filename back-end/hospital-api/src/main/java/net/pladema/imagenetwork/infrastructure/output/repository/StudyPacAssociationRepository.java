package net.pladema.imagenetwork.infrastructure.output.repository;

import net.pladema.imagenetwork.infrastructure.output.entity.StudyInformation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyPacAssociationRepository extends JpaRepository<StudyInformation, String> {
}
