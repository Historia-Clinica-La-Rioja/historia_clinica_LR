import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatOptionSelectionChange } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MedicalCoverageDto } from '@api-rest/api-model';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { RenaperService } from '@api-rest/services/renaper.service';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { Observable, of } from 'rxjs';

const DNI_TYPE_ID = 1;
@Component({
	selector: 'app-medical-coverage',
	templateUrl: './medical-coverage.component.html',
	styleUrls: ['./medical-coverage.component.scss']
})

export class MedicalCoverageComponent implements OnInit {

	healthInsuranceForm: FormGroup;
	prepagaForm: FormGroup;
	patientHealthInsurances: PatientMedicalCoverage[];
	patientPrivateHealthInsurances: PatientMedicalCoverage[];
	healthInsuranceFilteredMasterData: MedicalCoverageDto[];

	loading = true;

	private patientHealthInsunracesFromService$: Observable<PatientMedicalCoverage[]> = of([
		{
			id: 1,
			medicalCoverage: {
				acronym: 'OSPEA', name: 'OBRA SOCIAL DEL PERSONAL DE DIRE', rnos: '1',
			},
			affiliateNumber: '100',
			validDate: newMoment()

		},
		{
			id: 2,
			medicalCoverage: {
				acronym: 'OSA', name: 'OBRA SOCIAL DE AERONAVEGANTES', rnos: '2',
			},
			affiliateNumber: '789',
			validDate: newMoment()
		},

	]);
	private patientPrivateHealthInsunracesFromService$: Observable<PatientMedicalCoverage[]> = of([
		{
			id: 1,
			medicalCoverage: {
				name: 'PREPAGA 1',
			},
			affiliateNumber: '100',
			validDate: newMoment()

		},
		{
			id: 2,
			medicalCoverage: {
				name: 'PREPAGA 2',
			},
			affiliateNumber: '200',
			validDate: newMoment()
		},
		{
			id: 3,
			medicalCoverage: {
				name: 'PREPAGA 3',
			},
			affiliateNumber: '300',
			validDate: newMoment()

		}

	]);
	private healthInsuranceToAdd: HealthInsurance;
	private healthInsuranceMasterData: MedicalCoverageDto[];
	constructor(
		private formBuilder: FormBuilder,
		private renaperService: RenaperService,
		public readonly healthInsuranceService: HealthInsuranceService,
		public dialogRef: MatDialogRef<MedicalCoverageComponent>,
		@Inject(MAT_DIALOG_DATA) public personInfo: {
			genderId: number;
			identificationNumber: string;
			identificationTypeId: number;
		},
	) {
		this.patientHealthInsunracesFromService$.subscribe(
			patientHealthInsurances => this.patientHealthInsurances = patientHealthInsurances
		).unsubscribe();

		this.patientPrivateHealthInsunracesFromService$.subscribe(
			patientPrivateHealthInsurances =>
				this.patientPrivateHealthInsurances = patientPrivateHealthInsurances
		).unsubscribe();

	}


	ngOnInit(): void {

		this.healthInsuranceService.getAll().subscribe((values: MedicalCoverageDto[]) => {
			this.healthInsuranceFilteredMasterData = values;
			this.healthInsuranceMasterData = values;
		}
		);

		this.healthInsuranceForm = this.formBuilder.group({
			healthInsurance: [null, Validators.required],
			affiliateNumber: []
		});

		this.prepagaForm = this.formBuilder.group({
			name: [null, Validators.required],
			affiliateNumber: [],
			plan: [],
			startDate: [],
			endDate: []
		});

		this.healthInsuranceForm.controls.healthInsurance.valueChanges.subscribe((newValue: string) => {
			if (newValue) {
				this.healthInsuranceFilteredMasterData =
					this.healthInsuranceMasterData
						.filter(data => this.getFullHealthInsuranceText(data).toLowerCase().includes(newValue.toLowerCase()));
			} else {
				this.healthInsuranceFilteredMasterData = this.healthInsuranceMasterData;
			}
		});


		if (this.personInfo.identificationTypeId === DNI_TYPE_ID) {
			this.renaperService.getHealthInsurance
				({ genderId: this.personInfo.genderId, identificationNumber: this.personInfo.identificationNumber })
				.subscribe((healthInsurances: MedicalCoverageDto[]) => {
					if (healthInsurances) {
						console.log('Resultados de renaper', healthInsurances);

						healthInsurances.forEach(healthInsurance => {
							const patientMedicalCoverage = this.patientHealthInsurances
								.find(patientHealthInsurance => (patientHealthInsurance.medicalCoverage as HealthInsurance).rnos === healthInsurance.rnos);
							if (patientMedicalCoverage) {
								patientMedicalCoverage.validDate = newMoment() //'La fecha que te trae renaper';
							} else {
								this.patientHealthInsurances = this.patientHealthInsurances.concat(fromRenaperToPatientMedicalCoverage(healthInsurance));
							}
						});
					}
					this.loading = false;

					function fromRenaperToPatientMedicalCoverage(healthInsurance: MedicalCoverageDto): PatientMedicalCoverage {
						const medicalCoverage = {
							name: healthInsurance.name,
							acronym: healthInsurance.acronym,
							rnos: healthInsurance.rnos
						};

						return {
							id: null,
							affiliateNumber: null,
							medicalCoverage,
							validDate: newMoment() // Hay que usar la fecha que en realidad viene de renaper
						};
					}
				}, error => this.loading = false);
		} else {
			this.loading = false;
		}
	}


