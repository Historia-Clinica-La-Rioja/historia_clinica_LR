package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.entity.MedicationStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationStatementRepository extends JpaRepository<MedicationStatement, Integer> {

}
