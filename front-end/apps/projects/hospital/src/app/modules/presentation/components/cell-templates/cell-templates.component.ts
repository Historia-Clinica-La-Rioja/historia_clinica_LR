import {
	Component,
	EventEmitter,
	OnInit,
	Output,
	QueryList,
	TemplateRef,
	ViewChildren
} from '@angular/core';
import {DateFormat, momentFormat} from '@core/utils/moment.utils';
import {CtrlTemplateDirective} from '@presentation/directives/ctrl-template.directive';

@Component({
	selector: 'app-cell-templates',
	templateUrl: './cell-templates.component.html',
	styleUrls: ['./cell-templates.component.scss']
})
export class CellTemplatesComponent implements OnInit {

	@ViewChildren(CtrlTemplateDirective) templateRefs: QueryList<CtrlTemplateDirective>;
	@Output() valueChanged: EventEmitter<any> = new EventEmitter<any>();

	constructor() { }

	ngOnInit(): void {
	}

	 getTemplate(templateName: string): TemplateRef<any> {
		return this.templateRefs.toArray().find(x => x.name.toLowerCase() === templateName.toLowerCase()).template;
	 }

	 formatDate(date): string {
		 return date ? momentFormat(date, DateFormat.VIEW_DATE) : '';
	 }
}

export enum CellTemplates {
	TEXT = 'textTemplate',
	ERROR = 'errorTemplate',
	REMOVE_BUTTON = 'removeButtonTemplate',
	SNOMED_PROBLEM = 'snomedProblemTemplate',
	PROBLEM_SEVERITY = 'problemSeverityTemplate',
	START_AND_END_DATE = 'startAndEndDateTemplate',
}
