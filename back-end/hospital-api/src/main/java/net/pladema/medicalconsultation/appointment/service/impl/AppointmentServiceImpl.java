package net.pladema.medicalconsultation.appointment.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;
import lombok.AllArgsConstructor;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBookingBo;
import net.pladema.patient.service.PatientMedicalCoverageService;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment.RecurringAppointmentOption;
import net.pladema.medicalconsultation.appointment.infraestructure.output.repository.appointment.RecurringAppointmentType;
import net.pladema.medicalconsultation.appointment.repository.AppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentUpdateRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentEquipmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import net.pladema.medicalconsultation.appointment.repository.entity.CustomAppointment;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.RecurringAppointmentException;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.UpdateAppointmentDateException;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.UpdateAppointmentDateExceptionEnum;
import net.pladema.medicalconsultation.diary.service.domain.BlockBo;
import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.application.ports.OrderImageFileStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.repository.AppointmentObservationRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketImageBo;
import net.pladema.medicalconsultation.appointment.repository.domain.MedicalCoverageAppoinmentOrderBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentObservation;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSummaryBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentEnumException;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentException;
import net.pladema.medicalconsultation.appointment.service.ports.AppointmentStorage;
import net.pladema.medicalconsultation.appointment.service.ports.EquipmentAppointmentStorage;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.patient.service.domain.PatientCoverageInsuranceDetailsBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

