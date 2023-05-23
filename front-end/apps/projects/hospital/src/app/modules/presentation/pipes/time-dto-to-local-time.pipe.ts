import { Pipe, PipeTransform } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';
import { dateTimeDtoToStringDate } from '@api-rest/mapper/date-dto.mapper';

@Pipe({
	name: 'timeDtoToLocalTime'
})
export class TimeDtoToLocalTimePipe implements PipeTransform {

	transform(value: DateTimeDto): string {
		debugger;
		const date = new Date(dateTimeDtoToStringDate(value));
		return date.getHours().toLocaleString();
	}
}
