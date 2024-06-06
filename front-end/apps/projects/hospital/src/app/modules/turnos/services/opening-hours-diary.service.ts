import { Injectable } from '@angular/core';
import { DiaryOpeningHoursDto, EquipmentDiaryOpeningHoursDto } from '@api-rest/api-model';
import { toHourMinuteSecond } from '@core/utils/date.utils';
import { dateParseTime, isBetweenDates, dateISOParseDate } from '@core/utils/moment.utils';
import { MEDICAL_ATTENTION } from '@turnos/constants/descriptions';
import { CalendarWeekViewBeforeRenderEvent } from 'angular-calendar';
import { WeekViewHourSegment } from 'calendar-utils';

const ASSIGNABLE_CLASS = 'cursor-pointer';
const DIARY_SCHEDULED_CLASS = 'bg-green';
const DIARY_SPONTANEOUS_CLASS = 'bg-yellow';

@Injectable()
export class OpeningHoursDiaryService {

	dayStartHour: number;
	dayEndHour: number;
	equipmentDiaryOpeningHours: EquipmentDiaryOpeningHoursDto[] = [];

	constructor() { }

	setDiaryOpeningHours(equipmentDiaryOpeningHours: EquipmentDiaryOpeningHoursDto[]) {
		this.equipmentDiaryOpeningHours = equipmentDiaryOpeningHours;
	}

	getEquipmentDiaryOpeningHours(): EquipmentDiaryOpeningHoursDto[] {
		return this.equipmentDiaryOpeningHours;
	}

	loadOpeningHoursOfCalendar(renderEvent: CalendarWeekViewBeforeRenderEvent, startDate: string, endDate: string,) {
		renderEvent.hourColumns.forEach((hourColumn) => {
			const openingHours: DiaryOpeningHoursDto[] = this._getOpeningHoursFor(startDate, endDate, hourColumn.date);
			if (openingHours.length) {
				hourColumn.hours.forEach((hour) => {
					hour.segments.forEach((segment) => {
						openingHours.forEach(openingHour => {
							const from = dateParseTime(openingHour.openingHours.from);
							const to = dateParseTime(openingHour.openingHours.to);
							if (isBetween(segment, from, to)) {
								segment.cssClass = this.getOpeningHoursCssClass(openingHour);
							}
						});
					});
				});
			}
		});

		function isBetween(segment: WeekViewHourSegment, from: Date, to: Date) {
			return ((segment.date.getHours() > from.getHours()) ||
				(segment.date.getHours() === from.getHours() && segment.date.getMinutes() >= from.getMinutes()))
				&& ((segment.date.getHours() < to.getHours()) ||
					(segment.date.getHours() === to.getHours() && segment.date.getMinutes() < to.getMinutes()));
		}
	}

	private _getOpeningHoursFor(startDate: string, endDate: string, date: Date): DiaryOpeningHoursDto[] {
		const start = dateISOParseDate(startDate);
		const end = dateISOParseDate(endDate);

		return !isBetweenDates(date, start, end, '[]') ? [] :
			this.equipmentDiaryOpeningHours.filter(oh => oh.openingHours.dayWeekId === date.getDay())
	}

	private getOpeningHoursCssClass(openingHour: DiaryOpeningHoursDto): string {
		if (openingHour.medicalAttentionTypeId === MEDICAL_ATTENTION.SPONTANEOUS_ID) {
			return `${DIARY_SPONTANEOUS_CLASS} ${ASSIGNABLE_CLASS}`;
		} else {
			return `${DIARY_SCHEDULED_CLASS} ${ASSIGNABLE_CLASS}`;
		}
	}

	getOpeningHoursId(startDate: string, endDate: string, date: Date): number {
		const openingHoursSelectedDay = this._getOpeningHoursFor(startDate, endDate, date);

		const selectedOpeningHour = openingHoursSelectedDay.find(oh => {
			const hourFrom = dateParseTime(oh.openingHours.from);
			const hourTo = dateParseTime(oh.openingHours.to);
			const selectedHour = toHourMinuteSecond(date);

			return isBetweenDates(dateParseTime(selectedHour), hourFrom, hourTo, '[)');
		});

		return selectedOpeningHour?.openingHours.id;
	}

	setDayStartHourAndEndHour() {
		this.equipmentDiaryOpeningHours.forEach(oh => {
			const from = dateParseTime(oh.openingHours.from).getHours();
			if (this.dayStartHour === undefined || from <= this.dayStartHour) {
				this.dayStartHour = (from > 0) ? from - 1 : from;
			}
			const to = dateParseTime(oh.openingHours.to).getHours();
			if (this.dayEndHour === undefined || to >= this.dayEndHour) {
				this.dayEndHour = (to < 23) ? to + 1 : to;
			}
		});
	}
}
