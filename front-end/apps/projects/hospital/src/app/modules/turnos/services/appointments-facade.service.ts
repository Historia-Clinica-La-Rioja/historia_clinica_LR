import { Injectable } from '@angular/core';
import { CalendarEvent } from 'angular-calendar';
import { ReplaySubject, Observable, forkJoin, BehaviorSubject } from 'rxjs';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import {
	AppointmentShortSummaryDto,
	BasicPersonalDataDto,
	CreateAppointmentDto,
	ProfessionalDto,
	UpdateAppointmentDateDto,
	UpdateAppointmentDto,
	HolidayDto,
} from '@api-rest/api-model';
import {
	momentParseTime,
	DateFormat,
	momentParseDate,
} from '@core/utils/moment.utils';
import { map, first } from 'rxjs/operators';
import { CANCEL_STATE_ID, APPOINTMENT_STATES_ID } from '../constants/appointment';
import { PatientNameService } from "@core/services/patient-name.service";
import { AppointmentBlockMotivesFacadeService } from './appointment-block-motives-facade.service';
import { HolidaysService } from '@api-rest/services/holidays.service';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { toCalendarEvent } from '@turnos/utils/appointment.utils';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { DatePipeFormat } from '@core/utils/date.utils';

@Injectable({
	providedIn: 'root'
})
export class AppointmentsFacadeService {

	private agendaId: number;
	private appointmentDuration: number;


	private appointmenstEmitter = new ReplaySubject<CalendarEvent[]>(1);
	private holidayEmitter = new ReplaySubject<CalendarEvent[]>(1);
	private appointments$: Observable<CalendarEvent[]>;
	private holidays$: Observable<CalendarEvent[]>;
	private professionalSubject = new BehaviorSubject<ProfessionalDto>(null);

	private professional: ProfessionalDto;
	professional$ = this.professionalSubject.asObservable();

	private startDate: string;
	private endDate: string;

	constructor(
		private readonly appointmentService: AppointmentsService,
		private readonly patientNameService: PatientNameService,
		private readonly appointmentBlockMotivesFacadeService: AppointmentBlockMotivesFacadeService,
		private readonly holidayService: HolidaysService,
		private readonly translateService: TranslateService,
		private readonly datePipe: DatePipe

	) {
		this.appointments$ = this.appointmenstEmitter.asObservable();
		this.holidays$ = this.holidayEmitter.asObservable();
	}

	setProfessional(professional: ProfessionalDto) {
		if (professional?.id && this.professional?.id !== professional.id) {
			this.professional = professional;
			this.professionalSubject.next(professional);
			if (this.agendaId) {
				this.loadAppointments();
			}
		}
	}

	getProfessional() {
		return this.professional;
	}

	setValues(agendaId, appointmentDuration, startDate: string, endDate: string): void {
		this.agendaId = agendaId;
		this.appointmentDuration = appointmentDuration;
		this.startDate = startDate;
		this.endDate = endDate;
		this.loadAppointments();
	}

	clear(): void {
		this.appointmenstEmitter.next(undefined);
		this.agendaId = null;
	}

	public loadAppointments(): void {
		forkJoin([	this.appointmentService.getList([this.agendaId], this.professional?.id, this.startDate, this.endDate),
					this.holidayService.getHolidays(this.startDate, this.endDate)]).subscribe((result) => {
				const appointmentsCalendarEvents: CalendarEvent[] = result[0]
					.map(appointment => {
						const from = momentParseTime(appointment.hour).format(DateFormat.HOUR_MINUTE);
						let to = momentParseTime(from).add(this.appointmentDuration, 'minutes').format(DateFormat.HOUR_MINUTE);
						if (from > to) {
							to = momentParseTime(from).set({hour: 23, minute: 59}).format(DateFormat.HOUR_MINUTE);
						}
						const viewName = this.getViewName(appointment.patient?.person);
						const calendarEvent = toCalendarEvent(from, to, momentParseDate(appointment.date), appointment, viewName, this.appointmentBlockMotivesFacadeService);
						return calendarEvent;
					});
				const holidaysCalendarEvents = result[1].map(holiday => {
					return {
						start: dateDtoToDate(holiday.date),
						title: holiday.description,
						allDay: true
					}
				});
				this.appointmenstEmitter.next(appointmentsCalendarEvents.concat(holidaysCalendarEvents));
				this.holidayEmitter.next(holidaysCalendarEvents);
			});
	}

	private getViewName(person: BasicPersonalDataDto): string {
		return person ? [person.lastName, this.patientNameService.getPatientName(person.firstName, person.nameSelfDetermination)].
			filter(val => val).join(', ') : null;
	}

