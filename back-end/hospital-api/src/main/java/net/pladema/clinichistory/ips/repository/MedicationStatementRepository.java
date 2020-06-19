package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.entity.MedicationStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationStatementRepository extends JpaRepository<MedicationStatement, Integer>, MedicationStatementRepositoryCustom {

}
