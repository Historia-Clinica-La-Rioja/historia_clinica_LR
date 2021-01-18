import { Pipe, PipeTransform } from '@angular/core';
import { DateDto, DateTimeDto, TimeDto } from '@api-rest/api-model';
import { dateDtoToDate, dateTimeDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';

@Pipe({
	name: 'viewDateDto'
})
export class ViewDateDtoPipe implements PipeTransform {

	transform(date: DateDto | TimeDto | DateTimeDto, type: 'date' | 'time' | 'datetime' ): Date {
		switch (type)  {
			case 'date':
				return dateDtoToDate(date as DateDto);
			case 'time':
				return timeDtoToDate(date as TimeDto);
			case 'datetime':
				return dateTimeDtoToDate(date as DateTimeDto);
			default:
				return undefined;
		}
	}

}
