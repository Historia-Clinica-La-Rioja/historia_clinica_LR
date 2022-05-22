import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatOptionSelectionChange } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {
	CoverageDtoUnion,
	HealthInsuranceDto,
	MedicalCoverageDto,
	PrivateHealthInsuranceDto, PrivateHealthInsurancePlanDto
} from '@api-rest/api-model';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { RenaperService } from '@api-rest/services/renaper.service';
import { DateFormat, momentFormat, momentParse, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { MIN_DATE } from "@core/utils/date.utils";
import { PrivateHealthInsuranceService } from "@api-rest/services/private-health-insurance.service";
import { EPatientMedicalCoverageCondition } from '@api-rest/api-model';
const DNI_TYPE_ID = 1;
const VOLUNTARIA = 1;
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
	privateHealthInsuranceFilteredMasterData: PrivateHealthInsuranceDto[];
	plans: PrivateHealthInsurancePlanDto[];
	selectedPlan : PrivateHealthInsurancePlanDto;

	loading = true;

	private healthInsuranceToAdd: HealthInsurance;
	private privateHealthInsuranceToAdd : PrivateHealthInsurance;
	private healthInsuranceMasterData: MedicalCoverageDto[];
	private privateHealthInsuranceMasterData: PrivateHealthInsuranceDto[];

	minDate = MIN_DATE;

	constructor(
		private formBuilder: FormBuilder,
		private renaperService: RenaperService,
		public readonly healthInsuranceService: HealthInsuranceService,
		public readonly privateHealthInsuranceService: PrivateHealthInsuranceService,
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

			if (this.personInfo.identificationTypeId === DNI_TYPE_ID && this.personInfo.genderId) {
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

					}, _ => this.loading = false);
			} else {
				this.loading = false;
			}
		}
		);
		this.privateHealthInsuranceService.getAll().subscribe((values: PrivateHealthInsuranceDto[]) => {
			this.privateHealthInsuranceFilteredMasterData = values;
			this.privateHealthInsuranceMasterData = values;
		})
		this.healthInsuranceForm = this.formBuilder.group({
			healthInsurance: [null, Validators.required],
			affiliateNumber: [],
			condition: VOLUNTARIA
		});

		this.prepagaForm = this.formBuilder.group({
			name: [null, Validators.required],
			cuit: [],
			affiliateNumber: [],
			plan: [],
			startDate: [],
			endDate: [],
			condition: VOLUNTARIA
		});
		this.prepagaForm.controls.cuit.disable();
		this.healthInsuranceForm.controls.healthInsurance.valueChanges.subscribe((newValue: string) => {
			if (newValue) {
				this.healthInsuranceFilteredMasterData =
					this.healthInsuranceMasterData
						.filter(data => this.getFullHealthInsuranceText(data).toLowerCase().includes(newValue.toLowerCase()));
			} else {
				this.healthInsuranceFilteredMasterData = this.healthInsuranceMasterData;
			}
		});

		this.prepagaForm.controls.name.valueChanges.subscribe((newValue: string) => {
			if (newValue) {
				this.privateHealthInsuranceFilteredMasterData =
					this.privateHealthInsuranceMasterData
						.filter(data => data.name.toLowerCase().includes(newValue.toLowerCase()));
			} else {
				this.privateHealthInsuranceFilteredMasterData = this.privateHealthInsuranceMasterData;
			}
		});

	}


	selectHealthInsurance(event: MatOptionSelectionChange, healthInsurance: MedicalCoverageDto): void {
		if (event.isUserInput) {
			this.healthInsuranceToAdd = this.fromHealthInsuranceMasterDataToHealthInsurance(healthInsurance);
		}
	}

	selectPrivateHealthInsurance(event: MatOptionSelectionChange, privateHealthInsurance: PrivateHealthInsuranceDto): void {
		if (event.isUserInput) {
			this.selectedPlan = null;
			this.privateHealthInsuranceToAdd = this.fromPrivateHealthInsuranceMasterDataToPrivateHealthInsurance(privateHealthInsurance);
			this.privateHealthInsuranceService.getAllPlansById(privateHealthInsurance.id)
			this.privateHealthInsuranceService.getAllPlansById(privateHealthInsurance.id)
				.subscribe(plans =>{
					this.plans = plans;
			});
			this.prepagaForm.setControl('cuit', new FormControl(this.privateHealthInsuranceToAdd.cuit));
			this.prepagaForm.controls.cuit.disable();
		}
	}

	getFullHealthInsuranceText(healthInsurance: MedicalCoverageDto): string {
		return [healthInsurance.acronym, healthInsurance.name].filter(Boolean).join(' - ');
	}

	getPrivateHealthInsurancePlanText(patientMedicalCoverage: PatientMedicalCoverage): string {
		return [patientMedicalCoverage.privateHealthInsuranceDetails?.planName, patientMedicalCoverage?.condition].filter(Boolean).join(' | ');
	}

	getDatesText(patientMedicalCoverage: PatientMedicalCoverage): string {
		const initText = patientMedicalCoverage.privateHealthInsuranceDetails?.startDate ? 'desde ' +
			momentFormat(patientMedicalCoverage.privateHealthInsuranceDetails.startDate, DateFormat.VIEW_DATE) : '';

		const endText = patientMedicalCoverage.privateHealthInsuranceDetails?.endDate ? ' hasta ' +
			momentFormat(patientMedicalCoverage.privateHealthInsuranceDetails.endDate, DateFormat.VIEW_DATE) : '';

		return initText + endText;
	}
	// -----------------------------------------------------------------------------------------------------------------------------

	addHealthInsurance(formDirective: FormGroupDirective): void {
		if (this.healthInsuranceToAdd && this.healthInsuranceForm.valid) {
			const toAdd = this.getHealthInsuranceToAdd();
			this.patientMedicalCoverages = this.patientMedicalCoverages.concat(toAdd);
			formDirective.resetForm();
			this.healthInsuranceForm.reset();
			this.healthInsuranceForm.controls.condition.setValue(VOLUNTARIA);
			this.healthInsuranceToAdd = null;
		}
	}

	addPrivateHealthInsurance(formDirective: FormGroupDirective): void {
		if (this.privateHealthInsuranceToAdd && this.prepagaForm.valid) {
			const toAdd = this.getPrivateHealthInsuranceToAdd();
			this.patientMedicalCoverages = this.patientMedicalCoverages.concat(toAdd);
			formDirective.resetForm();
			this.prepagaForm.reset();
			this.prepagaForm.controls.condition.setValue(VOLUNTARIA);
			this.privateHealthInsuranceToAdd = null;
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
		const medicalCoverage = new PrivateHealthInsurance(this.privateHealthInsuranceToAdd.id,
			this.privateHealthInsuranceToAdd.name, 'PrivateHealthInsuranceDto',this.prepagaForm.value.cuit);
		let privateHealthInsuranceDetails;
		if (this.prepagaForm.value.startDate || this.prepagaForm.value.endDate || this.prepagaForm.value.plan) {
			privateHealthInsuranceDetails = {
				startDate: this.prepagaForm.value.startDate,
				endDate: this.prepagaForm.value.endDate,
				planId: this.prepagaForm.value.plan,
				planName: this.plans.filter(data => data.id==this.prepagaForm.value.plan).map(privateHealthInsurancePlan => (privateHealthInsurancePlan.plan))[0]
			};
		}
		const toAdd: PatientMedicalCoverage = {
			medicalCoverage,
			affiliateNumber: this.prepagaForm.value.affiliateNumber,
			validDate: newMoment(),
			privateHealthInsuranceDetails,
			active: true,
			condition: (this.prepagaForm.value.condition == VOLUNTARIA) ? EPatientMedicalCoverageCondition.VOLUNTARIA : EPatientMedicalCoverageCondition.OBLIGATORIA
		};
		return toAdd;
	}

	private getHealthInsuranceToAdd(): PatientMedicalCoverage {
		const toAdd: PatientMedicalCoverage = {
			medicalCoverage: this.healthInsuranceToAdd,
			affiliateNumber: this.healthInsuranceForm.value.affiliateNumber,
			validDate: newMoment(),
			active: true,
			condition: (this.healthInsuranceForm.value.condition == VOLUNTARIA) ? EPatientMedicalCoverageCondition.VOLUNTARIA : EPatientMedicalCoverageCondition.OBLIGATORIA
		};
		return toAdd;
	}

	private fromHealthInsuranceMasterDataToHealthInsurance(renaperResponse: MedicalCoverageDto): HealthInsurance {
		const healthInsuranceId = this.healthInsuranceFilteredMasterData
			.filter((s: MedicalCoverageDto) => s.rnos === renaperResponse.rnos)
			.map(s => s.id)[0];
		return new HealthInsurance(renaperResponse.rnos, renaperResponse.acronym, healthInsuranceId, renaperResponse.name, 'HealthInsuranceDto');
	}

	private fromPrivateHealthInsuranceMasterDataToPrivateHealthInsurance(privateHealthInsurance: PrivateHealthInsuranceDto): PrivateHealthInsurance {
		const privateHealthInsuranceId = this.privateHealthInsuranceFilteredMasterData
			.filter((s: PrivateHealthInsuranceDto) => s.id === privateHealthInsurance.id)
			.map(s => s.id)[0];
		return new PrivateHealthInsurance(privateHealthInsuranceId, privateHealthInsurance.name, 'HealthInsuranceDto',privateHealthInsurance.cuit);
	}

	private fromRenaperToPatientMedicalCoverage(healthInsurance: MedicalCoverageDto): PatientMedicalCoverage {
		const healthInsuranceId = this.healthInsuranceMasterData
			.filter((s: MedicalCoverageDto) => s.rnos === healthInsurance.rnos)
			.map(s => s.id)[0];
		const medicalCoverage = new HealthInsurance(healthInsurance.rnos, healthInsurance.acronym,
			healthInsuranceId, healthInsurance.name, 'HealthInsuranceDto');

		if(medicalCoverage.id === undefined) {
			this.healthInsuranceService.get(parseInt(healthInsurance.rnos))
				.subscribe(data => {
					medicalCoverage.id = data.id;
				})
		}

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
		planId?: number;
		planName?: string;
	};
	active: boolean;
	condition?: EPatientMedicalCoverageCondition;
}
export abstract class MedicalCoverage {
	id?: number;
	name: string;
	type: 'HealthInsuranceDto' | 'PrivateHealthInsuranceDto';
	cuit: string;
	constructor(id, name, type, cuit) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.cuit = cuit;
	}

	abstract toMedicalCoverageDto(): CoverageDtoUnion;
}

export class HealthInsurance extends MedicalCoverage {
	rnos: string;
	acronym?: string;

	constructor(rnos: string, acronym: string, id, name, type) {
		super(id, name, type, null);
		this.rnos = rnos;
		this.acronym = acronym;
	}

	public toMedicalCoverageDto(): HealthInsuranceDto {
		return {
			id: this.id,
			acronym: this.acronym,
			name: this.name,
			cuit: this.cuit,
			rnos: Number(this.rnos),
			type: 'HealthInsuranceDto',
		};
	}
}

export class PrivateHealthInsurance extends MedicalCoverage {
	constructor(id, name, type, cuit) {
		super(id, name, type, cuit);
	}

	toMedicalCoverageDto(): PrivateHealthInsuranceDto {
		return {
			id: this.id,
			name: this.name,
			cuit: this.cuit,
			type: 'PrivateHealthInsuranceDto',
		};
	}
}

export function determineIfIsHealthInsurance(toBeDetermined: HealthInsurance | PrivateHealthInsurance): toBeDetermined is HealthInsurance {
	if ((toBeDetermined as HealthInsurance).type) {
		return true;
	}
	return false; // case PrivateHealthInsurance
}
