package net.pladema.medicalconsultation.appointment.repository;

import net.pladema.UnitRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentAssn;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
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
        Integer institutionId = 2;
        LocalDate date = LocalDate.of(2020, 9, 5);

        DoctorsOffice doctorsOffice = mockDoctorsOffice(institutionId);
        save(doctorsOffice);

        Diary diary = mockDiary(doctorsOffice.getId());
        Integer diaryId = save(diary).getId();

        Short dayWeekId = 1;
        OpeningHours ohProgrammed = save(new OpeningHours(dayWeekId, LocalTime.of(10, 0), LocalTime.of(12, 0)));
        OpeningHours ohSpontaneous = save(new OpeningHours(dayWeekId, LocalTime.of(15, 0), LocalTime.of(18, 0)));

        save(new DiaryOpeningHours(diaryId, ohProgrammed.getId(), MedicalAttentionType.PROGRAMMED, (short) 10));
        save(new DiaryOpeningHours(diaryId, ohSpontaneous.getId(), MedicalAttentionType.SPONTANEOUS, (short) 0));

        save(new AppointmentState((short) AppointmentState.ASSIGNED, "Asignado"));
        save(new AppointmentState((short) AppointmentState.CONFIRMED, "Confirmado"));
        save(new AppointmentState((short) AppointmentState.ABSENT, "Ausente"));
        AppointmentState cancelledAppointmentState = save(new AppointmentState((short) AppointmentState.CANCELLED, "Cancelado"));
        save(new AppointmentState((short) AppointmentState.SERVED, "Atendido"));

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

        List<DailyAppointmentVo> result = dailyAppointmentRepository.getDailyAppointmentsByDiaryIdAndDate(institutionId, diaryId, date);

        assertThat(result)
                .hasSize(4);

        assertThat(result)
                .filteredOn(dav -> dav.getMedicalAttentionTypeId().equals(MedicalAttentionType.PROGRAMMED)).hasSize(2);

        assertThat(result)
                .filteredOn(dav -> dav.getMedicalAttentionTypeId().equals(MedicalAttentionType.SPONTANEOUS)).hasSize(2);

        assertThat(result)
                .allSatisfy(dav -> assertThat(dav.getAppointmentState()).isNotEqualTo(cancelledAppointmentState.getDescription()));
    }

    @Test
    public void test_getDailyAppointmentsByDiaryIdAndDate_differentInstitutionId(){
        Integer consultedInstitutionId = 5;

        Integer appointmentsInstitutionId = 2;
        LocalDate date = LocalDate.of(2020, 9, 5);

        DoctorsOffice doctorsOffice = mockDoctorsOffice(appointmentsInstitutionId);
        save(doctorsOffice);

        Diary diary = mockDiary(doctorsOffice.getId());
        Integer diaryId = save(diary).getId();

        Short dayWeekId = 1;
        OpeningHours ohProgrammed = save(new OpeningHours(dayWeekId, LocalTime.of(10, 0), LocalTime.of(12, 0)));
        OpeningHours ohSpontaneous = save(new OpeningHours(dayWeekId, LocalTime.of(15, 0), LocalTime.of(18, 0)));

        save(new DiaryOpeningHours(diaryId, ohProgrammed.getId(), MedicalAttentionType.PROGRAMMED, (short) 10));
        save(new DiaryOpeningHours(diaryId, ohSpontaneous.getId(), MedicalAttentionType.SPONTANEOUS, (short) 0));

        save(new AppointmentState((short) AppointmentState.ASSIGNED, "Asignado"));
        save(new AppointmentState((short) AppointmentState.CONFIRMED, "Confirmado"));
        save(new AppointmentState((short) AppointmentState.ABSENT, "Ausente"));
        AppointmentState cancelledAppointmentState = save(new AppointmentState((short) AppointmentState.CANCELLED, "Cancelado"));
        save(new AppointmentState((short) AppointmentState.SERVED, "Atendido"));

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

        List<DailyAppointmentVo> result = dailyAppointmentRepository.getDailyAppointmentsByDiaryIdAndDate(consultedInstitutionId, diaryId, date);

        assertThat(result)
                .hasSize(0);
    }

    private Diary mockDiary(Integer doctorsOfficeId){
        Diary diary = new Diary();
        diary.setHealthcareProfessionalId(1);
        diary.setDoctorsOfficeId(doctorsOfficeId);
        diary.setStartDate(LocalDate.of(2020, 9, 1));
        diary.setEndDate(LocalDate.of(2020, 11, 1));
        diary.setAppointmentDuration((short) 30);
        return diary;
    }

    private DoctorsOffice mockDoctorsOffice(Integer institutionId){
        DoctorsOffice doctorsOffice = new DoctorsOffice();
        doctorsOffice.setInstitutionId(institutionId);
        doctorsOffice.setClinicalSpecialtySectorId(10);
        doctorsOffice.setDescription("Office description");
        doctorsOffice.setOpeningTime(LocalTime.of(0, 0));
        doctorsOffice.setClosingTime(LocalTime.of(23, 59));
        return doctorsOffice;
    }
}
