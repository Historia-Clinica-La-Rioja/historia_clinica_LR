package net.pladema.medicalconsultation.doctorsoffice.repository;

import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorsOfficeRepository extends JpaRepository<DoctorsOffice, Integer> {
}
