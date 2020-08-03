package net.pladema.medicalconsultation.diary.repository;

import net.pladema.UnitRepository;
import net.pladema.medicalconsultation.diary.mocks.DiaryTestMocks;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class DiaryRepositoryTest extends UnitRepository {

	@Autowired
	private DiaryRepository diaryRepository;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_active_diaries_from_professional() {

		String startDate = "2020-05-04";
		String endDate = "2020-06-04";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		save(DiaryTestMocks.createDiary(1, 1, LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, true));


		save(DiaryTestMocks.createDiary(1, 1, LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, false));


		save(DiaryTestMocks.createDiary(2, 1, LocalDate.parse(startDate, formatter),
				LocalDate.parse(endDate, formatter), (short) 1, true, (short) 4, true, true, true));

		List<DiaryListVo> resultQuery = diaryRepository.getActiveDiariesFromProfessional(1);

		Assertions.assertThat(resultQuery)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1);
	}

}
