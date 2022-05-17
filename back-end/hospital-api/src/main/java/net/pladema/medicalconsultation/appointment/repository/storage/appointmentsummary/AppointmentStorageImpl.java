package net.pladema.medicalconsultation.appointment.repository.storage.appointmentsummary;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
		return new MedicalCoverageBo(medicalCoverage.getName(), medicalCoverage.getCuit(), medicalCoverage.getAffiliateNumber());
	}

	private ClinicalSpecialtyBo buildClinicalSpecialty(ClinicalSpecialtySummary clinicalSpecialty) {
		return new ClinicalSpecialtyBo(clinicalSpecialty.getSctid(), clinicalSpecialty.getName());
	}

	private InstitutionBo buildInstitution(InstitutionSummary institution) {
		return new InstitutionBo(institution.getId(), institution.getCuit(), institution.getSisaCode());
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
