import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiErrorMessageDto, TextTemplateDto } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { catchError, of } from 'rxjs';
import { TemplateManagementService } from '../../services/template-management.service';

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
	ERROR_KEY_DUPLICATED = 'duplicated-name'

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: TemplateData,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly templateManagementService: TemplateManagementService,
		public dialogRef: MatDialogRef<SaveTemplateComponent>
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			templateName: [null,[Validators.required
			]],
		})
	}

	saveTemplate(): void {
		const finalValueToSave: TextTemplateDto = { text: this.data.textReportInformer, name: this.form.get('templateName').value }
		if (this.form.valid) {
			this.templateManagementService.saveTemplate(finalValueToSave).pipe(
				catchError((error: ApiErrorMessageDto) => {
					this.nameAlreadyExists = error.code.includes(this.ERROR_KEY_DUPLICATED)
					if (!this.nameAlreadyExists)
						this.error()
					return of(false)
				}
				)
			)
				.subscribe(
					saveSucess => {
						if (saveSucess) {
							this.success()
						}
					}
				)
		}
	}

	clear(): void {
		this.form.controls.templateName.reset();
	}

	private success(): void {
		this.snackBarService.showSuccess('image-network.worklist.details_study.SNACKBAR_SUCCESS_TEMPLATE');
		this.dialogRef.close()
	}

	private error(): void {
		this.snackBarService.showError('image-network.worklist.details_study.SNACKBAR_ERROR');
		this.dialogRef.close()
	}

}

export interface TemplateData {
    textReportInformer: string,
}


export interface TemplateDataSave extends TemplateData {
    templateName: string
}

