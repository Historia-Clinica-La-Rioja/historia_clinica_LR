package net.pladema.medicine.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo;
import net.pladema.medicine.infrastructure.output.repository.entity.InstitutionMedicineFinancingStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionMedicineFinancingStatusRepository extends SGXAuditableEntityJPARepository<InstitutionMedicineFinancingStatus, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo(imfs.id, imfs.institutionId, imfs.financed, mfs.id, s.sctid, s.pt, mfs.financed) " +
			"FROM InstitutionMedicineFinancingStatus imfs " +
			"JOIN MedicineFinancingStatus mfs ON (imfs.medicineId = mfs.id) " +
			"JOIN Snomed s ON (mfs.id = s.id) " +
			"WHERE imfs.institutionId = :institutionId " +
			"AND imfs.deleteable.deleted IS FALSE " +
			"ORDER by s.pt ASC")
	List<InstitutionMedicineFinancingStatusBo> getByInstitutionId (@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo(imfs.id, imfs.institutionId, imfs.financed, mfs.id, s.sctid, s.pt, mfs.financed) " +
			"FROM InstitutionMedicineFinancingStatus imfs " +
			"JOIN MedicineFinancingStatus mfs ON (imfs.medicineId = mfs.id) " +
			"JOIN Snomed s ON (mfs.id = s.id) " +
			"WHERE imfs.deleteable.deleted IS FALSE " +
			"ORDER by s.pt ASC")
	List<InstitutionMedicineFinancingStatusBo> getAll();

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo(imfs.id, imfs.institutionId, imfs.financed, mfs.id, s.sctid, s.pt, mfs.financed) " +
			"FROM InstitutionMedicineFinancingStatus imfs " +
			"JOIN MedicineFinancingStatus mfs ON (imfs.medicineId = mfs.id) " +
			"JOIN Snomed s ON (mfs.id = s.id)" +
			"WHERE imfs IN (:ids) " +
			"ORDER by s.pt ASC")
	List<InstitutionMedicineFinancingStatusBo> getAllByIds(@Param("ids")List<Integer> ids);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineFinancingStatusBo(imfs.id, imfs.institutionId, imfs.financed, mfs.id, s.sctid, s.pt, mfs.financed) " +
			"FROM InstitutionMedicineFinancingStatus imfs " +
			"JOIN MedicineFinancingStatus mfs ON (imfs.medicineId = mfs.id) " +
			"JOIN Snomed s ON (mfs.id = s.id) " +
			"WHERE imfs.id = :id " +
			"ORDER by s.pt ASC")
	Optional<InstitutionMedicineFinancingStatusBo> findMedicineById (@Param("id") Integer id);

}
