import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { AddDiagnosticReportObservationsCommandDto, ClinicalSpecialtyDto, CompleteRequestDto, MasterDataDto, ReferenceRequestDto } from '@api-rest/api-model';
import { ReferenceMasterDataService } from '@api-rest/services/reference-master-data.service';
import { ChangeEvent } from 'react';
import { PrescripcionesService } from '../../services/prescripciones.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ReferenceCompleteStudyComponent } from '../reference-complete-study/reference-complete-study.component';
import { BehaviorSubject, Observable, forkJoin, map, of, tap } from 'rxjs';
import { ButtonType } from '@presentation/components/button/button.component';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ButtonService } from '../../services/button.service';
import { StudyInfo } from '../../services/study-results.service';
import { ControlTemplatesService } from '../../services/control-templates.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { CLOSURE_OPTIONS } from '@access-management/constants/reference';

@Component({
	selector: 'app-reference-study-close',
	templateUrl: './reference-study-close.component.html',
	styleUrls: ['./reference-study-close.component.scss'],
	providers: [ControlTemplatesService]
})
export class ReferenceStudyCloseComponent implements OnInit {
	@Input() patientId = 0;
	@Input() reference: ReferenceRequestDto;
	@Input() diagnosticReportId: number;
	@Input() studies: StudyInfo[];

