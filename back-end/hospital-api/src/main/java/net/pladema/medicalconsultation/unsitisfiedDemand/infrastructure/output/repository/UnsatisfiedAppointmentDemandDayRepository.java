package net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.repository;

import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.UnsatisfiedAppointmentDemandDay;

import net.pladema.medicalconsultation.unsitisfiedDemand.infrastructure.output.entity.embedded.UnsatisfiedAppointmentDemandDayPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnsatisfiedAppointmentDemandDayRepository extends JpaRepository<UnsatisfiedAppointmentDemandDay, UnsatisfiedAppointmentDemandDayPK> {
}
