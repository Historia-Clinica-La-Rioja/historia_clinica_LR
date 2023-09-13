package ar.lamansys.sgh.publicapi.infrastructure.output;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import ar.lamansys.sgh.publicapi.domain.AppointmentCancellationBo;
import ar.lamansys.sgh.publicapi.domain.AppointmentStatusBo;

import ar.lamansys.sgh.publicapi.domain.EncounterModeBo;

import ar.lamansys.sgh.publicapi.domain.GenderSelfDeterminationBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.out.AppointmentStorage;
import ar.lamansys.sgh.publicapi.domain.AppointmentDataBo;
import ar.lamansys.sgh.publicapi.domain.AppointmentMedicalCoverageBo;
import ar.lamansys.sgh.publicapi.domain.AppointmentsByUserBo;
import ar.lamansys.sgh.publicapi.domain.ClinicalSpecialtyBo;
import ar.lamansys.sgh.publicapi.domain.DoctorBo;
import ar.lamansys.sgh.publicapi.domain.InstitutionBo;
import ar.lamansys.sgh.publicapi.domain.PatientDataBo;

@Service
public class AppointmentDataStorageImpl implements AppointmentStorage {

	private final EntityManager entityManager;
	private final LocalDateMapper localDateMapper;

	public AppointmentDataStorageImpl(EntityManager entityManager, LocalDateMapper localDateMapper) {
		this.entityManager = entityManager;
		this.localDateMapper = localDateMapper;
	}

