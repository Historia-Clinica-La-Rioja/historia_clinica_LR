import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CompleteDiaryDto, DiaryOpeningHoursDto, MedicalCoverageDto} from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { CalendarView, CalendarWeekViewBeforeRenderEvent, DAYS_OF_WEEK } from 'angular-calendar';
import {
	buildFullDate,
	DateFormat,
	dateToMoment,
	dateToMomentTimeZone,
	momentParseDate,
	momentParseTime,
	newMoment
} from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DiaryService } from '@api-rest/services/diary.service';
import { Moment } from 'moment';
import { NewAppointmentComponent } from '../../../../../../dialogs/new-appointment/new-appointment.component';
import { CalendarEvent, WeekViewHourSegment } from 'calendar-utils';
import { MEDICAL_ATTENTION } from '../../../../../../constants/descriptions';
import { AppointmentComponent } from '../../../../../../dialogs/appointment/appointment.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MINUTES_IN_HOUR } from '../../../../../../constants/appointment';
import { AppointmentsFacadeService } from 'src/app/modules/turnos/services/appointments-facade.service';
import { map, take } from 'rxjs/operators';
import { forkJoin, Observable } from 'rxjs';
import { PermissionsService } from '@core/services/permissions.service';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';

const ASIGNABLE_CLASS = 'cursor-pointer';
const AGENDA_PROGRAMADA_CLASS = 'bg-green';
const AGENDA_ESPONTANEA_CLASS = 'bg-blue';

@Component({
	selector: 'app-agenda',
	templateUrl: './agenda.component.html',
	styleUrls: ['./agenda.component.scss']
})
export class AgendaComponent implements OnInit {

	readonly calendarViewEnum = CalendarView;
	view: CalendarView = CalendarView.Month;
	readonly MONDAY = DAYS_OF_WEEK.MONDAY;

	hourSegments: number;
	agenda: CompleteDiaryDto;

	viewDate: Date = new Date();
	loading = false;
	dayStartHour: number;
	dayEndHour: number;
	diaryOpeningHours: DiaryOpeningHoursDto[];

	enableAppointmentScheduling: boolean = true;

	constructor(
		private readonly cdr: ChangeDetectorRef,
		private readonly dialog: MatDialog,
		private readonly diaryService: DiaryService,
		private readonly route: ActivatedRoute,
		private snackBarService: SnackBarService,
		private readonly permissionsService: PermissionsService,
		public readonly appointmentFacade: AppointmentsFacadeService,
		private readonly healthInsuranceService: HealthInsuranceService,
	) {
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

	onClickedSegment(event) {
		if (this.getOpeningHoursId(event.date) && this.enableAppointmentScheduling) {
			const clickedDate: Moment = dateToMomentTimeZone(event.date);
			const openingHourId: number = this.getOpeningHoursId(event.date);
			const diaryOpeningHourDto: DiaryOpeningHoursDto =
				this.diaryOpeningHours.find(diaryOpeningHour => diaryOpeningHour.openingHours.id === openingHourId);

			forkJoin([
				this.existsAppointmentAt(event.date).pipe(take(1)),
				this.allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto, clickedDate).pipe(take(1))
			]).subscribe(([addingOverturn, numberOfOverturnsAssigned]) => {
				if (addingOverturn && (numberOfOverturnsAssigned === diaryOpeningHourDto.overturnCount)) {
					if (diaryOpeningHourDto.medicalAttentionTypeId !== MEDICAL_ATTENTION.SPONTANEOUS_ID) {
						this.snackBarService.showError('turnos.overturns.messages.ERROR');
					}
				} else {
					this.dialog.open(NewAppointmentComponent, {
						width: '28%',
						data: {
							date: clickedDate.format(DateFormat.API_DATE),
							diaryId: this.agenda.id,
							hour: clickedDate.format(DateFormat.HOUR_MINUTE_SECONDS),
							openingHoursId: openingHourId,
							overturnMode: addingOverturn
						}
					});
				}
			});
		}
	}

