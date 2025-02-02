package snomed.relations.cache.infrastructure.output.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import snomed.relations.cache.domain.GetCommercialMedicationSnomedBo;
import snomed.relations.cache.domain.SnomedBo;
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

	@Transactional(readOnly = true)
	@Query("SELECT NEW snomed.relations.cache.domain.GetCommercialMedicationSnomedBo(vcm.genericPt, vcm.genericSctid, vcm.commercialPt) " +
			"FROM VCommercialMedication vcm " +
			"WHERE fts(vcm.commercialPt, :commercialMedicationName) = TRUE")
	List<GetCommercialMedicationSnomedBo> fetchCommercialMedicationSnomedListByName(@Param("commercialMedicationName") String commercialMedicationName,
                                                                                    Pageable pageable);

	@Transactional(readOnly = true)
	@Query("SELECT NEW snomed.relations.cache.domain.SnomedBo(vcm.commercialSctid, vcm.commercialPt) " +
			"FROM VCommercialMedication vcm " +
			"WHERE vcm.genericSctid = :sctid " +
			"ORDER BY vcm.commercialPt")
    List<SnomedBo> fetchSuggestedCommercialMedicationSnomedListByGeneric(@Param("sctid") String genericMedicationSctid);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT vcm.presentationUnit " +
			"FROM VCommercialMedication vcm " +
			"WHERE vcm.genericSctid = :genericSctid ")
    String fetchGenericPresentationUnit(@Param("genericSctid") String genericSctid);

}