import { Injectable } from '@angular/core';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { CalendarEvent } from 'angular-calendar';
import { getAppointmentEnd, getAppointmentStart, toCalendarEvent } from '../utils/appointment.utils';
import { CreateAppointmentDto, BasicPersonalDataDto, AppointmentShortSummaryDto, AppointmentListDto, HolidayDto, UpdateAppointmentDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { ReplaySubject, Observable, forkJoin } from 'rxjs';
import { first, map } from 'rxjs/operators';
import { HolidaysService } from '@api-rest/services/holidays.service';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { APPOINTMENT_STATES_ID, CANCEL_STATE_ID } from '@turnos/constants/appointment';

@Injectable()
export class EquipmentAppointmentsFacadeService {

	private diaryId: number;
	private appointmentDuration: number;

	private appointmenstEmitter = new ReplaySubject<CalendarEvent[]>(1);
	private appointments$: Observable<CalendarEvent[]>;

	private holidayEmitter = new ReplaySubject<CalendarEvent[]>(1);
	private holidays$: Observable<CalendarEvent[]>;

	private startDate: string;
	private endDate: string;

	constructor(
		private readonly appointmentsService: AppointmentsService,
		private readonly patientNameService: PatientNameService,
		private readonly holidayService: HolidaysService
	) {
		this.appointments$ = this.appointmenstEmitter.asObservable();
		this.holidays$ = this.holidayEmitter.asObservable();
	}

	setValues(diaryId: number, appointmentDuration: number, startDate: string, endDate: string) {
		this.diaryId = diaryId;
		this.appointmentDuration = appointmentDuration;
		this.startDate = startDate;
		this.endDate = endDate;
		this.loadAppointments();
	}

	clear() {
		this.appointmenstEmitter.next(undefined);
		this.diaryId = null;
	}

	loadAppointments() {
		const appointmentList$ = this.appointmentsService.getEquipmentList(this.diaryId, this.startDate, this.endDate);
		const holidays$ = this.holidayService.getHolidays(this.startDate, this.endDate);

		forkJoin([appointmentList$, holidays$]).subscribe(([appointmentList, holidaysList]) => {
			const appointmentsCalendarEvents: CalendarEvent[] = this.toAppointmentsCalendarEvent(appointmentList);
			const holidaysCalendarEvents = this.toHolidaysCalendarEvent(holidaysList);

			this.appointmenstEmitter.next(appointmentsCalendarEvents.concat(holidaysCalendarEvents));
			this.holidayEmitter.next(holidaysCalendarEvents);
		});
	}

	getAppointments(): Observable<CalendarEvent[]> {
		return this.appointments$;
	}

	addAppointment(newAppointment: CreateAppointmentDto, orderId?: number, studyId?: number): Observable<number> {
		return this.appointmentsService.createAppointmentEquipment(newAppointment, orderId, studyId)
			.pipe(
				map((response: number) => {
					if (response) {
						return response;
					}
					return -1;
				})
			);
	}

	addAppointmentWithTranscribedOrder(newAppointment: CreateAppointmentDto, transcribedOrderId: number): Observable<number> {
		return this.appointmentsService.createAppointmentEquipmentWithTranscribedOrder(newAppointment, transcribedOrderId)
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
		return this.appointmentsService.changeStateAppointmentEquipment(appointmentId, CANCEL_STATE_ID, motivo)
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

	updatePhoneNumber(appointmentId: number, phonePrefix: string, phoneNumber: string): Observable<boolean> {
		return this.appointmentsService.updatePhoneNumber(appointmentId, phonePrefix, phoneNumber)
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

	verifyExistingEquipmentAppointment(patientId: number, date: string): Observable<AppointmentShortSummaryDto> {
		return this.appointmentsService.verifyExistingEquipmentAppointments(patientId, date);
	}

	getHolidays(): Observable<CalendarEvent[]> {
		return this.holidays$;
	}

	changeState(appointmentId: number, newStateId: APPOINTMENT_STATES_ID, motivo?: string): Observable<boolean> {
		return this.appointmentsService.changeStateAppointmentEquipment(appointmentId, newStateId, motivo)
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

	updateAppointment(appointment: UpdateAppointmentDto) {
		return this.appointmentsService.updateAppointment(appointment)
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

	private getViewName(person: BasicPersonalDataDto): string {
		return person ? [person.lastName, this.patientNameService.getPatientName(person.firstName, person.nameSelfDetermination)].
			filter(val => val).join(', ') : null;
	}

	private toAppointmentsCalendarEvent(appointments: AppointmentListDto[]): CalendarEvent[] {
		return appointments.map(appointment => {
			const from = getAppointmentStart(appointment.hour);
			const to = getAppointmentEnd(appointment.hour, this.appointmentDuration);
			const viewName = this.getViewName(appointment.patient?.person);
			return toCalendarEvent(from, to, dateISOParseDate(appointment.date), appointment, viewName);
		});
	}

	private toHolidaysCalendarEvent(holidaysDto: HolidayDto[]): CalendarEvent[] {
		return holidaysDto.map(holiday => {
			return {
				start: dateDtoToDate(holiday.date),
				title: holiday.description,
				allDay: true
			}
		});
	}

}

