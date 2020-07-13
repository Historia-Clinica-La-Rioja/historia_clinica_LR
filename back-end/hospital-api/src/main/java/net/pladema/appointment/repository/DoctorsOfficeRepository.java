package net.pladema.appointment.repository;

import net.pladema.appointment.repository.entity.DoctorsOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorsOfficeRepository extends JpaRepository<DoctorsOffice, Integer> {
}
