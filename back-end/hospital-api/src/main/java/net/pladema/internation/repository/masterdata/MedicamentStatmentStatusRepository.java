package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.MedicationStatementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentStatmentStatusRepository extends JpaRepository<MedicationStatementStatus, String> {

}