	formReferenceClosure: FormGroup;
	selectedFiles: File[] = [];
	selectedFilesShow = [];
	closureTypes$: Observable<MasterDataDto[]>;
	buttonTypeFlat = ButtonType.FLAT;
	disabled$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
	isLoading$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
	clinicalSpecialties$: Observable<ClinicalSpecialtyDto[]>;
	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly referenceMasterDataService: ReferenceMasterDataService,
		private readonly prescripcionesService: PrescripcionesService,
		private readonly snackBarService: SnackBarService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		public dialogRef: MatDialogRef<ReferenceCompleteStudyComponent>,
		public dialog: MatDialog,
		readonly buttonService: ButtonService,
		readonly controlTemplatesService: ControlTemplatesService,

	) { }

	ngOnInit() {
		this.formReferenceClosure = this.formBuilder.group({
			closureType: [null, [Validators.required]],
			description: [null, [Validators.required]],
			clinicalSpecialty: [null, [Validators.required]]
		}) as FormGroup & ReferenceClosureForm;

		this.closureTypes$ = this.referenceMasterDataService.getClosureTypes().pipe(
			map(((closureTypes: MasterDataDto[]) => 
				closureTypes.filter(closureType => closureType.description !== CLOSURE_OPTIONS[5].description))));

		this.clinicalSpecialties$ = forkJoin([this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties(), of(this.reference.clinicalSpecialties)])
			.pipe(
				map(([csProfessional, csReference]) => {
					return csReference.length ?
						csProfessional.filter(csp => csReference.some(csr => csr.id === csp.id))
						: csProfessional;
				}),
				tap(clinicalSpecialties => this.handleClinicalSpecialties(clinicalSpecialties)));
		this.formReferenceClosure.valueChanges.subscribe(_ =>
			this.buttonService.updateFormStatus(!this.formReferenceClosure.valid)
		);

		this.buttonService.submit$.subscribe(submit => {
			if (submit)
				this.completeStudy();
		});


		this.buttonService.submitPartialSave$.subscribe(submitPartialSlave => {
			if (submitPartialSlave)
				this.savedPartialStudy();
		});
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

	private completeStudy() {
		const completeRequest: CompleteRequestDto = this.buildRequest();

		let isPartialUpload = false;

		let reportObservations: AddDiagnosticReportObservationsCommandDto =
			this.controlTemplatesService.build(this.diagnosticReportId, isPartialUpload, completeRequest.referenceClosure);

		if (reportObservations?.procedureTemplateId && reportObservations?.values?.length > 0) {
			return this.prescripcionesService.completeStudyTemplateWhithForm(this.patientId,
				this.diagnosticReportId, completeRequest, this.selectedFiles, reportObservations).subscribe(_ => {
					this.snackBarService.showSuccess('ambulatoria.reference-study-close.SUCCESS');
					this.closeModal(false, true);
				}, error => {
					this.snackBarService.showError('ambulatoria.reference-study-close.ERROR_TITLE');
					this.closeModal(false, false);
				});
		} else {
			this.prescripcionesService.completeStudy(this.patientId, this.diagnosticReportId,
				completeRequest, this.selectedFiles).subscribe(_ => {
					this.snackBarService.showSuccess('ambulatoria.reference-study-close.SUCCESS');
					this.closeModal(false, true);
				}, error => {
					this.snackBarService.showError('ambulatoria.reference-study-close.ERROR_TITLE');
					this.closeModal(false, false);
					this.buttonService.resetLoading();
				});
		}
	}


	private partialSave() {
		const completeRequest: CompleteRequestDto = this.buildRequest();

		let isPartialUpload = true;

		let reportObservations: AddDiagnosticReportObservationsCommandDto =
			this.controlTemplatesService.build(this.diagnosticReportId, isPartialUpload, completeRequest.referenceClosure);

		if (reportObservations?.procedureTemplateId && reportObservations?.values?.length > 0) {
			return this.prescripcionesService.partialStudyTemplateWhithForm(this.patientId,
				this.diagnosticReportId, reportObservations).subscribe(_ => {
					this.snackBarService.showSuccess('ambulatoria.complete-info.SUCCESS');
					this.closeModal(false, true);
				}, error => {
					this.snackBarService.showSuccess('ambulatoria.complete-info.ERROR');
					this.closeModal(false, false);
				});
		}
	}

	private savedPartialStudy() {

		if (this.hasAnyFieldValue()) {
			const warnignComponent = this.dialog.open(DiscardWarningComponent,
				{
					disableClose: false,
					data: {
						title: 'ambulatoria.reference-study-close.warning-pop-up.TITLE',
						content: 'ambulatoria.reference-study-close.warning-pop-up.CONTENT',
						okButtonLabel: 'ambulatoria.reference-study-close.warning-pop-up.OK_BUTTON',
						cancelButtonLabel: 'ambulatoria.reference-study-close.warning-pop-up.CANCEL'
					},
					maxWidth: '500px'
				});
			warnignComponent.afterClosed().subscribe(confirmed =>
				confirmed ? this.partialSave() : this.buttonService.resetLoadingPartialSave()
			);
		}
		else
			this.partialSave();
	}

	private closeModal(simpleClose: boolean, completed?: boolean): void {
		this.dialogRef.close(simpleClose ? null : { completed });
	}

	private buildRequest(): CompleteRequestDto {
		return {
			observations: this.formReferenceClosure.value.description,
			referenceClosure: {
				referenceId: this.reference.id,
				clinicalSpecialtyId: this.formReferenceClosure.value.clinicalSpecialty.id,
				counterReferenceNote: this.formReferenceClosure.value.description,
				fileIds: [],
				closureTypeId: this.formReferenceClosure.value.closureType?.id
			}
		}
	}

	private handleClinicalSpecialties(clinicalSpecialties: ClinicalSpecialtyDto[]) {
		if (clinicalSpecialties.length) {
			const firstCS = clinicalSpecialties[0];
			this.formReferenceClosure.controls.clinicalSpecialty.setValue(firstCS);
		}
	}

	private hasAnyFieldValue(): boolean {
		const controls = this.formReferenceClosure.controls;

		for (const name in controls) {
			if (controls.hasOwnProperty(name)) {
				const control = controls[name];
				if (control.value !== null && control.value !== '') {
					return true;
				}
			}
		}
		return false || this.selectedFiles.length > 0;
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
	clinicalSpecialty: FormControl<ClinicalSpecialtyDto>;
}
