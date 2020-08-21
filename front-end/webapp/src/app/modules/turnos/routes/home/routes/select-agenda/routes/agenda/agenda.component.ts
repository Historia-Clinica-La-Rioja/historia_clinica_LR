import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AppointmentListDto, CompleteDiaryDto, DiaryOpeningHoursDto } from '@api-rest/api-model';
import { CalendarWeekViewBeforeRenderEvent } from 'angular-calendar';
import {
	buildFullDate,
	DateFormat,
	dateToMoment,
	momentParseDate,
	momentParseTime,
	newMoment,
	dateToMomentTimeZone
} from '@core/utils/moment.utils';
import { NewAgendaService } from '../../../../../../services/new-agenda.service';
import { MatDialog } from '@angular/material/dialog';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DiaryService } from '@api-rest/services/diary.service';
import { Moment } from 'moment';
import { NewAppointmentComponent } from '../../../../../../dialogs/new-appointment/new-appointment.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { CalendarEvent, WeekViewHourSegment } from 'calendar-utils';
import { MEDICAL_ATTENTION } from '../../../../../../constants/descriptions';
import { AppointmentComponent } from '../../../../../../dialogs/appointment/appointment.component';

const AGENDA_PROGRAMADA_CLASS = 'bg-green';
const AGENDA_ESPONTANEA_CLASS = 'bg-blue';
const enum COLORES {
	PROGRAMADA = '#7FC681',
	ESPONTANEA = '#2687C5',
	SOBRETURNO = '#E3A063'
}

@Component({
	selector: 'app-agenda',
	templateUrl: './agenda.component.html',
	styleUrls: ['./agenda.component.scss']
})
export class AgendaComponent implements OnInit {

	newAgendaService: NewAgendaService;
	agenda: CompleteDiaryDto;
	viewDate: Date = new Date();
	loading = false;
	dayStartHour: number;
	dayEndHour: number;
	diaryOpeningHours: DiaryOpeningHoursDto[];

	constructor(
		private readonly appointmentsService: AppointmentsService,
		private readonly cdr: ChangeDetectorRef,
		private readonly dialog: MatDialog,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly diaryService: DiaryService,
		private readonly route: ActivatedRoute,
	) {
		this.newAgendaService = new NewAgendaService(this.dialog, this.cdr);
	}

	ngOnInit(): void {
		this.loading = true;
		this.route.paramMap.subscribe((params: ParamMap) => {
			const idAgenda = Number(params.get('idAgenda'));
			this.diaryService.get(idAgenda).subscribe(agenda => {
				this.setAgenda(agenda);
			});
		});
	}

	loadCalendar(renderEvent: CalendarWeekViewBeforeRenderEvent) {
		renderEvent.hourColumns.forEach((hourColumn) => {
			const openingHours: DiaryOpeningHoursDto[] = this._getOpeningHoursFor(hourColumn.date);
			if (openingHours.length) {
				hourColumn.hours.forEach((hour) => {
					hour.segments.forEach((segment) => {
						openingHours.forEach(openingHour => {
							const from: Moment = momentParseTime(openingHour.openingHours.from);
							const to: Moment = momentParseTime(openingHour.openingHours.to);
							if (isBetween(segment, from, to)) {
								segment.cssClass = openingHour.medicalAttentionTypeId === MEDICAL_ATTENTION.SPONTANEOUS_ID ?
									AGENDA_ESPONTANEA_CLASS : AGENDA_PROGRAMADA_CLASS;
							}
						});
					});
				});
			}
		});

		function isBetween(segment: WeekViewHourSegment, from: Moment, to: Moment) {
			return ((segment.date.getHours() > from.hours()) ||
				(segment.date.getHours() === from.hours() && segment.date.getMinutes() >= from.minutes()))
				&& ((segment.date.getHours() < to.hours()) ||
					(segment.date.getHours() === to.hours() && segment.date.getMinutes() < to.minutes()));
		}
	}

	private _getOpeningHoursFor(date: Date): DiaryOpeningHoursDto[] {
		const dateMoment = dateToMoment(date);
		const start = momentParseDate(this.agenda.startDate);
		const end = momentParseDate(this.agenda.endDate);

		if (dateMoment.isBetween(start, end, 'date', '[]')) {
			return this.diaryOpeningHours.filter(oh => oh.openingHours.dayWeekId === date.getDay());
		}
		return [];
	}

	public onClickedSegment(event) {
		if (this.getOpeningHoursId(event.date)) {

			const clickedDate: Moment = dateToMomentTimeZone(event.date);

			const dialogRef = this.dialog.open(NewAppointmentComponent, {
				disableClose: true,
				width: '35%',
				data: {
					date: clickedDate.format(DateFormat.API_DATE),
					diaryId: this.agenda.id,
					hour: clickedDate.format(DateFormat.HOUR_MINUTE_SECONDS),
					openingHoursId: this.getOpeningHoursId(event.date)
				}
			});

			dialogRef.afterClosed().subscribe(submitted => {
				if (submitted) {
					this.loadAppointments();
				}
			});
		}
	}

