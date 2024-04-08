import { Pipe, PipeTransform } from '@angular/core';
import { capitalize } from '@core/utils/core.utils';

@Pipe({
	name: 'practiceList'
})
export class PracticesPipe implements PipeTransform {

	transform(practices: string[]): string {

		const practiceNames = practices.map((practice: string) => capitalize(practice)).join(', ');

		return practiceNames ? `${practiceNames}` : '';
	}
}