	@Override
	public AppointmentsByUserBo getAppointmentsDataByDni(String identificationNumber, Short identificationTypeId, Short genderId, String birthDate) {

		String select = " SELECT p2.id as p2id, p2.first_name, p2.middle_names, p2.last_name, p2.other_last_names, p2.birth_date, pe.gender_self_determination, "
				+ "pe.name_self_determination, pe.phone_prefix, pe.phone_number, "
				+ "i.id as instiid, i.name as iname, i.sisa_code, a.street , a.number, c.description as cityDescription, d3.description as deptDescription, "
				+ "hp.id as hpid, hp.license_number , p3.first_name as p3FirstName , p3.last_name as p3LastName, p3.identification_number , cs.id as CSPID, cs.name as csname, cs.sctid_code , "
				+ "app.id AS TURNOID, app.date_type_id, app.hour, is_overturn, "
				+ "mc.name, mc.cuit, mcp.plan, as2.id, as2.description, FALSE AS is_online, 'mock_link' as virtual_meet, 'mock_modality' as encounter_modality, has.reason, has.changed_state_date, "
				+ "d2.description as d2Description, spg.description as spgdescription ";

		String birthDateCheck = birthDate != null ? " and p2.birth_date = :birthdate " : " ";

		String query = select
				+ "FROM appointment app JOIN patient p ON p.id = app.patient_id "
				+ "JOIN person p2 ON p2.id = p.person_id " + "left join person_extended pe on pe.person_id = p2.id "
				+ "left join self_perceived_gender spg on spg.id = pe.gender_self_determination "
				+ "LEFT JOIN appointment_state as2 ON as2.id = app.appointment_state_id "
				+ "LEFT JOIN patient_medical_coverage pmc ON app.patient_medical_coverage_id = pmc.id "
				+ "LEFT JOIN medical_coverage mc ON mc.id = pmc.medical_coverage_id "
				+ "LEFT JOIN medical_coverage_plan mcp ON mc.id = mcp.medical_coverage_id "
				+ "join appointment_assn aa on aa.appointment_id = app.id "
				+ "join diary d on aa.diary_id = d.id "
				+ "join doctors_office do2 on do2.id = d.doctors_office_id "
				+ "join institution i on i.id = do2.institution_id "
				+ "left join dependency d2 on i.dependency_id = d2.id "
				+ "left join address a on a.id = i.address_id "
				+ "join city c on c.id = a.city_id "
				+ "join department d3 on d3.id = c.department_id "
				+ "join healthcare_professional hp on hp.id = d.healthcare_professional_id "
				+ "join person p3 on p3.id = hp.person_id "
				+ "JOIN professional_professions pp on pp.healthcare_professional_id = hp.id "
				+ "JOIN healthcare_professional_specialty hps ON (hps.professional_profession_id = pp.id) "
				+ "JOIN clinical_specialty cs ON cs.id = hps.clinical_specialty_id "
				+ "left join historic_appointment_state has on has.appointment_id = app.id and app.appointment_state_id = 4 "
				+ "WHERE cs.clinical_specialty_type_id = 2 "
				+ "and p2.identification_number = :identificationNumber and p2.identification_type_id = :identificationTypeId and p2.gender_id = :genderId "
				+ birthDateCheck
				+ " and pp.deleted = false and hps.deleted = false "
				+ "union all "
				+ select
				+ "FROM booking_appointment ba "
				+ "JOIN booking_person bp ON bp.id = ba.booking_person_id "
				+ "JOIN person p2 ON p2.identification_number = bp.identification_number "
				+ "JOIN patient p ON p.person_id = p2.id " + "JOIN appointment app ON app.id = ba.appointment_id AND app.patient_id IS null "
				+ "left join person_extended pe on pe.person_id = p2.id "
				+ "left join self_perceived_gender spg on spg.id = pe.gender_self_determination "
				+ "LEFT JOIN appointment_state as2 ON as2.id = app.appointment_state_id "
				+ "LEFT JOIN patient_medical_coverage pmc ON app.patient_medical_coverage_id = pmc.id "
				+ "LEFT JOIN medical_coverage mc ON mc.id = pmc.medical_coverage_id "
				+ "LEFT JOIN medical_coverage_plan mcp ON mc.id = mcp.medical_coverage_id "
				+ "join appointment_assn aa on aa.appointment_id = app.id "
				+ "join diary d on aa.diary_id = d.id "
				+ "join doctors_office do2 on do2.id = d.doctors_office_id "
				+ "join institution i on i.id = do2.institution_id "
				+ "left join dependency d2 on i.dependency_id = d2.id "
				+ "left join address a on a.id = i.address_id "
				+ "join city c on c.id = a.city_id "
				+ "join department d3 on d3.id = c.department_id "
				+ "join healthcare_professional hp on hp.id = d.healthcare_professional_id "
				+ "join person p3 on p3.id = hp.person_id "
				+ "JOIN professional_professions pp on pp.healthcare_professional_id = hp.id "
				+ "JOIN healthcare_professional_specialty hps ON (hps.professional_profession_id = pp.id) "
				+ "JOIN clinical_specialty cs ON cs.id = hps.clinical_specialty_id "
				+ "left join historic_appointment_state has on has.appointment_id = app.id and app.appointment_state_id = 4 "
				+ "WHERE cs.clinical_specialty_type_id = 2 and bp.identification_number = :identificationNumber "
				+ birthDateCheck
				+ " and pp.deleted = false and hps.deleted = false "
				+ "order by turnoid, cspid";

		var queryResult = entityManager.createNativeQuery(query)
						.setParameter("identificationNumber", identificationNumber)
						.setParameter("identificationTypeId", identificationTypeId)
						.setParameter("genderId", genderId);

		if(birthDate != null) {
			queryResult.setParameter("birthdate", localDateMapper.fromStringToLocalDate(birthDate));
		}

		return assembleResponse(queryResult.getResultList());

	}

