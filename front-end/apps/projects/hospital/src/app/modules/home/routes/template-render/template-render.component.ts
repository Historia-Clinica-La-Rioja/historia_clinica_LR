import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, Observable, map, switchMap } from 'rxjs';
import { ProcedureTemplateFullSummaryDto } from '@api-rest/api-model';
import { ProcedureTemplatesService } from '@api-rest/services/procedure-templates.service';
import { LoincFormValues } from '../../../hsi-components/loinc-form/loinc-input.model';

@Component({
	selector: 'app-template-render',
	templateUrl: './template-render.component.html',
	styleUrls: ['./template-render.component.scss']
})
export class TemplateRenderComponent {
	procedureTemplate$: Observable<ProcedureTemplateFullSummaryDto>;
	emitedValues: LoincFormValues;
	private valuesSubject: BehaviorSubject<LoincFormValues> = new BehaviorSubject<LoincFormValues>(undefined);
	inputValues$ = this.valuesSubject.asObservable();
	isChanged = false;

	constructor(
		activatedRoute: ActivatedRoute,
		procedureTemplatesService: ProcedureTemplatesService,
	) {
		this.procedureTemplate$ = activatedRoute.paramMap.pipe(
			map( params => {
				const templateId = Number(params.get('templateId'));
				return templateId;
			}),
			switchMap(templateId => procedureTemplatesService.findById(templateId)),
		);
		// this.valuesSubject.next({hasChanged: true, values: []});
	}

	changeValues(formValues: LoincFormValues) {
		this.emitedValues = formValues;
		this.isChanged = false;
		this.valuesSubject.next(undefined);
	}

	changed(sformValues: LoincFormValues) {
		this.isChanged = sformValues?.hasChanged;
	}

	save() {
		const values = this.emitedValues;
		this.emitedValues = undefined;
		this.valuesSubject.next(values);
	}

}

