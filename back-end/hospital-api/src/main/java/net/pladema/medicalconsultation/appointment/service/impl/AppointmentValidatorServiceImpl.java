package net.pladema.medicalconsultation.appointment.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.ValidationException;

import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.staff.service.HealthcareProfessionalService;

import static net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState.*;

@Service
public class AppointmentValidatorServiceImpl implements AppointmentValidatorService {

    public static final String OUTPUT = "Output -> {}";
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentValidatorServiceImpl.class);

    private final Map<Short, Collection<Short>> validStates;

    private final Collection<Short> statesWithReason;

    private final DiaryService diaryService;
    private final HealthcareProfessionalService healthcareProfessionalService;
    private final AppointmentService appointmentService;

	private final DiaryAssociatedProfessionalService diaryAssociatedProfessionalService;
    private final Function<Integer, Boolean> hasAdministrativeRole;
    private final Function<Integer, Boolean> hasProfessionalRole;

    public AppointmentValidatorServiceImpl(
            DiaryService diaryService,
            HealthcareProfessionalService healthcareProfessionalService,
            AppointmentService appointmentService,
            LoggedUserExternalService loggedUserExternalService,
			DiaryAssociatedProfessionalService diaryAssociatedProfessionalService
    ) {
        this.diaryService = diaryService;
        this.healthcareProfessionalService = healthcareProfessionalService;
        this.appointmentService = appointmentService;
		this.diaryAssociatedProfessionalService = diaryAssociatedProfessionalService;
        this.hasAdministrativeRole = loggedUserExternalService.hasAnyRoleInstitution(
                ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO
        );
        this.hasProfessionalRole = loggedUserExternalService.hasAnyRoleInstitution(
                ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO
        );

        this.validStates = buildValidStates();
        this.statesWithReason = Arrays.asList(CANCELLED, ABSENT);
    }

    @Override
    public boolean validateStateUpdate(Integer institutionId, Integer appointmentId, short appointmentStateId, String reason) {
        LOG.debug("Input parameters -> appointmentId {}, appointmentStateId {}, reason {}", appointmentId,
                appointmentStateId, reason);
        Optional<AppointmentBo> apmtOpt = appointmentService.getAppointment(appointmentId);

        if (apmtOpt.isPresent() && !validStateTransition(appointmentStateId, apmtOpt.get())) {
            throw new ValidationException("appointment.state.transition.invalid");
        }
        if (!validReason(appointmentStateId, reason)) {
            throw new ValidationException("appointment.state.reason.invalid");
        }
        validateRole(institutionId, apmtOpt);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return Boolean.TRUE;
    }

    private void validateRole(Integer institutionId, Optional<AppointmentBo> apmtOpt) {

        if (apmtOpt.isPresent() && Boolean.FALSE.equals(hasAdministrativeRole.apply(institutionId))) {
            DiaryBo diary = diaryService.getDiaryById(apmtOpt.get().getDiaryId());

            Integer professionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());
			List<Integer> associatedHealthcareProfessionals = diaryAssociatedProfessionalService.getAllAssociatedWithProfessionalsByHealthcareProfessionalId(professionalId);;
            if (Boolean.TRUE.equals(hasProfessionalRole.apply(institutionId)) && !diary.getHealthcareProfessionalId().equals(professionalId) && !associatedHealthcareProfessionals.contains(diary.getHealthcareProfessionalId())) {
                throw new ValidationException("appointment.new.professional.id.invalid}");
            }
        }
    }

    private boolean validReason(short appointmentStateId, String reason) {
        return !statesWithReason.contains(appointmentStateId) || reason != null;
    }

    private boolean validStateTransition(short appointmentStateId, AppointmentBo apmt) {
        return validStates.get(apmt.getAppointmentStateId()).contains(appointmentStateId);
    }

    private static Map<Short, Collection<Short>> buildValidStates() {
        return Map.of(
                BOOKED, Arrays.asList(ASSIGNED, CONFIRMED, CANCELLED),
                ASSIGNED, Arrays.asList(CONFIRMED, CANCELLED),
                CONFIRMED, Arrays.asList(ABSENT, CANCELLED, SERVED),
                ABSENT, Arrays.asList(CONFIRMED,ABSENT),
                SERVED, Collections.emptyList(),
                CANCELLED, Collections.singletonList(CANCELLED),
				OUT_OF_DIARY, Arrays.asList(CANCELLED, ASSIGNED, BOOKED)
        );
    }

}
