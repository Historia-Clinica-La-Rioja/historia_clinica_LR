import { Component, Input } from '@angular/core';
import { CompleteEquipmentDiaryDto } from '@api-rest/api-model';
import { CalendarView, CalendarWeekViewBeforeRenderEvent, DAYS_OF_WEEK } from 'angular-calendar';
import { momentParseDate } from '@core/utils/moment.utils';

import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subject } from 'rxjs';
import { DatePipeFormat } from '@core/utils/date.utils';
import * as moment from 'moment';
import { MINUTES_IN_HOUR } from '@turnos/constants/appointment';
import { EquipmentDiaryService } from '@api-rest/services/equipment-diary.service';
import { SearchEquipmentDiaryService } from '../../services/search-equipment-diary.service';
import { OpeningHoursDiaryService } from '../../services/opening-hours-diary.service';


@Component({
	selector: 'app-equipment-diary',
	templateUrl: './equipment-diary.component.html',
	styleUrls: ['./equipment-diary.component.scss'],
	providers: [SearchEquipmentDiaryService, OpeningHoursDiaryService]
})

export class EquipmentDiaryComponent {

	hourSegments: number;
	diary: CompleteEquipmentDiaryDto;

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
		private readonly searchEquipmentDiary: SearchEquipmentDiaryService,
		readonly openingHoursService: OpeningHoursDiaryService
	) { }

	loadCalendar(renderEvent: CalendarWeekViewBeforeRenderEvent) {
		if (this.diary) {
			this.openingHoursService.loadOpeningHoursOfCalendar(renderEvent, this.diary.startDate, this.diary.endDate);
		}
	}

	setAgenda(agenda: CompleteEquipmentDiaryDto) {
		this.diary = agenda;
		this.viewDate = this._getViewDate();
		this.hourSegments = MINUTES_IN_HOUR / agenda.appointmentDuration;
		this.openingHoursService.setDiaryOpeningHours(agenda.equipmentDiaryOpeningHours);
		this.openingHoursService.setDayStartHourAndEndHour();
	}

	goToDayViewOn(date: Date) {
		this.viewDate = date;
		this.view = this.calendarViewEnum.Day;
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

	private getAgenda(diaryId: number) {
		this.equipmentDiaryService.getBy(diaryId).subscribe(agenda => {
			this.setAgenda(agenda);
			this.searchEquipmentDiary.setAgendaSelected(agenda);
		}, _ => {
			this.snackBarService.showError('turnos.home.AGENDA_NOT_FOUND');
		});
	}
}
