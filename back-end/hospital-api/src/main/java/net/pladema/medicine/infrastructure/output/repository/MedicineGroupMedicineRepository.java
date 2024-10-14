package net.pladema.medicine.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;


import net.pladema.medicine.domain.MedicineGroupMedicineBo;
import net.pladema.medicine.infrastructure.output.repository.entity.MedicineGroupMedicine;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineGroupMedicineRepository extends SGXAuditableEntityJPARepository<MedicineGroupMedicine, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT mgm " +
			"FROM MedicineGroupMedicine mgm " +
			"WHERE mgm.medicineGroupId = :medicineGroupId " +
			"AND mgm.medicineId = :medicineId " +
			"AND mgm.deleteable.deleted IS FALSE")
	Optional<MedicineGroupMedicine> getByGroupIdAndMedicineId(@Param("medicineGroupId") Integer medicineGroupId, @Param("medicineId")Integer medicineId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.MedicineGroupMedicineBo(mgm.id, mgm.medicineGroupId, mfs.id, mfs.financed, s.pt) " +
			"FROM MedicineGroupMedicine mgm " +
			"JOIN MedicineFinancingStatus mfs ON (mgm.medicineId = mfs.id) " +
			"JOIN Snomed s ON (mfs.id = s.id) " +
			"WHERE mgm.medicineGroupId = :medicineGroupId " +
			"AND mgm.deleteable.deleted IS FALSE")
	List<MedicineGroupMedicineBo> getByMedicineGroupId(@Param("medicineGroupId") Integer medicineGroupId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.MedicineGroupMedicineBo(mgm.id, mgm.medicineGroupId, mfs.id, mfs.financed, s.pt, imfs.financed, imfs.institutionId) " +
			"FROM MedicineGroupMedicine mgm " +
			"JOIN MedicineFinancingStatus mfs ON (mgm.medicineId = mfs.id) " +
			"JOIN InstitutionMedicineFinancingStatus imfs ON (mfs.id = imfs.medicineId) " +
			"JOIN Snomed s ON (mfs.id = s.id) " +
			"WHERE mgm.medicineGroupId = :medicineGroupId " +
			"AND imfs.institutionId = :institutionId " +
			"AND mgm.deleteable.deleted IS FALSE")
	List<MedicineGroupMedicineBo> getByMedicineGroupIdAndInstitutionId(@Param("medicineGroupId") Integer medicineGroupId, @Param("institutionId") Integer institutionId);

}
