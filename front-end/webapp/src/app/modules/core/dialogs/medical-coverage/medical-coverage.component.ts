import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatOptionSelectionChange } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CoverageDtoUnion, HealthInsuranceDto, MedicalCoverageDto, PrivateHealthInsuranceDto } from '@api-rest/api-model';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { RenaperService } from '@api-rest/services/renaper.service';
import { DateFormat, momentFormat, momentParse, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

const DNI_TYPE_ID = 1;
@Component({
	selector: 'app-medical-coverage',
	templateUrl: './medical-coverage.component.html',
	styleUrls: ['./medical-coverage.component.scss']
})

export class MedicalCoverageComponent implements OnInit {

	healthInsuranceForm: FormGroup;
	prepagaForm: FormGroup;
	patientMedicalCoverages: PatientMedicalCoverage[];
	healthInsuranceFilteredMasterData: MedicalCoverageDto[];

	loading = true;

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
			initValues: PatientMedicalCoverage[]
		},
	) {
		this.patientMedicalCoverages = this.personInfo.initValues ? this.personInfo.initValues : [];
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
						healthInsurances.forEach(healthInsurance => {
							const patientMedicalCoverage = this.patientMedicalCoverages
								.find(patientHealthInsurance => (patientHealthInsurance.medicalCoverage as HealthInsurance).rnos === healthInsurance.rnos);
							if (!patientMedicalCoverage) {
								this.patientMedicalCoverages = this.patientMedicalCoverages.concat(this.fromRenaperToPatientMedicalCoverage(healthInsurance));
							} else if (healthInsurance.dateQuery) {
								patientMedicalCoverage.validDate = momentParse(healthInsurance.dateQuery, DateFormat.YEAR_MONTH);
							}
						});
					}
					this.loading = false;

				}, error => this.loading = false);
		} else {
			this.loading = false;
		}
	}


	selectHealthInsurance(event: MatOptionSelectionChange, healthInsurance: MedicalCoverageDto): void {
		if (event.isUserInput) {
			this.healthInsuranceToAdd = this.fromHealthInsuranceMasterDataToHealthInsurance(healthInsurance);
		}
	}

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
		if (this.healthInsuranceToAdd && this.healthInsuranceForm.valid) {
			const toAdd = this.getHealthInsuranceToAdd();
			this.patientMedicalCoverages = this.patientMedicalCoverages.concat(toAdd);
			this.healthInsuranceForm.reset();
			this.healthInsuranceToAdd = null;
		}
	}

	addPrivateHealthInsurance(): void {
		if (this.prepagaForm.valid) {
			const toAdd = this.getPrivateHealthInsuranceToAdd();
			this.patientMedicalCoverages = this.patientMedicalCoverages.concat(toAdd);
			this.prepagaForm.reset();
		}
	}

	save() {
		this.dialogRef.close({
			patientMedicalCoverages: this.patientMedicalCoverages.filter(pmc => pmc.id || isNewAndNotDeleted(pmc))
		});

		function isNewAndNotDeleted(pmc: PatientMedicalCoverage): boolean {
			return !pmc.id && pmc.active;
		}
	}

	close() {
		this.dialogRef.close();
	}

	getPatientHealthInsurances(): PatientMedicalCoverage[] {
		return this.patientMedicalCoverages.filter(s => s.medicalCoverage.type === 'HealthInsuranceDto' && s.active);
	}

	getPatientPrivateHealthInsurances(): PatientMedicalCoverage[] {
		return this.patientMedicalCoverages.filter(s => s.medicalCoverage.type === 'PrivateHealthInsuranceDto' && s.active);
	}

	private getPrivateHealthInsuranceToAdd(): PatientMedicalCoverage {
		const medicalCoverage = new PrivateHealthInsurance(this.prepagaForm.value.plan, null,
			this.prepagaForm.value.name, 'PrivateHealthInsuranceDto');
		let privateHealthInsuranceDetails;
		if (this.prepagaForm.value.startDate || this.prepagaForm.value.endDate) {
			privateHealthInsuranceDetails = {
				startDate: this.prepagaForm.value.startDate,
				endDate: this.prepagaForm.value.endDate
			};
		}

		const toAdd: PatientMedicalCoverage = {
			medicalCoverage,
			affiliateNumber: this.prepagaForm.value.affiliateNumber,
			validDate: newMoment(),
			privateHealthInsuranceDetails,
			active: true
		}
		return toAdd;
	}

	private getHealthInsuranceToAdd(): PatientMedicalCoverage {
		const toAdd: PatientMedicalCoverage = {
			medicalCoverage: this.healthInsuranceToAdd,
			affiliateNumber: this.healthInsuranceForm.value.affiliateNumber,
			validDate: newMoment(),
			active: true
		};
		return toAdd;
	}

	private fromHealthInsuranceMasterDataToHealthInsurance(renaperResponse: MedicalCoverageDto): HealthInsurance {
		const healthInsuranceId = this.healthInsuranceFilteredMasterData
			.filter((s: MedicalCoverageDto) => s.rnos === renaperResponse.rnos)
			.map(s => s.id)[0];
		return new HealthInsurance(renaperResponse.rnos, renaperResponse.acronym, healthInsuranceId, renaperResponse.name, 'HealthInsuranceDto');
	}

	private fromRenaperToPatientMedicalCoverage(healthInsurance: MedicalCoverageDto): PatientMedicalCoverage {
		const healthInsuranceId = this.healthInsuranceMasterData
			.filter((s: MedicalCoverageDto) => s.rnos === healthInsurance.rnos)
			.map(s => s.id)[0];
		const medicalCoverage = new HealthInsurance(healthInsurance.rnos, healthInsurance.acronym,
			healthInsuranceId, healthInsurance.name, 'HealthInsuranceDto');
		return {
			affiliateNumber: null,
			medicalCoverage,
			validDate: healthInsurance.dateQuery ? momentParse(healthInsurance.dateQuery, DateFormat.YEAR_MONTH) : newMoment(),
			active: true
		};
	}
}

