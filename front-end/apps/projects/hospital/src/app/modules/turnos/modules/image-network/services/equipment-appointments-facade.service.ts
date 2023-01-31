import { Injectable } from '@angular/core';
import { DateFormat, momentParseDate, momentParseTime } from '@core/utils/moment.utils';
import { CalendarEvent } from 'angular-calendar';
import { toCalendarEvent } from '../utils/appointment.utils';
import { CreateAppointmentDto, BasicPersonalDataDto, AppointmentShortSummaryDto, AppointmentListDto, HolidayDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { ReplaySubject, Observable, forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';
import { HolidaysService } from '@api-rest/services/holidays.service';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';

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

	addAppointment(newAppointment: CreateAppointmentDto): Observable<number> {
		return this.appointmentsService.createAppointmentEquipment(newAppointment)
			.pipe(
				map((response: number) => {
					if (response) {
						return response;
					}
					return -1;
				})
			);
	}

	verifyExistingEquipmentAppointment(patientId: number, date: string): Observable<AppointmentShortSummaryDto> {
		return this.appointmentsService.verifyExistingEquipmentAppointments(patientId, date);
	}

	getHolidays(): Observable<CalendarEvent[]> {
		return this.holidays$;
	}

	private getViewName(person: BasicPersonalDataDto): string {
		return person ? [person.lastName, this.patientNameService.getPatientName(person.firstName, person.nameSelfDetermination)].
			filter(val => val).join(', ') : null;
	}

	private toAppointmentsCalendarEvent(appointments: AppointmentListDto[]): CalendarEvent[] {
		return appointments.map(appointment => {
			const from = momentParseTime(appointment.hour).format(DateFormat.HOUR_MINUTE);
			let to = momentParseTime(from).add(this.appointmentDuration, 'minutes').format(DateFormat.HOUR_MINUTE);
			if (from > to) {
				to = momentParseTime(from).set({ hour: 23, minute: 59 }).format(DateFormat.HOUR_MINUTE);
			}
			const viewName = this.getViewName(appointment.patient?.person);
			const calendarEvent = toCalendarEvent(from, to, momentParseDate(appointment.date), appointment, viewName);
			return calendarEvent;
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
