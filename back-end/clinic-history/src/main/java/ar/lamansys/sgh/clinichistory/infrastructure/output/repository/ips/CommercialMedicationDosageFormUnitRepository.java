package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.CommercialMedicationDosageFormUnit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommercialMedicationDosageFormUnitRepository extends JpaRepository<CommercialMedicationDosageFormUnit, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT cmdfu.description " +
			"FROM CommercialMedicationDosageFormUnit cmdfu " +
			"JOIN CommercialMedicationPresentation cmp ON (cmp.pk.commercialMedicationDosageFormUnitId = cmdfu.id) " +
			"JOIN CommercialMedicationDosageForm cmdf ON (cmdf.id = cmp.pk.commercialMedicationDosageFormId) " +
			"WHERE cmdf.description = :dosageFormName")
	List<String> fetchAllValuesByDosageFormName(@Param("dosageFormName") String name);

}
