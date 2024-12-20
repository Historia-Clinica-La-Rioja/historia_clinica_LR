import { Component, Inject, OnInit, ChangeDetectorRef, AfterContentChecked } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, HCEHealthConditionDto, ReferenceProblemDto, MasterDataDto, ReferenceStudyDto } from '@api-rest/api-model';
import { ReferenceOriginInstitutionService } from '../../services/reference-origin-institution.service';
import { ReferenceProblemsService } from '../../services/reference-problems.service';
import { Observable, tap } from 'rxjs';
import { ReferenceMasterDataService } from '@api-rest/services/reference-master-data.service';
import { PRIORITY } from '../../constants/reference-masterdata';
import { VALIDATIONS } from '@core/utils/form.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { TranslateService } from '@ngx-translate/core';
import { BoxMessageInformation } from '@presentation/components/box-message/box-message.component';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';

@Component({
	selector: 'app-reference',
	templateUrl: './reference.component.html',
	styleUrls: ['./reference.component.scss'],
	providers: [ReferenceOriginInstitutionService, ReferenceProblemsService]
})
export class ReferenceComponent implements OnInit, AfterContentChecked {
	formReference: UntypedFormGroup;
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	DEFAULT_RADIO_OPTION = true;
	submitForm = false;
	updateDepartamentsAndInstitution = false;
	updateSpecialtiesAndCarelineFields = false;
	clearCarelinesAndSpecialties = false;
	priorities$: Observable<MasterDataDto[]>;
	regulationRequired = false;
	boxMessageInfo: BoxMessageInformation;

	PRIORITY = PRIORITY;
	readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: any,
		readonly referenceProblemsService: ReferenceProblemsService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialogRef: MatDialogRef<ReferenceComponent>,
		private changeDetector: ChangeDetectorRef,
		private readonly referenceMasterData: ReferenceMasterDataService,
		private readonly translateService: TranslateService
	) { }

	ngOnInit(): void {

		this.createReferenceForm();

		this.disableInputs();

		this.priorities$ = this.referenceMasterData.getPriorities().pipe(
			tap((priorities) => {
				const lowPriority = priorities.find(priority => priority.id === PRIORITY.LOW);
				this.formReference.controls.priority.setValue(lowPriority);
			}));

		this.boxMessageInfo = {
			message: '',
			showButtons: false
		}
		this.translateService.get('ambulatoria.paciente.nueva-consulta.solicitud-referencia.REGULATION_REQUIRED')
			.subscribe( message => this.boxMessageInfo.message = message );
	}

	ngAfterContentChecked(): void {
		this.changeDetector.detectChanges();
	}

	save(): void {
		this.submitForm = true;
		this.markInputsAsTouched();
		if (this.formReference.valid) {
			const reference = { data: this.buildReference(), files: this.selectedFiles, problems: this.referenceProblemsService.getReferenceProblems() };
			this.dialogRef.close(reference);
		}
	}

	private markInputsAsTouched() {
		this.formReference.controls.careLine.markAllAsTouched();
		this.formReference.controls.clinicalSpecialties.markAllAsTouched();
		this.formReference.controls.problems.markAsTouched();
	}

	private buildReference(): Reference {
		return {
			careLine: this.formReference.controls.careLine.value,
			clinicalSpecialties: this.formReference.controls.clinicalSpecialties.value,
			consultation: this.formReference.controls.consultation.value,
			note: this.formReference.value.summary,
			problems: this.referenceProblemsService.mapProblems(),
			procedure: false,
			fileIds: [],
			destinationInstitutionId: this.formReference.value.institutionDestinationId? this.formReference.value.institutionDestinationId : null,
			phonePrefix: this.formReference.value.phonePrefix,
			phoneNumber: this.formReference.value.phoneNumber,
			priority: this.formReference.value.priority.id,
			referenceStudy: this.createReferenceStudy(),
		}
	}

	private createReferenceStudy(): ReferenceStudyDto {
		if (!this.formReference.value.consultation)
			return {
				problem:  this.referenceProblemsService.firstProblem(),
				practice: this.formReference.controls.practiceOrProcedure.value || null,
				categoryId: this.formReference.value.studyCategory?.id.toString(),
			}
		return null;
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

	onDepartmentSelectionChange(department: number) {
		this.formReference.controls.departmentId.setValue(department);
	}

	onInstitutionSelectionChange(institutionId: number) {
		this.formReference.controls.institutionDestinationId.setValue(institutionId);
		this.activateSpecialtiesAndCarelineFields();
		this.activateDepartamentsAndInstitution();
	}

	onProvinceSelectionChange(province: number) {
		this.formReference.controls.provinceId.setValue(province);
		this.clearCarelinesAndSpecialties = true;
	}

	onRegulationRequiredChange(value : boolean){
		this.regulationRequired = value;
	}

	activateDepartamentsAndInstitution() {
		this.updateDepartamentsAndInstitution = true;
	}

	resetControls() {
		this.updateSpecialtiesAndCarelineFields = false;
		this.clearCarelinesAndSpecialties = false;
		this.changeDetector.detectChanges();
	}

	activateSpecialtiesAndCarelineFields() {
		this.updateSpecialtiesAndCarelineFields = true;
	}

	private createReferenceForm() {
		this.formReference = this.formBuilder.group({
			problems: [null, [Validators.required]],
			searchByCareLine: [this.DEFAULT_RADIO_OPTION],
			provinceId: [null],
			departmentId: [null],
			consultation: [true],
			procedure: [null],
			careLine: [null, [Validators.required]],
			clinicalSpecialties: [null, [Validators.required]],
			institutionDestinationId: [null],
			summary: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			provinceOrigin: [null],
			departmentOrigin: [null],
			institutionOrigin: [null],
			phoneNumber: [null, [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]],
			phonePrefix: [null, [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]],
			priority: [null],
			studyCategory: [null],
			practiceOrProcedure: [null],
		});
	}

	private disableInputs() {
		this.formReference.controls.clinicalSpecialties.disable();
	}

}

export interface HCEPersonalHistory {
	HCEHealthConditionDto: HCEHealthConditionDto;
	chronic: boolean
}

export interface Reference {
	careLine: CareLineDto,
	clinicalSpecialties: ClinicalSpecialtyDto[],
	consultation: boolean;
	destinationInstitutionId: number;
	fileIds: number[];
	note?: string;
	problems: ReferenceProblemDto[];
	procedure?: boolean;
	phoneNumber: string;
	phonePrefix: string;
	priority: number;
	referenceStudy: ReferenceStudyDto;
}