export interface PatientMedicalCoverage {
	id?: number;
	affiliateNumber: string;
	validDate: Moment;
	medicalCoverage: HealthInsurance | PrivateHealthInsurance;
	privateHealthInsuranceDetails?: {
		id?: number,
		startDate?: Moment;
		endDate?: Moment;
	};
	active: boolean;
}
export abstract class MedicalCoverage {
	id?: number;
	name: string;
	type: 'HealthInsuranceDto' | 'PrivateHealthInsuranceDto';

	constructor(id, name, type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	abstract toMedicalCoverageDto(): CoverageDtoUnion;
}

export class HealthInsurance extends MedicalCoverage {
	rnos: string;
	acronym?: string;

	constructor(rnos: string, acronym: string, id, name, type) {
		super(id, name, type);
		this.rnos = rnos;
		this.acronym = acronym;
	}

	public toMedicalCoverageDto(): HealthInsuranceDto {
		return {
			id: this.id,
			acronym: this.acronym,
			name: this.name,
			rnos: Number(this.rnos),
			type: 'HealthInsuranceDto',
		};
	}
}

export class PrivateHealthInsurance extends MedicalCoverage {
	plan?: string;

	constructor(plan, id, name, type) {
		super(id, name, type);
		this.plan = plan;
	}

	toMedicalCoverageDto(): PrivateHealthInsuranceDto {
		return {
			id: this.id,
			plan: this.plan,
			name: this.name,
			type: 'PrivateHealthInsuranceDto',
		};
	}
}

export function determineIfIsHealthInsurance(toBeDetermined: HealthInsurance | PrivateHealthInsurance): toBeDetermined is HealthInsurance {
	if ((toBeDetermined as HealthInsurance).type) {
		return true
	}
	return false // case PrivateHealthInsurance
}
