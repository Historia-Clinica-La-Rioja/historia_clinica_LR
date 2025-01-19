import { Pipe, PipeTransform } from '@angular/core';
import { mappings } from '../constants/medication-presentation';

@Pipe({
  	name: 'medicationPresentation'
})
export class MedicationPresentationPipe implements PipeTransform {

	transform(value: string): string[] {
		return mappings[value] || [value];
	}
}
