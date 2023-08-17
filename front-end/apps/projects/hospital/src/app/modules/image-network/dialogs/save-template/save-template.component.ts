import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-save-template',
	templateUrl: './save-template.component.html',
	styleUrls: ['./save-template.component.scss']
})
export class SaveTemplateComponent implements OnInit {

	public form: UntypedFormGroup;
	hideIcon = false;
	hasError = hasError;
	nameAlreadyExists = false

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: TemplateData,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snackBarService: SnackBarService,
		public dialogRef: MatDialogRef<SaveTemplateComponent>
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			templateName: [null,[Validators.required
			]],
		})
	}

	saveTemplate(): void {
		const finalValueToSave: TemplateDataSave = {...this.data, templateName: this.form.get('templateName').value }
		if(this.form.valid){
			console.log('llamar endpoints', finalValueToSave)
			this.success()
			this.dialogRef.close()
		}

	}

	clear(): void {
		this.form.controls.templateName.reset();
	}

	private success() {
		this.snackBarService.showSuccess('image-network.worklist.details_study.SNACKBAR_SUCCESS');
	}

	// private error() { TODO: Activar cuando esten los endpoints
	// 	this.snackBarService.showError('image-network.worklist.details_study.SNACKBAR_ERROR');
	// }

}

export interface TemplateData {
    text: string,
	userId: number,
	institutionId: number,
}


export interface TemplateDataSave extends TemplateData {
    templateName: string
}
