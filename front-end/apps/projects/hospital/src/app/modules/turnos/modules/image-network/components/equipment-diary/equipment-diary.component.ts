import { Component, Input, OnInit } from '@angular/core';
import { CompleteEquipmentDiaryDto, DiaryOpeningHoursDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { CalendarEvent, CalendarView, CalendarWeekViewBeforeRenderEvent, DAYS_OF_WEEK } from 'angular-calendar';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subject } from 'rxjs';
import { DatePipeFormat } from '@core/utils/date.utils';
import * as moment from 'moment';
import { MINUTES_IN_HOUR } from '@turnos/constants/appointment';
import { MEDICAL_ATTENTION } from '@turnos/constants/descriptions';
import { EquipmentDiaryService } from '@api-rest/services/equipment-diary.service';
import { SearchEquipmentDiaryService } from '../../services/search-equipment-diary.service';
import { PermissionsService } from '@core/services/permissions.service';
import { MatDialog } from '@angular/material/dialog';
import { OpeningHoursDiaryService } from '../../services/opening-hours-diary.service';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { EquipmentAppointmentsFacadeService } from '../../services/equipment-appointments-facade.service';
import { DateFormat, buildFullDate, dateToMoment, dateToMomentTimeZone, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { endOfMonth, endOfWeek, startOfMonth, startOfWeek } from 'date-fns';

@Component({
	selector: 'app-equipment-diary',
	templateUrl: './equipment-diary.component.html',
	styleUrls: ['./equipment-diary.component.scss'],
	providers: [SearchEquipmentDiaryService, OpeningHoursDiaryService, EquipmentAppointmentsFacadeService]
})
export class EquipmentDiaryComponent implements OnInit {

	hourSegments: number;
	diary: CompleteEquipmentDiaryDto;

	view: CalendarView = CalendarView.Week;
	viewDate: Date = new Date();

	refreshCalendar = new Subject<void>();

	hasRoleToCreate = false;
	appointments: CalendarEvent[] = [];
	holidays: CalendarEvent[] = [];

	startDate: string;
	endDate: string;

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
		readonly openingHoursService: OpeningHoursDiaryService,
		private readonly permissionService: PermissionsService,
		private readonly dialog: MatDialog,
		private readonly equipmentAppointmentsFacade: EquipmentAppointmentsFacadeService,
	) { }

	ngOnInit() {
		this.permissionService.hasContextAssignments$([ERole.ADMINISTRATIVO_RED_DE_IMAGENES]).subscribe(hasRole => this.hasRoleToCreate = hasRole);

		this.equipmentAppointmentsFacade.getAppointments().subscribe(appointments => {
			if (appointments) {
				this.appointments = appointments;
			}
		})
		this.equipmentAppointmentsFacade.getHolidays().subscribe(holidays => this.holidays = holidays);
	}

	loadCalendar(renderEvent: CalendarWeekViewBeforeRenderEvent) {
		if (this.diary) {
			this.openingHoursService.loadOpeningHoursOfCalendar(renderEvent, this.diary.startDate, this.diary.endDate);
		}
	}

	setAgenda(agenda: CompleteEquipmentDiaryDto) {
		this.diary = agenda;
		this.viewDate = this._getViewDate();
		this.setDateRange(this.viewDate);
		this.hourSegments = MINUTES_IN_HOUR / agenda.appointmentDuration;
		this.openingHoursService.setDiaryOpeningHours(agenda.equipmentDiaryOpeningHours);
		this.openingHoursService.setDayStartHourAndEndHour();
		this.equipmentAppointmentsFacade.setValues(agenda.id, agenda.appointmentDuration, this.startDate, this.endDate);
	}

	goToDayViewOn(date: Date) {
		this.viewDate = date;
		this.view = this.calendarViewEnum.Day;
		this.setDateRange(date);
	}

	onClickedSegment(event) {
		const openingHourId = this.openingHoursService.getOpeningHoursId(this.diary.startDate, this.diary.endDate, event.date);
		if (openingHourId) {
			const clickedDate: Moment = dateToMomentTimeZone(event.date);
			const diaryOpeningHourDto: DiaryOpeningHoursDto = this.openingHoursService.getEquipmentDiaryOpeningHours().find(diaryOpeningHour => diaryOpeningHour.openingHours.id === openingHourId);

			const busySlot = this.getAppointmentAt(event.date)
			const numberOfOverturnsAssigned = this.allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto, clickedDate)

			const addingOverturn = !!busySlot;

			if (this.verifyOverturns(addingOverturn, numberOfOverturnsAssigned, diaryOpeningHourDto)) {
				return;
			}

			this.openNewAppointmentDialog(clickedDate, openingHourId, addingOverturn);		
		}
	}

	changeViewDate(date: Date) {
		if (this.view !== CalendarView.Month) {
			this.setDateRange(date);
			this.equipmentAppointmentsFacade.setValues(this.diary.id, this.diary.appointmentDuration, this.startDate, this.endDate);
		}
	}

	private getAppointmentAt(date: Date): CalendarEvent {
		return this.appointments.find(appointment => appointment.start.getTime() === date.getTime());
	}

	private allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto: DiaryOpeningHoursDto, clickedDate: Moment): number {
		const openingHourStart = buildFullDate(diaryOpeningHourDto.openingHours.from, clickedDate);
		const openingHourEnd = buildFullDate(diaryOpeningHourDto.openingHours.to, clickedDate);
		return this.appointments.filter(event =>
			event.meta?.overturn && dateToMoment(event.start).isBetween(openingHourStart, openingHourEnd, null, '[)')
		).length
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

	private openNewAppointmentDialog(clickedDate: Moment, openingHourId: number, addingOverturn: boolean) {
		const dialogRef = this.dialog.open(NewAppointmentComponent, {
			width: '35%',
			data: {
				date: clickedDate.format(DateFormat.API_DATE),
				diaryId: this.diary.id,
				hour: clickedDate.format(DateFormat.HOUR_MINUTE_SECONDS),
				openingHoursId: openingHourId,
				overturnMode: addingOverturn,
				patientId: null,
				isEquipmentAppointment: true
			}
		});
		dialogRef.afterClosed().subscribe(() => this.equipmentAppointmentsFacade.loadAppointments());
	}

	private setDateRange(date: Date) {
		if (CalendarView.Day === this.view) {
			const d = moment(date);
			this.startDate = momentFormat(d, DateFormat.API_DATE);
			this.endDate = momentFormat(d, DateFormat.API_DATE);
			return;
		}
		if (CalendarView.Month === this.view) {
			const from = startOfMonth(date);
			const to = endOfMonth(date);
			this.startDate = momentFormat(moment(from), DateFormat.API_DATE);
			this.endDate = momentFormat(moment(to), DateFormat.API_DATE);
			return;
		}
		const start = startOfWeek(date, { weekStartsOn: 1 });
		this.startDate = momentFormat(moment(start), DateFormat.API_DATE);

		const end = endOfWeek(date, { weekStartsOn: 1 });
		this.endDate = momentFormat(moment(end), DateFormat.API_DATE);
	}

	private verifyOverturns(addingOverturn: boolean, numberOfOverturnsAssigned: number, diaryOpeningHourDto: DiaryOpeningHoursDto) {
		if (addingOverturn && (numberOfOverturnsAssigned === diaryOpeningHourDto.overturnCount)) {
			if (diaryOpeningHourDto.medicalAttentionTypeId !== MEDICAL_ATTENTION.SPONTANEOUS_ID) {
				this.snackBarService.showError('turnos.overturns.messages.ERROR');
				return true;
			}
		}
		else
			return false;
	}

}
