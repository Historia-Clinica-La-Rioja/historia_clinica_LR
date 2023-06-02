import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { GenderDto, PersonBasicDataResponseDto } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PersonService } from '@api-rest/services/person.service';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { finalize } from 'rxjs';
import { WarningEditIdentificationNumberComponent } from '../warning-edit-identification-number/warning-edit-identification-number.component';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';

@Component({
	selector: 'app-edit-identification-number',
	templateUrl: './edit-identification-number.component.html',
	styleUrls: ['./edit-identification-number.component.scss']
})
export class EditIdentificationNumberComponent implements OnInit {
	hasError = hasError;
	form: FormGroup;
	genders: GenderDto[];
	formSubmitted: boolean = false;
	isLoading: boolean;

	constructor(private formBuilder: FormBuilder,
		private personMasterDataService: PersonMasterDataService,
		private personService: PersonService,
		private snackBarService: SnackBarService,
		private dialog: MatDialog,
		public dialogRef: MatDialogRef<EditIdentificationNumberComponent>) { }

	ngOnInit(): void {
		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});
		this.form = this.formBuilder.group({
			identificationNumber: [null, [Validators.required]],
			genderId: [null, [Validators.required]]
		})
	}

	save() {
		this.formSubmitted = true;
		if (this.form.valid) {
			this.formSubmitted = true;
			this.callRenaperService();
		}
	}

	private callRenaperService(): void {
		this.personService.getRenaperPersonData({
			identificationNumber: this.form.controls.identificationNumber.value,
			genderId: this.form.controls.genderId.value,
		})
			.pipe(finalize(() => this.isLoading = false))
			.subscribe(
				personData => {
					if (personData) {
						const dialogRef = this.dialog.open(WarningEditIdentificationNumberComponent, {
							data: {
								personData: personData,
								dni: this.form.controls.identificationNumber.value,
							},
							disableClose: true,
							width: '40%',
							autoFocus: false,
						})
						const personDataCustom: PersonBasicDataResponseCustom = {
							personData: personData,
							identificationNumber: this.form.controls.identificationNumber.value,
							genderId: this.form.controls.genderId.value,
						}
						dialogRef.afterClosed().subscribe(confirmed => {
							if (confirmed) {
								this.dialogRef.close(personDataCustom);
							}
						})
					} else {
						const dialogRefNoData = this.dialog.open(DiscardWarningComponent, {
							data: {
								title: 'pacientes.audit.TITLE_NO_DATA_RENAPER',
								content: 'pacientes.audit.SUBTITLE_NO_DATA_RENAPER',
								okButtonLabel: 'buttons.RETRY',
							}
						})
						dialogRefNoData.afterClosed().subscribe(retry => {
							if (!retry) {
								this.dialogRef.close();
							}
						})
					}

				}, () => {
					this.snackBarService.showError('pacientes.search.RENAPER_TIMEOUT');
				});
	}

}
export interface PersonBasicDataResponseCustom {
	personData: PersonBasicDataResponseDto,
	identificationNumber: number,
	genderId:number,
}
