package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DailyAppointmentRepositoryImpl implements DailyAppointmentRepository {

    private final EntityManager entityManager;

    public DailyAppointmentRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyAppointmentVo> getDailyAppointmentsByDiaryIdAndDate(Integer institutionId, Integer diaryId, LocalDate date) {

        String sqlQuery =
                "SELECT NEW net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo(a, astate, oh.from, oh.to, doh.medicalAttentionTypeId) " +
                "FROM Appointment AS a " +
                "JOIN AppointmentAssn AS assn ON (a.id = assn.pk.appointmentId) " +
                "JOIN AppointmentState AS astate ON (a.appointmentStateId = astate.id) " +
                "JOIN OpeningHours AS oh ON (assn.pk.openingHoursId = oh.id) " +
                "JOIN Diary AS d ON (assn.pk.diaryId = d.id) " +
                "JOIN DoctorsOffice AS do ON (d.doctorsOfficeId = do.id) " +
                "JOIN DiaryOpeningHours AS doh on ((doh.pk.diaryId = :diaryId) AND (doh.pk.openingHoursId = assn.pk.openingHoursId)) " +
                "WHERE assn.pk.diaryId = :diaryId " +
                "AND a.dateTypeId = :date " +
                "AND NOT a.appointmentStateId = :appointmentStateId " +
                "AND do.institutionId = :institutionId " +
                "ORDER BY a.hour ASC ";

        List<DailyAppointmentVo> result = entityManager.createQuery(sqlQuery)
                .setParameter("diaryId", diaryId)
                .setParameter("date", date)
                .setParameter("appointmentStateId", AppointmentState.CANCELLED)
                .setParameter("institutionId", institutionId)
                .getResultList();

        return result;
    }
}
