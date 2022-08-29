import { SnackBarService } from './../../presentation/services/snack-bar.service';
import { ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { fromEvent, Observable } from 'rxjs';
import { finalize, map, takeUntil } from 'rxjs/operators';
import { CalendarEvent, DAYS_OF_WEEK } from 'angular-calendar';
import { WeekViewHourSegment } from 'calendar-utils';
import { addDays, addMinutes, endOfWeek } from 'date-fns';
import { Moment } from 'moment';

import { buildFullDate, currentWeek, DateFormat, dateToMoment, momentFormat } from '@core/utils/moment.utils';
import { REMOVEATTENTION } from '@core/constants/validation-constants';
import { DiaryOpeningHoursDto, OccupationDto, TimeRangeDto } from '@api-rest/api-model';

import { MEDICAL_ATTENTION } from '../constants/descriptions';
import { NewAttentionComponent } from '../dialogs/new-attention/new-attention.component';
import { getDayHoursIntervalsByMinuteValue } from '@core/utils/date.utils';

function floorToNearest(amount: number, precision: number) {
	return Math.floor(amount / precision) * precision;
}

function ceilToNearest(amount: number, precision: number) {
	return Math.ceil(amount / precision) * precision;
}

const colors: any = {
	blue: {
		primary: '#FFF5E0',
		secondary: '#FFF5E0',
	},
	green: {
		primary: '#D6FBD8',
		secondary: '#D6FBD8',
	},
};

export class AgendaHorarioService {

	private diaryOpeningHours: CalendarEvent[] = [];
	private occupiedOpeningHours: CalendarEvent[] = [];

	private mappedCurrentWeek: Moment[] = [];

	constructor(
		private readonly dialog: MatDialog,
		private readonly cdr: ChangeDetectorRef,
		private readonly viewDate: Date,
		private readonly weekStartsOn: DAYS_OF_WEEK,
		private readonly snackBarService: SnackBarService
	) {
		currentWeek().forEach(day => {
			this.mappedCurrentWeek[day.day()] = day;
		});
	}

	private appointmentDuration: number;

	getMedicalAttentionTypeText(medicalAttentionTypeId: number): string {
		const medicalAttentionType = medicalAttentionTypeId === 2 ? 'Espontánea' : 'Programada';
		return `<strong>Atención ${medicalAttentionType} </strong> <br>`;
	}

	getOverturnsText(overturnCount: number): string {
		return overturnCount > 0 ? '<span>Atiende sobreturnos</span>' : '<span>No atiende sobreturnos</span>';
	}

	startDragToCreate(segment: WeekViewHourSegment, segmentElement: HTMLElement): void {
		const dragToSelectEvent: CalendarEvent = {
			id: this.diaryOpeningHours.length,
			title: '',
			start: segment.date,
			meta: {
				tmpEvent: true,
			},
		};

		this.diaryOpeningHours = [...this.diaryOpeningHours, dragToSelectEvent];

		fromEvent<MouseEvent>(document, 'mousemove')
			.pipe(
				finalize(() => {
					if (dragToSelectEvent.end) {
						this.openConfirmDialogForEvent(dragToSelectEvent);
					} else {
						this.removeTempEvent(dragToSelectEvent);
						this.refresh();
					}
				}),
				takeUntil(fromEvent(document, 'mouseup'))
			)
			.subscribe((mouseMoveEvent: MouseEvent) => {
				this.limitNewEventsEnd(dragToSelectEvent, mouseMoveEvent, segment, segmentElement);
				this.refresh();
			});
	}

	openConfirmDialogForEvent(event: CalendarEvent): void {
		const possibleScheduleHours = getDayHoursIntervalsByMinuteValue(event.start, this.appointmentDuration);
		const dialogRef = this.dialog.open(NewAttentionComponent,
			{
				data: {
					start: possibleScheduleHours.find(date => date.getTime() === event.start.getTime()),
					end: possibleScheduleHours.find(date => date.getTime() === event.end.getTime()),
					overturnCount: event.meta.overturnCount,
					medicalAttentionTypeId: event.meta.medicalAttentionType?.id,
					possibleScheduleHours,
					availableForBooking: event.meta.availableForBooking,
				}
			});
		dialogRef.afterClosed().subscribe(dialogInfo => {
			if (!dialogInfo) {
				if (event.meta?.tmpEvent) {
					this.removeTempEvent(event);
				}
			} else {
				event.start = dialogInfo.startingHour;
				event.end = dialogInfo.endingHour;
				if (this.thereIsValidTurnAvailability(event))
					this.setNewEvent(event, dialogInfo);
				else {
					this.snackBarService.showError('turnos.agenda-setup.messages.TURN_ERROR');
					this.removeTempEvent(event);
				}
			}
			this.refresh();
		});
	}

	private thereIsValidTurnAvailability(event: CalendarEvent): boolean {
		return this.occupiedOpeningHours
			.filter(occupiedTurn => event.start.getDay() === occupiedTurn.start.getDay())
			.every(occupiedTurn => this.compareTurnHourThreshold(event, occupiedTurn));
	}

	private compareTurnHourThreshold(event, occupiedTurn: CalendarEvent): boolean {
		const eventStartHour = event.start.getTime();
		const eventEndHour = event.end.getTime();
		const occupiedTurnStartHour = occupiedTurn.start.getTime();
		const occupiedTurnEndHour = occupiedTurn.end.getTime();

		return (eventStartHour >= occupiedTurnEndHour ||
			(eventStartHour < occupiedTurnStartHour && eventEndHour <= occupiedTurnStartHour));
	}

	openEditDialogForEvent(event: CalendarEvent): void {
		const possibleScheduleHours = getDayHoursIntervalsByMinuteValue(event.start, this.appointmentDuration);
		if (event.meta) {
			const dialogRef = this.dialog.open(NewAttentionComponent,
				{
					data: {
						start: possibleScheduleHours.find(date => date.getTime() === event.start.getTime()),
						end: possibleScheduleHours.find(date => date.getTime() === event.end.getTime()),
						overturnCount: event.meta.overturnCount,
						medicalAttentionTypeId: event.meta.medicalAttentionType?.id,
						isEdit: true,
						possibleScheduleHours,
						availableForBooking: event.meta.availableForBooking
					}
				});
			dialogRef.afterClosed().subscribe(dialogInfo => {
				if (!dialogInfo) {
					if (event.meta?.tmpEvent) {
						this.removeTempEvent(event);
					}
				} else {
					if (dialogInfo === REMOVEATTENTION)
						this.setNewEvent(event, dialogInfo);
					else {
						event.start = dialogInfo.startingHour;
						event.end = dialogInfo.endingHour;
						if (this.thereIsValidTurnAvailability(event))
							this.setNewEvent(event, dialogInfo);
						else {
							this.snackBarService.showError('turnos.agenda-setup.messages.TURN_ERROR');
							this.removeTempEvent(event);
						}
					}
				}
				this.refresh();
			});
		}
	}

	getMedicalAttentionColor(medicalAttentionTypeId: number): any {
		return medicalAttentionTypeId === MEDICAL_ATTENTION.SPONTANEOUS_ID ? colors.blue : colors.green;
	}

	setAppointmentDuration(duration: number) {
		this.appointmentDuration = duration;
		this.diaryOpeningHours = [];
		this.refresh();
	}

	getAppointmentDuration(): number {
		return this.appointmentDuration;
	}

	getEvents(): CalendarEvent[] {
		return this.diaryOpeningHours.concat(this.occupiedOpeningHours);
	}

	setDiaryOpeningHours(diaryOpeningHours: DiaryOpeningHoursDto[]): void {
		const events: CalendarEvent[] = diaryOpeningHours.map((diaryOpeningHour: DiaryOpeningHoursDto) =>
			this.toEditableCalendarEvents(diaryOpeningHour));
		this.diaryOpeningHours = events;
		this.refresh();
	}

	setWeeklyDoctorsOfficeOcupation(occupations$: Observable<OccupationDto[]>): void {
		occupations$.pipe(
			map(occupations => this.occupationsToCalendarEvents(occupations))
		).subscribe((doctorsOfficeEvents: CalendarEvent[]) => {
			this.occupiedOpeningHours = doctorsOfficeEvents;
			this.refresh();
		});
	}

	getDiaryOpeningHours(): DiaryOpeningHoursDto[] {
		return this.diaryOpeningHours.map(event => toDiaryOpeningHoursDto(event));

		function toDiaryOpeningHoursDto(event: CalendarEvent): DiaryOpeningHoursDto {
			return {
				openingHours: {
					dayWeekId: event.start.getDay(),
					from: momentFormat(dateToMoment(event.start), DateFormat.HOUR_MINUTE_SECONDS),
					to: momentFormat(dateToMoment(event.end), DateFormat.HOUR_MINUTE_SECONDS),
				},
				medicalAttentionTypeId: event.meta.medicalAttentionType.id,
				overturnCount: event.meta.overturnCount,
				externalAppointmentsAllowed: event.meta.availableForBooking
			};
		}
	}

	eventClicked({ event }: { event: CalendarEvent }): void {
		this.openEditDialogForEvent(event);
	}

	private occupationsToCalendarEvents(occupations: OccupationDto[]): CalendarEvent[] {
		let doctorsOfficeEvents: CalendarEvent[] = [];
		occupations.forEach(ocupation => {
			const events: CalendarEvent[] = ocupation.timeRanges
				.map((timeRange: TimeRangeDto) => {
					return {
						start: this.getFullDate(ocupation.id, timeRange.from),
						end: this.getFullDate(ocupation.id, timeRange.to),
						title: 'Horario ocupado',
						color: {
							primary: '#C8C8C8',
							secondary: '#C8C8C8'
						}
					};
				});
			doctorsOfficeEvents = doctorsOfficeEvents.concat(events);
		});
		return doctorsOfficeEvents;
	}

	private toEditableCalendarEvents(diaryOpeningHour: DiaryOpeningHoursDto): CalendarEvent {
		return {
			start: this.getFullDate(diaryOpeningHour.openingHours.dayWeekId, diaryOpeningHour.openingHours.from),
			end: this.getFullDate(diaryOpeningHour.openingHours.dayWeekId, diaryOpeningHour.openingHours.to),
			title: this.getMedicalAttentionTypeText(diaryOpeningHour.medicalAttentionTypeId)
				+ this.getOverturnsText(diaryOpeningHour.overturnCount),
			color: this.getMedicalAttentionColor(diaryOpeningHour.medicalAttentionTypeId),
			meta: {
				medicalAttentionType: { id: diaryOpeningHour.medicalAttentionTypeId },
				overturnCount: diaryOpeningHour.overturnCount,
				availableForBooking: diaryOpeningHour.externalAppointmentsAllowed,
			}
		};
	}

	private getFullDate(dayNumber: number, time: string): Date {
		return buildFullDate(time, this.mappedCurrentWeek[dayNumber]).toDate();
	}

	private limitNewEventsEnd(dragToSelectEvent: CalendarEvent, mouseMoveEvent: MouseEvent, segment: WeekViewHourSegment, segmentElement: HTMLElement): void {

		const segmentPosition = segmentElement.getBoundingClientRect();
		const minutesDiff = ceilToNearest(mouseMoveEvent.clientY - segmentPosition.top, this.appointmentDuration);
		const daysDiff = floorToNearest(mouseMoveEvent.clientX - segmentPosition.left, segmentPosition.width) / segmentPosition.width;
		if (daysDiff > 0) {
			return;
		}
		const newEnd = addDays(addMinutes(segment.date, minutesDiff), daysDiff);
		const endOfView = endOfWeek(this.viewDate, { weekStartsOn: this.weekStartsOn });
		if (newEnd.getHours() >= 0 && newEnd.getDay() !== dragToSelectEvent.start.getDay()) {
			newEnd.setTime(dragToSelectEvent.start.getTime());
			newEnd.setHours(23);
			newEnd.setMinutes(59);
			newEnd.setSeconds(0);
		}
		if (newEnd > segment.date && newEnd < endOfView) {
			const eventAlreadySetAt = this.diaryOpeningHours.filter(event => event !== dragToSelectEvent)
				.filter(event => event.start.getDay() === dragToSelectEvent.start.getDay())
				.filter(event => event.end > dragToSelectEvent.start)
				.map(event => event.start)
				.filter(start => newEnd > start);
			if (eventAlreadySetAt.length > 0) {
				return;
			}
			dragToSelectEvent.end = newEnd;
		}

	}

	private refresh() {
		this.diaryOpeningHours = [...this.diaryOpeningHours];
		this.cdr.detectChanges();
	}

	private removeTempEvent(event: CalendarEvent): void {
		const index: number = this.diaryOpeningHours.indexOf(event);
		if (index !== -1) {
			this.diaryOpeningHours.splice(index, 1);
		}
	}

	private setNewEvent(event: CalendarEvent, dialogInfo) {
		if (dialogInfo === REMOVEATTENTION) {
			this.diaryOpeningHours = this.diaryOpeningHours.filter((iEvent) => iEvent !== event);
		} else {
			delete event.meta?.tmpEvent;
			event.meta = dialogInfo;
			event.title = this.getMedicalAttentionTypeText(dialogInfo.medicalAttentionType.id);
			event.color = this.getMedicalAttentionColor(dialogInfo.medicalAttentionType.id);
			event.title += this.getOverturnsText(dialogInfo.overturnCount);
		}
	}

}