@AllArgsConstructor
@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

	private static final String OUTPUT = "Output -> {}";

	private final static Integer NO_CHILD_APPOINTMENTS = 0;

	private final static Integer ONE_APPOINTMENT_LEFT = 1;

	private final AppointmentRepository appointmentRepository;

	private final AppointmentObservationRepository appointmentObservationRepository;

	private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

	private final SharedStaffPort sharedStaffPort;

	private final FeatureFlagsService featureFlagsService;

	private final DateTimeProvider dateTimeProvider;

	private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;

	private final InstitutionExternalService institutionExternalService;

	private final MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	private final AppointmentStorage appointmentStorage;

	private final AppointmentUpdateRepository appointmentUpdateRepository;

	private final AppointmentOrderImageRepository appointmentOrderImageRepository;

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	private final LocalDateMapper localDateMapper;

	private final EquipmentAppointmentStorage equipmentAppointmentStorage;

	private final OrderImageFileStorage orderImageFileStorage;

	private final PatientMedicalCoverageService patientMedicalCoverageService;

	private final AppointmentAssnRepository appointmentAssnRepository;

	@Override
	public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds, LocalDate from, LocalDate to) {
		log.debug("Input parameters -> diaryIds {}", diaryIds);
		Collection<AppointmentBo> result = new ArrayList<>();
		if (!diaryIds.isEmpty()) {
			result = appointmentStorage.getAppointmentsByDiaries(diaryIds, from, to).stream().distinct()
					.collect(Collectors.toList());
		}
		result = setIsAppointmentProtected(result, diaryIds);
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByEquipmentDiary(Integer equipmentDiaryId, LocalDate from, LocalDate to) {
		log.debug("Input parameters -> equipmentDiaryId {}", equipmentDiaryId);
		Collection<AppointmentBo> result = new ArrayList<>();
		result = appointmentStorage.getAppointmentsByEquipmentDiary(equipmentDiaryId, from, to).stream().distinct()
				.collect(Collectors.toList());

		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<EquipmentAppointmentBo> getAppointmentsByEquipmentId(Integer equipmentId, Integer institutionId, LocalDate from, LocalDate to) {
		log.debug("Input parameters -> equipmentDiaryId {} institutionId {}, from {} to {}", equipmentId, institutionId, from, to);
		Collection<EquipmentAppointmentBo> result = equipmentAppointmentStorage.getAppointmentsByEquipmentId(equipmentId, institutionId, from, to)
				.stream()
				.distinct()
				.sorted(Comparator.comparing(EquipmentAppointmentBo::getDate, Comparator.nullsFirst(Comparator.naturalOrder())).thenComparing(EquipmentAppointmentBo::getHour))
				.map(e -> {
					e.setTranscribedOrderAttachedFiles(orderImageFileStorage.getOrderImageFileInfo(e.getTranscribedServiceRequestId()));
					return e;
				})
				.collect(Collectors.toList());
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByProfessionalInInstitution(Integer healthcareProfessionalId, Integer institutionId, LocalDate from, LocalDate to) {
		log.debug("Input parameters -> diaryIds {}", healthcareProfessionalId);
		Collection<AppointmentBo> result = new ArrayList<>();
		if (healthcareProfessionalId!=null) {
			result = appointmentStorage.getAppointmentsByProfessionalInInstitution(healthcareProfessionalId, institutionId, from, to).stream()
					.distinct()
					.collect(Collectors.toList());
			List<Integer> diaryIds = result.stream().map(AppointmentBo::getDiaryId).collect(Collectors.toList());
			result = setIsAppointmentProtected(result, diaryIds);
		}
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}


	@Override
	public Collection<AppointmentBo> getFutureActiveAppointmentsByDiary(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);
		Collection<AppointmentBo> result = appointmentRepository.getFutureActiveAppointmentsByDiary(diaryId).stream()
				.map(AppointmentBo::fromAppointmentDiaryVo)
				.distinct()
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour) {
		log.debug("Input parameters -> diaryId {}, openingHoursId {}, date {}, hour {}, appointmentId {}", diaryId, openingHoursId, date, hour);
		boolean result = appointmentRepository.existAppointment(diaryId, openingHoursId, date, hour);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<AppointmentBo> findBlockedAppointmentBy(Integer diaryId, LocalDate date, LocalTime hour) {
		log.debug("Input parameters -> diaryId {}, date {}, hour {}", diaryId, date, hour);
		var res = appointmentRepository.findBlockedAppointmentBy(diaryId, date, hour);
		log.debug(OUTPUT, res);
		return res.stream().findFirst().map(AppointmentBo::newFromAppointment);
	}

	public boolean existAppointment(Integer diaryId, LocalDate date, LocalTime hour, Integer appointmentId) {
		log.debug("Input parameters -> diaryId {}, date {}, hour {}, appointmentId {}", diaryId, date, hour, appointmentId);
		boolean result = appointmentRepository.existAppointment(diaryId, date, hour, appointmentId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	@Transactional
	public boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason) {
		log.debug("Input parameters -> appointmentId {}, appointmentStateId {}, userId {}, reason {}", appointmentId, appointmentStateId, userId, reason);
		appointmentRepository.updateState(appointmentId, appointmentStateId, userId);
		if(appointmentStateId == 4 || appointmentStateId == 3) //si se cambia a "Ausente" o "Cancelado", se borra la asociacion turno-orden medica
			appointmentOrderImageRepository.deleteByAppointment(appointmentId);

		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO)) {
			Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);

			if (appointment.isPresent() && appointment.get().getParentAppointmentId() != null)
				checkRemainingChildAppointments(appointment.get().getParentAppointmentId());
		}

		historicAppointmentStateRepository.save(new HistoricAppointmentState(appointmentId, appointmentStateId, reason));
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public Optional<AppointmentBo> getAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<AppointmentBo>	result = appointmentRepository.getAppointment(appointmentId).stream().findFirst().map(AppointmentBo::fromAppointmentVo);
		if (result.isPresent()) {
			List<Integer> diaryIds = result.stream().map(AppointmentBo::getDiaryId).collect(Collectors.toList());
			result = setIsAppointmentProtected(result.stream().collect(Collectors.toList()), diaryIds)
					.stream().findFirst();
			var referenceAppointment = sharedReferenceCounterReference.getReferenceByAppointmentId(result.get().getId());
			result.get().setHasAssociatedReference(referenceAppointment.isPresent());
			result.get().setAssociatedReferenceClosureTypeId(referenceAppointment.map(ReferenceAppointmentStateDto::getReferenceClosureTypeId).orElse(null));
		}
		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO)) {
			if (appointmentRepository.recurringAppointmentQuantityByParentId(appointmentId) == NO_CHILD_APPOINTMENTS) {
				if (result.isPresent())
					result.get().setHasAppointmentChilds(false);
			} else {
				if (result.isPresent())
					result.get().setHasAppointmentChilds(true);
			}
		}
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<AppointmentBo> getAppointmentSummary(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<AppointmentBo>	result = appointmentRepository.getAppointmentSummary(appointmentId).stream().findFirst().map(AppointmentBo::fromAppointmentVo);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Optional<AppointmentBo> getEquipmentAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<AppointmentBo>	result = appointmentRepository.getEquipmentAppointment(appointmentId).stream().findFirst().map(AppointmentBo::fromAppointmentVo);
		if (result.isPresent()) {
			List<Integer> diaryIds = result.stream().map(AppointmentBo::getDiaryId).collect(Collectors.toList());
			result = setIsAppointmentProtected(result.stream().collect(Collectors.toList()), diaryIds)
					.stream().findFirst();
		}
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean hasCurrentAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}, date {} ", patientId, healthProfessionalId, date);
		boolean result = !(appointmentRepository.getAppointmentsId(patientId, healthProfessionalId, date).isEmpty());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean hasOldAppointment(Integer patientId, Integer healthProfessionalId) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}", patientId, healthProfessionalId);
		boolean result = !(appointmentRepository.getOldAppointmentsId(patientId, healthProfessionalId).isEmpty());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<Integer> getOldAppointments(Integer patientId, Integer healthProfessionalId) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}", patientId, healthProfessionalId);
		List<Integer> result = appointmentRepository.getOldAppointmentsId(patientId, healthProfessionalId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthcareProfessionalId {}", patientId, healthcareProfessionalId);
		List<Integer> result = appointmentRepository.getAppointmentsId(patientId, healthcareProfessionalId, date);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean updatePhoneNumber(Integer appointmentId, String phonePrefix, String phoneNumber, Integer userId) {
		appointmentRepository.updatePhoneNumber(appointmentId,phonePrefix,phoneNumber,userId);
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public boolean saveObservation(Integer appointmentId, String observation) {
		AppointmentObservation appointmentObservation = AppointmentObservation.builder()
				.appointmentId(appointmentId)
				.observation(observation)
				.createdBy(UserInfo.getCurrentAuditor())
				.build();
		appointmentObservationRepository.save(appointmentObservation);
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public boolean updateDate(Integer appointmentId, LocalDate date, LocalTime time, Integer openingHoursId, Short recurringAppointmentTypeId) {
		appointmentRepository.updateDate(appointmentId, date, time);

		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO)) {
			appointmentRepository.updateRecurringType(appointmentId, recurringAppointmentTypeId);
		}

		appointmentAssnRepository.updateOpeningHoursId(openingHoursId, appointmentId);
		verifyProtectedCondition(appointmentId);
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public boolean updateMedicalCoverage(Integer appointmentId, Integer patientMedicalCoverage) {
		appointmentRepository.findById(appointmentId).ifPresent(a -> {
			if(a.isAssigned()) {
				a.setPatientMedicalCoverageId(patientMedicalCoverage);
				appointmentRepository.save(a);
			}
		});
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public List<AppointmentBookingBo> getCompleteBookingAppointmentInfo(String identificationNumber) {
		log.debug("Input parameters -> identificationNumber {}", identificationNumber);
		List<AppointmentBookingBo> result = appointmentRepository.getCompleteBookingAppointmentInfo(identificationNumber).stream().map(AppointmentBookingBo::new)
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId,
									  LocalDate currentDate) {
		List<Integer> medicalCoverages = appointmentRepository.getMedicalCoverage(patientId, currentDate,
				AppointmentState.CONFIRMED,AppointmentState.ASSIGNED, healthcareProfessionalId);
		Integer patientMedicalCoverageId = medicalCoverages.stream().findAny().orElse(null);
		log.debug(OUTPUT, patientMedicalCoverageId);
		return patientMedicalCoverageId;
	}

	@Override
	public PatientMedicalCoverageBo getMedicalCoverageFromAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		return appointmentRepository.getAppointmentMedicalCoverageId(appointmentId)
				.flatMap(patientMedicalCoverageService::getCoverage)
				.orElse(null);
	}

	@Override
	public PatientMedicalCoverageBo getCurrentAppointmentMedicalCoverage(Integer patientId, Integer institutionId) {
		log.debug("Input parameters -> patientId {}, institutionId {}", patientId, institutionId);

		var appointmentId = this.getCurrentAppointmentId(patientId, institutionId);
		if(appointmentId == null)
			return null;

		var medicalCoverageIdOpt = appointmentRepository.getAppointmentMedicalCoverageId(appointmentId);
		if(medicalCoverageIdOpt.isPresent()){
			var result = this.makePatientMedicalCoverageBo(patientExternalMedicalCoverageService
					.getCoverage(medicalCoverageIdOpt.get()));
			if(result != null) {
				log.trace(OUTPUT, result);
				return result;
			}
		}

		return new PatientMedicalCoverageBo();
	}

	public boolean setAppointmentPatientMedicalCoverageId(Integer patientId, List<Integer> patientMedicalCoverages, Integer newPatientMedicalCoverageId) {
		log.debug("Input parameters -> patientId {}, patientMedicalCoverages {}, newValuePMCId {}", patientId, patientMedicalCoverages, newPatientMedicalCoverageId);

		ZonedDateTime zonedDateTime = localDateMapper.fromLocalDateTimeToZonedDateTime(LocalDateTime.now());
		LocalDate today = zonedDateTime.toLocalDate();
		LocalTime currentHour = zonedDateTime.toLocalTime();

		patientMedicalCoverages.forEach((patientMedicalCoverageId) ->
						appointmentRepository.updateAppointmentsIdByPatientMedicalCoverage(patientId, patientMedicalCoverageId, today, currentHour, newPatientMedicalCoverageId)
				);

		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public Collection<AppointmentBo> getFutureActiveAppointmentsByEquipmentDiary(Integer equipmentDiaryId) {
		log.debug("Input parameters -> equipmentDiaryId {}", equipmentDiaryId);
		Collection<AppointmentBo> result = appointmentRepository.getFutureActiveAppointmentsByEquipmentDiary(equipmentDiaryId).stream()
				.map(AppointmentBo::fromAppointmentDiaryVo)
				.distinct()
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	private Integer getCurrentAppointmentId(Integer patientId, Integer institutionId) {
		log.debug("Input parameters -> patientId {}, institutionId {}", patientId, institutionId);

		ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
		var currentDateTime = dateTimeProvider.nowDateTimeWithZone(institutionZoneId);
		var userId = UserInfo.getCurrentAuditor();
		var getCurrentAppointmentsByPatient = appointmentRepository.getCurrentAppointmentsByPatient(patientId,
				institutionId, userId, currentDateTime.toLocalDate());
		if(getCurrentAppointmentsByPatient.isEmpty())
			return null;

		var result = getCurrentAppointmentsByPatient.get(0);
		log.debug(OUTPUT, result);
		return result;
	}

	private PatientMedicalCoverageBo makePatientMedicalCoverageBo(PatientMedicalCoverageDto dto){
		log.trace("Input parameters -> patientMedicalCoverageDto {}", dto);
		if(dto == null)
			return null;

		var coverageDto = dto.getMedicalCoverage();
		var medicalCoverageBo = new PatientCoverageInsuranceDetailsBo(coverageDto.getId(),
				coverageDto.getName(), coverageDto.getCuit(), coverageDto.getType());
		var vigencyDate = dto.getVigencyDate();

		var result = new PatientMedicalCoverageBo(dto.getId(),
				vigencyDate != null ? LocalDate.parse(vigencyDate) : null,
				dto.getActive(),
				dto.getAffiliateNumber(),
				medicalCoverageBo,
				dto.getCondition(),
				dto.getStartDate() != null ? LocalDate.parse(dto.getStartDate()) : null,
				dto.getEndDate() != null ? LocalDate.parse(dto.getEndDate()) : null,
				dto.getPlanId(),
				dto.getPlanName());

		if (dto.getPlanId() != null) {
			var planIdOpt = medicalCoveragePlanRepository.findById(dto.getPlanId());
			planIdOpt.ifPresent(medicalCoveragePlan -> result.setPlanName(medicalCoveragePlan.getPlan()));
		}

		log.trace(OUTPUT, result);
		return result;
	}

	private Collection<AppointmentAssignedBo> getAssignedAppointmentsByPatient(Integer patientId, LocalDate minDate, LocalDate maxDate) {
		log.debug("Input parameters -> patientId {}", patientId);
		Collection<AppointmentAssignedBo> result;
		result = appointmentRepository.getAssignedAppointmentsByPatient(patientId, minDate, maxDate).stream().map(AppointmentAssignedBo::new)
				.collect(Collectors.toList());
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<AppointmentAssignedBo> getCompleteAssignedAppointmentInfo(Integer patientId, LocalDate minDate, LocalDate maxDate){
		log.debug("Input parameters -> patientId {}, minDate {}, maxDate {}", patientId, minDate, maxDate);
		Collection<AppointmentAssignedBo> result = this.getAssignedAppointmentsByPatient(patientId, minDate, maxDate);

		List<Integer> userIds = result.stream().map(AppointmentAssignedBo::getProfessionalId)
				.distinct().collect(Collectors.toList());
		Map<Integer, ProfessionalInfoDto> professionals = userIds.stream()
				.collect(Collectors.toMap(id -> id, sharedStaffPort::getProfessionalCompleteInfo));
		result.forEach(appointment -> {
			ProfessionalInfoDto professional = professionals.get(appointment.getProfessionalId());
			appointment.setClinicalSpecialtyName(appointment.getClinicalSpecialtyName());
			appointment.setRespectiveProfessionalName(professional.getFirstName(),professional.getMiddleNames(),
					professional.getLastName(), professional.getOtherLastNames(), professional.getNameSelfDetermination(),
					featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
			var referenceAppointment = sharedReferenceCounterReference.getReferenceByAppointmentId(appointment.getId());
			appointment.setHasAssociatedReference(referenceAppointment.isPresent());
			appointment.setAssociatedReferenceClosureTypeId(referenceAppointment.map(ReferenceAppointmentStateDto::getReferenceClosureTypeId).orElse(null));
		});
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public AppointmentBo updateAppointment(UpdateAppointmentBo updateAppointmentBo) {
		var appointment = appointmentRepository.findById(updateAppointmentBo.getAppointmentId());
		if(appointment.isPresent()){
			appointment.get().setAppointmentStateId(updateAppointmentBo.getAppointmentStateId());
			appointment.get().setPatientId(updateAppointmentBo.getPatientId());
			appointment.get().setPatientMedicalCoverageId(updateAppointmentBo.getPatientMedicalCoverageId());
			appointment.get().setIsOverturn(updateAppointmentBo.isOverturn());
			appointment.get().setPhoneNumber(updateAppointmentBo.getPhoneNumber());
			return AppointmentBo.newFromAppointment(appointmentRepository.save(appointment.get()));
		}
		return new AppointmentBo();
	}

	@Override
	public void delete(AppointmentBo appointmentBo) {
		appointmentRepository.deleteById(appointmentBo.getId());
	}

	@Override
	public AppointmentTicketBo getAppointmentTicketData(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		var result = this.appointmentRepository.getAppointmentTicketData(appointmentId).orElseThrow(
				()-> new AppointmentException(AppointmentEnumException.APPOINTMENT_ID_NOT_FOUND, "el id no corresponde con ningun turno asignado")
		);
		result.setIncludeNameSelfDetermination(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS.isActive());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public AppointmentTicketImageBo getAppointmentImageTicketData(Integer appointmentId, boolean isTranscribed) {
		log.debug("Input parameters -> appointmentId {}, transcribed {}", appointmentId, isTranscribed);
	   	var result = isTranscribed ? this.appointmentRepository.getAppointmentImageTranscribedTicketData(appointmentId).orElseThrow(
				()-> new AppointmentException(AppointmentEnumException.APPOINTMENT_ID_NOT_FOUND, "el id no corresponde con ningun turno asignado")) :
				this.appointmentRepository.getAppointmentImageTicketData(appointmentId).orElseThrow(
						()-> new AppointmentException(AppointmentEnumException.APPOINTMENT_ID_NOT_FOUND, "el id no corresponde con ningun turno asignado"));
		if (result.getMedicalCoverage() == null && result.getMedicalCoverageAcronym() == null)
		{
			Optional<MedicalCoverageAppoinmentOrderBo> medicalCoverage = this.appointmentRepository.getMedicalCoverageOrderByAppointment(appointmentId);
			result.setMedicalCoverage(medicalCoverage.get().getMedicalCoverage());
			result.setMedicalCoverageAcronym(medicalCoverage.get().getMedicalCoverageAcronym());
		}
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public AppointmentShortSummaryBo getAppointmentFromDeterminatedDate(Integer patientId, Integer institutionId, LocalDate date, LocalTime hour) {
		log.debug("Input parameters -> patientId {}, date {}", patientId, date);
		validateAppointmentDate(institutionId,date,hour);
		AppointmentShortSummaryBo result = null;
		List<AppointmentShortSummaryBo> appointmentShortSummaryBoList = this.appointmentRepository.getAppointmentFromDeterminatedDate(patientId, date);
		if (!appointmentShortSummaryBoList.isEmpty())
			result = appointmentShortSummaryBoList.get(0);
		return result;
	}

	private void validateAppointmentDate(Integer institutionId, LocalDate date, LocalTime hour) {
		ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
		LocalDate todayDate = LocalDateTime.now().minusHours(3).toLocalDate();
		LocalTime todayTime = dateTimeProvider.nowDateTimeWithZone(institutionZoneId).toLocalTime();

		if (date.isBefore(todayDate))
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_BEFORE_NOW, "La fecha del turno es anterior a la fecha actual.");
		if (date.isEqual(todayDate) && hour.isBefore(todayTime))
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_BEFORE_NOW, "El horario del turno es anterior al horario actual.");
	}

	@Override
	public AppointmentEquipmentShortSummaryBo getAppointmentEquipmentFromDeterminatedDate(Integer patientId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, date {}", patientId, date);
		AppointmentEquipmentShortSummaryBo result = null;
		List<AppointmentEquipmentShortSummaryBo> appointmentEquipmentShortSummaryBoList = this.appointmentRepository.getAppointmentEquipmentFromDeterminatedDate(patientId, date);
		if (!appointmentEquipmentShortSummaryBoList.isEmpty())
			result = appointmentEquipmentShortSummaryBoList.get(0);
		return result;
	}


	@Override
	public List<Integer> getAppointmentsBeforeDateByStates(List<Short> statesIds, LocalDateTime maxAppointmentDate, Short limit){
		log.debug("Input parameters -> stateIds {}, maxAppointmentDate {}, limit {}", statesIds, maxAppointmentDate, limit);
		var result = appointmentUpdateRepository.getAppointmentsBeforeDateByStates(statesIds, maxAppointmentDate, limit);
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public List<AppointmentBo> generateBlockedAppointments(Integer diaryId, BlockBo block, DiaryBo diaryBo, LocalDate startingBlockingDate, LocalDate endingBlockingDate) {
		List<LocalDate> blockedDates = startingBlockingDate.datesUntil(endingBlockingDate).collect(Collectors.toList());
		blockedDates.add(endingBlockingDate);
		blockedDates = blockedDates.stream().filter(potentialBlockedDay -> diaryBo.getDiaryOpeningHours()
						.stream().anyMatch(diaryOpeningHours -> dayIsIncludedInOpeningHours(potentialBlockedDay, diaryOpeningHours)))
				.collect(Collectors.toList());

		List<AppointmentBo> listAppointments = new ArrayList<>();

		if (block.isFullBlock())
			completeDiaryBlock(block, diaryBo, blockedDates, listAppointments);
		else
			blockedDates.forEach(date -> generateBlockInterval(diaryBo, listAppointments, date, block));

		assertNoAppointments(diaryId, listAppointments);
		return listAppointments;
	}

	@Override
	public List<AppointmentBo> unblockAppointments(BlockBo unblock, DiaryBo diaryBo, LocalDate startingBlockingDate, LocalDate endingBlockingDate) {
		List<LocalDate> blockedDates = startingBlockingDate.datesUntil(endingBlockingDate).collect(Collectors.toList());
		blockedDates.add(endingBlockingDate);

		List<AppointmentBo> listAppointments = new ArrayList<>();

		if (unblock.isFullBlock())
			completeDiaryUnblock(unblock, diaryBo, blockedDates, listAppointments);
		else
			blockedDates.forEach(date -> generateUnblockInterval(diaryBo, listAppointments, date, unblock));

		if (listAppointments.isEmpty())
			throw new ConstraintViolationException("No hay ningún bloqueo de agenda en ese día y horario.",Collections.emptySet());

		return listAppointments;
	}

	@Override
	public Integer patientHasCurrentAppointment(Integer institutionId, Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		Integer result = this.getCurrentAppointmentId(patientId, institutionId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<AppointmentSummaryBo> getAppointmentDataByAppointmentIds(List<Integer> appointmentIds) {
		log.debug("Input parameter -> appointmentIds {}", appointmentIds);
		return appointmentRepository.getAppointmentDataByAppointmentIds(appointmentIds);
	}

	@Override
	public Boolean openingHourAllowedProtectedAppointment(Integer openingHoursId, Integer diaryId) {
		log.debug("Input parameter -> openingHoursId {}, diaryId {} ", openingHoursId, diaryId);
		Boolean result = appointmentRepository.openingHourAllowedProtectedAppointment(openingHoursId, diaryId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public void deleteLabelFromAppointment(Integer diaryId, List<Integer> ids) {
		log.debug("Input parameters -> diaryId {}", ids);
		appointmentRepository.deleteLabelFromAppointment(diaryId, ids);
	}

	@Override
	public Boolean hasOldAppointmentWithMinDateLimit(Integer patientId, Integer healthcareProfessionalId, Short minDateLimit) {
		log.debug("Input parameters -> patientId {}, healthcareProfessionalId {}, minDateLimit {}", patientId, healthcareProfessionalId, minDateLimit);
		LocalDate fiveDaysAgo = LocalDate.now().minus(minDateLimit, ChronoUnit.DAYS);
		Boolean result = appointmentRepository.hasOldAppointmentWithMinDateLimitByPatientId(patientId, healthcareProfessionalId, fiveDaysAgo);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public Boolean hasFutureAppointmentByPatientId(Integer patientId, Integer healthcareProfessionalId) {
		log.debug("Input parameters -> patientId {}, healthcareProfessionalId {}", patientId, healthcareProfessionalId);
		Boolean result = appointmentRepository.hasFutureAppointmentsByPatientId(patientId, healthcareProfessionalId);
		log.debug(OUTPUT, result);
		return result;
	}

	public void checkAppointmentEveryWeek(Integer diaryId, LocalTime hour, LocalDate date, Short dayWeekId, Integer appointmentId) {
		log.debug("Input parameters -> diaryId {}, hour {}, date {}, dayWeekId {}, appointmentId {}", diaryId, hour, date, dayWeekId, appointmentId);
		List<Appointment> appointments = appointmentRepository.getLaterAppointmentsByHourAndDate(diaryId, hour, date, dayWeekId, appointmentId);
		Boolean isOverturn = isAppointmentOverturn(appointmentId);
		if (!appointments.isEmpty() && !isOverturn)
			throw new RecurringAppointmentException("Ya hay al menos un turno ocupado en alguna de las fechas");
	}

	@Override
	public void checkUpdateType(AppointmentBo currentAppointment, AppointmentBo newAppointment) {
		log.debug("Input parameters -> currentAppointment {}, newAppointment {}", currentAppointment, newAppointment);
		if (newAppointment.getAppointmentOptionId() == RecurringAppointmentOption.CURRENT_APPOINTMENT.getId())
			updateDate(newAppointment.getId(), newAppointment.getDate(), newAppointment.getHour(), newAppointment.getOpeningHoursId(), currentAppointment.getRecurringTypeBo().getId());

		if (newAppointment.getAppointmentOptionId() == RecurringAppointmentOption.CURRENT_AND_NEXT_APPOINTMENTS.getId())
			updateRecurringAppointmentDate(currentAppointment, newAppointment);

		if (newAppointment.getAppointmentOptionId() == RecurringAppointmentOption.ALL_APPOINTMENTS.getId()) {
			Optional<AppointmentBo> parentAppointment = getAppointment(currentAppointment.getParentAppointmentId());
			if (parentAppointment.isPresent()) {
				currentAppointment.setDate(parentAppointment.get().getDate());
				currentAppointment.setId(parentAppointment.get().getId());
			}
			updateRecurringAppointmentDate(currentAppointment, newAppointment);
		}
		verifyRecurringAppointmentsOverturn(currentAppointment.getDiaryId());
	}

	@Override
	public CustomRecurringAppointmentBo getCustomAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		CustomAppointment entity = appointmentRepository.getCustomAppointment(appointmentId);
		CustomRecurringAppointmentBo bo = new CustomRecurringAppointmentBo();
		if (entity != null) {
			bo.setEndDate(entity.getEndDate());
			bo.setRepeatEvery(entity.getRepeatEvery());
			bo.setWeekDayId(entity.getDayWeekId());
		}
		log.debug(OUTPUT, bo);
		return bo;
	}

	@Override
	public void checkRemainingChildAppointments(Integer appointmentId) {
		appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
			if (appointment.getParentAppointmentId() == null) {
				changeNoAppointmentsLeft(appointment);
			}
		});
	}

	@Override
	public void deleteCustomAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		appointmentRepository.deleteCustomAppointment(appointmentId);
	}

	@Override
	public void deleteParentId(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		appointmentRepository.deleteParentId(appointmentId);
	}

	private void changeLastAppointmentLeft(Integer appointmentId) {
		Integer lastAppointmentChildId = appointmentRepository.getLastAppointmentIdChildByParentId(appointmentId);
		appointmentRepository.updateRecurringType(lastAppointmentChildId, RecurringAppointmentType.NO_REPEAT.getId());
		deleteCustomAppointment(lastAppointmentChildId);
		deleteParentId(lastAppointmentChildId);
	}

	private void changeNoAppointmentsLeft(Appointment appointment) {
		Integer appointmentsQuantity = appointmentRepository.recurringAppointmentQuantityByParentId(appointment.getId());
		if (appointmentsQuantity.equals(NO_CHILD_APPOINTMENTS)) {
			appointmentRepository.updateRecurringType(appointment.getId(), RecurringAppointmentType.NO_REPEAT.getId());
			deleteCustomAppointment(appointment.getId());
		}

		if (appointmentsQuantity.equals(ONE_APPOINTMENT_LEFT) && appointment.getAppointmentStateId().equals(AppointmentState.CANCELLED)) {
			changeLastAppointmentLeft(appointment.getId());
		}
	}

	private short getDayOfWeek(LocalDate localDate) {
		int dayOfWeek = DayOfWeek.from(localDate).getValue();
		if (dayOfWeek == DayOfWeek.SUNDAY.getValue())
			dayOfWeek = 0;
		return (short) dayOfWeek;
	}

	@Override
	public Boolean isAppointmentOverturn(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		return appointmentRepository.isAppointmentOverturn(appointmentId);
	}

	@Override
	public void verifyRecurringAppointmentsOverturn(Integer diaryId) {
		log.debug("Input parameters -> diaryId {}", diaryId);

		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO)) {

			List<Appointment> assignedAppointments = appointmentRepository.getAllAssignedAppointmentsFromDiary(diaryId);

			Map<LocalDateTime, List<Appointment>> hash = assignedAppointments.stream()
					.collect(Collectors.groupingBy(appointment -> LocalDateTime.of(appointment.getDateTypeId(), appointment.getHour())));

			for (Map.Entry<LocalDateTime, List<Appointment>> entry : hash.entrySet()) {
				List<Appointment> appointments = entry.getValue();
				if (appointments.get(0).getIsOverturn()) {
					Appointment firstAppointment = appointments.get(0);
					firstAppointment.setIsOverturn(false);
					firstAppointment.setHour(firstAppointment.getHour());
					firstAppointment.setDateTypeId(firstAppointment.getDateTypeId());
					appointmentRepository.save(firstAppointment);
				}
			}
		}
	}

	private void updateRecurringAppointmentDate(AppointmentBo currentAppointment, AppointmentBo newAppointment) {
		Integer parentAppointmentId = currentAppointment.getParentAppointmentId();
		if (parentAppointmentId == null)
			parentAppointmentId = currentAppointment.getId();

		List<Appointment> laterAppointments = appointmentRepository.getLaterAppointmentsByDate(newAppointment.getDiaryId(), currentAppointment.getDate(), getDayOfWeek(currentAppointment.getDate()), parentAppointmentId);
		for(Appointment a : laterAppointments) {
			updateDate(
					a.getId(),
					a.getDateTypeId(),
					newAppointment.getHour(),
					newAppointment.getOpeningHoursId(),
					a.getRecurringAppointmentTypeId()
			);
		}
		updateDate(currentAppointment.getId(), currentAppointment.getDate(), newAppointment.getHour(), newAppointment.getOpeningHoursId(), currentAppointment.getRecurringTypeBo().getId());
	}

	private boolean dayIsIncludedInOpeningHours(LocalDate date, DiaryOpeningHoursBo diaryOpeningHours) {
		final int SUNDAY_DB_VALUE = 0;
		if (date.getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue())
			return diaryOpeningHours.getOpeningHours().getDayWeekId() == SUNDAY_DB_VALUE;
		return diaryOpeningHours.getOpeningHours().getDayWeekId() == date.getDayOfWeek().getValue();
	}

	private void completeDiaryBlock(BlockBo block, DiaryBo diaryBo, List<LocalDate> blockedDates, List<AppointmentBo> listAppointments) {
		blockedDates.forEach(blockedDate -> {
			List<DiaryOpeningHoursBo> relatedOpeningHours = diaryBo.getDiaryOpeningHours().stream()
					.filter(diaryOpeningHours -> dayIsIncludedInOpeningHours(blockedDate, diaryOpeningHours)).collect(Collectors.toList());
			relatedOpeningHours.forEach(openingHours -> {
				BlockBo currentBlock = new BlockBo(blockedDate, blockedDate,
						openingHours.getOpeningHours().getFrom(), openingHours.getOpeningHours().getTo(),
						block.getAppointmentBlockMotiveId());
				generateBlockInterval(diaryBo, listAppointments, blockedDate, currentBlock);
			});
		});
	}

	private void generateBlockInterval(DiaryBo diaryBo, List<AppointmentBo> listAppointments, LocalDate blockedDate, BlockBo block) {
		listAppointments.addAll(getSlots(block, diaryBo).stream().map(slot -> mapTo(blockedDate, diaryBo, slot, block)).collect(Collectors.toList()));
	}

	private AppointmentBo mapTo(LocalDate date, DiaryBo diaryBo, LocalTime hour, BlockBo block) {
		var openingHours = diaryBo.getDiaryOpeningHours();
		AppointmentBo appointmentBo = new AppointmentBo();
		appointmentBo.setDiaryId(diaryBo.getId());
		appointmentBo.setDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth()));
		appointmentBo.setHour(hour);
		appointmentBo.setAppointmentStateId(AppointmentState.BLOCKED);
		appointmentBo.setOverturn(false);
		appointmentBo.setOpeningHoursId(getOpeningHourId(openingHours, date, hour).getOpeningHours().getId());
		appointmentBo.setAppointmentBlockMotiveId(block.getAppointmentBlockMotiveId());
		appointmentBo.setModalityId(EAppointmentModality.NO_MODALITY.getId());
		return appointmentBo;
	}

	private DiaryOpeningHoursBo getOpeningHourId(List<DiaryOpeningHoursBo> openingHours, LocalDate date, LocalTime hour) {
		var dayOfWeek = date.getDayOfWeek().getValue() == 7 ? EDayOfWeek.SUNDAY.getId() : (short)date.getDayOfWeek().getValue();
		return openingHours.stream()
				.filter(oh -> oh.getOpeningHours().getDayWeekId().equals(dayOfWeek))
				.filter(oh -> (oh.getOpeningHours().getFrom().isBefore(hour) || oh.getOpeningHours().getFrom().equals(hour)) &&
						(oh.getOpeningHours().getTo().isAfter(hour)))
				.findFirst().orElseThrow((() -> new ConstraintViolationException("Los horarios de inicio y fin deben pertenecer al mismo período de la agenda.",
						Collections.emptySet())));
	}


	private List<LocalTime> getSlots(BlockBo block, DiaryBo diaryBo) {
		var appointmentDuration = diaryBo.getAppointmentDuration();
		var localTimeInit = block.getInit();
		var localTimeEnd = block.getEnd();

		assertTimeLimits(localTimeInit, localTimeEnd, appointmentDuration);

		var slots = Stream.iterate(localTimeInit, d -> d.plusMinutes(appointmentDuration))
				.limit(ChronoUnit.MINUTES.between(localTimeInit, localTimeEnd) / appointmentDuration)
				.collect(Collectors.toList());

		if (localTimeEnd.getHour() == 23 && localTimeEnd.getMinute() == 59) {
			var lastTime = slots.get(slots.size()-1).plusMinutes(appointmentDuration);
			slots.add(lastTime);
		}
		log.debug(OUTPUT, slots);
		return slots;
	}

	private void assertTimeLimits(LocalTime localTimeInit, LocalTime localTimeEnd, Short appointmentDuration) {
		if (localTimeEnd.isBefore(localTimeInit) || localTimeEnd.equals(localTimeInit))
			throw new ConstraintViolationException("La segunda hora seleccionada debe ser posterior a la primera.", Collections.emptySet());

		if(localTimeInit.getMinute() % appointmentDuration != 0)
			throw new ConstraintViolationException("La hora de inicio no es múltiplo de la duración del turno.", Collections.emptySet());

		if(localTimeEnd.getMinute() % appointmentDuration != 0 && (localTimeEnd.getMinute() != 59 && localTimeEnd.getHour() != 23))
			throw new ConstraintViolationException("La hora de fin no es múltiplo de la duración del turno.", Collections.emptySet());
	}

	private void assertNoAppointments(Integer diaryId, List<AppointmentBo> listAppointments) {
		if(listAppointments.stream().anyMatch(appointmentBo ->
				existAppointment(diaryId,
						appointmentBo.getOpeningHoursId(),
						appointmentBo.getDate(),
						appointmentBo.getHour()
				)
		))
			throw new ConstraintViolationException("Algún horario de la franja horaria seleccionada tiene un turno o ya está bloqueado.", Collections.emptySet());
	}

	private void completeDiaryUnblock(BlockBo unblock, DiaryBo diaryBo, List<LocalDate> blockedDates, List<AppointmentBo> listAppointments) {
		blockedDates.forEach(blockedDate -> {
			List<DiaryOpeningHoursBo> relatedOpeningHours = diaryBo.getDiaryOpeningHours().stream()
					.filter(diaryOpeningHours -> dayIsIncludedInOpeningHours(blockedDate, diaryOpeningHours)).collect(Collectors.toList());
			relatedOpeningHours.forEach(openingHours -> {
				BlockBo currentUnblock = new BlockBo(blockedDate, blockedDate,
						openingHours.getOpeningHours().getFrom(), openingHours.getOpeningHours().getTo(),
						unblock.getAppointmentBlockMotiveId());
				generateUnblockInterval(diaryBo, listAppointments, blockedDate, currentUnblock);
			});
		});
	}

	private void generateUnblockInterval(DiaryBo diaryBo, List<AppointmentBo> listAppointments, LocalDate blockedDate, BlockBo unblock) {
		listAppointments.addAll(getSlots(unblock, diaryBo).stream().map(slot -> findBlockedAppointmentBy(diaryBo.getId(),
						LocalDate.of(blockedDate.getYear(), blockedDate.getMonth(), blockedDate.getDayOfMonth()),
						slot))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList()));
	}

	private Collection<AppointmentBo> setIsAppointmentProtected(Collection<AppointmentBo> appointments, List<Integer> diaryIds) {
		List<Integer> protectedAppointments = sharedReferenceCounterReference.getProtectedAppointmentsIds(diaryIds);
		appointments.forEach(a -> a.setProtected(protectedAppointments.contains(a.getId())));
		return appointments;
	}
	
	private void verifyProtectedCondition(Integer appointmentId) {
		boolean isProtected = sharedReferenceCounterReference.isProtectedAppointment(appointmentId);
		if (isProtected)
			sharedReferenceCounterReference.updateProtectedAppointment(appointmentId);
	}
}
