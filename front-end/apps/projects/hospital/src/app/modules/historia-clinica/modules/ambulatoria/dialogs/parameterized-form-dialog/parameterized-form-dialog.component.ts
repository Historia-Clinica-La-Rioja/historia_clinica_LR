import { Component, OnInit } from '@angular/core';
import { ParameterizedFormService } from '@api-rest/services/parameterized-form.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { listToTypeaheadOptions } from '@presentation/utils/typeahead.mapper.utils';
import { map, Observable } from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import { CompletedParameterizedFormParameterDto, CompleteParameterizedFormDto, ParameterizedFormDto } from '@api-rest/api-model';
import { ParameterizedFormValidationsService } from '../../services/parameterized-form-validations.service';

@Component({
	selector: 'app-parameterized-form-dialog',
	templateUrl: './parameterized-form-dialog.component.html',
	styleUrls: ['./parameterized-form-dialog.component.scss']
})
export class ParameterizedFormDialogComponent implements OnInit {

	selectedForm: ParameterizedFormDto;
	institutionalParameterizedForms$: Observable<TypeaheadOption<ParameterizedFormDto>[]>;
	completedParameterizedFormParameterDto: CompletedParameterizedFormParameterDto[] = [];

	constructor(
		private readonly parameterizedFormService: ParameterizedFormService,
		private dialogRef: MatDialogRef<ParameterizedFormDialogComponent>,
		private readonly parameterizedFormValidationsService: ParameterizedFormValidationsService,
	) { }

	ngOnInit() {
		this.institutionalParameterizedForms$ = this.parameterizedFormService.getList().pipe(map((parameterizedForm: ParameterizedFormDto[]) =>
			listToTypeaheadOptions<ParameterizedFormDto>(parameterizedForm, 'name')
		));
	}

	save() {
		const isValidForm = this.parameterizedFormValidationsService.isValidForm();
		if (isValidForm) {
			const parameterizedFormInformation = this.buildParameterizedFormInformation();
			this.dialogRef.close(parameterizedFormInformation);
		}
	}

	private buildParameterizedFormInformation(): ParameterizedFormToSave {
		return {
			completeParameterizedFormDto: {
				id: this.selectedForm.id,
				parameters: this.completedParameterizedFormParameterDto
			},
			selectedFormName: this.selectedForm.name
		}
	}
}

export interface ParameterizedFormToSave {
	completeParameterizedFormDto: CompleteParameterizedFormDto;
	selectedFormName: string;
}