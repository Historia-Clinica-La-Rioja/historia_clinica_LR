package ar.lamansys.online.infraestructure.output.repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Service;

import ar.lamansys.online.application.professional.ProfessionalAvailabilityStorage;
import ar.lamansys.online.domain.professional.AppointmentDiaryBo;
import ar.lamansys.online.domain.professional.AvailabilityBo;
import ar.lamansys.online.domain.professional.BookingProfessionalBo;
import ar.lamansys.online.domain.professional.DiaryAvailabilityBo;
import ar.lamansys.online.domain.professional.DiaryListBo;
import ar.lamansys.online.domain.professional.ProfessionalAvailabilityBo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class ProfessionalAvailabilityStorageImpl implements ProfessionalAvailabilityStorage {

	private static final Short PROGRAMADA = 1;

	private final EntityManager entityManager;


	@Override
	public Optional<ProfessionalAvailabilityBo> findAvailabilityByPracticeAndProfessional(
			Integer institutionId, Integer professionalId,
			Integer clinicalSpecialtyId,
			Integer practiceId
	) {
		log.debug("Find availability");

		var diaries = getActiveDiaries(professionalId, institutionId);
		var days = getDays(professionalId, clinicalSpecialtyId, practiceId);
		diaries = diaries.stream().filter(diaryListBo -> isForTheseDays(days, diaryListBo)).collect(Collectors.toList());
		var activeAppointments = getAppointmentsByDiaries(diaries);


		var slots = diaries.stream()
				.filter(this::isScheduled)
				.map(diary -> assembleListOfSlots(diary, days, activeAppointments))
				.flatMap(List::stream)
				.filter(diaryAvailabilityBo -> !diaryAvailabilityBo.getSlots().getSlots().isEmpty())
				.collect(Collectors.toList());

		ProfessionalAvailabilityBo availability = new ProfessionalAvailabilityBo(
				slots,
				new BookingProfessionalBo(
						professionalId,
						getName(professionalId),
				true
				)
		);

		log.debug("Find availability -> {}", availability);

		return Optional.of(availability);
	}

	private boolean isScheduled(DiaryListBo diary) {
		String query = "select doh.medical_attention_type_id  " +
				"from diary_opening_hours doh " +
				"where doh.opening_hours_id = :openingHoursId " +
				"and doh.diary_id = :diaryId " +
				"and doh.external_appointments_allowed = true";

		try {
			return entityManager.createNativeQuery(query)
					.setParameter("diaryId",diary.getId())
					.setParameter("openingHoursId", diary.getOpeningHoursId())
					.getSingleResult().equals(PROGRAMADA);
		}
		catch (NoResultException nre) {
			return false;
		}
	}

	private boolean isForTheseDays(List<Short> days, DiaryListBo diaryListBo) {
		var day = getOpeningHourDay(diaryListBo);
		return days.contains(day);
	}

	private Short getOpeningHourDay(DiaryListBo diaryListBo) {
		String query = "SELECT oh.day_week_id " +
				"FROM opening_hours oh " +
				"WHERE oh.id = :openingHoursId";
		return (Short)entityManager.createNativeQuery(query)
				.setParameter("openingHoursId", diaryListBo.getOpeningHoursId())
				.getSingleResult();
	}

	private String getName(Integer professionalId) {
		String query = "SELECT CONCAT(p.last_name,', ', p.first_name) as name " +
				"FROM v_booking_healthcare_professional hp " +
				"JOIN v_booking_person p ON hp.person_id = p.id " +
				"WHERE hp.id = :professionalId";
		return (String) entityManager.createNativeQuery(query)
				.setParameter("professionalId", professionalId)
				.getSingleResult();
	}

	private List<DiaryAvailabilityBo> assembleListOfSlots(DiaryListBo diary, List<Short> days, List<AppointmentDiaryBo> activeAppointments) {
		return createListOfSlots(diary, days, activeAppointments).stream()
				.map(slot -> new DiaryAvailabilityBo(diary, slot))
				.collect(Collectors.toList());
	}

	private List<AvailabilityBo> createListOfSlots(DiaryListBo diary, List<Short> days, List<AppointmentDiaryBo> activeAppointments) {
		var localDays = days.stream().map(Short::intValue).collect(Collectors.toList());

		var validDates = diary.getStartDate().isBefore(LocalDate.now())  ?
				getWorkingDays(LocalDate.now(), diary.getEndDate(), localDays) :
				getWorkingDays(diary.getStartDate(), diary.getEndDate(), localDays);

		return validDates.stream()
				.map(day -> constructAvailabilityBo(diary, day, activeAppointments))
				.collect(Collectors.toList());
	}

	private AvailabilityBo constructAvailabilityBo(DiaryListBo diary, LocalDate day, List<AppointmentDiaryBo> activeAppointments) {
		if(!getOpeningHourDay(diary).equals((short)day.getDayOfWeek().getValue())) {
			return new AvailabilityBo(day, new ArrayList<>());
		}

		var diaryAppointmentsForADay = activeAppointments.stream()
				.filter(appointmentDiaryBo ->
						appointmentDiaryBo.getDiaryId().equals(diary.getId()) &&
								appointmentDiaryBo.getDate().equals(day))
				.collect(Collectors.toList());

		var freeSlots = Stream.iterate(diary.getFrom(), d -> d.plusMinutes(diary.getAppointmentDuration()))
				.limit(ChronoUnit.MINUTES.between(diary.getFrom(), diary.getTo())/diary.getAppointmentDuration())
				.filter(time -> inTheFuture(time, day) && diaryAppointmentsForADay.stream().noneMatch(d -> d.getHour().equals(time)))
				.map(LocalTime::toString)
				.collect(Collectors.toList());

		return new AvailabilityBo(day, freeSlots);
	}

	private boolean inTheFuture(LocalTime time, LocalDate day) {
		return day.isAfter(LocalDate.now()) || time.isAfter(LocalTime.now());
	}

	private List<LocalDate> getWorkingDays(LocalDate start, LocalDate end, List<Integer> days) {
		return start.datesUntil(end).filter(date -> days.contains(date.getDayOfWeek().getValue()))
				.collect(Collectors.toList());
	}

	private List<Short> getDays(Integer professionalId, Integer clinicalSpecialtyId, Integer practiceId) {
		String query = "SELECT m.day " +
				"FROM mandatory_professional_practice_free_days AS m " +
				"JOIN clinical_specialty_mandatory_medical_practice AS c " +
				"ON c.id = m.clinical_specialty_mandatory_medical_practice_id " +
				"WHERE c.clinical_specialty_id = :clinicalSpecialtyId " +
				"AND c.mandatory_medical_practice_id = :practiceId " +
				"AND m.healthcare_professional_id = :healthcareProfessionalId";

		List<Object> result = this.entityManager.createNativeQuery(query)
				.setParameter("practiceId", practiceId)
				.setParameter("clinicalSpecialtyId", clinicalSpecialtyId)
				.setParameter("healthcareProfessionalId", professionalId)
				.getResultList();
		return result.stream().map(day -> (Short)day).collect(Collectors.toList());
	}

	private List<DiaryListBo> getActiveDiaries(Integer professionalId, Integer institutionId) {
		String query = "SELECT d.id, " +
				"d.doctors_office_id, " +
				"dof.description, " +
				"d.start_date, " +
				"d.end_date, " +
				"d.appointment_duration, " +
				"oh.from, " +
				"oh.to, " +
				"oh.id as opening_hours_id " +
				"FROM v_booking_diary AS d " +
				"JOIN v_booking_doctors_office AS dof ON (dof.id = d.doctors_office_id) " +
				"JOIN v_booking_diary_opening_hours AS doh ON (doh.diary_id = d.id) " +
				"JOIN v_booking_opening_hours AS oh ON (doh.opening_hours_id = oh.id) " +
				"WHERE d.healthcare_professional_id = :hcpId " +
				"AND dof.institution_id = :instId " +
				"AND d.active = true " +
				"AND (d.deleted IS NULL OR d.deleted = false) " +
				"AND d.end_date > CURRENT_DATE ";

		List<Object[]> result = this.entityManager.createNativeQuery(query)
				.setParameter("hcpId", professionalId)
				.setParameter("instId", institutionId)
				.getResultList();
		return result.stream().map(row -> new DiaryListBo(
				(Integer) row[0],
				(Integer) row[1],
				(String) row[2],
				((Date) row[3]).toLocalDate(),
				((Date) row[4]).toLocalDate(),
				(Short) row[5],
				((Time) row[6]).toLocalTime(),
				((Time) row[7]).toLocalTime(),
				(Integer) row[8]
		)).collect(Collectors.toList());
	}

	private List<AppointmentDiaryBo> getAppointmentsByDiaries(List<DiaryListBo> diaries) {
		String query = "SELECT a.id, a.patient_id, aa.diary_id, a.date_type_id, a.hour, a.appointment_state_id, a.is_overturn, " +
				"a.patient_medical_coverage_id, a.phone_number, doh.medical_attention_type_id " +
				"FROM v_booking_appointment AS a " +
				"JOIN v_booking_appointment_assn AS aa ON (a.id = aa.appointment_id) " +
				"JOIN v_booking_diary d ON (d.id = aa.diary_id) " +
				"JOIN v_booking_diary_opening_hours AS doh ON (doh.diary_id = d.id AND doh.opening_hours_id = aa.opening_hours_id) " +
				"WHERE aa.diary_id IN (:diaryIds) AND (d.deleted = false OR d.deleted IS NULL) " +
				"AND NOT a.appointment_state_id = 4 " +
				"AND a.date_type_id >= CURRENT_DATE " +
				"ORDER BY d.id, a.is_overturn";

		List<Object[]> result = this.entityManager.createNativeQuery(query)
				.setParameter("diaryIds", diaries.stream().map(DiaryListBo::getId).collect(Collectors.toList()))
				.getResultList();
		return result.stream().map(row -> new AppointmentDiaryBo(
				(Integer)row[0],
				(Integer)row[1],
				(Integer) row[2],
				((Date)row[3]).toLocalDate(),
				((Time)row[4]).toLocalTime(),
				(Short)row[5],
				(boolean)row[6],
				(Integer)row[7],
				(String)row[8],
				(Short)row[9])).collect(Collectors.toList());
	}

	@Override
	public Optional<List<ProfessionalAvailabilityBo>> findAvailabilityByPractice(Integer institutionId,
																				 Integer clinicalSpecialtyId,
																				 Integer practiceId,
																				 Integer medicalCoverageId) {
		log.debug("Find availability");
		List<BookingProfessionalBo> professionals = findProfessionals(institutionId, clinicalSpecialtyId, practiceId, medicalCoverageId);
		var result = professionals.stream()
				.map(professional -> findAvailabilityByPracticeAndProfessional(
					institutionId, 
					professional.getId(),
					clinicalSpecialtyId,
					practiceId
				))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
		log.debug("Find availability -> {}", result);
		return Optional.of(result);

	}

	private List<BookingProfessionalBo> findProfessionals(Integer institutionId, Integer clinicalSpecialtyId, Integer practiceId, Integer medicalCoverageId) {
		String query = "SELECT DISTINCT CONCAT(p.last_name,', ',p.first_name) AS professional, " +
				"mppfd.healthcare_professional_id " +
				"FROM mandatory_professional_practice_free_days mppfd " +
				"JOIN clinical_specialty_mandatory_medical_practice csmmp ON csmmp.id = mppfd.clinical_specialty_mandatory_medical_practice_id " +
				"JOIN v_booking_healthcare_professional hp ON mppfd.healthcare_professional_id = hp.id " +
				"JOIN v_booking_person p ON p.id = hp.person_id " +
				"INNER JOIN v_booking_user_person up ON up.person_id = p.id " +
				"INNER JOIN v_booking_user_role ur ON up.user_id = ur.user_id " +
				"WHERE ur.institution_id = :institutionId " +
				"AND csmmp.clinical_specialty_id = :clinicalSpecialtyId " +
				"AND csmmp.mandatory_medical_practice_id = :practiceId ";

		List<Object[]> result = entityManager.createNativeQuery(query)
				.setParameter("clinicalSpecialtyId", clinicalSpecialtyId)
				.setParameter("practiceId", practiceId)
				.setParameter("institutionId", institutionId)
				.getResultList();

		return result.stream().map(row -> new BookingProfessionalBo(
				(Integer)row[1],
				(String)row[0],
				isCovered((Integer)row[1], medicalCoverageId)
		)).collect(Collectors.toList());

	}

	private boolean isCovered(Integer healthcareProfessionalId, Integer medicalCoverageId) {
		String query = "SELECT CASE WHEN "+
				"EXISTS( SELECT 1 FROM healthcare_professional_health_insurance " +
				" WHERE healthcare_professional_id = :healthcareProfessionalId " +
				"AND medical_coverage_id = :medicalCoverageId) THEN 1 ELSE 0 END";

		return entityManager.createNativeQuery(query)
				.setParameter("medicalCoverageId", medicalCoverageId)
				.setParameter("healthcareProfessionalId", healthcareProfessionalId)
				.getSingleResult().equals(1);
	}

}
