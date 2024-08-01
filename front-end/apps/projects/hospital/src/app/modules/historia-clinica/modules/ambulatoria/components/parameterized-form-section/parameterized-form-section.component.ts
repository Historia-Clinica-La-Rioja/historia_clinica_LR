import { Component, Output, EventEmitter } from '@angular/core';
import { ParameterizedFormDialogComponent, ParameterizedFormToSave } from '../../dialogs/parameterized-form-dialog/parameterized-form-dialog.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { CompleteParameterizedFormDto } from '@api-rest/api-model';

@Component({
	selector: 'app-parameterized-form-section',
	templateUrl: './parameterized-form-section.component.html',
	styleUrls: ['./parameterized-form-section.component.scss']
})
export class ParameterizedFormSectionComponent {

	parameterizedFormToSave: ParameterizedFormToSave[] = [];
	@Output() completeParameterizedForms = new EventEmitter<CompleteParameterizedFormDto[]>();

	constructor(
		private readonly dialogService: DialogService<ParameterizedFormDialogComponent>,
	) { }

	openDialog() {
		const dialogRef = this.dialogService.open(ParameterizedFormDialogComponent, {
			dialogWidth: DialogWidth.MEDIUM,			
		}, {});

		dialogRef.afterClosed().subscribe((parameterizedForm: ParameterizedFormToSave) => {
			if (parameterizedForm) {
				this.parameterizedFormToSave.push(parameterizedForm);
				this.completeParameterizedForms.emit(this.parameterizedFormToSave.map(completeData => completeData.completeParameterizedFormDto));
			}
		})
	}

	remove(indexToRemove: number) {
		this.parameterizedFormToSave.splice(indexToRemove, 1);
		this.completeParameterizedForms.emit(this.parameterizedFormToSave.map(completeData => completeData.completeParameterizedFormDto));
	}


}
