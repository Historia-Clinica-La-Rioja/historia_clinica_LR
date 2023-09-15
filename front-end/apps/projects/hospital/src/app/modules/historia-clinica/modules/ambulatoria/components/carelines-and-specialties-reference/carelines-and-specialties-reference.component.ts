import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UntypedFormGroup, Validators } from '@angular/forms';
import { AddressDto, CareLineDto, ClinicalSpecialtyDto, PracticeDto, ReferenceProblemDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ReferenceProblemsService } from '../../services/reference-problems.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { PracticesService } from '@api-rest/services/practices.service';
import { CareLineInstitutionPracticeService } from '@api-rest/services/care-line-institution-practice.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
	selector: 'app-carelines-and-specialties-reference',
	templateUrl: './carelines-and-specialties-reference.component.html',
	styleUrls: ['./carelines-and-specialties-reference.component.scss']
})

export class CarelinesAndSpecialtiesReferenceComponent implements OnInit {
	studyCategories$: Observable<any[]>;
	practices$: Observable<any[]> = null;
	practices = [];
	originalPractices = [];
	submit = false;
	@Input() set submitForm(submit: boolean) {
		this.submit = submit;
		if (submit && !this.formReference.value.consultation) {
			this.formReference.controls.studyCategory.markAllAsTouched();
			this.formReference.controls.practiceOrProcedure.markAllAsTouched();
		}
	};

	@Input() formReference: UntypedFormGroup;
	@Input() set setProvinceId(provinceId: number) {
		this.provinceId = provinceId;
		this.setAllSpecialties();
	};
	@Input() problems: any[];
	@Output() updateDepartamentsAndInstitution = new EventEmitter();
	@Input() set updateFormFields(problems: any[]) {
		this.setProblems = problems.map(problem => problem.snomed.sctid);
		this.setCareLines();
	}
	careLines: CareLineDto[] = [];
	allClinicalSpecialties: ClinicalSpecialtyDto[] = [];
	specialtiesSubject$ = new BehaviorSubject<ClinicalSpecialtyDto[]>([]);
	allSpecialtiesSubject$ = new BehaviorSubject<ClinicalSpecialtyDto[]>([]);
	specialties$: Observable<ClinicalSpecialtyDto[]>;
	DEFAULT_RADIO_OPTION = true;
	provinceId: number;
	originInstitutionInfo: AddressDto;
	setProblems: any[] = [];
	constructor(
		private readonly careLineService: CareLineService,
		private readonly clinicalSpecialty: ClinicalSpecialtyService,
		private readonly referenceProblemsService: ReferenceProblemsService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly practicesService: PracticesService,
		private readonly careLineInstitutionPracticeService: CareLineInstitutionPracticeService,

	) { }

	ngOnInit(): void {
		this.formReference.controls.searchByCareLine.valueChanges.subscribe((changes) => {
			if (!changes) {
				this.formReference.controls.practiceOrProcedure.setValue(null);
				this.formReference.controls.practiceOrProcedure.reset();
				this.practicesService.getPracticesFromInstitutions().subscribe((practices) => {
					this.originalPractices = practices;
					this.practices = this.toTypeaheadOptions(practices, 'pt');
				});
			}
		});

		this.formReference.controls.careLine.valueChanges.subscribe((changes) => {
			if (changes) {
				this.formReference.controls.practiceOrProcedure.setValue(null);
				this.formReference.controls.practiceOrProcedure.reset();
				this.careLineInstitutionPracticeService.getPracticesByCareLine(changes.id).subscribe((practices) => {
					this.originalPractices = practices;
					this.practices = this.toTypeaheadOptions(practices, 'pt');
				});
			}
		})

		this.studyCategories$ = this.requestMasterDataService.categories();
		this.subscribesToChangesInForm();
	}


	setSpecialtyCareLine(): void {
		const careLine = this.formReference.value.careLine;
		if (careLine) {
			this.formReference.controls.clinicalSpecialtyId.enable();
			this.formReference.controls.clinicalSpecialtyId.setValidators([Validators.required]);
			this.formReference.updateValueAndValidity();
			this.specialtiesSubject$.next(this.formReference.value.careLine.clinicalSpecialties);
		}
	}
	setSpecialty() {
		this.formReference.controls.clinicalSpecialtyId.setValue(null);
		this.formReference.controls.clinicalSpecialtyId.reset();
	}

