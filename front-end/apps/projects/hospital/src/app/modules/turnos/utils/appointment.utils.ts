import { AppointmentListDto } from "@api-rest/api-model";
import { DateFormat, buildFullDate, momentParseDate, momentParseTime } from "@core/utils/moment.utils";
import { APPOINTMENT_STATES_ID, BLUE_TEXT, COLORES, GREY_TEXT, PURPLE_TEXT, TEMPORARY_PATIENT, WHITE_TEXT } from "@turnos/constants/appointment";
import { AppointmentBlockMotivesFacadeService } from "@turnos/services/appointment-block-motives-facade.service";
import { CalendarEvent } from "angular-calendar";
import { Moment } from "moment";

export function toCalendarEvent(from: string, to: string, date: Moment, appointment: AppointmentListDto, viewName?: string, appointmentBlockMotivesFacadeService?: AppointmentBlockMotivesFacadeService): CalendarEvent {
    const fullName = [appointment.patient?.person.lastName, appointment.patient?.person.firstName].
        filter(val => val).join(', ');

    const fullNameWithNameSelfDetermination = appointment.patient?.person.nameSelfDetermination ?
        [appointment.patient.person.lastName, appointment.patient.person.nameSelfDetermination].filter(val => val).join(', ') : null;

    const title = getTitle();

    return {
        start: buildFullDate(from, date).toDate(),
        end: buildFullDate(to, date).toDate(),
        title,
        color: {
            primary: getColor(appointment),
            secondary: showProtectedAppointment(appointment) ? 'transparent' : getColor(appointment)
        },
        cssClass: getSpanColor(appointment),
        meta: {
            patient: {
                id: appointment.patient?.id,
                fullName,
                identificationNumber: appointment.patient?.person.identificationNumber,
                typeId: appointment.patient?.typeId,
                fullNameWithNameSelfDetermination: fullNameWithNameSelfDetermination,
                identificationTypeId: appointment.patient?.person.identificationTypeId,
                genderId: appointment.patient?.person.genderId,
            },
            overturn: appointment.overturn,
            appointmentId: appointment.id,
            appointmentStateId: appointment.appointmentStateId,
            date: buildFullDate(appointment.hour, momentParseDate(appointment.date)),
            phonePrefix: appointment.phonePrefix,
            phoneNumber: appointment.phoneNumber,
            rnos: appointment.healthInsuranceId,
            medicalCoverageName: appointment.medicalCoverageName,
            affiliateNumber: appointment.medicalCoverageAffiliateNumber,
            createdOn: appointment.createdOn,
            professionalPersonDto: appointment.professionalPersonDto
        }
    };

    function getTitle(): string {

        if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED) {
            return appointmentBlockMotivesFacadeService?.getAppointmentBlockMotiveById(appointment.appointmentBlockMotiveId);
        }
        if (appointment.patient?.typeId === TEMPORARY_PATIENT) {
            return `${momentParseTime(from).format(DateFormat.HOUR_MINUTE)} ${viewName ? viewName : ''} (Temporal)`;
        }
        return `${momentParseTime(from).format(DateFormat.HOUR_MINUTE)}	 ${viewName}`;
    }
}

export function getColor(appointment: AppointmentListDto): COLORES {
    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED) {
        return COLORES.BLOCKED
    }

    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.OUT_OF_DIARY) {
        return COLORES.FUERA_DE_AGENDA;
    }

    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BOOKED) {
        return COLORES.RESERVA_VALIDACION;
    }

    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED) {
        return COLORES.CONFIRMED;
    }

    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT) {
        return COLORES.ABSENT;
    }

    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.SERVED) {
        return COLORES.SERVED;
    }

    if (showProtectedAppointment(appointment)) {
        return COLORES.PROTECTED;
    }

    if (!appointment?.patient?.id) {
        return COLORES.RESERVA_ALTA;
    }

    if (appointment.overturn) {
        return COLORES.SOBRETURNO;
    }

    return COLORES.ASSIGNED;
}

export function getSpanColor(appointment: AppointmentListDto): string {
    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT || appointment.appointmentStateId === APPOINTMENT_STATES_ID.SERVED) {
        return GREY_TEXT;
    }

    if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BOOKED) {
        return BLUE_TEXT;
    }

    if (showProtectedAppointment(appointment)) {
		return PURPLE_TEXT;
	}

    return WHITE_TEXT;
}

function showProtectedAppointment(appointment: AppointmentListDto) {
    return appointment.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED && appointment.protected
}