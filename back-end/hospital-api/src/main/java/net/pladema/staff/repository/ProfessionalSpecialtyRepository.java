package net.pladema.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.staff.repository.entity.ProfessionalSpecialty;

@Repository
public interface ProfessionalSpecialtyRepository extends JpaRepository<ProfessionalSpecialty, Integer> {

}
