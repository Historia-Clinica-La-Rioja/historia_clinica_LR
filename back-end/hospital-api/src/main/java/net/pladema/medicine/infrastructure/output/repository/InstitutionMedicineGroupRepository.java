package net.pladema.medicine.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.output.repository.entity.InstitutionMedicineGroup;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionMedicineGroupRepository extends SGXAuditableEntityJPARepository<InstitutionMedicineGroup, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineGroupBo(img.id, img.medicineGroupId, img.institutionId, mg.name, mg.isDomain, img.deleteable.deleted) " +
			"FROM InstitutionMedicineGroup img " +
			"JOIN MedicineGroup mg ON (mg.id = img.medicineGroupId) " +
			"WHERE img.institutionId = :institutionId " +
			"AND mg.deleteable.deleted IS FALSE ")
	List<InstitutionMedicineGroupBo> getByInstitutionId(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineGroupBo(img.id, img.medicineGroupId, img.institutionId, mg.name, mg.isDomain, img.deleteable.deleted) " +
			"FROM InstitutionMedicineGroup img " +
			"JOIN MedicineGroup mg ON (mg.id = img.medicineGroupId) " +
			"WHERE mg.deleteable.deleted IS FALSE")
	List<InstitutionMedicineGroupBo> getAll();

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineGroupBo(img.id, img.medicineGroupId, img.institutionId, mg.name, mg.isDomain, img.deleteable.deleted) " +
			"FROM InstitutionMedicineGroup img " +
			"JOIN MedicineGroup mg ON (mg.id = img.medicineGroupId) " +
			"WHERE img.id = :id ")
	Optional<InstitutionMedicineGroupBo> getGroupById(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicine.domain.InstitutionMedicineGroupBo(img.id, img.medicineGroupId, img.institutionId, mg.name, mg.isDomain, img.deleteable.deleted) " +
			"FROM InstitutionMedicineGroup img " +
			"JOIN MedicineGroup mg ON (mg.id = img.medicineGroupId) " +
			"WHERE img.id IN :ids ")
	List<InstitutionMedicineGroupBo> getAllByIds(@Param("ids") List<Integer> ids);

	@Transactional
	@Modifying
	@Query(value="UPDATE institution_medicine_group " +
			"SET deleted = FALSE " +
			"WHERE id = :id", nativeQuery = true)
	void setDeletedFalse(@Param("id")Integer id);

}
