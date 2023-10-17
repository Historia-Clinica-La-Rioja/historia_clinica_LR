package ar.lamansys.online.infraestructure.output.repository.diary;

import ar.lamansys.online.domain.diary.DiaryBasicDataBo;
import ar.lamansys.online.domain.diary.OpeningHoursBasicDataBo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Repository
public class DiaryBookingDataStorageImpl implements DiaryBookingDataStorage {

	private EntityManager entityManager;

	@Override
	public DiaryBasicDataBo fetchDiaryBasicDataByDiaryId(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);
		DiaryBasicDataBo result = new DiaryBasicDataBo();
		String sqlQuery = "SELECT d.start_date, d.end_date " +
				"FROM diary d " +
				"WHERE d.id = :diaryId";
		List<Object[]> queryResult = entityManager.createNativeQuery(sqlQuery).setParameter("diaryId", diaryId).getResultList();
		if (!queryResult.isEmpty()) {
			result.setStartDate(((Date) queryResult.get(0)[0]).toLocalDate());
			result.setEndDate(((Date) queryResult.get(0)[1]).toLocalDate());

			sqlQuery = "SELECT doh.opening_hours_id, oh.from, oh.to " +
					"FROM diary_opening_hours doh " +
					"JOIN opening_hours oh ON (oh.id = doh.opening_hours_id) " +
					"WHERE doh.diary_id = :diaryId";
			List<OpeningHoursBasicDataBo> openingHours = new ArrayList<>();
			queryResult = entityManager.createNativeQuery(sqlQuery).setParameter("diaryId", diaryId).getResultList();
			queryResult.forEach(partialResult -> {
				OpeningHoursBasicDataBo element = new OpeningHoursBasicDataBo();
				element.setId((Integer) partialResult[0]);
				element.setFrom(((Time) partialResult[1]).toLocalTime());
				element.setTo(((Time) partialResult[2]).toLocalTime());
				openingHours.add(element);
			});
			result.setOpeningHours(openingHours);
		}
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public boolean fetchIfAppointmentAlreadyAssigned(Integer diaryId, Integer openingHoursId, String date, String time) {
		log.debug("Input parameters -> diaryId {}, openingHoursId {}, time {}", diaryId, openingHoursId, time);
		String sqlQuery = "SELECT 1 " +
				"FROM appointment a " +
				"JOIN appointment_assn aa ON (aa.appointment_id = a.id) " +
				"WHERE a.hour = :time " +
				"AND aa.diary_id = :diaryId " +
				"AND aa.opening_hours_id = :openingHoursId " +
				"AND a.date_type_id = :date";
		List<Integer> queryResult = entityManager.createNativeQuery(sqlQuery)
				.setParameter("diaryId", diaryId)
				.setParameter("openingHoursId", openingHoursId)
				.setParameter("time", Time.valueOf(time))
				.setParameter("date", Date.valueOf(date))
				.getResultList();
		return !queryResult.isEmpty();
	}

}
