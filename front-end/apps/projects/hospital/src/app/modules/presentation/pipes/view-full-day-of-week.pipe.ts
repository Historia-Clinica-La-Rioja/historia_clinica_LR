import { formatDayOfWeek } from '@core/utils/date.utils';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'viewFullDayOfWeek'
})
export class ViewFullDayOfWeekPipe implements PipeTransform {

  transform(date: Date): string{
    return formatDayOfWeek(date);
  }

}
