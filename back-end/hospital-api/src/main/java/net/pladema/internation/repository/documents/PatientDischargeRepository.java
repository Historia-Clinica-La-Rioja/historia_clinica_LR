package net.pladema.internation.repository.documents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.internation.repository.documents.entity.PatientDischarge;

@Repository
public interface PatientDischargeRepository extends JpaRepository<PatientDischarge, Integer> {

}