	getAppointments(): Observable<CalendarEvent[]> {
		return this.appointments$;
	}

	getHolidays(): Observable<CalendarEvent[]> {
		return this.holidays$;
	}

	checkIfHoliday(holidays: HolidayDto[], newAppointmentDate: string): Date {
		let selectedHolidayDay;
		holidays.map(day => {
			const holidayDate = dateDtoToDate(day.date);
			const appointmentDate = new Date(newAppointmentDate);

			const holidayUTCDate = holidayDate.getUTCDate() + ',' + holidayDate.getUTCMonth() + ',' + holidayDate.getUTCFullYear();
			const appointmentUTCDate = appointmentDate.getUTCDate() + ',' + appointmentDate.getUTCMonth() + ',' + appointmentDate.getUTCFullYear();
			if (holidayUTCDate === appointmentUTCDate) {
				selectedHolidayDay = holidayDate;
			}
			
		});
		return selectedHolidayDay;
	}

	getHolidayData(selectedDay: Date) {
		const holidayText = this.translateService.instant('turnos.holiday.HOLIDAY_RELATED');
		const holidayDateText = this.datePipe.transform(selectedDay, DatePipeFormat.FULL_DATE);
		return {
			title: 'turnos.holiday.TITLE',
			content: `${holidayDateText.charAt(0).toUpperCase() + holidayDateText.slice(1)} ${holidayText}`,
			contentBold: `turnos.holiday.HOLIDAY_DISCLAIMER`,
			okButtonLabel: 'turnos.holiday.OK_BUTTON',
			cancelButtonLabel: 'turnos.holiday.CANCEL_BUTTON',
		};
	}

	updatePhoneNumber(appointmentId: number, phonePrefix: string, phoneNumber: string): Observable<boolean> {
		return this.appointmentService.updatePhoneNumber(appointmentId, phonePrefix, phoneNumber)
			.pipe(
				map((response: boolean) => {
					if (response) {
						this.appointments$.pipe(first()).subscribe((events: CalendarEvent[]) => {
							const toEdit: CalendarEvent = events.find(event => event.meta?.appointmentId === appointmentId);
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
							const toEdit: CalendarEvent = events.find(event => event.meta?.appointmentId === appointmentId);
							toEdit.meta.observation = observation;
							this.appointmenstEmitter.next(events);
						});
						return true;
					}
					return false;
				})
			);
	}

	updateDate(updateAppointmentDate: UpdateAppointmentDateDto): Observable<boolean> {
		return this.appointmentService.updateDate(updateAppointmentDate)
			.pipe(
				map((response: boolean) => {
					if (response) {
						this.appointments$.pipe(first()).subscribe((events: CalendarEvent[]) => {
							const toEdit: CalendarEvent = events.find(event => event.meta?.appointmentId === updateAppointmentDate.appointmentId);
							toEdit.meta.date = updateAppointmentDate.date
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

	addAppointment(newAppointment: CreateAppointmentDto): Observable<number> {
		return this.appointmentService.create(newAppointment)
			.pipe(
				map((response: number) => {
					if (response) {
						return response;
					}
					return -1;
				})
			);
	}

	cancelAppointment(appointmentId: number, motivo: string): Observable<boolean> {
		return this.appointmentService.changeState(appointmentId, CANCEL_STATE_ID, motivo)
			.pipe(
				map((response: boolean) => {
					if (response) {
						this.appointments$.pipe(first()).subscribe((events: CalendarEvent[]) => {
							const validEvents = events.filter(event => event.meta?.appointmentId !== appointmentId);
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
								const updatedEvent: CalendarEvent = events.find(event => event.meta?.appointmentId === appointment.appointmentId);
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
								const updatedEvent: CalendarEvent = events.find(event => event.meta?.appointmentId === appointmentId);
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

	verifyExistingAppointment(patientId: number, date: string, hour: string, institutionId?: number): Observable<AppointmentShortSummaryDto> {
		return this.appointmentService.verifyExistingAppointments(patientId, date, hour, institutionId);
	}

	cancelRecurringAppointments(appointmentId: number, cancelAllAppointments: boolean): Observable<boolean> {
		return this.appointmentService.cancelRecurringAppointments(appointmentId, cancelAllAppointments);
	}

	createExpiredAppointment(newExpiredAppointment: CreateAppointmentDto): Observable<number> {
		return this.appointmentService.createExpiredAppointment(newExpiredAppointment);
	}

	
}
