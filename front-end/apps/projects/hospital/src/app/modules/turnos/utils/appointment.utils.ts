import { CalendarEvent } from 'angular-calendar';

import {
	AppointmentListDto, DiaryLabelDto,
} from '@api-rest/api-model';
import {
	DateFormat,
	buildFullDateFromDate,
	dateISOParseDate,
	dateParseTime,
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
import { convertDateTimeDtoToDate, stringToTimeDto, timeDtotoString } from '@api-rest/mapper/date-dto.mapper';
import { format, isAfter } from 'date-fns';
import { timeDifference, toHourMinute } from '@core/utils/date.utils';

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
    return `${setDiaryLabelColor(appointment?.diaryLabelDto)} 
			<article>
				${timeDtotoString(stringToTimeDto(from))} ${viewName}. 
				${setDiaryLabelDescription(appointment?.diaryLabelDto)}
			</article>
			<div class="appointment-aside"></div>`

}

export function toCalendarEvent(from: string, to: string, date: Date, appointment: AppointmentListDto, viewName?: string, appointmentBlockMotivesFacadeService?: AppointmentBlockMotivesFacadeService): CalendarEvent {
	const fullName = [appointment.patient?.person.lastName, appointment.patient?.person.firstName].
		filter(val => val).join(', ');

	const fullNameWithNameSelfDetermination = appointment.patient?.person.nameSelfDetermination ?
		[appointment.patient.person.lastName, appointment.patient.person.nameSelfDetermination].filter(val => val).join(', ') : null;

	const title = getTitle();

	return {
		start: buildFullDateFromDate(from, date),
		end: buildFullDateFromDate(to, date),
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
			date: buildFullDateFromDate(appointment.hour, dateISOParseDate(appointment.date)),
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

        if(appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED) {
			return getConfirmedTitle();
		}

        return defaultHtml(from, appointment, viewName);
    }

	function getConfirmedTitle(): string {
		return `${setDiaryLabelColor(appointment?.diaryLabelDto)} 
				<article>
					${timeDtotoString(stringToTimeDto(from))}
					${viewName}.
					${setDiaryLabelDescription(appointment?.diaryLabelDto)}
				</article>
				<div class="appointment-aside">
					<span class='material-icons-outlined'>hourglass_empty</span>
					${timeDifference(convertDateTimeDtoToDate(appointment.updatedOn))}
				</div>`;
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

export function getStudiesNames(studies: string[] , titleOrder: string) {
    let partialStudies = [...studies]
    let firstElement = partialStudies.shift()
    let firstStudiesNames = ` ${titleOrder} - ${firstElement}`
    let studiesPartial = ''
    if (partialStudies.length > 0) {
        partialStudies.forEach(study => {
            studiesPartial = studiesPartial + `, ${study} `
        })
    }
    return firstStudiesNames + studiesPartial
}

export const getAppointmentStart = (hour: string): string => {
	return format(dateParseTime(hour), DateFormat.HOUR_MINUTE);
}

export const getAppointmentEnd = (hour: string, appointmentDuration: number): string => {
	const fromDate = dateParseTime(hour);
	const toDate = dateParseTime(hour);
	const appointmentEnding = toDate.getMinutes() + appointmentDuration;
	toDate.setMinutes(appointmentEnding);

	if (isAfter(fromDate, toDate))
		toDate.setHours(23, 59);
	return toHourMinute(toDate);
}
