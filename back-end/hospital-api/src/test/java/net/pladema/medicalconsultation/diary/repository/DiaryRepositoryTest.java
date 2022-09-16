package net.pladema.medicalconsultation.diary.repository;

import net.pladema.UnitRepository;
import net.pladema.medicalconsultation.diary.mocks.DiaryTestMocks;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class DiaryRepositoryTest extends UnitRepository {

	@Autowired
	private DiaryRepository diaryRepository;

	@BeforeEach
	public void setUp() throws Exception {
	}

	@Test
	@Disabled
	void test_active_diaries_from_professional() {

		String startDate = "2020-05-04";
		String endDate = "2020-06-04";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalTime openingTime = LocalTime.parse("09:00:23", timeFormatter);
		LocalTime closingTime = LocalTime.parse("12:00:32", timeFormatter);


		DoctorsOffice doctorsOffice = save(DiaryTestMocks.createDoctorsOffice(1, 1, "DOCTORS_OFFICE", openingTime, closingTime));

		save(DiaryTestMocks.createDiary(1, doctorsOffice.getId(), LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, true, 1));


		save(DiaryTestMocks.createDiary(1, 1, LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, false, 1));


		save(DiaryTestMocks.createDiary(2, 1, LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, true, 1));

		save(DiaryTestMocks.createClinicalSpecialty((short) 1, "1", "1"));

		List<DiaryListVo> resultQuery = diaryRepository.getActiveDiariesFromProfessional(1, 1);

		Assertions.assertThat(resultQuery)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
	}

}
