import { DateDto, DateTimeDto, TimeDto } from '@api-rest/api-model';
import {
	dateDtoToDate,
	dateTimeDtoToDate,
	dateToDateDto,
	dateToDateTimeDto,
	dateToTimeDto,
	timeDtoToDate
} from '@api-rest/mapper/date-dto.mapper';
import { parse } from 'date-fns';

describe('date-dto.mapper', () => {

	const dateDtoMock: DateDto = {
		day: 29,
		month: 4,
		year: 1996
	};
	const timeDtoMock: TimeDto = {
		hours: 10,
		minutes: 30
	};
	const dateTimeDtoMock: DateTimeDto = {
		date: dateDtoMock,
		time: timeDtoMock
	};
	const dateMock: Date = new Date(1996, 3, 29, 10, 30, 15);


	it('it should return true if the DateDto is correctly mapped to Date', () => {
		const expectedDate: Date = new Date(1996, 3, 29);
		const mappedDate = dateDtoToDate(dateDtoMock);

		const correctlyMapped = (expectedDate.getTime() === mappedDate.getTime());

		expect(correctlyMapped).toBe(true);
	});

	it('it should return true if the TimeDto is correctly mapped to Date', () => {

		const expectedTime: Date = parse(`10:30:00`, 'HH:mm:ss', new Date());

		const mappedTime = timeDtoToDate(timeDtoMock);

		const correctlyMapped = (expectedTime.getTime() === mappedTime.getTime());

		expect(correctlyMapped).toBe(true);
	});

	it('it should return true if the DateTimeDto is correctly mapped to Date', () => {

		const mappedTime = timeDtoToDate(timeDtoMock);
		const expectedDateTime: Date = new Date(1996, 3, 29, mappedTime.getHours(), mappedTime.getMinutes(), mappedTime.getSeconds());

		const mappedDateTime = dateTimeDtoToDate(dateTimeDtoMock);

		const correctlyMapped = (expectedDateTime.getTime() === mappedDateTime.getTime());

		expect(correctlyMapped).toBe(true);
	});

	it('it should return true if the Date is correctly mapped to DateDto', () => {

		const mappedDateDto: DateDto = dateToDateDto(dateMock);

		const correctlyMapped = (mappedDateDto.day === dateMock.getDate() &&
			mappedDateDto.month === dateMock.getMonth() + 1 &&
			mappedDateDto.year === dateMock.getFullYear()
		);

		expect(correctlyMapped).toBe(true);
	});

	it('it should return true if the Date is correctly mapped to TimeDto', () => {

		const mappedTimeDto: TimeDto = dateToTimeDto(dateMock);

		const correctlyMapped = (
			mappedTimeDto.hours === dateMock.getHours() &&
			mappedTimeDto.minutes === dateMock.getMinutes() &&
			mappedTimeDto.seconds === dateMock.getSeconds()
		);

		expect(correctlyMapped).toBe(true);
	});

	it('it should return true if the Date is correctly mapped to DateTimeDto', () => {

		const mappedDateTimeDto: DateTimeDto = dateToDateTimeDto(dateMock);

		const correctlyMapped = (
			mappedDateTimeDto.date.day === dateMock.getDate() &&
			mappedDateTimeDto.date.month === dateMock.getMonth() + 1 &&
			mappedDateTimeDto.date.year === dateMock.getFullYear() &&
			mappedDateTimeDto.time.hours === dateMock.getHours() &&
			mappedDateTimeDto.time.minutes === dateMock.getMinutes() &&
			mappedDateTimeDto.time.seconds === dateMock.getSeconds()
		);

		expect(correctlyMapped).toBe(true);
	});
});
