import { Injectable } from '@angular/core';
import { DiaryOpeningHoursDto, EquipmentDiaryOpeningHoursDto } from '@api-rest/api-model';
import { momentParseTime, dateToMoment, momentParseDate, dateToMomentTimeZone, DateFormat } from '@core/utils/moment.utils';
import { MEDICAL_ATTENTION } from '@turnos/constants/descriptions';
import { CalendarWeekViewBeforeRenderEvent } from 'angular-calendar';
import { Moment } from 'moment';
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

	private _getOpeningHoursFor(startDate: string, endDate: string, date: Date): DiaryOpeningHoursDto[] {
		const dateMoment = dateToMoment(date);
		const start = momentParseDate(startDate);
		const end = momentParseDate(endDate);

		if (dateMoment.isBetween(start, end, 'date', '[]')) {
			return this.equipmentDiaryOpeningHours.filter(oh => oh.openingHours.dayWeekId === date.getDay());
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

	getOpeningHoursId(startDate: string, endDate: string, date: Date): number {
		const openingHoursSelectedDay = this._getOpeningHoursFor(startDate, endDate, date);

		const selectedOpeningHour = openingHoursSelectedDay.find(oh => {
			const hourFrom = momentParseTime(oh.openingHours.from);
			const hourTo = momentParseTime(oh.openingHours.to);
			const selectedHour = dateToMomentTimeZone(date).format(DateFormat.HOUR_MINUTE_SECONDS);

			return momentParseTime(selectedHour).isBetween(hourFrom, hourTo, null, '[)');
		});

		return selectedOpeningHour?.openingHours.id;
	}

	setDayStartHourAndEndHour() {
		this.equipmentDiaryOpeningHours.forEach(oh => {
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
}
