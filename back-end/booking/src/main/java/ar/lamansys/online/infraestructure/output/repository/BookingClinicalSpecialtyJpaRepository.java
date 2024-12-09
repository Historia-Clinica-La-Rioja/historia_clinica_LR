package ar.lamansys.online.infraestructure.output.repository;

import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.lamansys.online.infraestructure.output.entity.VBookingClinicalSpecialty;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookingClinicalSpecialtyJpaRepository extends JpaRepository<VBookingClinicalSpecialty, Integer>{

	@Query("SELECT NEW ar.lamansys.online.infraestructure.output.entity.VBookingClinicalSpecialty(" +
	"cs.id, cs.name, cs.clinicalSpecialtyTypeId) "+
	"FROM HealthcareProfessionalSpecialty hps JOIN ClinicalSpecialty cs ON cs.id = hps.clinicalSpecialtyId " +
	"ORDER BY cs.name")
	List<VBookingClinicalSpecialty> findAllAssociatedWithAProfessional();

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT NEW ar.lamansys.online.domain.specialty.BookingSpecialtyBo(cs.id, cs.name)" +
			" FROM BookingInstitution bi" +
			" JOIN Institution i ON bi.institutionId = i.id" +
			" JOIN DoctorsOffice do ON do.institutionId = i.id" +
			" JOIN Diary d ON d.doctorsOfficeId = do.id" +
			" JOIN DiaryOpeningHours doh ON doh.pk.diaryId = d.id" +
			" JOIN ClinicalSpecialty cs ON cs.id = d.clinicalSpecialtyId" +
			" WHERE d.deleteable.deleted <> TRUE" +
			" AND d.endDate > CURRENT_DATE" +
			" AND cs.clinicalSpecialtyTypeId = :specialtyType" +
			" AND doh.externalAppointmentsAllowed IS TRUE" +
			" AND doh.medicalAttentionTypeId = :medicalAttentionType" +
			" ORDER BY cs.name")
	List<BookingSpecialtyBo> findSpecialtiesByProfessionals(@Param("specialtyType") Short specialtyType,
															@Param("medicalAttentionType") Short medicalAttentionType);
}