	setInformation() {
		if (!this.formReference.value.institutionDestinationId) {
			this.clearFormFields();
			return;
		}
		if (this.formReference.value.searchByCareLine != this.DEFAULT_RADIO_OPTION)
			this.setSpecialties();

	}

	setSpecialtiesByProvince() {
		this.specialtiesSubject$.next(this.allClinicalSpecialties);
	}

	private setSpecialties() {
		const institutionId = this.formReference.value.institutionDestinationId;
		if (institutionId) {
			this.formReference.controls.clinicalSpecialtyId.enable();
			this.specialties$ = this.clinicalSpecialty.getClinicalSpecialtyByInstitution(institutionId);
			this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
		}
	}

	private subscribesToChangesInForm() {
		this.formReference.controls.searchByCareLine.valueChanges.subscribe(option => {
			if (option === this.DEFAULT_RADIO_OPTION) {
				disableInputs(this.formReference, this.referenceProblemsService.mapProblems());
				this.updateClinicalSpecialtyFormField();
			} else {
				this.formReference.controls.clinicalSpecialtyId.enable();
				this.updateCareLineFormField();
				this.setSpecialties();
			}
			function disableInputs(formReference: UntypedFormGroup, referenceProblemDto: ReferenceProblemDto[]) {
				if (referenceProblemDto.length === 0) {
					formReference.controls.careLine.disable();
				}
			}
		});
	}

	private updateClinicalSpecialtyFormField() {
		this.formReference.controls.clinicalSpecialtyId.reset();
		this.formReference.controls.clinicalSpecialtyId.disable();
		this.formReference.controls.careLine.setValidators([Validators.required]);
		this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
	}

	private updateCareLineFormField() {
		this.formReference.controls.careLine.removeValidators([Validators.required]);
		this.formReference.controls.careLine.setValue(null);
		this.formReference.controls.careLine.updateValueAndValidity();
	}

	private toTypeaheadOptions(response: any[], attribute: string): TypeaheadOption<any>[] {
		return response.map(r => {
			return {
				value: r.id,
				compareValue: r[attribute],
				viewValue: r[attribute]
			}
		})
	}

	clearFormFields() {

		this.specialtiesSubject$.next(null);

	}

	setCareLines() {
		this.formReference.controls.careLine.reset();

		if (this.formReference.value.searchByCareLine) {
			this.formReference.controls.clinicalSpecialtyId.reset();
			this.formReference.controls.clinicalSpecialtyId.disable();
		}

		if (this.setProblems.length > 0) {
			this.careLineService.getByProblemSnomedSctids(this.setProblems).subscribe((careLine: CareLineDto[]) => {
				this.careLines = careLine;
			});
		}

		if (this.setProblems.length > 0) {
			this.formReference.controls.careLine.enable();
		} else {
			this.formReference.controls.careLine.disable();

		}

	}

	selectedOption($event: any) {
		this.formReference.controls.consultation.setValue(!!!$event);
		if ($event) {
			this.formReference.controls.clinicalSpecialtyId.removeValidators([Validators.required]);
			this.formReference.controls.clinicalSpecialtyId.setValue(null);
			this.formReference.controls.practiceOrProcedure.setValidators([Validators.required]);
			this.formReference.controls.studyCategory.setValidators([Validators.required]);
		}
		else {
			this.formReference.controls.practiceOrProcedure.removeValidators([Validators.required]);
			this.formReference.controls.practiceOrProcedure.setValue(null);
			this.formReference.controls.studyCategory.removeValidators([Validators.required]);
			this.formReference.controls.studyCategory.setValue(null);
			this.formReference.controls.clinicalSpecialtyId.setValidators([Validators.required]);
		}

		this.formReference.controls.practiceOrProcedure.updateValueAndValidity();
		this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
		this.formReference.controls.studyCategory.updateValueAndValidity();
	}

	onPracticeSelectionChange($event: any) {
		const practice = this.originalPractices.find((p: PracticeDto) => p.id === $event);
		this.formReference.controls.practiceOrProcedure.setValue(practice);
	}

	private setAllSpecialties() {
		if (this.provinceId)
			this.clinicalSpecialty.getClinicalSpecialtiesByProvinceId(this.provinceId).subscribe((clinicalSpecialties: ClinicalSpecialtyDto[]) => {
				this.allClinicalSpecialties = clinicalSpecialties;
				this.allSpecialtiesSubject$.next(clinicalSpecialties);
			});
	}
}
