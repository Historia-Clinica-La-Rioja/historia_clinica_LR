import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UntypedFormGroup, Validators } from '@angular/forms';
import { AddressDto, CareLineDto, ClinicalSpecialtyDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Component({
	selector: 'app-carelines-and-specialties-reference',
	templateUrl: './carelines-and-specialties-reference.component.html',
	styleUrls: ['./carelines-and-specialties-reference.component.scss']
})

export class CarelinesAndSpecialtiesReferenceComponent implements OnInit {

	@Input() formReference: UntypedFormGroup;
	@Input() set setProvinceId(provinceId: number) {
		if (provinceId) {
			this.careLineService.getCareLinesByProvinceId(provinceId).subscribe((careLine: CareLineDto[]) =>
				this.careLines = careLine
			);
			this.clinicalSpecialty.getClinicalSpecialtiesByProvinceId(provinceId).subscribe((clinicalSpecialties: ClinicalSpecialtyDto[]) => {
				this.allClinicalSpecialties = clinicalSpecialties;
				this.specialtiesSubject.next(clinicalSpecialties);
			});
		}
	};

	@Output() updateDepartamentsAndInstitution = new EventEmitter();
	careLines: CareLineDto[] = [];
	allClinicalSpecialties: ClinicalSpecialtyDto[] = [];
	specialtiesSubject = new BehaviorSubject<ClinicalSpecialtyDto[]>([]);
	allSpecialtiesSubject = new BehaviorSubject<ClinicalSpecialtyDto[]>([]);
	specialties$: Observable<ClinicalSpecialtyDto[]>;
	DEFAULT_RADIO_OPTION = true;

	originInstitutionInfo: AddressDto;
	constructor(
		private readonly careLineService: CareLineService,
		private readonly clinicalSpecialty: ClinicalSpecialtyService,
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
			this.specialtiesSubject.next(this.formReference.value.careLine.clinicalSpecialties);
		}
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
		this.specialtiesSubject.next(this.allClinicalSpecialties);
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
				this.updateClinicalSpecialtyFormField();
			} else {
				this.updateCareLineFormField();
				this.setSpecialties();
			}
		});
	}

	private updateClinicalSpecialtyFormField() {
		this.formReference.controls.careLine.setValidators([Validators.required]);
		this.formReference.controls.clinicalSpecialtyId.updateValueAndValidity();
	}

	private updateCareLineFormField() {
		this.formReference.controls.careLine.removeValidators([Validators.required]);
		this.formReference.controls.careLine.setValue(null);
		this.formReference.controls.careLine.updateValueAndValidity();
	}

	clearFormFields() {

		this.specialtiesSubject.next(null);

	}



}
