package net.pladema.medicalconsultation.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.domain.EquipmentAppointmentVo;
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.ports.EquipmentAppointmentStorage;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EquipmentAppointmentStorageImpl implements EquipmentAppointmentStorage {

    private final EntityManager entityManager;

    public List<EquipmentAppointmentBo> getAppointmentsByEquipmentId(Integer equipmentId, Integer institutionId, LocalDate from, LocalDate to) {

        String sqlString = "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.EquipmentAppointmentVo(" +
                "a, pe.identificationTypeId, pe.identificationNumber, " +
                "(CASE WHEN aoi.destInstitutionId != :institutionId THEN i.id ELSE NULL END) AS idInstitution, " +
                "i.name, aoi.reportStatusId, aoi.studyId, aoi.orderId, aoi.transcribedOrderId) " +
                "FROM Appointment AS a " +
                "JOIN EquipmentAppointmentAssn AS eaa ON (a.id = eaa.pk.appointmentId) " +
                "LEFT JOIN AppointmentObservation AS ao ON (a.id = ao.appointmentId) " +
                "JOIN EquipmentDiary ed ON (ed.id = eaa.pk.equipmentDiaryId) " +
                "JOIN Equipment AS e ON (ed.equipmentId = e.id) " +
                "JOIN Patient AS p ON (a.patientId = p.id) " +
                "JOIN Person AS pe ON (pe.id = p.personId) " +
                "JOIN PersonExtended AS pex ON (pe.id = pex.id) " +
                "LEFT JOIN AppointmentOrderImage AS aoi ON (a.id = aoi.pk.appointmentId) " +
                "LEFT JOIN Institution AS i ON (aoi.destInstitutionId = i.id) " +
                "WHERE e.id = :equipmentId " +
                "AND a.appointmentStateId IN (1,2,3,5) " +
                (from!=null ? "AND a.dateTypeId >= :from " : "") +
                (to!=null ? "AND a.dateTypeId <= :to " : "");

        Query query = entityManager.createQuery(sqlString, EquipmentAppointmentVo.class)
                .setParameter("institutionId", institutionId)
                .setParameter("equipmentId", equipmentId);
        if (from != null)
            query.setParameter("from", from);
        if (to != null)
            query.setParameter("to", to);

        return ((List<EquipmentAppointmentVo>) query.getResultList()).stream()
                .map(EquipmentAppointmentBo::fromEquipmentAppointmentVo)
                .collect(Collectors.toList());
    }
}
