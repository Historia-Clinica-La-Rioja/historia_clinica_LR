package ar.lamansys.sgh.publicapi.reports.infrastructure.output;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.reports.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.publicapi.reports.application.port.out.FetchDailyHoursStorage;
import ar.lamansys.sgh.publicapi.reports.domain.HierarchicalUnitBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.AppointmentAssnBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DailyHoursBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DayReportBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.DiaryOhsBo;
import ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate.ProfessionalDataBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchDailyHoursStorageImpl implements FetchDailyHoursStorage {

	private EntityManager entityManager;

	private static final Short SERVICE_TYPE = 8;

	@Override
	public List<DayReportBo> fetchDiaryHoursByDay(String from, String to, Integer institutionId, Integer hierarchicalUnitId) {
		LocalDate dateFrom = LocalDate.parse(from);
		LocalDate dateTo = LocalDate.parse(to);

		final String WHERE_INSTITUTION_ID = institutionId == null ? "" : " AND i.id = " + institutionId;
		final String WHERE_HIERARCHICAL_UNIT_ID = hierarchicalUnitId == null ? "" : " AND coalesce(hu.closest_service_id, hu2.closest_service_id) = " + hierarchicalUnitId;


		String query = "select distinct " +
				"i.id as id_institucion, i.name as nombre_institucion, " +
		"d.id as id_agenda, d.start_date as comienzo_agenda, " +
		"d.end_date as fin_agenda, d.appointment_duration as duracion_turno, " +
		"d.hierarchical_unit_id as id_unidad_jerarquica,  hu.alias as nombre_unidad_jerarquica, " +
		"doh.opening_hours_id as id_opening_hour, doh.overturn_count as cant_sobreturnos, oh.from as desde_opening_hour, " +
		"oh.to as hasta_opening_hour, oh.day_week_id as dia_de_la_semana, " +
 		"hp.id as id_profesional, pe.cuil as cuil_profesional, it.description as tipo_ident_profesional, " +
		"p.first_name as primer_nombre, p.middle_names as medio_nombre, p.last_name as apellido, " +
		"p.other_last_names as otros_apellidos, pe.name_self_determination as nombre_autodeterminado, " +
		"p.identification_number as numero_ident_profesional, cs.sctid_code, cs.name, " +
		"case when dp.id is null then 'Consulta' else 'Práctica' end, hu.type_id,  " +
		"hu2.id as closest_service_id, hu2.alias as closest_service_alias " +
		"from diary d join diary_opening_hours doh on doh.diary_id = d.id " +
		"join opening_hours oh on oh.id = doh.opening_hours_id " +
		"join doctors_office do2 on do2.id = d.doctors_office_id " +
		"join institution i on i.id = do2.institution_id " +
		"left join hierarchical_unit hu on hu.id = d.hierarchical_unit_id " +
		"left join hierarchical_unit hu2 on hu2.id = hu.closest_service_id " +
		"join healthcare_professional hp on hp.id = d.healthcare_professional_id " +
		"join person p on p.id = hp.person_id " +
		"join identification_type it on it.id = p.identification_type_id " +
		"left join person_extended pe on pe.person_id = p.id " +
		"left join clinical_specialty cs on cs.id = d.clinical_specialty_id " +
		"left join diary_practice dp on dp.diary_id = d.id " +
		"where d.deleted is not true and (:from <= d.end_date and d.start_date <= :to) " + WHERE_INSTITUTION_ID + WHERE_HIERARCHICAL_UNIT_ID;


		var queryResult = entityManager.createNativeQuery(query)
				.setParameter("from", dateFrom)
				.setParameter("to", dateTo)
				.getResultList();
		entityManager.clear();

		List<DayReportBo> result = new ArrayList<>();

		while(!dateFrom.isAfter(dateTo)){
			var day = DayReportBo.builder()
					.day(dateFrom)
					.dailyHours(generateDay(dateFrom, queryResult, hierarchicalUnitId))
					.build();

			if(!day.getDailyHours().isEmpty())
				result.add(day);

			dateFrom = dateFrom.plusDays(1);
		}

		return result;
	}

	private List<DailyHoursBo> generateDay(LocalDate date, List<Object[]> queryResult, Integer hierarchicalUnitId) {

		var filteredList = queryResult.stream()
				//filtro los otros día de la semana
				.filter(a -> a[12].equals((short)(date.getDayOfWeek().getValue() % 7)))
				//filtro las agendas que están por fuera de ese día
				.filter(a -> !(((Date)a[3]).toLocalDate().isAfter(date) || ((Date)a[4]).toLocalDate().isBefore(date)))
				.map(this::generateDiaryOhsBo)
				.collect(Collectors.toList());

		var ohs = filteredList.stream()
				.map(DiaryOhsBo::getOpeningHourId)
				.collect(Collectors.toList());

		var appointments = getAppointments(ohs, date);

		var mapByDiary = filteredList.stream()
				.collect(Collectors.groupingBy(DiaryOhsBo::getDiaryId));

		return mapByDiary.keySet().stream()
				.map(elem -> buildDailyHoursBo(mapByDiary.get(elem), appointments.stream()
						.filter(a -> a.getDiaryId().equals(elem))
						.collect(Collectors.toList())))
				.collect(Collectors.toList());
	}

	private DiaryOhsBo generateDiaryOhsBo(Object[] a) {

		return DiaryOhsBo.builder()
				.institutionId((Integer)a[0])
				.institutionName((String)a[1])
				.diaryId((Integer)a[2])
				.diaryStart(((Date)a[3]).toLocalDate())
				.diaryEnd(((Date)a[4]).toLocalDate())
				.appointmentDuration((Short)a[5])
				.hierarchicalUnitId((Integer)a[6])
				.hierarchicalUnitAlias((String)a[7])
				.hierarchicalUnitType((Integer)a[25])
				.openingHourId((Integer)a[8])
				.overturnCount((Short)a[9])
				.openingHourFrom(((Time)a[10]).toLocalTime())
				.openingHourTo(((Time)a[11]).toLocalTime())
				.dayOfTheWeek((Short)a[12])
				.professionalId((Integer)a[13])
				.cuil((String)a[14])
				.identificationType((String)a[15])
				.firstName((String)a[16])
				.middleName(a[17] == null ? null : (String)a[17])
				.lastName((String)a[18])
				.otherLastNames(a[19] == null ? null : (String)a[19])
				.selfPerceivedName(a[20] == null ? null : (String)a[20])
				.identificationNumber((String)a[21])
				.clinicalSpecialtySnomed((String)a[22])
				.clinicalSpecialtyName((String)a[23])
				.diaryType((String)a[24])
				.hierarchicalUnitType((Integer)a[25])
				.hierarchicalParentServiceUnitId((Integer)a[26])
				.hierarchicalParentServiceUnitAlias((String)a[27])
				.build();
	}

	private DailyHoursBo buildDailyHoursBo(List<DiaryOhsBo> query, List<AppointmentAssnBo> appointments) {

		var firstElem = query.get(0);
		var countBlocked = appointments.stream().filter(ap -> ap.getBlockReason() != null).count();
		var countAppointments = appointments.size() - countBlocked;
		var appointmentsMinutes = query.stream().mapToLong(e -> ChronoUnit.MINUTES.between(e.getOpeningHourFrom(), e.getOpeningHourTo())).sum() + 1;
		var countOverturns = query.stream().mapToInt(DiaryOhsBo::getOverturnCount).sum();
		var possibleAppointments = appointmentsMinutes / firstElem.getAppointmentDuration() - countBlocked;

		return DailyHoursBo.builder()
				.diaryId(firstElem.getDiaryId())
				.institutionCode(firstElem.getInstitutionId().toString())
				.institutionName(firstElem.getInstitutionName())
				.hierarchicalUnit(firstElem.getHierarchicalUnitId() != null ?
						new HierarchicalUnitBo(firstElem.getHierarchicalUnitId(),
						firstElem.getHierarchicalUnitAlias(), firstElem.getHierarchicalUnitType().shortValue()) :
						new HierarchicalUnitBo(null, null, null))
				.serviceHierarchicalUnit(firstElem.getHierarchicalParentServiceUnitId() != null ?
						new HierarchicalUnitBo(firstElem.getHierarchicalParentServiceUnitId(),
						firstElem.getHierarchicalParentServiceUnitAlias(), SERVICE_TYPE) :
						new HierarchicalUnitBo(null, null, null))
				.professionalData(ProfessionalDataBo.builder()
						.id(firstElem.getProfessionalId())
						.identificationType(firstElem.getIdentificationType())
						.identificationNumber(firstElem.getIdentificationNumber())
						.cuil(firstElem.getCuil())
						.lastName(firstElem.getLastName())
						.middleNames(firstElem.getMiddleName())
						.firstName(firstElem.getFirstName())
						.selfPerceivedName(firstElem.getSelfPerceivedName())
						.build())
				.clinicalSpecialty(ClinicalSpecialtyBo.builder()
						.description(firstElem.getClinicalSpecialtyName())
						.snomedId(firstElem.getClinicalSpecialtySnomed())
						.build())
				.diaryType(firstElem.getDiaryType())
				.minutesInAppointments(countAppointments * firstElem.getAppointmentDuration())
				.possibleAppointments(possibleAppointments == 0 ? possibleAppointments : possibleAppointments + countOverturns)
				.interruptions(countBlocked)
				.interruptionsDescriptions(appointments.stream()
						.map(AppointmentAssnBo::getBlockReason)
						.filter(Objects::nonNull)
						.distinct().collect(Collectors.toList()))
				.build();
	}

	private List<AppointmentAssnBo> getAppointments(List<Integer> ohs, LocalDate day) {
		String query = "WITH excluded(item) AS (VALUES(4)) " +
				"select a.id, aa.diary_id, aa.opening_hours_id, abm.description " +
				"from appointment a left join appointment_block_motive abm on abm.id = a.appointment_block_motive_id " +
				"join appointment_assn aa on aa.appointment_id = a.id " +
				"where aa.opening_hours_id in :ohs and a.date_type_id = :date and a.deleted is not true " +
				"and not exists (SELECT 1 FROM excluded e WHERE a.appointment_state_id = e.item)";

		List<Object[]> result = entityManager.createNativeQuery(query)
				.setParameter("ohs", ohs)
				.setParameter("date", day)
				.getResultList();

		return result.stream()
				.map(elem -> AppointmentAssnBo.builder()
						.appointmentId((Integer) elem[0])
						.diaryId((Integer) elem[1])
						.openingHoursId((Integer) elem[2])
						.blockReason(elem[3] == null ? null : (String)elem[3])
						.build())
				.collect(Collectors.toList());

	}
}
