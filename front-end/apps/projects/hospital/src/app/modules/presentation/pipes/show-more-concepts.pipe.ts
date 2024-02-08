import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'showMoreConcepts'
})
export class ShowMoreConceptsPipe implements PipeTransform {

	transform(concepts: string[]): string {
		if (!concepts || !concepts.length)
			return "";

		const conceptToShow = concepts[0];
		return concepts.length > 1 ? `${conceptToShow} (+ ${concepts.length - 1} más)` : conceptToShow;
	}

}
