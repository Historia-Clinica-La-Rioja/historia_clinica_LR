import { CalendarEvent } from 'angular-calendar';
import { Moment } from 'moment';

import {
	AppointmentListDto, DiaryLabelDto,
} from '@api-rest/api-model';
import {
    DateFormat,
    buildFullDate,
    momentParseDate,
    momentParseTime,
} from '@core/utils/moment.utils';
import {
	APPOINTMENT_STATES_ID,
	BLUE_TEXT,
	COLOR,
	COLORES,
	GREY_TEXT,
	PURPLE_TEXT,
	TEMPORARY_PATIENT,
	WHITE_TEXT,
    getDiaryLabel,
} from '@turnos/constants/appointment';
import {
	AppointmentBlockMotivesFacadeService,
} from '@turnos/services/appointment-block-motives-facade.service';

function setDiaryLabelColor(diaryLabelDto: DiaryLabelDto): string {
    if (!diaryLabelDto) return '';

    const color: COLOR = getDiaryLabel(diaryLabelDto.colorId);
    return `<hr class="appointment-label" color=${color.color}>`
}

function setDiaryLabelDescription(diaryLabelDto: DiaryLabelDto): string {
    if (!diaryLabelDto) return '';

    return diaryLabelDto.description;
}

function defaultHtml(from: string, appointment: AppointmentListDto, viewName?: string): string {
    return `<div class="appointment-description">
                ${setDiaryLabelColor(appointment?.diaryLabelDto)}
                <article>
                    ${momentParseTime(from).format(DateFormat.HOUR_MINUTE)} ${viewName}.
                    ${setDiaryLabelDescription(appointment?.diaryLabelDto)}
                </article>
            </div>`
}

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
            secondary: showTransparentAppointment(appointment) ? 'transparent' : getColor(appointment)
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
                email: appointment.patientEmail,
				names: {
					firstName: appointment.patient?.person.firstName,
					lastName: appointment.patient?.person.lastName,
					middleNames: appointment.patient?.person.middleNames,
					nameSelfDetermination: appointment.patient?.person.nameSelfDetermination,
					otherLastNames: appointment.patient?.person.otherLastNames,
				}
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
            professionalPersonDto: appointment.professionalPersonDto,
        }
    };

    function getTitle(): string {

        if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED) {
            return appointmentBlockMotivesFacadeService?.getAppointmentBlockMotiveById(appointment.appointmentBlockMotiveId);
        }
        if (appointment.patient?.typeId === TEMPORARY_PATIENT) {
            viewName = '(Temporal)';
        }

        return defaultHtml(from, appointment, viewName);
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

    if (isAProtectedAppointment(appointment)) {
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

    if (isAProtectedAppointment(appointment)) {
        return PURPLE_TEXT;
    }

    return WHITE_TEXT;
}

function showTransparentAppointment(appointment: AppointmentListDto): boolean {
	return isAProtectedAppointment(appointment) || appointment.expiredRegister;
}

function isAProtectedAppointment(appointment: AppointmentListDto): boolean {
	return appointment.appointmentStateId === APPOINTMENT_STATES_ID.ASSIGNED && appointment.protected
}

export function getHourFromString(value: string): RegExpMatchArray {
    const pattern = /\b\d{1,2}:\d{2}\b/g;
    return value.match(pattern);
}

export const MAX_APPOINTMENT_PER_HOUR = 4;