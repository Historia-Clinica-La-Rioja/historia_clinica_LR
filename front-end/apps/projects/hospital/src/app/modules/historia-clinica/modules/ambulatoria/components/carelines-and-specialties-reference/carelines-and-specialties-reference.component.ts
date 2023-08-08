import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UntypedFormGroup, Validators } from '@angular/forms';
import { AddressDto, CareLineDto, ClinicalSpecialtyDto, ReferenceProblemDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ReferenceProblemsService } from '../../services/reference-problems.service';

@Component({
	selector: 'app-carelines-and-specialties-reference',
	templateUrl: './carelines-and-specialties-reference.component.html',
	styleUrls: ['./carelines-and-specialties-reference.component.scss']
})

export class CarelinesAndSpecialtiesReferenceComponent implements OnInit {

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
	) { }

	ngOnInit(): void {
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
	setSpecialty(){
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

	private setAllSpecialties() {
		if (this.provinceId)
			this.clinicalSpecialty.getClinicalSpecialtiesByProvinceId(this.provinceId).subscribe((clinicalSpecialties: ClinicalSpecialtyDto[]) => {
				this.allClinicalSpecialties = clinicalSpecialties;
				this.allSpecialtiesSubject$.next(clinicalSpecialties);
			});
	}
}
