package net.pladema.medicalconsultation.diary.repository;

import net.pladema.UnitRepository;
import net.pladema.medicalconsultation.diary.mocks.DiaryTestMocks;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryOpeningHoursVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryOpeningHours;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class DiaryOpeningHoursRepositoryTest extends UnitRepository {

	@Autowired
	private DiaryOpeningHoursRepository diaryOpeningHoursRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_active_diaries_from_professional() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Agenda 1
		String startDate = "2020-05-04";
		String endDate = "2020-06-04";
		Diary diary1 = save(DiaryTestMocks.createDiary(1, 1, LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, true));

		String from = "09:15";
		String to = "12:00";

		OpeningHours oh1 = save(new OpeningHours((short)1, LocalTime.parse(from, timeFormatter), LocalTime.parse(to, timeFormatter)));
		save(new DiaryOpeningHours(diary1.getId(), oh1.getId(), MedicalAttentionType.PROGRAMMED, (short) 1 ));


		from = "14:00";
		to = "18:00";
		OpeningHours oh2 = save(new OpeningHours((short)1, LocalTime.parse(from, timeFormatter), LocalTime.parse(to, timeFormatter)));
		save(new DiaryOpeningHours(diary1.getId(), oh2.getId(), MedicalAttentionType.SPONTANEOUS, (short) 4 ));

		// Agenda 2
		startDate = "2020-06-04";
		endDate = "2020-07-04";
		Diary diary2 = save(DiaryTestMocks.createDiary(1, 1, LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, true));

		from = "09:15";
		to = "12:00";
		OpeningHours oh3 = save(new OpeningHours((short)1, LocalTime.parse(from, timeFormatter), LocalTime.parse(to, timeFormatter)));
		save(new DiaryOpeningHours(diary2.getId(), oh3.getId(), MedicalAttentionType.PROGRAMMED, (short) 1 ));

		from = "14:00";
		to = "18:00";
		OpeningHours oh4 = save(new OpeningHours((short)1, LocalTime.parse(from, timeFormatter), LocalTime.parse(to, timeFormatter)));
		save(new DiaryOpeningHours(diary2.getId(), oh4.getId(), MedicalAttentionType.SPONTANEOUS, (short) 4 ));

		// Agenda 1
		List<DiaryOpeningHoursVo> resultQuery = diaryOpeningHoursRepository.getDiariesOpeningHours(Arrays.asList(diary1.getId()));

		assertThat(resultQuery)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2);

		resultQuery.stream().forEach(rq -> {
			assertThat(rq.getDiaryId())
					.isNotNull()
					.isEqualTo(diary1.getId());
		});
	}
}