	viewAppointment(event: CalendarEvent): void {

		if (event.meta.rnos) {
			this.healthInsuranceService.get(event.meta.rnos)
				.subscribe((medicalCoverageDto: MedicalCoverageDto) => {
					event.meta.healthInsurance = medicalCoverageDto;
					this.dialog.open(AppointmentComponent, {
						data: event.meta
					});
				});
		} else {
			this.dialog.open(AppointmentComponent, {
				data: event.meta,
			});
		}
	}

	setAgenda(agenda: CompleteDiaryDto): void {
		this.loading = true;
		delete this.dayEndHour;
		delete this.dayStartHour;
		this.agenda = agenda;
		this.setEnableAppointmentScheduling();
		this.viewDate = this._getViewDate();
		this.hourSegments = MINUTES_IN_HOUR / agenda.appointmentDuration;
		this.appointmentFacade.setValues(agenda.id, agenda.startDate, agenda.endDate, agenda.appointmentDuration);

		this.diaryOpeningHours = agenda.diaryOpeningHours;
		this.setDayStartHourAndEndHour(agenda.diaryOpeningHours);
		this.loading = false;

	}

	goToDayViewOn(date: Date) {
		this.viewDate = date;
		this.view = this.calendarViewEnum.Day;
	}

	private setDayStartHourAndEndHour(openingHours: DiaryOpeningHoursDto[]) {
		openingHours.forEach(oh => {
			const from = momentParseTime(oh.openingHours.from).hour();
			if (this.dayStartHour === undefined || from < this.dayStartHour) {
				this.dayStartHour = (from > 0) ? from - 1 : from;
			}
			const to = momentParseTime(oh.openingHours.to).hour();
			const toMinutes = momentParseTime(oh.openingHours.to).minutes();
			if (this.dayEndHour === undefined || to >= this.dayEndHour) {
				this.dayEndHour = (toMinutes > 0) ? to : to - 1;
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

	private existsAppointmentAt(date: Date): Observable<boolean> {
		return this.appointmentFacade.getAppointments().pipe(
			map(array => {
				return array.filter(appointment => appointment.start.getTime() === date.getTime()).length > 0;
			})
		);
	}

	private allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto: DiaryOpeningHoursDto, clickedDate: Moment): Observable<number> {

		const openingHourStart = buildFullDate(diaryOpeningHourDto.openingHours.from, clickedDate);
		const openingHourEnd = buildFullDate(diaryOpeningHourDto.openingHours.to, clickedDate);

		return this.appointmentFacade.getAppointments().pipe(
			map((array: CalendarEvent[]) =>
				array.filter(event =>
					event.meta.overturn && dateToMoment(event.start).isBetween(openingHourStart, openingHourEnd, null, '[)')
				).length
			)
		);

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

	private setEnableAppointmentScheduling(): void {
		if (this.agenda.professionalAssignShift) {
			this.enableAppointmentScheduling = true;
		} else {
			this.permissionsService.hasContextAssignments$([ERole.ADMINISTRATIVO, ERole.ADMINISTRADOR_AGENDA])
				.subscribe(hasAdministrativeRole => {
					this.enableAppointmentScheduling = hasAdministrativeRole;
				});
		}
	}

	private getOpeningHoursCssClass(openingHour: DiaryOpeningHoursDto): string {
		if (openingHour.medicalAttentionTypeId === MEDICAL_ATTENTION.SPONTANEOUS_ID) {
			return this.enableAppointmentScheduling ? `${AGENDA_ESPONTANEA_CLASS} ${ASIGNABLE_CLASS}` : AGENDA_ESPONTANEA_CLASS;
		} else {
			return this.enableAppointmentScheduling ? `${AGENDA_PROGRAMADA_CLASS} ${ASIGNABLE_CLASS}` : AGENDA_PROGRAMADA_CLASS;
		}
	}

}

