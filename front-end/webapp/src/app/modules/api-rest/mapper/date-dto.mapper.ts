import { DateDto, DateTimeDto, TimeDto } from '@api-rest/api-model';
import { parse } from 'date-fns';

const TIME_FORMAT = 'HH:mm:ss';

export const dateDtoToDate = (dateDto: DateDto): Date =>
	new Date(dateDto.year, dateDto.month - 1, dateDto.day);

export const timeDtoToDate = (timeDto: TimeDto): Date => {
	const seconds = timeDto.seconds ? timeDto.seconds : 0;

	return parse(`${timeDto.hours}:${timeDto.minutes}:${seconds}`, TIME_FORMAT, new Date());
};

export const dateTimeDtoToDate = (dateTimeDto: DateTimeDto): Date => {
	const dateTimeMapped: Date = timeDtoToDate(dateTimeDto.time);
	const dateMapped: Date = dateDtoToDate(dateTimeDto.date);

	dateTimeMapped.setDate(dateMapped.getDate());
	dateTimeMapped.setMonth(dateMapped.getMonth());
	dateTimeMapped.setFullYear(dateMapped.getFullYear());

	return dateTimeMapped;
};

export const dateToDateTimeDto = (date: Date): DateTimeDto => {
	return {
		date: {
			day: date.getDate(),
			month: date.getMonth() + 1,
			year: date.getFullYear()
		},
		time: dateToTimeDto(date)
	};
};

export const dateToTimeDto = (date: Date): TimeDto => {
	return {
		hours: date.getHours(),
		minutes: date.getMinutes(),
		seconds: date.getSeconds()
	};
};

export const dateToDateDto = (date: Date): DateDto => {
	return {
		day: date.getDate(),
		month: date.getMonth() + 1,
		year: date.getFullYear()
	};
};
