import { Component, Inject, OnInit, ChangeDetectorRef, AfterContentChecked } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, HCEPersonalHistoryDto, ReferenceDto, ReferenceProblemDto } from '@api-rest/api-model';
import { ReferenceOriginInstitutionService } from '../../services/reference-origin-institution.service';

@Component({
	selector: 'app-reference',
	templateUrl: './reference.component.html',
	styleUrls: ['./reference.component.scss'],
	providers: [ReferenceOriginInstitutionService]
})
export class ReferenceComponent implements OnInit, AfterContentChecked {

	formReference: FormGroup;
	problemsList: any[] = [];
	referenceProblemDto: ReferenceProblemDto[] = [];
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	DEFAULT_RADIO_OPTION = '0';
	submitForm = false;
	updateSpecialtiesAndCarelineFields = false;
	clearCarelinesAndSpecialties = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		private readonly formBuilder: FormBuilder,
		private readonly dialogRef: MatDialogRef<ReferenceComponent>,
		private changeDetector: ChangeDetectorRef
	) { }

	ngOnInit(): void {
		this.createReferenceForm();

		this.disableInputs();
	}

	ngAfterContentChecked(): void {
		this.changeDetector.detectChanges();
	}

	private mapProblems(problems: any[]): ReferenceProblemDto[] {
		return problems.map(problem => ({
			id: problem.id,
			snomed: problem.snomed,
		}));
	}

	save(): void {
		this.submitForm = true;
		this.formReference.controls.careLine.markAllAsTouched();
		this.formReference.controls.clinicalSpecialtyId.markAllAsTouched();
		if (this.formReference.valid) {
			const reference = { data: this.buildReference(), files: this.selectedFiles, problems: this.getReferenceProblems() };
			this.dialogRef.close(reference);
		}
	}

	private buildReference(): ReferenceDto {
		return {
			careLineId: this.formReference.controls.careLine.value,
			clinicalSpecialtyId: this.formReference.controls.clinicalSpecialtyId.value,
			consultation: true,
			note: this.formReference.value.summary,
			problems: this.mapProblems(this.referenceProblemDto),
			procedure: false,
			fileIds: [],
			destinationInstitutionId: this.formReference.value.institutionDestinationId,
			phonePrefix: this.formReference.value.phonePrefix,
			phoneNumber: this.formReference.value.phoneNumber
		}
	}

	onSelectFileFormData($event): void {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
	}

	removeSelectedFile(index): void {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}

	private getReferenceProblems(): HCEPersonalHistory[] {
		let referenceProblems: HCEPersonalHistory[] = [];
		this.referenceProblemDto.forEach(referenceProblemDto => {
			const problemToAdd = this.problemsList.find(problem => problem.hcePersonalHistoryDto.snomed.sctid === referenceProblemDto.snomed.sctid)
			referenceProblems.push(problemToAdd);

		});
		return referenceProblems;
	}

	onProvinceSelectionChange(province: number) {
		this.formReference.controls.provinceId.setValue(province);
		this.clearCarelinesAndSpecialties = true;
	}

	onDepartmentSelectionChange(department: number) {
		this.formReference.controls.departmentId.setValue(department);
		if (department) {
			this.clearCarelinesAndSpecialties = true;
		}
	}

	onInstitutionSelectionChange(institutionId: number) {
		this.formReference.controls.institutionDestinationId.setValue(institutionId);
		this.activateSpecialtiesAndCarelineFields()
	}

	activateSpecialtiesAndCarelineFields() {
		this.updateSpecialtiesAndCarelineFields = true;
	}

	setReferenceProblemsDto(referenceProblemDto: ReferenceProblemDto[]) {
		this.referenceProblemDto = referenceProblemDto;
	}

	setProblemsList(problems: any[]) {
		this.problemsList = problems;
	}

	resetControls() {
		this.updateSpecialtiesAndCarelineFields = false;
		this.clearCarelinesAndSpecialties = false;
		this.changeDetector.detectChanges();
	}

	private createReferenceForm() {
		this.formReference = this.formBuilder.group({
			problems: [null, [Validators.required]],
			searchByCareLine: [this.DEFAULT_RADIO_OPTION],
			provinceId: [null],
			departmentId: [null],
			consultation: [null],
			procedure: [null],
			careLine: [null, [Validators.required]],
			clinicalSpecialtyId: [null, [Validators.required]],
			institutionDestinationId: [null, [Validators.required]],
			summary: [null],
			provinceOrigin: [null],
			departmentOrigin: [null],
			institutionOrigin: [null],
			phoneNumber: [null, [Validators.required, Validators.maxLength(20)]],
			phonePrefix: [null, [Validators.required, Validators.maxLength(10)]]
		});
	}

	private disableInputs() {
		this.formReference.controls.clinicalSpecialtyId.disable();
		this.formReference.controls.procedure.disable();
		this.formReference.controls.careLine.disable();
	}

}

export interface HCEPersonalHistory {
	hcePersonalHistoryDto: HCEPersonalHistoryDto;
	chronic: boolean
}

export interface Reference {
	careLine: CareLineDto,
	clinicalSpecialty: ClinicalSpecialtyDto,
	consultation: boolean;
	destinationInstitutionId: number;
	fileIds: number[];
	note?: string;
	problems: ReferenceProblemDto[];
	procedure?: boolean;
	phoneNumber: string;
	phonePrefix: string;
}
