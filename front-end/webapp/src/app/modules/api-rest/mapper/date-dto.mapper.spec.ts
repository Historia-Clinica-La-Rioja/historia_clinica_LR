import { DateDto, DateTimeDto, TimeDto } from '@api-rest/api-model';
import { dateDtoToDate, dateTimeDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { parse } from 'date-fns';

describe('date-dto.mapper', () => {

	const dateDto: DateDto = {
		day: 29,
		month: 4,
		year: 1996
	};
	const timeDto: TimeDto = {
		hours: 10,
		minutes: 30
	};

	const dateTimeDto: DateTimeDto = {
		date: dateDto,
		time: timeDto
	};

	it('it should return true if the DateDto is correctly mapped to Date', () => {
		const expectedDate: Date = new Date(1996, 3, 29);
		const mappedDate = dateDtoToDate(dateDto);

		const correctlyMapped = (expectedDate.getTime() === mappedDate.getTime());

		expect(correctlyMapped).toBe(true);
	});

	it('it should return true if the TimeDto is correctly mapped to Date', () => {

		const expectedTime: Date = parse(`10:30:00`, 'HH:mm:ss', new Date());

		const mappedTime = timeDtoToDate(timeDto);

		const correctlyMapped = (expectedTime.getTime() === mappedTime.getTime());

		expect(correctlyMapped).toBe(true);
	});

	it('it should return true if the DateTimeDto is correctly mapped to Date', () => {

		const mappedTime = timeDtoToDate(timeDto);
		const expectedDateTime: Date = new Date(1996, 3, 29, mappedTime.getHours(), mappedTime.getMinutes(), mappedTime.getSeconds());

		const mappedDateTime = dateTimeDtoToDate(dateTimeDto);

		const correctlyMapped = (expectedDateTime.getTime() === mappedDateTime.getTime());

		expect(correctlyMapped).toBe(true);
	});
});
