import { Component, Input } from '@angular/core';
import { ResultPractice } from '../../dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';

@Component({
	selector: 'app-show-closed-forms-template',
	templateUrl: './show-closed-forms-template.component.html',
	styleUrls: ['./show-closed-forms-template.component.scss']
})
export class ShowClosedFormsTemplateComponent {

	templateResult: ResultPractice;
	resultsWithFilteredTitles: StudyResults[];

	@Input() set result(templateResult: ResultPractice[]) {
		this.templateResult = JSON.parse(JSON.stringify(templateResult));
		this.resultsWithFilteredTitles = this.getUniqueDescriptions(this.templateResult.templateResult);
	}

	private getUniqueDescriptions(items: StudyResults[]): StudyResults[] {
		const seenDescriptions: Set<string> = new Set();

		return items.map(item => ({
			...item,
			description: seenDescriptions.has(item.description)
				? ""
				: (seenDescriptions.add(item.description), item.description)
		}));
	}
}

export interface StudyResults {
	procedureParameterId: number;
	description: string;
	value: string;

}