	setAgenda(agenda: CompleteDiaryDto): void {
		this.loading = true;
		delete this.dayEndHour;
		delete this.dayStartHour;
		this.agenda = agenda;
		this.viewDate = this._getViewDate();
		this.newAgendaService.setAppointmentDuration(this.agenda.appointmentDuration);

		this.diaryOpeningHoursService.getMany([this.agenda.id])
			.subscribe((openingHours: DiaryOpeningHoursDto[]) => {
				this.diaryOpeningHours = openingHours;
				this.setDayStartHourAndEndHour(openingHours);
				this.loading = false;
			});

		this.loadAppointments();
	}

	private loadAppointments() {
		this.appointmentsService.getList([this.agenda.id], this.agenda.startDate, this.agenda.endDate)
			.subscribe((appointments: AppointmentListDto[]) => {
				const appointmentsCalendarEvents: CalendarEvent[] = appointments
					.map(appointment => {
						const from = momentParseTime(appointment.hour).format(DateFormat.HOUR_MINUTE);
						const to = momentParseTime(from).add(this.agenda.appointmentDuration, 'minutes').format(DateFormat.HOUR_MINUTE);
						return toCalendarEvent(from, to, momentParseDate(appointment.date), appointment);
					});
				this.newAgendaService.setEvents(appointmentsCalendarEvents);
			});
	}

	private setDayStartHourAndEndHour(openingHours: DiaryOpeningHoursDto[]) {
		openingHours.forEach(oh => {
			const from = momentParseTime(oh.openingHours.from).hour();
			if (!this.dayStartHour || from < this.dayStartHour) {
				this.dayStartHour = (from > 0) ? from - 1 : from;
			}
			const to = momentParseTime(oh.openingHours.to).hour();
			if (!this.dayEndHour || to > this.dayEndHour) {
				this.dayEndHour = (to < 23) ? to + 1 : to;
			}
		});
	}

	/**
	 * returns a Date that defines which week is going to show in the header of the calendar
	 *
	 */
	private _getViewDate(): Date {
		const momentStartDate = momentParseDate(this.agenda.startDate);
		const momentEndDate = momentParseDate(this.agenda.endDate);
		const today = newMoment();

		if (today.isBetween(momentStartDate, momentEndDate)) {
			return new Date();
		}
		if (today.isBefore(momentStartDate)) {
			return momentStartDate.toDate();
		}
		return momentEndDate.toDate();
	}

	private getOpeningHoursId(date: Date): number {
		const openingHoursSelectedDay = this._getOpeningHoursFor(date);

		const selectedOpeningHour = openingHoursSelectedDay.find(oh => {
			const hourFrom = momentParseTime(oh.openingHours.from);
			const hourTo = momentParseTime(oh.openingHours.to);
			const selectedHour = dateToMomentTimeZone(date).format(DateFormat.HOUR_MINUTE_SECONDS);

			return momentParseTime(selectedHour).isBetween(hourFrom, hourTo, null, '[)');
		});

		return selectedOpeningHour?.openingHours.id;
	}

	viewAppointment({ event }: { event: CalendarEvent }): void {
		const appointmentDialogRef = this.dialog.open(AppointmentComponent, {
			data: event.meta,
		});

		appointmentDialogRef.afterClosed().subscribe(data => {
			if (data) {
				this.loadAppointments();
			}
		});
	}

}

function toCalendarEvent(from: string, to: string, date: Moment, appointment: AppointmentListDto): CalendarEvent {
	return {
		start: buildFullDate(from, date).toDate(),
		end: buildFullDate(to, date).toDate(),
		title: `${from} ${appointment.patient.person.lastName} ${appointment.patient.person.firstName}`,
		color: {
			primary: getColor(),
			secondary: getColor()
		},
		meta: {
			patient: {
				id: appointment.patient.id,
				fullName: appointment.patient.person.firstName + ' ' + appointment.patient.person.lastName,
				identificationNumber: appointment.patient.person.identificationNumber,
				phoneNumber: appointment.patient.person.phoneNumber,
			},
			appointmentId: appointment.id,
			date: buildFullDate(appointment.hour, momentParseDate(appointment.date)),
			medicalCoverage: {
				name: appointment.medicalCoverageName,
				affiliateNumber: appointment.medicalCoverageAffiliateNumber,
			},

		}
	};

	function getColor(): COLORES {
		if (appointment.overturn) {
			return COLORES.SOBRETURNO;
		}
		return appointment.medicalAttentionTypeId === MEDICAL_ATTENTION.SPONTANEOUS_ID ? COLORES.ESPONTANEA : COLORES.PROGRAMADA;
	}
}

