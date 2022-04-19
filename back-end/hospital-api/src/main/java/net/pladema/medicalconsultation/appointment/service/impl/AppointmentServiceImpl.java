package net.pladema.medicalconsultation.appointment.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.establishment.controller.service.InstitutionExternalService;

import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;

import net.pladema.patient.service.domain.PatientCoverageInsuranceDetailsBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.HistoricAppointmentStateRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.repository.entity.HistoricAppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentRepository appointmentRepository;

	private final HistoricAppointmentStateRepository historicAppointmentStateRepository;

	private final SharedStaffPort sharedStaffPort;

	private final FeatureFlagsService featureFlagsService;

	private final DateTimeProvider dateTimeProvider;

	private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;

	private final InstitutionExternalService institutionExternalService;

	private final MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
								  HistoricAppointmentStateRepository historicAppointmentStateRepository,
								  SharedStaffPort sharedStaffPort,
								  DateTimeProvider dateTimeProvider,
								  PatientExternalMedicalCoverageService patientExternalMedicalCoverageService,
								  InstitutionExternalService institutionExternalService,
								  MedicalCoveragePlanRepository medicalCoveragePlanRepository,
								  FeatureFlagsService featureFlagsService) {
		this.appointmentRepository = appointmentRepository;
		this.historicAppointmentStateRepository = historicAppointmentStateRepository;
		this.sharedStaffPort = sharedStaffPort;
		this.featureFlagsService = featureFlagsService;
		this.dateTimeProvider = dateTimeProvider;
		this.patientExternalMedicalCoverageService = patientExternalMedicalCoverageService;
		this.institutionExternalService = institutionExternalService;
		this.medicalCoveragePlanRepository = medicalCoveragePlanRepository;
	}

	@Override
	public Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds) {
		log.debug("Input parameters -> diaryIds {}", diaryIds);
		Collection<AppointmentBo> result = new ArrayList<>();
		if (!diaryIds.isEmpty())
			result = appointmentRepository.getAppointmentsByDiaries(diaryIds).stream()
					.map(AppointmentBo::fromAppointmentDiaryVo)
					.distinct()
					.collect(Collectors.toList());
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
		log.debug("Input parameters -> diaryId {}, openingHoursId {}, date {}, hour {}", diaryId, openingHoursId, date, hour);
		boolean result = appointmentRepository.existAppointment(diaryId, openingHoursId, date, hour);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason) {
		log.debug("Input parameters -> appointmentId {}, appointmentStateId {}, userId {}, reason {}", appointmentId, appointmentStateId, userId, reason);
		appointmentRepository.updateState(appointmentId, appointmentStateId, userId);
		historicAppointmentStateRepository.save(new HistoricAppointmentState(appointmentId, appointmentStateId, reason));
		log.debug(OUTPUT, Boolean.TRUE);
		return Boolean.TRUE;
	}

	@Override
	public Optional<AppointmentBo> getAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		Optional<AppointmentBo>	result = appointmentRepository.getAppointment(appointmentId).stream().findFirst().map(AppointmentBo::fromAppointmentVo);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean hasConfirmedAppointment(Integer patientId, Integer healthProfessionalId, LocalDate date) {
		log.debug("Input parameters -> patientId {}, healthProfessionalId {}, date {} ", patientId, healthProfessionalId, date);
		boolean result = !(appointmentRepository.getAppointmentsId(patientId, healthProfessionalId, date).isEmpty());
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
	public Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId,
									  LocalDate currentDate) {
		List<Integer> medicalCoverages = appointmentRepository.getMedicalCoverage(patientId, currentDate,
				AppointmentState.CONFIRMED, healthcareProfessionalId);
		Integer patientMedicalCoverageId = medicalCoverages.stream().findAny().orElse(null);
		log.debug(OUTPUT, patientMedicalCoverageId);
		return patientMedicalCoverageId;
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

	private Integer getCurrentAppointmentId(Integer patientId, Integer institutionId) {
		log.debug("Input parameters -> patientId {}, institutionId {}", patientId, institutionId);

		ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
		var currentDateTime = dateTimeProvider.nowDateTimeWithZone(institutionZoneId);
		var userId = UserInfo.getCurrentAuditor();
		var getConfirmedAppointmentsByPatient = appointmentRepository.getConfirmedAppointmentsByPatient(patientId,
				institutionId, userId, currentDateTime.toLocalDate(), currentDateTime.toLocalTime());
		if(getConfirmedAppointmentsByPatient.isEmpty())
			return null;

		var result = getConfirmedAppointmentsByPatient.get(0);
		log.debug(OUTPUT, result);
		return result;
	}

	private PatientMedicalCoverageBo makePatientMedicalCoverageBo(PatientMedicalCoverageDto dto){
		log.trace("Input parameters -> patientMedicalCoverageDto {}", dto);
		if(dto == null)
			return null;

		var coverageDto = dto.getMedicalCoverage();
		var medicalCoverageBo = new PatientCoverageInsuranceDetailsBo(coverageDto.getId(),
				coverageDto.getName(), coverageDto.getCuit(), coverageDto.obtainCoverageType());
		var vigencyDate = dto.getVigencyDate();

		var result = new PatientMedicalCoverageBo(dto.getId(),
				vigencyDate != null ? LocalDate.parse(vigencyDate) : null,
				dto.getActive(),
				dto.getAffiliateNumber(),
				medicalCoverageBo,
				dto.getCondition(),
				LocalDate.parse(dto.getStartDate()),
				LocalDate.parse(dto.getEndDate()),
				dto.getPlanId(),
				dto.getPlanName());

		if (dto.getPlanId() != null) {
			var planIdOpt = medicalCoveragePlanRepository.findById(dto.getPlanId());
			planIdOpt.ifPresent(medicalCoveragePlan -> result.setPlanName(medicalCoveragePlan.getPlan()));
		}

		log.trace(OUTPUT, result);
		return result;
	}

	private Collection<AppointmentAssignedBo> getAssignedAppointmentsByPatient(Integer patientId) {
		log.debug("Input parameters -> patientId {}", patientId);
		Collection<AppointmentAssignedBo> result;
		result = appointmentRepository.getAssignedAppointmentsByPatient(patientId).stream().map(AppointmentAssignedBo::new)
				.collect(Collectors.toList());
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return result;
	}

	@Override
	public Collection<AppointmentAssignedBo> getCompleteAssignedAppointmentInfo(Integer patientId){
		log.debug("Input parameters -> patientId {}", patientId);
		Collection<AppointmentAssignedBo> resultService = this.getAssignedAppointmentsByPatient(patientId);
		Collection<AppointmentAssignedBo> result = resultService.stream()
				.parallel()
				.map(appointmentAssigned ->{
					var basicHealtcareDtoMap = sharedStaffPort.getProfessionalCompleteInfo(appointmentAssigned.getProfessionalId());
					appointmentAssigned.setSpecialties(basicHealtcareDtoMap.getClinicalSpecialties().stream()
							.map(specialty -> {return specialty.getName();})
							.collect(Collectors.toList()));
					if(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && basicHealtcareDtoMap.getNameSelfDetermination() != null && !basicHealtcareDtoMap.getNameSelfDetermination().isEmpty())
						appointmentAssigned.setProfessionalName(basicHealtcareDtoMap.getNameSelfDetermination() + ' ' + basicHealtcareDtoMap.getLastName() + ' ' + basicHealtcareDtoMap.getId());
					else
						appointmentAssigned.setProfessionalName(basicHealtcareDtoMap.getFirstName() + ' ' + basicHealtcareDtoMap.getLastName());
					return appointmentAssigned;
				}).collect(Collectors.toList());
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
}
