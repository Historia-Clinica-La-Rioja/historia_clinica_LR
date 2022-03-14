import { formatHour } from '@core/utils/date.utils';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'viewFullHour'
})
export class ViewFullHourPipe implements PipeTransform {

  transform(date: Date): string{
    return formatHour(date);
  }

}
