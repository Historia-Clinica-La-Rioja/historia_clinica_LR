import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { BasicPatientDto, GenderDto, IdentificationTypeDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import {getError, hasError, VALIDATIONS } from '@core/utils/form.utils';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { forkJoin, Observable } from 'rxjs';

@Component({
	selector: 'app-search-patient',
	templateUrl: './search-patient.component.html',
	styleUrls: ['./search-patient.component.scss']
})
export class SearchPatientComponent implements OnInit {


	formSearch: FormGroup;
	formSearchById: FormGroup;
	identificationTypes$: Observable<IdentificationTypeDto[]>;
	genders$: Observable<GenderDto[]>;
	cardPatient: {
		basicData: PatientBasicData,
		photo: PersonPhotoDto
	};
	foundPatient: Patient;
	readonly MIN_VALUE = 0;
	hasError = hasError;
	getError = getError;
	isFormSubmitted = false;
	constructor(
		private dialogRef: MatDialogRef<SearchPatientComponent>,
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly formBuilder: FormBuilder,
		private readonly patientService: PatientService,
		private readonly snackBarService: SnackBarService,
		private readonly mapperService: MapperService
	) { }

	ngOnInit(): void {
		this.genders$ = this.personMasterDataService.getGenders();
		this.identificationTypes$ = this.personMasterDataService.getIdentificationTypes();

		this.formSearch = this.formBuilder.group({
			identificationType: [IDENTIFICATION_TYPE_IDS.DNI, Validators.required],
			identificationNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			gender: [null, Validators.required],
		});

		this.formSearchById = this.formBuilder.group({
			id: [null, [Validators.min(this.MIN_VALUE) , Validators.required]]
		});
	}


	search(): void {
		this.isFormSubmitted = true;
		if (this.formSearch.valid) {
			this.cardPatient = null;
			const formSearchValue = this.formSearch.value;
			const searchRequest = {
				identificationTypeId: formSearchValue.identificationType,
				identificationNumber: +formSearchValue.identificationNumber.replace(REMOVE_SUBSTRING_DNI, ''),
				genderId: formSearchValue.gender,
			};
			this.patientService.getPatientMinimal(searchRequest).subscribe(
				(data: number[]) => {
					if (data.length) {
						this.patientSearch(data[0]);
					} else {
						this.snackBarService.showError('pacientes.search_patient.PATIENT_NOT_FOUND');
					}
				}
			);
		}
	}

	searchById(): void {
		if (this.formSearchById.valid) {
			this.cardPatient = null;
			this.patientSearch(this.formSearchById.value.id);
		}
	}

	clearResults(): void {
		this.cardPatient = null;
		this.isFormSubmitted = false;
	}

	selectPatient() {
		this.dialogRef.close(this.foundPatient);
	}

	private patientSearch(patientId: number) {

		const basicPatientDto$: Observable<BasicPatientDto> = this.patientService.getPatientBasicData(patientId);
		const photo$: Observable<PersonPhotoDto> = this.patientService.getPatientPhoto(patientId);

		forkJoin([basicPatientDto$, photo$])
			.subscribe(([basicPatientDto, photo]) => {
				this.cardPatient = {
					basicData: this.mapperService.toPatientBasicData(basicPatientDto),
					photo
				};

				this.foundPatient = {
					basicData: basicPatientDto,
					photo
				};
			}, _ => {
				this.snackBarService.showError('pacientes.search_patient.PATIENT_NOT_FOUND');
			});
	}
}

export class Patient {
	basicData: BasicPatientDto;
	photo: PersonPhotoDto;
}
