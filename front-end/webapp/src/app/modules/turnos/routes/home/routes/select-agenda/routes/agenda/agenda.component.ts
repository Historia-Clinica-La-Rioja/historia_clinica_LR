import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CompleteDiaryDto, DiaryOpeningHoursDto } from '@api-rest/api-model';
import { CalendarWeekViewBeforeRenderEvent } from 'angular-calendar';
import { momentParseDate, momentParseTime, newMoment } from '@core/utils/moment.utils';
import { NewAgendaService } from '../../../../../../services/new-agenda.service';
import { MatDialog } from '@angular/material/dialog';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DiaryService } from '@api-rest/services/diary.service';
import { Moment } from 'moment';
import { NewAppointmentComponent } from './../../../../../../dialogs/new-appointment/new-appointment.component';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { WeekViewHourSegment } from 'calendar-utils';

const AGENDA_PROGRAMADA_CLASS = 'bg-green';
const AGENDA_ESPONTANEA_CLASS = 'bg-blue';


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
			const openingHours: DiaryOpeningHoursDto[] = getOpeningHoursFor(hourColumn.date, this.diaryOpeningHours);
			if (openingHours.length) {
				hourColumn.hours.forEach((hour) => {
					hour.segments.forEach((segment) => {
						openingHours.forEach(openingHour => {
							const from: Moment = momentParseTime(openingHour.openingHours.from);
							const to: Moment = momentParseTime(openingHour.openingHours.to);

							if (isBetween(segment, from, to)) {
								segment.cssClass = openingHour.medicalAttentionTypeId === 1 ? AGENDA_PROGRAMADA_CLASS : AGENDA_ESPONTANEA_CLASS;
							}
						});
					});
				});
			}
		});

		function getOpeningHoursFor(date: Date, openingHours: DiaryOpeningHoursDto[]): DiaryOpeningHoursDto[] {
			return openingHours.filter(oh => oh.openingHours.dayWeekId === date.getDay());
		}

		function isBetween(segment: WeekViewHourSegment, from: Moment, to: Moment) {
			return ((segment.date.getHours() > from.hours()) ||
				(segment.date.getHours() === from.hours() && segment.date.getMinutes() >= from.minutes()))
				&& ((segment.date.getHours() < to.hours()) ||
					(segment.date.getHours() === to.hours() && segment.date.getMinutes() < to.minutes()));
		}
	}

	public onClickedSegment(event) {
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

	openDialog() {
		const dialogRef = this.dialog.open(NewAppointmentComponent, {
			disableClose: true,
			width: '35%',
			data: {
				date: null,
				diaryId: null,
				hour: null,
				openingHoursId: null
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {

			}
		}
		);
	}

}
