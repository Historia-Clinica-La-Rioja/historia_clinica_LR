import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { fromEvent, Observable } from 'rxjs';
import { finalize, map, takeUntil } from 'rxjs/operators';
import { CalendarEvent, DAYS_OF_WEEK } from 'angular-calendar';
import { WeekViewHourSegment } from 'calendar-utils';
import { addDays, addMinutes, endOfWeek } from 'date-fns';

import { buildFullDateFromDate, currentDateWeek } from '@core/utils/moment.utils';
import { REMOVEATTENTION } from '@core/constants/validation-constants';
import { DiaryOpeningHoursDto, OccupationDto, TimeRangeDto } from '@api-rest/api-model';

import { MEDICAL_ATTENTION } from '../constants/descriptions';
import { NewAttentionComponent } from '../dialogs/new-attention/new-attention.component';
import { getDayHoursIntervalsByMinuteValue, toHourMinuteSecond } from '@core/utils/date.utils';

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

	private daysOfCurrentWeek: Date[] = [];
	private hasSelectedLinesOfCare = false;
	private editMode = false;
	private appointmentDuration: number;

	constructor(
		private readonly dialog: MatDialog,
		private readonly cdr: ChangeDetectorRef,
		private readonly viewDate: Date,
		private readonly weekStartsOn: DAYS_OF_WEEK,
		private readonly snackBarService: SnackBarService,
		private readonly diaryType: EDiaryType
	) {
		currentDateWeek().forEach(day => {
			this.daysOfCurrentWeek[day.getDay()] = day;
		});
	}

	getMedicalAttentionTypeText(medicalAttentionTypeId: number): string {
		const medicalAttentionType = medicalAttentionTypeId === 2 ? 'Espontánea' : 'Programada';
		return `Atención ${medicalAttentionType}`;
	}

	startDragToCreate(segment: WeekViewHourSegment, segmentElement: HTMLElement, hasSelectedLinesOfCare?: boolean, editMode?: boolean): void {
		this.editMode = editMode;
		this.hasSelectedLinesOfCare = hasSelectedLinesOfCare;
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
					availableForBooking: !!event.meta.availableForBooking,
					protectedAppointmentsAllowed: !!event.meta.protectedAppointmentsAllowed,
					hasSelectedLinesOfCare: this.hasSelectedLinesOfCare,
					editMode: this.editMode,
					patientVirtualAttentionAllowed: !!event.meta.patientVirtualAttentionAllowed,
					secondOpinionVirtualAttentionAllowed: !!event.meta.secondOpinionVirtualAttentionAllowed,
					onSiteAttentionAllowed: true,
					diaryType: this.diaryType,
					regulationProtectedAppointmentsAllowed: !!event.meta.regulationProtectedAppointmentsAllowed,
				},
				maxHeight: 'fit-content',
				autoFocus: false,
				height: 'max-content'
			});
		dialogRef.afterClosed().subscribe(dialogInfo => {
			if (!dialogInfo) {
				if (event.meta?.tmpEvent) {
					this.removeTempEvent(event);
				}
			} else {
				event.start = dialogInfo.startingHour;
				event.end = dialogInfo.endingHour;
				if (this.thereIsValidTurnAvailabilityByEvent(event))
					this.setNewEvent(event, dialogInfo);
				else {
					this.snackBarService.showError('turnos.agenda-setup.messages.TURN_ERROR');
					this.removeTempEvent(event);
				}
			}
			this.refresh();
		});
	}

	private thereIsValidAvailability(): boolean {
		return !this.occupiedOpeningHours.some(occupiedOpeningHour =>
			!this.diaryOpeningHours.every(diaryOpeningHour =>
				this.compareTurnHourThreshold(occupiedOpeningHour, diaryOpeningHour)
			)
		);
	}

	private thereIsValidTurnAvailabilityByEvent(event: CalendarEvent): boolean {
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

	openEditDialogForEvent(event: CalendarEvent, hasSelectedLinesOfCare?: boolean): void {
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
						availableForBooking: !!event.meta.availableForBooking,
						hasSelectedLinesOfCare: hasSelectedLinesOfCare,
						openingHoursId: event.meta.diaryOpeningHourId,
						protectedAppointmentsAllowed: !!event.meta.protectedAppointmentsAllowed,
						patientVirtualAttentionAllowed: !!event.meta.patientVirtualAttentionAllowed,
						secondOpinionVirtualAttentionAllowed: !!event.meta.secondOpinionVirtualAttentionAllowed,
						onSiteAttentionAllowed: !!event.meta.onSiteAttentionAllowed,
						diaryType: this.diaryType,
						regulationProtectedAppointmentsAllowed: !!event.meta.regulationProtectedAppointmentsAllowed,
					},
					maxHeight: 'fit-content',
					autoFocus: false,
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
						if (this.thereIsValidTurnAvailabilityByEvent(event))
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

	setWeeklyOcupation(occupations$: Observable<OccupationDto[]>): void {
		occupations$.pipe(
			map(occupations => this.occupationsToCalendarEvents(occupations))
		).subscribe((doctorsOfficeEvents: CalendarEvent[]) => {
			this.occupiedOpeningHours = doctorsOfficeEvents;
			if (!this.thereIsValidAvailability())
				this.snackBarService.showError('turnos.agenda-setup.messages.TURN_ERROR');
			this.refresh();
		});
	}

	getDiaryOpeningHours(): DiaryOpeningHoursDto[] {
		return this.diaryOpeningHours.map(event =>
			toDiaryOpeningHoursDto(event));

		function toDiaryOpeningHoursDto(event: CalendarEvent): DiaryOpeningHoursDto {

			return {
				openingHours: {
					dayWeekId: event.start.getDay(),
					from: toHourMinuteSecond(event.start),
					to: toHourMinuteSecond(event.end),
				},
				externalAppointmentsAllowed: event.meta.availableForBooking,
				medicalAttentionTypeId: event.meta.medicalAttentionType.id,
				overturnCount: event.meta.overturnCount,
				protectedAppointmentsAllowed: event.meta.protectedAppointmentsAllowed,
				patientVirtualAttentionAllowed: event.meta.patientVirtualAttentionAllowed,
				secondOpinionVirtualAttentionAllowed: event.meta.secondOpinionVirtualAttentionAllowed,
				onSiteAttentionAllowed: event.meta.onSiteAttentionAllowed,
				regulationProtectedAppointmentsAllowed: event.meta.regulationProtectedAppointmentsAllowed
			};
		}
	}

	eventClicked({ event }: { event: CalendarEvent }, hasSelectedLinesOfCare?: boolean): void {
		this.openEditDialogForEvent(event, hasSelectedLinesOfCare);
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
			title: this.getMedicalAttentionTypeText(diaryOpeningHour.medicalAttentionTypeId),
			color: this.getMedicalAttentionColor(diaryOpeningHour.medicalAttentionTypeId),
			meta: {
				diaryOpeningHourId: diaryOpeningHour.openingHours.id,
				medicalAttentionType: { id: diaryOpeningHour.medicalAttentionTypeId },
				overturnCount: diaryOpeningHour.overturnCount,
				availableForBooking: diaryOpeningHour?.externalAppointmentsAllowed,
				protectedAppointmentsAllowed: diaryOpeningHour.protectedAppointmentsAllowed,
				patientVirtualAttentionAllowed: diaryOpeningHour.patientVirtualAttentionAllowed,
				secondOpinionVirtualAttentionAllowed: diaryOpeningHour.secondOpinionVirtualAttentionAllowed,
				onSiteAttentionAllowed: diaryOpeningHour.onSiteAttentionAllowed,
				regulationProtectedAppointmentsAllowed: diaryOpeningHour.regulationProtectedAppointmentsAllowed
			}

		};
	}

	private getFullDate(dayNumber: number, time: string): Date {
		return buildFullDateFromDate(time, this.daysOfCurrentWeek[dayNumber]);
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
		}
	}

}

export enum EDiaryType {
	CLASSIC,
	EQUIPMENT
}