	private AppointmentsByUserBo assembleResponse(List<Object[]> queryResult) {
		AppointmentsByUserBo result = new AppointmentsByUserBo();
		if(!queryResult.isEmpty()) {
			PatientDataBo patientDataBo = PatientDataBo.builder()
					.id((Integer)queryResult.get(0)[0])
					.firstName((String)queryResult.get(0)[1])
					.middleNames(queryResult.get(0)[2] == null ? null : (String)queryResult.get(0)[2])
					.lastName((String)queryResult.get(0)[3])
					.otherLastName(queryResult.get(0)[4] == null ? null : (String)queryResult.get(0)[4])
					.birthDate(((Date)queryResult.get(0)[5]).toLocalDate())
					.genderSelfDetermination(GenderSelfDeterminationBo.builder()
							.id(queryResult.get(0)[6] == null ? null : ((Short)queryResult.get(0)[6]))
							.description(queryResult.get(0)[40] == null ? null : ((String)queryResult.get(0)[40]))
							.build())
					.nameSelfDetermination(queryResult.get(0)[7] == null ? null : (String)queryResult.get(0)[7])
					.phonePrefix(queryResult.get(0)[8] == null ? null : (String)queryResult.get(0)[8])
					.phoneNumber(queryResult.get(0)[9] == null ? null : (String)queryResult.get(0)[9])
					.build();
			result.setPatientData(patientDataBo);
			ArrayList<AppointmentDataBo> appointments = new ArrayList<>();
			queryResult.forEach(row -> addRow(appointments, row));
			result.setAppointmentData(appointments);
		}
		return result;
	}

	private void addRow(ArrayList<AppointmentDataBo> appointments, Object[] row) {
		AppointmentDataBo example = new AppointmentDataBo();
		example.setId((Integer)row[25]);
		int index = appointments.indexOf(example);
		if(index == -1) {
			appointments.add(getAppointmentData(row));
		} else {
			appointments.get(index).getDoctor().getSpecialtyList().add(ClinicalSpecialtyBo.builder()
					.id((Integer)row[22])
					.description((String)row[23])
					.snomedId((String)row[24])
					.build());
		}
	}


	private AppointmentDataBo getAppointmentData(Object[] row) {
		InstitutionBo institutionBo = InstitutionBo.builder()
				.id((Integer)row[10])
				.name((String)row[11])
				.sisaCode(row[12] == null ? null : (String)row[12])
				.address(row[13] == null ? null : (row[13] + " " + row[14]))
				.city((String)row[15])
				.department((String)row[16])
				.dependency(row[39] == null ? null : (String)row[39])
				.build();

		DoctorBo doctorBo = DoctorBo.builder()
				.id((Integer)row[17])
				.licenseNumber((String)row[18])
				.name((String)row[19])
				.lastName((String)row[20])
				.identificationNumber((String)row[21])
				.specialtyList(new ArrayList<>(List.of(ClinicalSpecialtyBo.builder()
								.id((Integer)row[22])
								.description((String)row[23])
								.snomedId((String)row[24])
								.build())))
				.build();

		Integer id = (Integer)row[25];
		LocalDate day = ((Date)row[26]).toLocalDate();
		LocalTime hour = ((Time)row[27]).toLocalTime();
		Boolean isOverturn = ((Boolean)row[28]);

		AppointmentMedicalCoverageBo appointmentMedicalCoverageBo = AppointmentMedicalCoverageBo.builder()
				.name(row[29] == null ? null : (String)row[29])
				.cuit(row[30] == null ? null : (String)row[30])
				.plan(row[31] == null ? null : (String)row[31])
				.build();

		AppointmentStatusBo appointmentStatusBo = AppointmentStatusBo.builder()
				.id((Short)row[32])
				.description((String)row[33])
				.build();

		Boolean isOnline = ((Boolean)row[34]);
		String virtualcallLink = ((String)row[35]);
		EncounterModeBo encounterModeBo = EncounterModeBo.builder()
				.id(1)
				.description((String)row[36])
				.build();
		AppointmentCancellationBo appointmentCancellationBo = AppointmentCancellationBo.builder()
				.reason(row[37] == null ? null : (String)row[37])
				.cancellationTime(row[38] == null ? null : ((Timestamp)row[38]).toLocalDateTime())
				.build();

		return AppointmentDataBo.builder()
				.institution(institutionBo)
				.doctor(doctorBo)
				.appointmentMedicalCoverage(appointmentMedicalCoverageBo)
				.appointmentStatus(appointmentStatusBo)
				.appointmentCancellation(appointmentCancellationBo)
				.videocallLink(virtualcallLink)
				.encounterMode(encounterModeBo)
				.isOverturn(isOverturn)
				.isOnline(isOnline)
				.id(id)
				.day(day)
				.hour(hour)
				.build();

	}
}
