package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.UnitRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentAssn;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import net.pladema.person.repository.entity.HealthInsurance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class DailyAppointmentRepositoryTest extends UnitRepository {

    @Autowired
    private EntityManager entityManager;

    private DailyAppointmentRepositoryImpl dailyAppointmentRepository;

    @Before
    public void setUp(){
        this.dailyAppointmentRepository = new DailyAppointmentRepositoryImpl(entityManager);
    }

    @Test
    public void test_getDailyAppointmentsByDiaryIdAndDate_success(){
        Integer diaryId = 5;
        LocalDate date = LocalDate.of(2020, 9, 5);

        Short dayWeekId = 1;
        OpeningHours ohProgrammed = save(new OpeningHours(dayWeekId, LocalTime.of(10, 0), LocalTime.of(12, 0)));
        OpeningHours ohSpontaneous = save(new OpeningHours(dayWeekId, LocalTime.of(15, 0), LocalTime.of(18, 0)));

        save(new DiaryOpeningHours(diaryId, ohProgrammed.getId(), MedicalAttentionType.PROGRAMMED, (short) 10));
        save(new DiaryOpeningHours(diaryId, ohSpontaneous.getId(), MedicalAttentionType.SPONTANEOUS, (short) 0));

        save(new AppointmentState((short) 1, "Asignado"));
        save(new AppointmentState((short) 2, "Confirmado"));
        save(new AppointmentState((short) 3, "Ausente"));
        AppointmentState cancelledAppointmentState = save(new AppointmentState((short) 4, "Cancelado"));
        save(new AppointmentState((short) 5, "Atendido"));

        HealthInsurance hi = new HealthInsurance(1, "OSDE", "OSDE");
        save(hi);

        // programmed appointments
        Appointment pa1 = save(new Appointment(null, date, LocalTime.of(10, 20), AppointmentState.ASSIGNED, false, 1, hi.getRnos(), "Cob medica", "112", "011-1548"));
        save(new AppointmentAssn(diaryId, ohProgrammed.getId(), pa1.getId()));
        Appointment pa2 = save(new Appointment(null, date, LocalTime.of(11, 40), AppointmentState.ABSENT, true, 1, null, "Cob medica", "112", "011-1548"));
        save(new AppointmentAssn(diaryId, ohProgrammed.getId(), pa2.getId()));
        Appointment pa3 = save(new Appointment(null, date, LocalTime.of(11, 00), AppointmentState.CANCELLED, false, 1, hi.getRnos(), "Cob medica", "112", "011-1548"));
        save(new AppointmentAssn(diaryId, ohProgrammed.getId(), pa3.getId()));

        // spontaneous appointments
        Appointment sa1 = save(new Appointment(null, date, LocalTime.of(15, 00), AppointmentState.ASSIGNED, false, 1, null, null, "112", "011-1548"));
        save(new AppointmentAssn(diaryId, ohSpontaneous.getId(), sa1.getId()));
        Appointment sa2 = save(new Appointment(null, date, LocalTime.of(17, 20), AppointmentState.ASSIGNED, false, 1, hi.getRnos(), "Cob medica", "112", "011-1548"));
        save(new AppointmentAssn(diaryId, ohSpontaneous.getId(), sa2.getId()));

        // appointments that doesn't belong to the diary
        Appointment a1 = save(new Appointment(null, date, LocalTime.of(17, 20), AppointmentState.ASSIGNED, false, 1, hi.getRnos(), "Cob medica", "112", "011-1548"));
        save(new AppointmentAssn(2, ohSpontaneous.getId(), a1.getId()));

        List<DailyAppointmentVo> result = dailyAppointmentRepository.getDailyAppointmentsByDiaryIdAndDate(diaryId, date);

        assertThat(result)
                .hasSize(4);

        assertThat(result)
                .filteredOn(dav -> dav.getMedicalAttentionTypeId().equals(MedicalAttentionType.PROGRAMMED)).hasSize(2);

        assertThat(result)
                .filteredOn(dav -> dav.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS)).hasSize(2);

        assertThat(result)
                .allSatisfy(dav -> assertThat(dav.getAppointmentState()).isNotEqualTo(cancelledAppointmentState.getDescription()));
    }
}
