import { DateDto, DateTimeDto, TimeDto } from '@api-rest/api-model';
import { formatDateOnlyISO, formatTimeOnlyISO } from '@core/utils/date.utils';
import { parse } from 'date-fns';

const TIME_FORMAT = 'HH:mm:ss';

export const dateDtoToDate = (dateDto: DateDto): Date =>
	new Date(dateDto.year, dateDto.month - 1, dateDto.day);

export const timeDtoToDate = (timeDto: TimeDto): Date => {
	return parse(`${timeDto.hours}:${timeDto.minutes}:${timeDto.seconds || 0}`, TIME_FORMAT, new Date());
};

export const dateTimeDtoToDate = (dateTimeDto: DateTimeDto): Date => {
	const timeMapped: Date = timeDtoToDate(dateTimeDto.time);
	const dateMapped: Date = dateDtoToDate(dateTimeDto.date);
	const dateTimeMapped: Date = dateMapped;
	dateTimeMapped.setHours(timeMapped.getHours());
	dateTimeMapped.setMinutes(timeMapped.getMinutes());
	dateTimeMapped.setSeconds(timeMapped.getSeconds());
	return dateTimeMapped;
};

export const dateDtoAndTimeDtoToDate = (date: DateDto, time: TimeDto): Date => {
	const timeMapped: Date = timeDtoToDate(time);
	const dateMapped: Date = dateDtoToDate(date);
	const dateTimeMapped: Date = dateMapped;
	dateTimeMapped.setHours(timeMapped.getHours());
	dateTimeMapped.setMinutes(timeMapped.getMinutes());
	dateTimeMapped.setSeconds(timeMapped.getSeconds());
	return dateTimeMapped;
};

export const convertDateTimeDtoToDate = (dateTimeDto: DateTimeDto): Date => {
	const { day, month, year } = dateTimeDto.date;
	const { hours, minutes = 0, seconds = 0 } = dateTimeDto.time;
	return new Date(Date.UTC(year, month - 1, day, hours, minutes, seconds));
}

export const convertDateDtoToDate = (dateDto: DateDto): Date => {
	const { day, month, year } = dateDto;
	return new Date(Date.UTC(year, month - 1, day));
}

export const dateTimeDtotoLocalDate = (dateTimeDto: DateTimeDto): Date => {
	return dateTimeDto ? new Date(dateTimeDtoToStringDate(dateTimeDto)) : undefined;
}

export const dateTimeDtoToStringDate = (dateTimeDto: DateTimeDto): string => {
	const date = formatDateOnlyISO(dateDtoToDate(dateTimeDto.date));
	const time = formatTimeOnlyISO(timeDtoToDate(dateTimeDto.time));
	return (date + 'T' + time.split("-")[0] + '.000' + 'Z');
};

export const dateToDateTimeDto = (date: Date): DateTimeDto => {
	return {
		date: dateToDateDto(date),
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

export const dateToDateTimeDtoUTC = (date: Date): DateTimeDto => {
	return {
		date: {
			year: date.getUTCFullYear(),
			month: date.getUTCMonth() + 1,
			day: date.getUTCDate()
		},
		time: {
			hours: date.getUTCHours(),
			minutes: date.getUTCMinutes(),
			seconds: date.getUTCSeconds()
		},
	};
};

export const stringToTimeDto = (date: string): TimeDto => {
	const timeArray = date.split(":");
	return {
		hours: +timeArray[0],
		minutes: +timeArray[1],
		seconds: +timeArray[2]
	}
}

export const stringToDate = (date: string): Date => new Date(date);

export const timeToString = (time: string): string => {
	const timeArray = time.split(":");
	return timeArray[0] + ":" + timeArray[1]
}

export const mapDateWithHypenToDateWithSlash = (date: string): string => {
	const dateArray = date.split("-");
	return dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0]
}

export const mapToString = (date: DateDto): string => {
	return date.year.toString() + date.month.toString() + date.day.toString();
}

export const timeDtotoString = (time: TimeDto): string => {
	const hours = time.hours.toString().padStart(2, '0');
	const minutes = time.minutes.toString().padStart(2, '0');
	return `${hours}:${minutes}`;
}

export const timeDtotoFullTimeString = (time: TimeDto): string => {
	const hours = time.hours.toString().padStart(2, '0');
	const minutes = time.minutes.toString().padStart(2, '0');
	const seconds = time.seconds.toString().padStart(2, '0');
	return `${hours}:${minutes}:${seconds}`;
}
