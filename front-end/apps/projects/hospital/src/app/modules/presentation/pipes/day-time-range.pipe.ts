import { DatePipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';
import { capitalize } from '@core/utils/core.utils';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DEFAULT_LANG } from './../../../app.component';


@Pipe({
	name: 'dayTimeRange'
})
export class DayTimeRangePipe implements PipeTransform {

	constructor(){}

	datePipe = new DatePipe(DEFAULT_LANG);

	transform(initDate: Date, endDate: Date): string {
		if (initDate && endDate) {
			const dayOfWeek: string = this.datePipe.transform(initDate, 'EEEE');
			const initHourMin = this.datePipe.transform(initDate, DatePipeFormat.SHORT_TIME);
			const endHourMin = this.datePipe.transform(endDate, DatePipeFormat.SHORT_TIME);
			return `${capitalize(dayOfWeek)}, ${initHourMin}hs a ${endHourMin}hs`;
		}
	}

}
