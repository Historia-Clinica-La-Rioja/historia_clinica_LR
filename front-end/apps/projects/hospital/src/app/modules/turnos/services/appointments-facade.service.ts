import { Injectable } from '@angular/core';
import { CalendarEvent } from 'angular-calendar';
import { ReplaySubject, Observable } from 'rxjs';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { AppointmentListDto, CreateAppointmentDto } from '@api-rest/api-model';
import { momentParseTime, DateFormat, momentParseDate, buildFullDate } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { map, first } from 'rxjs/operators';
import { CANCEL_STATE_ID, APPOINTMENT_STATES_ID } from '../constants/appointment';
import { PatientNameService } from "@core/services/patient-name.service";

const enum COLORES {
	ASSIGNED = '#4187FF',
	CONFIRMED = '#FFA500',
	ABSENT = '#D5E0D5',
	BLOCKED = '#C2C2C2',
	SERVED = '#A3EBAF',
	PROGRAMADA = '#7FC681',
	ESPONTANEA = '#2687C5',
	SOBRETURNO = '#E3A063',
	RESERVA_ALTA = '#F2994A',
	RESERVA_VALIDACION = '#EB5757'
}
const TEMPORARY_PATIENT = 3;
const GREY_TEXT = 'calendar-event-grey-text';
const WHITE_TEXT = 'calendar-event-white-text';

const APPOINTMENT_COLORS_STATES: AppointmentColorsStates[] = [
	{
		id: APPOINTMENT_STATES_ID.ASSIGNED,
		color: COLORES.ASSIGNED
	},
	{
		id: APPOINTMENT_STATES_ID.CONFIRMED,
		color: COLORES.CONFIRMED
	},
	{
		id: APPOINTMENT_STATES_ID.ABSENT,
		color: COLORES.ABSENT
	},
	{
		id: APPOINTMENT_STATES_ID.SERVED,
		color: COLORES.SERVED
	}
];

@Injectable({
	providedIn: 'root'
})
export class AppointmentsFacadeService {

	private agendaId: number;
	private appointmentDuration: number;


	private appointmenstEmitter = new ReplaySubject<CalendarEvent[]>(1);
	private appointments$: Observable<CalendarEvent[]>;

	constructor(
		private readonly appointmentService: AppointmentsService,
		private readonly patientNameService: PatientNameService,

	) {
		this.appointments$ = this.appointmenstEmitter.asObservable();
	}

	setValues(agendaId, appointmentDuration): void {
		this.agendaId = agendaId;
		this.appointmentDuration = appointmentDuration;
		this.loadAppointments();
	}

	clear(): void {
		this.appointmenstEmitter.next(undefined);
	}

	private loadAppointments(): void {
		this.appointmentService.getList([this.agendaId])
			.subscribe((appointments: AppointmentListDto[]) => {
				const appointmentsCalendarEvents: CalendarEvent[] = appointments
					.map(appointment => {
						const from = momentParseTime(appointment.hour).format(DateFormat.HOUR_MINUTE);
						const to = momentParseTime(from).add(this.appointmentDuration, 'minutes').format(DateFormat.HOUR_MINUTE);
						const viewName = [appointment.patient.person.lastName, this.patientNameService.getPatientName(appointment.patient.person.firstName, appointment.patient.person.nameSelfDetermination)].
						filter(val => val).join(', ');
						const calendarEvent = toCalendarEvent(from, to, momentParseDate(appointment.date), appointment, viewName);
						return calendarEvent;
					});
				this.appointmenstEmitter.next(appointmentsCalendarEvents);
			});
	}

	getAppointments(): Observable<CalendarEvent[]> {
		return this.appointments$;
	}


