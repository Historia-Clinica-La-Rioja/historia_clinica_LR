import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CompleteDiaryDto, DiaryOpeningHoursDto } from '@api-rest/api-model';
import { CalendarEvent } from 'angular-calendar';
import { momentParseDate, momentParseTime, newMoment } from '@core/utils/moment.utils';
import { NewAgendaService } from '../../../../../../services/new-agenda.service';
import { MatDialog } from '@angular/material/dialog';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DiaryService } from '@api-rest/services/diary.service';
import { Moment } from 'moment';

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

	constructor(
		private readonly cdr: ChangeDetectorRef,
		private readonly dialog: MatDialog,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly route: ActivatedRoute,
		private readonly diaryService: DiaryService
	) {
		this.newAgendaService = new NewAgendaService(this.dialog, this.cdr);
	}

	ngOnInit(): void {
		this.loading = true;
		this.route.paramMap.subscribe((params: ParamMap) => {
				const idAgenda = Number(params.get('idAgenda'));
				this.diaryService.get(idAgenda).subscribe(agenda => this.setAgenda(agenda));
			});
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
				const events: CalendarEvent[] = [];
				let startDate = momentParseDate(this.agenda.startDate);
				const endDate = momentParseDate(this.agenda.endDate);


				while (startDate.isBefore(endDate)) {
					const dayopeningHours = openingHours
						.find(oh => oh.openingHours.dayWeekId === startDate.day());
					if (dayopeningHours) {
						events.push(toCalendarEvent(dayopeningHours, startDate));
					}
					startDate = startDate.add(1, 'days');
				}

				openingHours.forEach(oh => {
					const from = momentParseTime(oh.openingHours.from).hour();
					if (!this.dayStartHour || from < this.dayStartHour) {
						this.dayStartHour = from;
					}
					const to = momentParseTime(oh.openingHours.to).hour();
					if (!this.dayEndHour || to > this.dayStartHour) {
						this.dayEndHour = to;
					}
				});
				this.newAgendaService.setEvents(events);
				this.loading = false;
			});

		function toCalendarEvent(diaryOpeningHours: DiaryOpeningHoursDto, date: Moment) {
			return {
				start: buildFullDate(diaryOpeningHours.openingHours.from, date),
				end: buildFullDate(diaryOpeningHours.openingHours.to, date),
				title: ``,
				color: {
					primary: '#C8C8C8',
					secondary: '#C8C8C8'
				}
			};
		}

		function buildFullDate(time: string, date: Moment): Date {
			const timeMoment: Moment = momentParseTime(time);
			date.set({
				hour: timeMoment.hour(),
				minute: timeMoment.minute()
			});
			return date.toDate();
		}
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

}
