package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.EquipmentAppointmentVo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.entity.EquipmentAppointmentAssn;
import net.pladema.medicalconsultation.appointment.repository.entity.EquipmentAppointmentAssnPK;

@Repository
public interface EquipmentAppointmentAssnRepository extends JpaRepository<EquipmentAppointmentAssn, EquipmentAppointmentAssnPK> {

    @Transactional
    @Modifying
    @Query("UPDATE EquipmentAppointmentAssn AS aassn " +
            "SET aassn.pk.openingHoursId = :openingHoursId " +
            "WHERE aassn.pk.appointmentId = :appointmentId")
    void updateOpeningHoursId(@Param("openingHoursId") Integer openingHoursId,
							  @Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.AppointmentVo(eaa.pk.equipmentDiaryId, a, edoh.medicalAttentionTypeId, has.reason, ao.observation, ao.createdBy)" +
			"FROM Appointment AS a " +
			"JOIN EquipmentAppointmentAssn AS eaa ON (a.id = eaa.pk.appointmentId) " +
			"LEFT JOIN AppointmentObservation AS ao ON (a.id = ao.appointmentId) " +
			"LEFT JOIN HistoricAppointmentState AS has ON (a.id = has.pk.appointmentId) " +
			"JOIN EquipmentDiary ed ON (ed.id = eaa.pk.equipmentDiaryId ) " +
			"JOIN EquipmentDiaryOpeningHours AS edoh ON (edoh.pk.equipmentDiaryId = ed.id) " +
			"WHERE a.id = :appointmentId " +
			"AND edoh.pk.openingHoursId = eaa.pk.openingHoursId " +
			"AND a.deleteable.deleted = FALSE OR a.deleteable.deleted IS NULL " +
			"AND ( has.pk.changedStateDate IS NULL OR has.pk.changedStateDate = " +
			"   ( SELECT MAX (subHas.pk.changedStateDate) FROM HistoricAppointmentState subHas WHERE subHas.pk.appointmentId = a.id) ) " +
			"ORDER BY has.pk.changedStateDate DESC")
	List<AppointmentVo> getEquipmentAppointment(@Param("appointmentId") Integer appointmentId);

	@Transactional(readOnly = true)
	@Query("SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.EquipmentAppointmentVo(a, pe.identificationTypeId, pe.identificationNumber, " +
			"(CASE WHEN aoi.destInstitutionId != :institutionId THEN i.id ELSE NULL END) AS idInstitution, i.name, aoi.reportStatusId, s.pt) " +
			"FROM Appointment AS a " +
			"JOIN EquipmentAppointmentAssn AS eaa ON (a.id = eaa.pk.appointmentId) " +
			"LEFT JOIN AppointmentObservation AS ao ON (a.id = ao.appointmentId) " +
			"JOIN EquipmentDiary ed ON (ed.id = eaa.pk.equipmentDiaryId) " +
			"JOIN Equipment AS e ON (ed.equipmentId = e.id) " +
			"JOIN Patient AS p ON (a.patientId = p.id) " +
			"JOIN Person AS pe ON (pe.id = p.personId) " +
			"JOIN PersonExtended AS pex ON (pe.id = pex.id) "+
			"LEFT JOIN AppointmentOrderImage AS aoi ON (a.id = aoi.pk.appointmentId) "+
			"LEFT JOIN Institution AS i ON (aoi.destInstitutionId = i.id) "+
			"LEFT JOIN DiagnosticReport AS dr ON (aoi.studyId = dr.id) " +
			"LEFT JOIN Snomed AS s ON (dr.snomedId = s.id) " +
			"WHERE e.id = :equipmentId " +
			"AND a.appointmentStateId IN (1,2,3,5)")
	List<EquipmentAppointmentVo> getAppointmentsByEquipmentId(@Param("equipmentId") Integer equipmentId, @Param("institutionId") Integer institutionId);
}
