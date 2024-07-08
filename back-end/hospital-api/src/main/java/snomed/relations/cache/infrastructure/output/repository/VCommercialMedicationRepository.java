package snomed.relations.cache.infrastructure.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import snomed.relations.cache.infrastructure.output.repository.entity.VCommercialMedication;
import snomed.relations.cache.domain.CommercialMedicationBo;

import java.util.List;

@Repository
public interface VCommercialMedicationRepository extends JpaRepository<VCommercialMedication, String> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT new snomed.relations.cache.domain.CommercialMedicationBo(vcm.commercialSctid, vcm.commercialPt, " +
			"vcm.genericSctid, vcm.genericPt) " +
			"FROM VCommercialMedication vcm")
	List<CommercialMedicationBo> getCommercialMedications();

}