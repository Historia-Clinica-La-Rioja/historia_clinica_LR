import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { BasicPatientDto, ERole, GenderDto, IdentificationTypeDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import {getError, hasError, VALIDATIONS } from '@core/utils/form.utils';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { MapperService } from '@presentation/services/mapper.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { catchError, forkJoin, Observable, of, take } from 'rxjs';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';

const ROUTE_SEARCH = 'pacientes';

@Component({
	selector: 'app-search-patient',
	templateUrl: './search-patient.component.html',
	styleUrls: ['./search-patient.component.scss'],
})
export class SearchPatientComponent implements OnInit {

	@Output() onSelectedPatient = new EventEmitter<Patient>();
	formSearch: UntypedFormGroup;
	formSearchById: UntypedFormGroup;
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
	showAddPatient = false;

	private readonly routePrefix = `institucion/${this.contextService.institutionId}/`;

	constructor(
		private readonly permissionService: PermissionsService,
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly patientService: PatientService,
		private readonly snackBarService: SnackBarService,
		private readonly mapperService: MapperService,
		private readonly router: Router,
		private readonly contextService: ContextService
	) {	}

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
		this.showAddPatient = false;
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
						this.patientNotFound();
					}
				}
			);
		}
	}

	searchById(): void {
		this.isFormSubmitted = true;
		this.showAddPatient = false;
		if (this.formSearchById.valid) {
			this.cardPatient = null;
			this.patientService.getPatientBasicData(this.formSearchById.value.id).pipe(
				catchError(error => {
					if (error.status === 400) {
						this.snackBarService.showError('pacientes.search_patient.PATIENT_NOT_FOUND');
						this.patientNotFound();
					}
					return of(null);
				})
			).subscribe(
				(data: BasicPatientDto) => {
					if (data) {
						this.patientSearch(this.formSearchById.value.id);
					} else {
						this.snackBarService.showError('pacientes.search_patient.PATIENT_NOT_FOUND');
						this.patientNotFound();
					}
				}
			);
		}
	}

	clearResults(): void {
		this.cardPatient = null;
		this.isFormSubmitted = false;
		this.showAddPatient = false;
		this.onSelectedPatient.emit(null);
	}

	selectPatient() {
		this.onSelectedPatient.emit(this.foundPatient);
	}

	private patientSearch(patientId: number) {
		this.showAddPatient = false;
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
				this.patientNotFound();
			});
	}

	private patientNotFound(){
		this.permissionService.hasContextAssignments$([ERole.ADMINISTRATIVO])
			.pipe(take(1)).subscribe(hasRole => {
				this.showAddPatient = hasRole;
			});
	}

	goCreatePatient() {
		this.router.navigate([this.routePrefix + ROUTE_SEARCH],
			{
				queryParams: {
					fromGuardModule: true,
					identificationTypeId: this.formSearch.value.identificationType,
					identificationNumber: this.formSearch.value.identificationNumber,
					genderId: this.formSearch.value.gender
				}
			});
	}
}

export class Patient {
	basicData: BasicPatientDto;
	photo: PersonPhotoDto;
}
