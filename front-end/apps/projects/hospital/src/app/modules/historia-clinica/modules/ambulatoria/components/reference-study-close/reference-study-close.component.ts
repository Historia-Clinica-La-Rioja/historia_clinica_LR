import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { CompleteRequestDto, MasterDataDto, ReferenceRequestDto } from '@api-rest/api-model';
import { ReferenceMasterDataService } from '@api-rest/services/reference-master-data.service';
import { ChangeEvent } from 'react';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { MatDialogRef } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ReferenceCompleteStudyComponent } from '../reference-complete-study/reference-complete-study.component';
import { Observable, map } from 'rxjs';

@Component({
	selector: 'app-reference-study-close',
	templateUrl: './reference-study-close.component.html',
	styleUrls: ['./reference-study-close.component.scss']
})
export class ReferenceStudyCloseComponent implements OnInit {
	@Input() patientId = 0;
	@Input() reference: ReferenceRequestDto;
	@Input() diagnosticReportId: number;

	formReferenceClosure: FormGroup;
	selectedFiles: File[] = [];
	selectedFilesShow = [];
	closureTypes$: Observable<MasterDataDto[]>;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly referenceMasterDataService: ReferenceMasterDataService,
		private readonly prescripcionesService: PrescripcionesService,
		private readonly snackBarService: SnackBarService,
		public dialogRef: MatDialogRef<ReferenceCompleteStudyComponent>,

	) { }

	ngOnInit() {
		this.formReferenceClosure = this.formBuilder.group({
			closureType: [null, [Validators.required]],
			description: [null, [Validators.required]]
		}) as FormGroup & ReferenceClosureForm;

		this.closureTypes$ = this.referenceMasterDataService.getClosureTypes().pipe(
			map(((closureTypes: MasterDataDto[]) => closureTypes)));
	}

	removeSelectedFile(index: number) {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}

	onSelectFileFormData($event: ChangeEvent<HTMLInputElement>) {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
	}

	completeStudy() {
		const completeRequest = this.buildRequest();
		this.prescripcionesService.completeStudy(this.patientId, this.diagnosticReportId,
			completeRequest, this.selectedFiles).subscribe(_ => {

				this.snackBarService.showSuccess('ambulatoria.reference-study-close.SUCCESS');
				this.closeModal(false, true);
			}, error => {
				this.snackBarService.showError(error.text);
			});
	}

	private closeModal(simpleClose: boolean, completed?: boolean): void {
		this.dialogRef.close(simpleClose ? null : { completed });
	}

	private buildRequest(): CompleteRequestDto {
		return {
			observations: this.formReferenceClosure.value.description,
			referenceClosure: {
				referenceId: this.reference.id,
				clinicalSpecialtyId: this.reference.clinicalSpecialtyId,
				counterReferenceNote: this.formReferenceClosure.value.description,
				closureTypeId: this.formReferenceClosure.value.closureType.id
			}
		}
	}
}

export interface ReferenceClosureDto {
	clinicalSpecialtyId: number;
	closureTypeId: number;
	counterReferenceNote: string;
	id: number;
	referenceId: number;
}

interface ReferenceClosureForm {
	closureType: FormControl<MasterDataDto[]>;
	description: FormControl<string>;
}
