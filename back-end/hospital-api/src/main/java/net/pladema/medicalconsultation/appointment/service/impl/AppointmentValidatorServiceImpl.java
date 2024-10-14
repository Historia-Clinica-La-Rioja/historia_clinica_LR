package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentEnumException;
import net.pladema.medicalconsultation.appointment.service.exceptions.AppointmentException;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.UpdateAppointmentDateException;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.UpdateAppointmentDateExceptionEnum;
import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.ABSENT;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.ASSIGNED;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.BOOKED;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.CANCELLED;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.CONFIRMED;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.OUT_OF_DIARY;
import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.SERVED;
import javax.validation.ConstraintViolationException;
import net.pladema.medicalconsultation.appointment.service.impl.exceptions.RecurringAppointmentException;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.medicalconsultation.appointment.infrastructure.output.repository.appointment.RecurringAppointmentOption;
import net.pladema.medicalconsultation.appointment.infrastructure.output.repository.appointment.RecurringAppointmentType;
import net.pladema.medicalconsultation.appointment.service.domain.CreateCustomAppointmentBo;

@Service
public class AppointmentValidatorServiceImpl implements AppointmentValidatorService {

    public static final String OUTPUT = "Output -> {}";
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentValidatorServiceImpl.class);

	private static final Short WEEK_DAYS = 7;

    private final Map<Short, Collection<Short>> validStates;

    private final Collection<Short> statesWithReason;

	private final DiaryService diaryService;
	private final DiaryOpeningHoursService diaryOpeningHoursService;

    private final HealthcareProfessionalService healthcareProfessionalService;
    private final AppointmentService appointmentService;

	private final DiaryAssociatedProfessionalService diaryAssociatedProfessionalService;
    private final Function<Integer, Boolean> hasAdministrativeRole;
    private final Function<Integer, Boolean> hasProfessionalRole;
	private final InstitutionExternalService institutionExternalService;
	private final DateTimeProvider dateTimeProvider;

	private final LocalDateMapper localDateMapper;
	private final FeatureFlagsService featureFlagsService;
	private final static String APPOINTMENT_ERROR = "Este turno ya no existe";
	private final static String OCUPPIED_APPOINTMENT = "Ya hay un turno ocupado en ese horario";

    public AppointmentValidatorServiceImpl(
			DiaryService diaryService, DiaryOpeningHoursService diaryOpeningHoursService, HealthcareProfessionalService healthcareProfessionalService,
			AppointmentService appointmentService, DiaryAssociatedProfessionalService diaryAssociatedProfessionalService,
			LoggedUserExternalService loggedUserExternalService, InstitutionExternalService institutionExternalService, DateTimeProvider dateTimeProvider, LocalDateMapper localDateMapper, FeatureFlagsService featureFlagsService) {
        this.diaryService = diaryService;
		this.diaryOpeningHoursService = diaryOpeningHoursService;
		this.healthcareProfessionalService = healthcareProfessionalService;
        this.appointmentService = appointmentService;
		this.diaryAssociatedProfessionalService = diaryAssociatedProfessionalService;
        this.hasAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(
                ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO
        );
        this.hasProfessionalRole = loggedUserExternalService.hasAnyRoleInstitution(
                ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO
        );
		this.institutionExternalService = institutionExternalService;
		this.dateTimeProvider = dateTimeProvider;
		this.localDateMapper = localDateMapper;
		this.featureFlagsService = featureFlagsService;

		this.validStates = buildValidStates();
        this.statesWithReason = Arrays.asList(CANCELLED, ABSENT);
    }

    @Override
    public boolean validateStateUpdate(Integer institutionId, Integer appointmentId, short appointmentStateId, String reason) {
        LOG.debug("Input parameters -> appointmentId {}, appointmentStateId {}, reason {}", appointmentId,
                appointmentStateId, reason);
        Optional<AppointmentBo> apmtOpt = appointmentService.getAppointmentSummary(appointmentId);

		if (apmtOpt.isPresent() && !validStateTransition(appointmentStateId, apmtOpt.get())) {
			throw new AppointmentException(AppointmentEnumException.TRANSITION_STATE_INVALID, "Nuevo estado de turno inválido");
		}
        if (!validReason(appointmentStateId, reason)) {
			throw new AppointmentException(AppointmentEnumException.ABSENT_REASON_REQUIRED, "Debe especificarse el motivo");
		}
        validateRole(institutionId, apmtOpt);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return Boolean.TRUE;
    }

    private void validateRole(Integer institutionId, Optional<AppointmentBo> apmtOpt) {

        if (apmtOpt.isPresent() && Boolean.FALSE.equals(hasAdministrativeRole.apply(institutionId))) {
            DiaryBo diary = diaryService.getDiaryById(apmtOpt.get().getDiaryId());

            Integer professionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());
			List<Integer> associatedHealthcareProfessionals = diaryAssociatedProfessionalService.getAllAssociatedWithProfessionalsByHealthcareProfessionalId(institutionId, professionalId);
            if (Boolean.TRUE.equals(hasProfessionalRole.apply(institutionId)) && !diary.getHealthcareProfessionalId().equals(professionalId) && !associatedHealthcareProfessionals.contains(diary.getHealthcareProfessionalId())) {
 				throw new AppointmentException(AppointmentEnumException.DIARY_PROFESSIONAL_INVALID, "La agenda no pertenece al profesional indicado");
			}
        }
    }

    private boolean validReason(short appointmentStateId, String reason) {
        return !statesWithReason.contains(appointmentStateId) || reason != null;
    }

    private boolean validStateTransition(short appointmentStateId, AppointmentBo apmt) {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_ATENDER_TURNO_MANUAL) && apmt.getAppointmentStateId() == CONFIRMED && appointmentStateId == SERVED) {
			return true;
		} else {
			return validStates.get(apmt.getAppointmentStateId()).contains(appointmentStateId);
		}
    }

	public boolean validateDateUpdate(Integer institutionId, Integer appointmentId, LocalDate date, LocalTime time){
		LOG.debug("Input parameters -> appointmentId {}, date {}, time {}", appointmentId, date, time);
		Optional<DiaryBo> diary = diaryService.getDiaryByAppointment(appointmentId);
		Optional<AppointmentBo> appointmentBo = appointmentService.getAppointmentSummary(appointmentId);
		ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
		LocalDate todayDate = dateTimeProvider.nowDate();
		LocalTime todayTime = dateTimeProvider.nowDateTimeWithZone(institutionZoneId).toLocalTime();
		
		if ((date.isBefore(todayDate)) || ((date.equals(todayDate)) && (time.isBefore(todayTime)))){
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_BEFORE_NOW, "El horario del turno es anterior a la hora actual.");
		}

		if ((diary.get().getStartDate().isAfter(date)) || (diary.get().getEndDate().isBefore(date))) {
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_OUT_OF_DIARY_RANGE, "La fecha del turno se encuentra fuera del rango de la agenda.");
		}
		if (appointmentBo.get().getAppointmentStateId() != 1){
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_STATE_INVALID, "El estado del turno es invalido.");
		}

		if (appointmentService.findBlockedAppointmentBy(appointmentBo.get().getDiaryId(),date,time).isPresent()) {
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_BLOCKED, "En ese horario la agenda se encuentra bloqueada.");
		}

		Collection<DiaryOpeningHoursBo> diaryOpeningHours = diaryOpeningHoursService.getDiaryOpeningHours(diary.get().getId());
		for (DiaryOpeningHoursBo doh: diaryOpeningHours) {
			OpeningHoursBo oh = doh.getOpeningHours();
			if (Objects.equals(oh.getDayWeekId(), DateUtils.getWeekDay(date))) {
				if(((oh.getFrom().isBefore(time)) && (oh.getTo().isAfter(time))) || (oh.getFrom().equals(time))) {
					return true;
				}
			}
		}
		throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_OUT_OF_OPENING_HOURS, String.format("El horario del turno se encuentra fuera de la agenda."));
	}

	@Override
	public LocalDate checkAppointmentEveryWeek(String hour, String date, Integer diaryId, Integer appointmentId, Short recurringAppointmentOption, Integer openingHoursId) {
		LOG.debug("Input parameters -> hour {}, date {}, diaryId {}, appointmentId {}, recurringAppointmentOption {}, openingHoursId {}", hour, date, diaryId, appointmentId, recurringAppointmentOption, openingHoursId);

		AppointmentBo appointmentBo = getAppointment(appointmentId, RecurringAppointmentType.CUSTOM);

		if (recurringAppointmentOption != null && recurringAppointmentOption.equals(RecurringAppointmentOption.CURRENT_APPOINTMENT.getId())) {
			if (appointmentService.existAppointment(diaryId, localDateMapper.fromStringToLocalDate(date), localDateMapper.fromStringToLocalTime(hour), appointmentId))
				throw new RecurringAppointmentException(OCUPPIED_APPOINTMENT);
		} else {
			if (appointmentBo.getParentAppointmentId() != null)
				date = setStartDate(appointmentBo, recurringAppointmentOption, date);

			LocalDate localDate = localDateMapper.fromStringToLocalDate(date);
			LocalTime localTime = localDateMapper.fromStringToLocalTime(hour);
			appointmentService.checkAppointmentEveryWeek(
					diaryId,
					localTime,
					localDate,
					getDayOfWeek(localDate),
					appointmentId
			);
		}
		return diaryService.getDiary(diaryId).get().getEndDate();
	}

	@Override
	public void checkCustomAppointment(CreateCustomAppointmentBo bo) {
		LOG.debug("Input parameters -> bo {}", bo);
		setEndDateDto(bo);
		AppointmentBo appointmentBo = bo.getCreateAppointmentBo();
		AppointmentBo appointment = getAppointment(appointmentBo.getId(), RecurringAppointmentType.EVERY_WEEK);

		if (appointmentBo.getAppointmentOptionId() != null && appointmentBo.getAppointmentOptionId().equals(RecurringAppointmentOption.CURRENT_APPOINTMENT.getId())) {
			if (appointmentService.existAppointment(appointmentBo.getDiaryId(), appointmentBo.getDate(), appointmentBo.getHour(), appointmentBo.getId()))
				throw new RecurringAppointmentException(OCUPPIED_APPOINTMENT);
		} else {
			if (appointment.getParentAppointmentId() != null) {
				appointmentBo.setDate(
						localDateMapper.fromStringToLocalDate(
								setStartDate(appointment,
										appointmentBo.getAppointmentOptionId(),
										localDateMapper.fromLocalDateToString(appointmentBo.getDate())
								)
						)
				);
			}

			if (!bo.isOverturn())
				checkWeekRepetition(bo);
		}
	}

	private short getDayOfWeek(LocalDate localDate) {
		int dayOfWeek = DayOfWeek.from(localDate).getValue();
		if (dayOfWeek == DayOfWeek.SUNDAY.getValue())
			dayOfWeek = 0;
		return (short) dayOfWeek;
	}

	private AppointmentBo getAppointment(Integer appointmentId, RecurringAppointmentType recurringAppointmentType) {
		Optional<AppointmentBo> appointment = appointmentService.getAppointment(appointmentId);

		if (appointment.isEmpty())
			throw new ConstraintViolationException(APPOINTMENT_ERROR, Collections.emptySet());

		assertState(appointment.get(), recurringAppointmentType.getId());
		return appointment.get();
	}

	private void assertState(AppointmentBo appointmentBo, Short recurringAppointmentTypeId) {
		LOG.debug("Input parameters -> appointmentBo {}", appointmentBo);
		if (appointmentBo.getRecurringTypeBo().getId() == recurringAppointmentTypeId)
			throw new ConstraintViolationException("El turno se quiere cambiar a una recurrencia no permitida", Collections.emptySet());
	}

	private void checkWeekRepetition(CreateCustomAppointmentBo bo) {
		for (LocalDate initDate = bo.getDate().plusDays(WEEK_DAYS * bo.getRepeatEvery()); !initDate.isAfter(bo.getEndDate()); initDate = initDate.plusDays(WEEK_DAYS * bo.getRepeatEvery())) {
			if (appointmentService.existAppointment(bo.getDiaryId(), bo.getOpeningHours(), initDate, bo.getHour()))
				throw new RecurringAppointmentException("Ya hay al menos un turno ocupado en alguna de las fechas");
		}
	}

	private String setStartDate(AppointmentBo appointment, Short recurringAppointmentOption, String date) {
		Optional<AppointmentBo> parentAppointment = appointmentService.getAppointment(appointment.getParentAppointmentId());
		if (parentAppointment.isPresent()) {
			if (recurringAppointmentOption.equals(RecurringAppointmentOption.ALL_APPOINTMENTS.getId()))
				date = localDateMapper.fromLocalDateToString(parentAppointment.get().getDate());
			if (recurringAppointmentOption.equals(RecurringAppointmentOption.CURRENT_AND_NEXT_APPOINTMENTS.getId()))
				date = localDateMapper.fromLocalDateToString(appointment.getDate());
		}
		return date;
	}

	private void setEndDateDto(CreateCustomAppointmentBo bo) {
		LocalDate selectedDate = bo.getCustomRecurringAppointmentBo().getEndDate();
		DiaryBo diaryBo = diaryService.getDiaryById(bo.getCreateAppointmentBo().getDiaryId());
		if (selectedDate == null || selectedDate.isAfter(diaryBo.getEndDate()))
			bo.getCustomRecurringAppointmentBo().setEndDate(diaryBo.getEndDate());
	}

	private static Map<Short, Collection<Short>> buildValidStates() {
        return Map.of(
                BOOKED, Arrays.asList(ASSIGNED, CONFIRMED, CANCELLED),
                ASSIGNED, Arrays.asList(CONFIRMED, CANCELLED, ABSENT),
                CONFIRMED, Arrays.asList(ABSENT, CANCELLED, SERVED, ASSIGNED),
                ABSENT, Arrays.asList(CONFIRMED,ABSENT, ASSIGNED),
                SERVED, Collections.emptyList(),
                CANCELLED, Collections.emptyList(),
				OUT_OF_DIARY, Arrays.asList(CANCELLED, ASSIGNED, BOOKED)
        );
    }

}
