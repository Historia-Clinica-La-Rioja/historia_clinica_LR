import { Injectable } from '@angular/core';
import { CalendarEvent } from 'angular-calendar';
import { ReplaySubject, Observable } from 'rxjs';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { AppointmentListDto, BasicPersonalDataDto, CreateAppointmentDto, UpdateAppointmentDto } from '@api-rest/api-model';
import {
	momentParseTime,
	DateFormat,
	momentParseDate,
	buildFullDate,
} from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { map, first } from 'rxjs/operators';
import { CANCEL_STATE_ID, APPOINTMENT_STATES_ID } from '../constants/appointment';
import { PatientNameService } from "@core/services/patient-name.service";
import { AppointmentBlockMotivesFacadeService } from './appointment-block-motives-facade.service';

const enum COLORES {
	ASSIGNED = '#4187FF',
	CONFIRMED = '#FFA500',
	ABSENT = '#D5E0D5',
	BLOCKED = '#7D807D',
	SERVED = '#A3EBAF',
	PROGRAMADA = '#7FC681',
	ESPONTANEA = '#2687C5',
	SOBRETURNO = '#E3A063',
	RESERVA_ALTA = '#FFFFFF',
	RESERVA_VALIDACION = '#EB5757',
	FUERA_DE_AGENDA = '#FF0000'
}

const TEMPORARY_PATIENT = 3;
const GREY_TEXT = 'calendar-event-grey-text';
const WHITE_TEXT = 'calendar-event-white-text';
const BLUE_TEXT = 'calendar-event-blue-text';

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

	private intervalId: any;
	private professionalId: number;

	constructor(
		private readonly appointmentService: AppointmentsService,
		private readonly patientNameService: PatientNameService,
		private readonly appointmentBlockMotivesFacadeService: AppointmentBlockMotivesFacadeService,

	) {
		this.appointments$ = this.appointmenstEmitter.asObservable();
	}

	setProfessionalId(id: number) {
		this.professionalId = id;
	}

	setValues(agendaId, appointmentDuration): void {
		this.agendaId = agendaId;
		this.appointmentDuration = appointmentDuration;
		this.loadAppointments();
	}

	clear(): void {
		this.appointmenstEmitter.next(undefined);
	}

	public loadAppointments(): void {

		this.appointmentService.getList([this.agendaId], this.professionalId)
			.subscribe((appointments: AppointmentListDto[]) => {
				const appointmentsCalendarEvents: CalendarEvent[] = appointments
					.map(appointment => {
						const from = momentParseTime(appointment.hour).format(DateFormat.HOUR_MINUTE);
						const to = momentParseTime(from).add(this.appointmentDuration, 'minutes').format(DateFormat.HOUR_MINUTE);
						const viewName = this.getViewName(appointment.patient?.person);
						const calendarEvent = toCalendarEvent(from, to, momentParseDate(appointment.date), appointment, viewName, this.appointmentBlockMotivesFacadeService);
						return calendarEvent;
					});
				this.appointmenstEmitter.next(appointmentsCalendarEvents);
			});
	}

	setInterval() {
		this.intervalId = setInterval(() => this.loadAppointments(), 20000);
	}

	clearInterval() {
		clearInterval(this.intervalId);
	}

	private getViewName(person: BasicPersonalDataDto): string {
		return person ? [person.lastName, this.patientNameService.getPatientName(person.firstName, person.nameSelfDetermination)].
			filter(val => val).join(', ') : null;
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

	updateObservation(appointmentId: number, observation: string): Observable<boolean> {
		return this.appointmentService.updateObservation(appointmentId, observation)
			.pipe(
				map((response: boolean) => {
					if (response) {
						this.appointments$.pipe(first()).subscribe((events: CalendarEvent[]) => {
							const toEdit: CalendarEvent = events.find(event => event.meta.appointmentId === appointmentId);
							toEdit.meta.observation = observation;
							this.appointmenstEmitter.next(events);
						});
						return true;
					}
					return false;
				})
			);
	}

	update(events) {
		this.appointmenstEmitter.next(events);
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

	updateAppointment(appointment: UpdateAppointmentDto) {
		return this.appointmentService.updateAppointment(appointment)
			.pipe(
				map(() => {
						this.appointments$.subscribe(
							(events: CalendarEvent[]) => {
								const updatedEvent: CalendarEvent = events.find(event => event.meta.appointmentId === appointment.appointmentId);
								updatedEvent.meta.appointmentStateId = appointment.appointmentStateId
								this.loadAppointments();
							}
						).unsubscribe();
						return true;
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
								this.loadAppointments();
							}
						).unsubscribe();
						return true;
					}
					return false;
				})
			);
	}
}

export function toCalendarEvent(from: string, to: string, date: Moment, appointment: AppointmentListDto, viewName: string, appointmentBlockMotivesFacadeService?: AppointmentBlockMotivesFacadeService): CalendarEvent {
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
			secondary: getColor(appointment)
		},
		cssClass: getSpanColor(appointment.appointmentStateId),
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
		}
	};

	function getTitle(): string {

		if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED) {
			return appointmentBlockMotivesFacadeService?.getAppointmentBlockMotiveById(appointment.appointmentBlockMotiveId);
		}
		if (appointment.patient?.typeId === TEMPORARY_PATIENT) {
			return `${momentParseTime(from).format(DateFormat.HOUR_MINUTE_12)} ${viewName ? viewName : ''} (Temporal)`;
		}
		return `${momentParseTime(from).format(DateFormat.HOUR_MINUTE_12)}	 ${viewName}`;
	}
}

export function getColor(appointment: AppointmentListDto): COLORES {
	if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED) {
		return COLORES.BLOCKED
	}

	if(appointment.appointmentStateId === APPOINTMENT_STATES_ID.OUT_OF_DIARY) {
		return COLORES.FUERA_DE_AGENDA;
	}



	if (appointment.overturn) {
		return COLORES.SOBRETURNO;
	}

	if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.BOOKED) {
		return COLORES.RESERVA_VALIDACION;
	}

	if (appointment.appointmentStateId === APPOINTMENT_STATES_ID.CONFIRMED) {
		return COLORES.CONFIRMED;
	}

	if(appointment.appointmentStateId === APPOINTMENT_STATES_ID.ABSENT) {
		return COLORES.ABSENT;
	}

	if(appointment.appointmentStateId === APPOINTMENT_STATES_ID.SERVED) {
		return COLORES.SERVED;
	}
	if (!appointment?.patient?.id) {
		return COLORES.RESERVA_ALTA;
	}

	return COLORES.ASSIGNED;
}

export function getSpanColor(appointmentStateId: number): string {
	if (appointmentStateId === APPOINTMENT_STATES_ID.ABSENT || appointmentStateId === APPOINTMENT_STATES_ID.SERVED) {
		return GREY_TEXT;
	}

	if (appointmentStateId === APPOINTMENT_STATES_ID.BOOKED) {
		return BLUE_TEXT;
	}

	return WHITE_TEXT;
}

interface AppointmentColorsStates {
	id: APPOINTMENT_STATES_ID,
	color: COLORES
}
