package net.pladema.internation.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.internation.repository.core.entity.PatientDischarge;

@Repository
public interface PatientDischargeRepository extends JpaRepository<PatientDischarge, Integer> {

}
