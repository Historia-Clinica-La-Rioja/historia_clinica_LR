package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.dates.utils.DateUtils;
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

@Service
public class AppointmentValidatorServiceImpl implements AppointmentValidatorService {

    public static final String OUTPUT = "Output -> {}";
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentValidatorServiceImpl.class);

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

    public AppointmentValidatorServiceImpl(
			DiaryService diaryService, DiaryOpeningHoursService diaryOpeningHoursService, HealthcareProfessionalService healthcareProfessionalService,
			AppointmentService appointmentService, DiaryAssociatedProfessionalService diaryAssociatedProfessionalService,
			LoggedUserExternalService loggedUserExternalService, InstitutionExternalService institutionExternalService, DateTimeProvider dateTimeProvider) {
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
        return validStates.get(apmt.getAppointmentStateId()).contains(appointmentStateId);
    }

	public boolean validateDateUpdate(Integer institutionId, Integer appointmentId, LocalDate date, LocalTime time){
		LOG.debug("Input parameters -> appointmentId {}, date {}, time {}", appointmentId, date, time);
		Optional<DiaryBo> diary = diaryService.getDiaryByAppointment(appointmentId);
		Optional<AppointmentBo> appointmentBo = appointmentService.getAppointmentSummary(appointmentId);
		ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
		LocalDate todayDate = dateTimeProvider.nowDate();
		LocalTime todayTime = dateTimeProvider.nowDateTimeWithZone(institutionZoneId).toLocalTime();
		
		if ((date.isBefore(todayDate)) || ((date.equals(todayDate)) && (time.isBefore(todayTime)))){
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_BEFORE_NOW, String.format("El horario del turno es anterior a la hora actual."));
		}

		if ((diary.get().getStartDate().isAfter(date)) || (diary.get().getEndDate().isBefore(date))) {
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_OUT_OF_DIARY_RANGE, String.format("La fecha del turno se encuentra fuera del rango de la agenda."));
		}
		if (appointmentBo.get().getAppointmentStateId() != 1){
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_STATE_INVALID, String.format("El estado del turno es invalido."));
		}

		if(appointmentService.existAppointment(appointmentBo.get().getDiaryId(),date,time)) {
			throw new UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum.APPOINTMENT_DATE_ALREADY_ASSIGNED, String.format("En ese horario ya existe un turno asignado o la agenda se encuentra bloqueada."));
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
