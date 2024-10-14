import { DateDto } from '@api-rest/api-model';

export const dateDtoFromISO = (dateInISO: string): DateDto => {
	var dateParts = dateInISO.split("-");
	return {
		day: parseInt(dateParts[2], 10),
		month: parseInt(dateParts[1], 10),
		year: parseInt(dateParts[0], 10),
	};
};

