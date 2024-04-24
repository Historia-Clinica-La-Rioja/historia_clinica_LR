import { Component, Input } from '@angular/core';
import { ResultPractice } from '../../dialogs/ordenes-prescripciones/ver-resultados-estudio/ver-resultados-estudio.component';

@Component({
	selector: 'app-show-closed-forms-template',
	templateUrl: './show-closed-forms-template.component.html',
	styleUrls: ['./show-closed-forms-template.component.scss']
})
export class ShowClosedFormsTemplateComponent {

	templateResult: ResultPractice;
	@Input() set result(templateResult: ResultPractice[]) {

		this.templateResult = JSON.parse(JSON.stringify(templateResult));
		this.removeRepeatedTitles();
	}

	private removeRepeatedTitles() {
		this.templateResult.templateResult.forEach((item: StudyResults) => {
			if (this.templateResult[item.procedureParameterId] === undefined) {
				this.templateResult[item.procedureParameterId] = true;
			} else {
				item.description = "";
			}
		});
	}
}

export interface StudyResults {
	procedureParameterId: number;
	description: string;
	value: string;

}
