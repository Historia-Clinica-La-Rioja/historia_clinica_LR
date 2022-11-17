package net.pladema.medicalconsultation.appointment.repository.storage.appointmentsummary;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentFilterBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentInfoBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.AppointmentStatusBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.ClinicalSpecialtyBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.DoctorBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.InstitutionBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.MedicalCoverageBo;
import net.pladema.medicalconsultation.appointment.service.fetchappointments.domain.PatientBo;
import net.pladema.medicalconsultation.appointment.service.ports.AppointmentStorage;

@Slf4j
@Service
public class AppointmentStorageImpl implements AppointmentStorage {

	private final EntityManager entityManager;

	public AppointmentStorageImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<AppointmentInfoBo> fetchAppointments(AppointmentFilterBo filter) {
		String sqlString =
		"SELECT vs " +
		"FROM VAppointmentSummary AS vs " +
		"WHERE vs.institution.id = :institutionId " +
				(filter.getIdentificationNumber() != null ? " AND vs.patient.identificationNumber = :identificationNumber " : "") +
				(filter.getStartDate() != null ? " AND vs.dateTypeId >= :startDate " : "") +
				(filter.getEndDate() != null ? " AND vs.dateTypeId <= :endDate " : "") +
				(filter.hasAppointmentStatus() ? " AND vs.status.id IN (:appointmentStatusId) " : "");
		Query query = entityManager.createQuery(sqlString, VAppointmentSummary.class)
				.setParameter("institutionId", filter.getInstitutionId());
		if (filter.getIdentificationNumber() != null)
			query.setParameter("identificationNumber", filter.getIdentificationNumber());
		if (filter.getStartDate() != null)
			query.setParameter("startDate", filter.getStartDate());
		if (filter.getEndDate() != null)
			query.setParameter("endDate", filter.getEndDate());
		if (filter.hasAppointmentStatus())
			query.setParameter("appointmentStatusId", filter.getIncludeAppointmentStatus());
		return ((List<VAppointmentSummary>) query.getResultList()).stream()
				.map(this::mapTo)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByProfessionalInInstitution(Integer healthcareProfessionalId, Integer institutionId, LocalDate from, LocalDate to) {
		String sqlString =
				"SELECT  NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
						"aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
						"a.patientMedicalCoverageId,a.phonePrefix, a.phoneNumber, doh.medicalAttentionTypeId, " +
						"a.appointmentBlockMotiveId, a.updateable.updatedOn) " +
						"FROM Appointment AS a " +
						"JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
						"JOIN Diary d ON (d.id = aa.pk.diaryId )" +
						"JOIN DiaryOpeningHours AS doh ON (doh.pk.diaryId = d.id) " +
						"JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
						"WHERE d.healthcareProfessionalId = :healthcareProfessionalId " +
						"AND do.institutionId = :institutionId " +
						"AND d.active = true " +
						"AND (d.deleteable.deleted = false OR d.deleteable.deleted is null) " +
						"AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR +
						"AND (a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL) " +
						(from!=null ? "AND a.dateTypeId >= :from " : "") +
						(to!=null ? "AND a.dateTypeId <= :to " : "") +
						"ORDER BY d.id,a.isOverturn";
		Query query = entityManager.createQuery(sqlString, AppointmentDiaryVo.class)
				.setParameter("healthcareProfessionalId", healthcareProfessionalId)
				.setParameter("institutionId", institutionId);
		if (from != null)
			query.setParameter("from", from);
		if (to != null)
			query.setParameter("to", to);
		return ((List<AppointmentDiaryVo>) query.getResultList()).stream()
				.map(AppointmentBo::fromAppointmentDiaryVo)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds, LocalDate from, LocalDate to) {
		String sqlString =
				"SELECT  NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
						"aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
						"a.patientMedicalCoverageId,a.phonePrefix, a.phoneNumber, doh.medicalAttentionTypeId, " +
						"a.appointmentBlockMotiveId, a.updateable.updatedOn)" +
						"FROM Appointment AS a " +
						"JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
						"JOIN Diary d ON (d.id = aa.pk.diaryId ) " +
						"JOIN DiaryOpeningHours  AS doh ON (doh.pk.diaryId = d.id) " +
						"WHERE aa.pk.diaryId IN (:diaryIds) AND (d.deleteable.deleted = false OR d.deleteable.deleted is null ) " +
						(from!=null ? "AND a.dateTypeId >= :from " : "") +
						(to!=null ? "AND a.dateTypeId <= :to " : "") +
						"AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR +
						"AND (a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL) " +

						"ORDER BY d.id,a.isOverturn";
		Query query = entityManager.createQuery(sqlString, AppointmentDiaryVo.class)
				.setParameter("diaryIds", diaryIds);
		if (from != null)
			query.setParameter("from", from);
		if (to != null)
			query.setParameter("to", to);
		return ((List<AppointmentDiaryVo>) query.getResultList()).stream()
				.map(AppointmentBo::fromAppointmentDiaryVo)
				.collect(Collectors.toList());
	}

	private AppointmentInfoBo mapTo(VAppointmentSummary row) {
		var result = new AppointmentInfoBo();
		result.setId(row.getId());
		result.setDateTypeId(row.getDateTypeId());
		result.setHour(row.getHour());
		result.setOverturn(row.isOverturn());
		result.setPhoneNumber(row.getPhoneNumber());
		result.setPhonePrefix(row.getPhonePrefix());
		result.setStatus(buildStatus(row.getStatus()));
		result.setPatient(buildPatient(row.getPatient()));
		result.setDoctor(buildDoctor(row.getDoctor()));
		result.setInstitution(buildInstitution(row.getInstitution()));
		result.setClinicalSpecialty(buildClinicalSpecialty(row.getClinicalSpecialty()));
		result.setMedicalCoverage(buildMedicalCoverage(row.getMedicalCoverage()));
		return result;
	}

	private MedicalCoverageBo buildMedicalCoverage(MedicalCoverageInfoSummary medicalCoverage) {
		return medicalCoverage != null ? new MedicalCoverageBo(medicalCoverage.getName(), medicalCoverage.getCuit(), medicalCoverage.getAffiliateNumber()) : null;
	}

	private ClinicalSpecialtyBo buildClinicalSpecialty(ClinicalSpecialtySummary clinicalSpecialty) {
		return clinicalSpecialty != null ? new ClinicalSpecialtyBo(clinicalSpecialty.getSctid(), clinicalSpecialty.getName()) : null;
	}

	private InstitutionBo buildInstitution(InstitutionSummary institution) {
		return institution != null ?  new InstitutionBo(institution.getId(), institution.getCuit(), institution.getSisaCode()) : null;
	}

	private DoctorBo buildDoctor(DoctorInfoSummary doctor) {
		return new DoctorBo(doctor.getLicenseNumber(), doctor.getFirstName(), doctor.getLastName(), doctor.getIdentificationNumber());
	}

	private PatientBo buildPatient(PatientInfoSummary patient) {
		return new PatientBo(patient.getId(), patient.getFirstName(), patient.getLastName(),
				patient.getIdentificationNumber(), patient.getGenderId());
	}

	private AppointmentStatusBo buildStatus(AppointmentStatusSummary status) {
		return new AppointmentStatusBo(status.getId(), status.getDescription());
	}
}