	updatePhoneNumber(appointmentId: number, phonePrefix: string, phoneNumber: string): Observable<boolean> {
		return this.appointmentService.updatePhoneNumber(appointmentId, phonePrefix, phoneNumber)
			.pipe(
				map((response: boolean) => {
					if (response) {
						this.appointments$.pipe(first()).subscribe((events: CalendarEvent[]) => {
							const toEdit: CalendarEvent = events.find(event => event.meta.appointmentId === appointmentId);
							toEdit.meta.phoneNumber = phoneNumber;
							toEdit.meta.phonePrefix = phonePrefix;
							this.appointmenstEmitter.next(events);
						});
						return true;
					}
					return false;
				})
			);
	}

	addAppointment(newAppointment: CreateAppointmentDto): Observable<boolean> {
		return this.appointmentService.create(newAppointment)
			.pipe(
				map((response: number) => {
					if (response) {
						this.loadAppointments(); // TODO En lugar de hacer otro llamado al BE evaluar si se puede agregar appointments$
						return true;
					}
					return false;
				})
			);
	}

	cancelAppointment(appointmentId: number, motivo: string): Observable<boolean> {
		return this.appointmentService.changeState(appointmentId, CANCEL_STATE_ID, motivo)
			.pipe(
				map((response: boolean) => {
					if (response) {
						this.appointments$.pipe(first()).subscribe((events: CalendarEvent[]) => {
							const validEvents = events.filter(event => event.meta.appointmentId !== appointmentId);
							this.appointmenstEmitter.next(validEvents);
						});
						return true;
					}
					return false;
				})
			);
	}

	changeState(appointmentId: number, newStateId: APPOINTMENT_STATES_ID, motivo?: string): Observable<boolean> {
		return this.appointmentService.changeState(appointmentId, newStateId, motivo)
			.pipe(
				map((response: boolean) => {
					if (response) {
						this.appointments$.subscribe(
							(events: CalendarEvent[]) => {
								const updatedEvent: CalendarEvent = events.find(event => event.meta.appointmentId === appointmentId);
								updatedEvent.meta.appointmentStateId = newStateId;
							}
						).unsubscribe();
						return true;
					}
					return false;
				})
			);
	}
}

export function toCalendarEvent(from: string, to: string, date: Moment, appointment: AppointmentListDto, viewName: string): CalendarEvent {
	const fullName = [appointment.patient.person.lastName, appointment.patient.person.firstName].
		filter(val => val).join(', ');

	const fullNameWithNameSelfDetermination = appointment.patient.person.nameSelfDetermination ?
		[appointment.patient.person.lastName, appointment.patient.person.nameSelfDetermination].filter(val => val).join(', ') : null;

	const title = appointment.patient.typeId === TEMPORARY_PATIENT ?
		`${momentParseTime(from).format(DateFormat.HOUR_MINUTE_12)} ${viewName} (Temporario)` : `${momentParseTime(from).format(DateFormat.HOUR_MINUTE_12)}	 ${viewName}`;

	return {
		start: buildFullDate(from, date).toDate(),
		end: buildFullDate(to, date).toDate(),
		title,
		color: {
			primary: getColor(appointment.appointmentStateId),
			secondary: getColor(appointment.appointmentStateId)
		},
		cssClass: getSpanColor(appointment.appointmentStateId),
		meta: {
			patient: {
				id: appointment.patient.id,
				fullName,
				identificationNumber: appointment.patient.person.identificationNumber,
				typeId: appointment.patient.typeId,
				fullNameWithNameSelfDetermination: fullNameWithNameSelfDetermination,
				identificationTypeId: appointment.patient.person.identificationTypeId,
				genderId: appointment.patient.person.genderId,
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
		}
	};
}

export function getColor(appointmentStateId: number): COLORES {
	return APPOINTMENT_COLORS_STATES.find(appointmentColor => appointmentColor.id === appointmentStateId).color;
}

export function getSpanColor(appointmentStateId: number): string {
	return ((appointmentStateId === APPOINTMENT_STATES_ID.ABSENT) || (appointmentStateId === APPOINTMENT_STATES_ID.SERVED)) ? GREY_TEXT : WHITE_TEXT;
}

interface AppointmentColorsStates {
	id: APPOINTMENT_STATES_ID,
	color: COLORES
}
