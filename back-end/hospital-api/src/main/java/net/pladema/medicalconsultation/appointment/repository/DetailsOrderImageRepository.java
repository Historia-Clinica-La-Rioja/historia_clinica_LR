package net.pladema.medicalconsultation.appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImagePK;
import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImage;

@Repository
public interface DetailsOrderImageRepository extends JpaRepository<DetailsOrderImage, DetailsOrderImagePK> {
}
