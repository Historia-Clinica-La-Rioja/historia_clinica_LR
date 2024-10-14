package net.pladema.medicalconsultation.appointment.repository.storage.appointmentsummary;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import lombok.RequiredArgsConstructor;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppointmentStorageImpl implements AppointmentStorage {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Collection<AppointmentBo> getAppointmentsByProfessionalInInstitution(Integer healthcareProfessionalId, Integer institutionId, LocalDate from, LocalDate to) {
        String sqlString =
                "SELECT  NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
                        "aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
                        "a.patientMedicalCoverageId,a.phonePrefix, a.phoneNumber, doh.medicalAttentionTypeId, " +
                        "a.appointmentBlockMotiveId, a.updateable.updatedOn, a.creationable.createdOn, p.id, p.firstName, p.lastName, " +
						"pex.nameSelfDetermination, p.middleNames, p.otherLastNames, bp.email, dl, aa.pk.openingHoursId) " +
                        "FROM Appointment AS a " +
                        "JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
                        "JOIN DiaryOpeningHours AS doh ON (aa.pk.openingHoursId = doh.pk.openingHoursId AND aa.pk.diaryId = doh.pk.diaryId) " +
                        "JOIN Diary d ON (d.id = aa.pk.diaryId )" +
                        "JOIN DoctorsOffice AS do ON (do.id = d.doctorsOfficeId) " +
                        "LEFT JOIN UserPerson us ON (a.creationable.createdBy = us.pk.userId) " +
                        "LEFT JOIN Person p ON (us.pk.personId = p.id) " +
                        "LEFT JOIN PersonExtended pex ON (p.id = pex.id) " +
                        "LEFT JOIN BookingAppointment ba ON a.id = ba.pk.appointmentId " +
                        "LEFT JOIN BookingPerson bp ON ba.pk.bookingPersonId = bp.id " +
                        "LEFT JOIN DiaryLabel dl ON (a.diaryLabelId = dl.id) " +
                        "WHERE d.healthcareProfessionalId = :healthcareProfessionalId " +
                        "AND do.institutionId = :institutionId " +
                        "AND d.active = true " +
                        "AND (d.deleteable.deleted = false OR d.deleteable.deleted is null) " +
                        "AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR +
                        "AND (a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL) " +
                        (from != null ? "AND a.dateTypeId >= :from " : "") +
                        (to != null ? "AND a.dateTypeId <= :to " : "") +
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
    @Transactional(readOnly = true)
	public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds, LocalDate from, LocalDate to) {
		String sqlString =
				"SELECT  NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
						"aa.pk.diaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
						"a.patientMedicalCoverageId,a.phonePrefix, a.phoneNumber, doh.medicalAttentionTypeId, " +
						"a.appointmentBlockMotiveId, a.updateable.updatedOn, a.creationable.createdOn, p.id, p.firstName, p.lastName, " +
						"pex.nameSelfDetermination, p.middleNames, p.otherLastNames, bp.email, dl, aa.pk.openingHoursId)" +
						"FROM Appointment AS a " +
						"JOIN AppointmentAssn AS aa ON (a.id = aa.pk.appointmentId) " +
						"LEFT JOIN DiaryOpeningHours AS doh ON (aa.pk.openingHoursId = doh.pk.openingHoursId AND aa.pk.diaryId = doh.pk.diaryId)" +
						"JOIN Diary d ON (d.id = aa.pk.diaryId ) " +
						"LEFT JOIN UserPerson us ON (a.creationable.createdBy = us.pk.userId) " +
						"LEFT JOIN Person p ON (us.pk.personId = p.id) " +
						"LEFT JOIN PersonExtended pex ON (p.id = pex.id) " +
						"LEFT JOIN BookingAppointment ba ON a.id = ba.pk.appointmentId " +
						"LEFT JOIN BookingPerson bp ON ba.pk.bookingPersonId = bp.id " +
						"LEFT JOIN DiaryLabel dl ON (a.diaryLabelId = dl.id) " +
						"WHERE aa.pk.diaryId IN (:diaryIds) AND (d.deleteable.deleted = false OR d.deleteable.deleted is null) " +
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

    @Override
    @Transactional(readOnly = true)
    public Collection<AppointmentBo> getAppointmentsByEquipmentDiary(Integer equipmentDiaryId, LocalDate from, LocalDate to) {
        String sqlString =
                "SELECT  NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentDiaryVo(" +
                        "eaa.pk.equipmentDiaryId, a.id, a.patientId, a.dateTypeId, a.hour, a.appointmentStateId, a.isOverturn, " +
                        "a.patientMedicalCoverageId,a.phonePrefix, a.phoneNumber, edoh.medicalAttentionTypeId, " +
                        "a.appointmentBlockMotiveId, a.updateable.updatedOn, a.creationable.createdOn, p.id, p.firstName, p.lastName, " +
						"pex.nameSelfDetermination, p.middleNames, p.otherLastNames, bp.email, dl, eaa.pk.openingHoursId)" +
                        "FROM Appointment AS a " +
                        "JOIN EquipmentAppointmentAssn AS eaa ON (a.id = eaa.pk.appointmentId) " +
                        "JOIN EquipmentDiaryOpeningHours AS edoh ON (eaa.pk.openingHoursId = edoh.pk.openingHoursId AND edoh.pk.equipmentDiaryId = eaa.pk.equipmentDiaryId) " +
                        "JOIN EquipmentDiary ed ON (ed.id = eaa.pk.equipmentDiaryId) " +
                        "JOIN UserPerson us ON (a.creationable.createdBy = us.pk.userId) " +
                        "JOIN Person p ON (us.pk.personId = p.id) " +
                        "JOIN PersonExtended pex ON (p.id = pex.id) " +
                        "LEFT JOIN BookingAppointment ba ON a.id = ba.pk.appointmentId " +
                        "LEFT JOIN BookingPerson bp ON ba.pk.bookingPersonId = bp.id " +
                        "LEFT JOIN DiaryLabel dl ON (a.diaryLabelId = dl.id) " +
                        "WHERE eaa.pk.equipmentDiaryId = :equipmentDiaryId" +
                        " AND (ed.deleteable.deleted = false OR ed.deleteable.deleted is null ) " +
                        (from != null ? "AND a.dateTypeId >= :from " : "") +
                        (to != null ? "AND a.dateTypeId <= :to " : "") +
                        "AND NOT a.appointmentStateId = " + AppointmentState.CANCELLED_STR +
                        "AND (a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL) " +

                        "ORDER BY ed.id,a.isOverturn";
        Query query = entityManager.createQuery(sqlString, AppointmentDiaryVo.class)
                .setParameter("equipmentDiaryId", equipmentDiaryId);
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
        return clinicalSpecialty != null ? new ClinicalSpecialtyBo(clinicalSpecialty.getSctid(), clinicalSpecialty.getId(), clinicalSpecialty.getName()) : null;
    }

    private InstitutionBo buildInstitution(InstitutionSummary institution) {
        return institution != null ? new InstitutionBo(institution.getId(), institution.getCuit(), institution.getSisaCode()) : null;
    }

    private DoctorBo buildDoctor(DoctorInfoSummary doctor) {
        return new DoctorBo(doctor.getLicenseNumber(), doctor.getFirstName(), doctor.getLastName(),
				doctor.getIdentificationNumber(), doctor.getGenderId());
    }

    private PatientBo buildPatient(PatientInfoSummary patient) {
        return new PatientBo(patient.getId(), patient.getFirstName(), patient.getLastName(),
                patient.getIdentificationNumber(), patient.getGenderId());
    }

    private AppointmentStatusBo buildStatus(AppointmentStatusSummary status) {
        return new AppointmentStatusBo(status.getId(), status.getDescription());
    }
}