	selectHealthInsurance(event: MatOptionSelectionChange, healthInsurance: MedicalCoverageDto): void {
		if (event.isUserInput) {
			this.healthInsuranceToAdd = fromHealthInsuranceMasterDataToHealthInsurance(healthInsurance);
		}

		function fromHealthInsuranceMasterDataToHealthInsurance(renaperResponse: MedicalCoverageDto): HealthInsurance {
			return {
				name: renaperResponse.name,
				acronym: renaperResponse.acronym,
				rnos: renaperResponse.rnos
			};
		}
	}


	// ---------------Estaria bueno que sean parte de cada clase y que cada uno implemente el getTitleText() (o algo asi) a su manera-------
	getFullHealthInsuranceText(healthInsurance: MedicalCoverageDto): string {
		return [healthInsurance.acronym, healthInsurance.name].filter(Boolean).join(' - ');
	}

	getPrivateHealthInsuranceText(medicalCoverage: PrivateHealthInsurance): string {
		return [medicalCoverage.name, medicalCoverage.plan].filter(Boolean).join(' / ');
	}

	getDatesText(patientMedicalCoverage: PatientMedicalCoverage): string {
		const initText = patientMedicalCoverage.privateHealthInsuranceDetails?.startDate ? 'desde ' +
			momentFormat(patientMedicalCoverage.privateHealthInsuranceDetails.startDate, DateFormat.VIEW_DATE) : '';

		const endText = patientMedicalCoverage.privateHealthInsuranceDetails?.endDate ? ' hasta ' +
			momentFormat(patientMedicalCoverage.privateHealthInsuranceDetails.endDate, DateFormat.VIEW_DATE) : '';

		return initText + endText;
	}
	// -----------------------------------------------------------------------------------------------------------------------------

	addHealthInsurance(): void {
		if (this.healthInsuranceForm.valid) {
			const toAdd = this.getHealthInsuranceToAdd();
			this.patientHealthInsurances = this.patientHealthInsurances.concat(toAdd);
			this.healthInsuranceForm.reset();
		}
	}

	addPrivateHealthInsurance(): void {
		if (this.prepagaForm.valid) {
			const toAdd = this.getPrivateHealthInsuranceToAdd();
			this.patientPrivateHealthInsurances = this.patientPrivateHealthInsurances.concat(toAdd);
			this.prepagaForm.reset();
		}
	}

	save() {
		this.dialogRef.close({
			patientHealthInsurances: this.patientHealthInsurances,
			patientPrivateHealthInsurances: this.patientPrivateHealthInsurances
		});
	}

	private getPrivateHealthInsuranceToAdd(): PatientMedicalCoverage {
		const medicalCoverage: PrivateHealthInsurance = {
			name: this.prepagaForm.value.name,
			plan: this.prepagaForm.value.plan
		};

		let privateHealthInsuranceDetails;
		if (this.prepagaForm.value.startDate || this.prepagaForm.value.endDate) {
			privateHealthInsuranceDetails = {
				startDate: this.prepagaForm.value.startDate,
				endDate: this.prepagaForm.value.endDate
			};
		}

		const toAdd: PatientMedicalCoverage = {
			id: null, // Este valor se lo tiene que asignar la BD entonces hay que ver si hay que hacer un dto nuevo o que
			medicalCoverage,
			affiliateNumber: this.prepagaForm.value.affiliateNumber,
			validDate: newMoment(),
			privateHealthInsuranceDetails,
		}
		return toAdd;
	}

	private getHealthInsuranceToAdd(): PatientMedicalCoverage {
		const toAdd: PatientMedicalCoverage = {
			id: null, // Este valor se lo tiene que asignar la BD entonces hay que ver si hay que hacer un dto nuevo o que
			medicalCoverage: this.healthInsuranceToAdd,
			affiliateNumber: this.healthInsuranceForm.value.affiliateNumber,
			validDate: newMoment()
		};
		return toAdd;
	}
}

export interface PatientMedicalCoverage {
	id: number; // Me sirve por si edito el campo de nro de afiliado
	affiliateNumber: string;
	validDate: Moment;
	medicalCoverage: HealthInsurance | PrivateHealthInsurance;
	privateHealthInsuranceDetails?: {
		startDate?: Moment;
		endDate?: Moment;
	};
}
export interface MedicalCoverage {
	//id: number;
	name: string;
}

export interface HealthInsurance extends MedicalCoverage {
	rnos: string;
	acronym?: string;
}

export interface PrivateHealthInsurance extends MedicalCoverage {
	plan?: string;
}
