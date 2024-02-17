import { Pipe, PipeTransform } from '@angular/core';
import { capitalize } from '@core/utils/core.utils';

@Pipe({
  	name: 'stringSeparator',
})
export class StringSeparatorPipe implements PipeTransform {

	transform(list: string[]): string {

		const listValues = list.map((val: string) => capitalize(val)).join(', ');

		return listValues ? `${listValues}` : '';
	}

}
