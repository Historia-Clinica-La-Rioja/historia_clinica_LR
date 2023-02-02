import { Component, Input } from '@angular/core';
import { CompleteEquipmentDiaryDto, DiaryOpeningHoursDto } from '@api-rest/api-model';
import { CalendarView, CalendarWeekViewBeforeRenderEvent, DAYS_OF_WEEK } from 'angular-calendar';
import { dateToMoment, momentParseDate, momentParseTime } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { WeekViewHourSegment } from 'calendar-utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subject } from 'rxjs';
import { DatePipeFormat } from '@core/utils/date.utils';
import * as moment from 'moment';
import { MINUTES_IN_HOUR } from '@turnos/constants/appointment';
import { MEDICAL_ATTENTION } from '@turnos/constants/descriptions';
import { EquipmentDiaryService } from '@api-rest/services/equipment-diary.service';
import { SearchEquipmentDiaryService } from '../../services/search-equipment-diary.service';

const ASSIGNABLE_CLASS = 'cursor-pointer';
const DIARY_SCHEDULED_CLASS = 'bg-green';
const DIARY_SPONTANEOUS_CLASS = 'bg-yellow';

@Component({
	selector: 'app-equipment-diary',
	templateUrl: './equipment-diary.component.html',
	styleUrls: ['./equipment-diary.component.scss'],
	providers: [SearchEquipmentDiaryService]
})

export class EquipmentDiaryComponent {

	hourSegments: number;
	diary: CompleteEquipmentDiaryDto;

	dayStartHour: number;
	dayEndHour: number;
	diaryOpeningHours: DiaryOpeningHoursDto[] = [];
	view: CalendarView = CalendarView.Week;
	viewDate: Date = new Date();

	refreshCalendar = new Subject<void>();

	readonly calendarViewEnum = CalendarView;
	readonly MONDAY = DAYS_OF_WEEK.MONDAY;
	readonly dateFormats = DatePipeFormat;

	@Input()
	set diaryId(diaryId: number) {
		if (diaryId) {
			this.getAgenda(diaryId);
		}
	}

	constructor(
		private snackBarService: SnackBarService,
		private readonly equipmentDiaryService: EquipmentDiaryService,
		private readonly searchEquipmentDiary: SearchEquipmentDiaryService
	) { }

	loadCalendar(renderEvent: CalendarWeekViewBeforeRenderEvent) {
		if (this.diary)
			renderEvent.hourColumns.forEach((hourColumn) => {
				const openingHours: DiaryOpeningHoursDto[] = this._getOpeningHoursFor(hourColumn.date);
				if (openingHours.length) {
					hourColumn.hours.forEach((hour) => {
						hour.segments.forEach((segment) => {
							openingHours.forEach(openingHour => {
								const from: Moment = momentParseTime(openingHour.openingHours.from);
								const to: Moment = momentParseTime(openingHour.openingHours.to);
								if (isBetween(segment, from, to)) {
									segment.cssClass = this.getOpeningHoursCssClass(openingHour);
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

	setAgenda(agenda: CompleteEquipmentDiaryDto) {
		this.diary = agenda;
		this.viewDate = this._getViewDate();
		this.hourSegments = MINUTES_IN_HOUR / agenda.appointmentDuration;
		this.diaryOpeningHours = agenda.equipmentDiaryOpeningHours;
		this.setDayStartHourAndEndHour(agenda.equipmentDiaryOpeningHours);
	}

	goToDayViewOn(date: Date) {
		this.viewDate = date;
		this.view = this.calendarViewEnum.Day;
	}

	private setDayStartHourAndEndHour(openingHours: DiaryOpeningHoursDto[]) {
		openingHours.forEach(oh => {
			const from = momentParseTime(oh.openingHours.from).hour();
			if (this.dayStartHour === undefined || from <= this.dayStartHour) {
				this.dayStartHour = (from > 0) ? from - 1 : from;
			}
			const to = momentParseTime(oh.openingHours.to).hour();
			if (this.dayEndHour === undefined || to >= this.dayEndHour) {
				this.dayEndHour = (to < 23) ? to + 1 : to;
			}
		});
	}

	private _getViewDate(): Date {
		const momentStartDate = momentParseDate(this.diary.startDate);
		const momentEndDate = momentParseDate(this.diary.endDate);
		const lastSelectedDate = moment(this.viewDate);

		if (lastSelectedDate.isBetween(momentStartDate, momentEndDate)) {
			return this.viewDate;
		}
		if (lastSelectedDate.isSameOrBefore(momentStartDate)) {
			return momentStartDate.toDate();
		}
		return momentEndDate.toDate();
	}

	private _getOpeningHoursFor(date: Date): DiaryOpeningHoursDto[] {
		const dateMoment = dateToMoment(date);
		const start = momentParseDate(this.diary.startDate);
		const end = momentParseDate(this.diary.endDate);

		if (dateMoment.isBetween(start, end, 'date', '[]')) {
			return this.diaryOpeningHours.filter(oh => oh.openingHours.dayWeekId === date.getDay());
		}
		return [];
	}

	private getOpeningHoursCssClass(openingHour: DiaryOpeningHoursDto): string {
		if (openingHour.medicalAttentionTypeId === MEDICAL_ATTENTION.SPONTANEOUS_ID) {
			return `${DIARY_SPONTANEOUS_CLASS} ${ASSIGNABLE_CLASS}`;
		} else {
			return `${DIARY_SCHEDULED_CLASS} ${ASSIGNABLE_CLASS}`;
		}
	}

	private getAgenda(diaryId: number) {
		this.equipmentDiaryService.getBy(diaryId).subscribe(agenda => {
			this.setAgenda(agenda);
			this.searchEquipmentDiary.setAgendaSelected(agenda);
		}, _ => {
			this.snackBarService.showError('turnos.home.AGENDA_NOT_FOUND');
		});
	}
}
