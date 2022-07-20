package ar.lamansys.online.infraestructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.lamansys.online.infraestructure.output.entity.BookingInstitution;

@Repository
public interface BookingInstitutionRepository extends JpaRepository<BookingInstitution, Integer> {
}
