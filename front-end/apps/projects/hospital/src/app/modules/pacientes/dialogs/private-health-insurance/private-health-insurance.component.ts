import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { EPatientMedicalCoverageCondition } from "@api-rest/api-model";
import { MedicalCoveragePlanDto } from "@api-rest/api-model";
import { PrivateHealthInsuranceDto } from "@api-rest/api-model";

import { MIN_DATE } from "@core/utils/date.utils";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import {
	EMedicalCoverageType,
	PatientMedicalCoverage,
	PrivateHealthInsurance
} from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { newMoment } from "@core/utils/moment.utils";
import { MatOptionSelectionChange } from "@angular/material/core";
import { PrivateHealthInsuranceService } from "@api-rest/services/private-health-insurance.service";

@Component({
	selector: 'app-private-health-insurance',
	templateUrl: './private-health-insurance.component.html',
	styleUrls: ['./private-health-insurance.component.scss']
})
export class PrivateHealthInsuranceComponent implements OnInit {

	minDate = MIN_DATE;
	prepagaForm: FormGroup;
	selectedPlan : MedicalCoveragePlanDto;
	patientMedicalCoverages: PatientMedicalCoverage[];
	plans: MedicalCoveragePlanDto[];
	conditions = [EPatientMedicalCoverageCondition.VOLUNTARIA, EPatientMedicalCoverageCondition.OBLIGATORIA];
	isPrivateHealthInsuranceToAdd: boolean;

	privateHealthInsuranceFilteredMasterData: PrivateHealthInsuranceDto[];
	private privateHealthInsuranceMasterData: PrivateHealthInsuranceDto[];
	private privateHealthInsuranceToAdd : PrivateHealthInsurance;

	constructor(private formBuilder: FormBuilder,
				private readonly privateHealthInsuranceService: PrivateHealthInsuranceService,
				public dialogRef: MatDialogRef<PrivateHealthInsuranceComponent>,
				@Inject(MAT_DIALOG_DATA) public data: { privateHealthInsuranceToupdate: PatientMedicalCoverage},) {
	}

	ngOnInit(): void {
		this.isPrivateHealthInsuranceToAdd = this.data.privateHealthInsuranceToupdate ? false : true;

		this.privateHealthInsuranceService.getAll().subscribe((values: PrivateHealthInsuranceDto[]) => {
			this.privateHealthInsuranceFilteredMasterData = values;
			this.privateHealthInsuranceMasterData = values;
		})

		this.prepagaForm = this.formBuilder.group({
			name: [null, Validators.required],
			affiliateNumber: [],
			plan: [],
			startDate: [],
			endDate: [],
			condition: EPatientMedicalCoverageCondition.VOLUNTARIA
		});

		if (!this.isPrivateHealthInsuranceToAdd) {
			this.uploadPrivateHealthInsuranceForm();
		}

		this.prepagaForm.controls.name.valueChanges.subscribe((newValue: string) => {
			if (newValue) {
				this.privateHealthInsuranceFilteredMasterData = this.privateHealthInsuranceMasterData
					.filter(data => data.name.toLowerCase().includes(newValue.toLowerCase()));
			} else {
				this.privateHealthInsuranceFilteredMasterData = this.privateHealthInsuranceMasterData;
			}
		});
	}

	private uploadPrivateHealthInsuranceForm() {
		this.privateHealthInsuranceToAdd = this.data.privateHealthInsuranceToupdate.medicalCoverage as PrivateHealthInsurance;
		this.prepagaForm.setControl('name', new FormControl(this.data.privateHealthInsuranceToupdate.medicalCoverage.name));
		this.prepagaForm.setControl('affiliateNumber', new FormControl(this.data.privateHealthInsuranceToupdate.affiliateNumber));
		this.prepagaForm.setControl('condition', new FormControl(this.data.privateHealthInsuranceToupdate.condition ? this.data.privateHealthInsuranceToupdate.condition : EPatientMedicalCoverageCondition.VOLUNTARIA));
		this.prepagaForm.setControl('startDate', new FormControl(this.data.privateHealthInsuranceToupdate.startDate));
		this.prepagaForm.setControl('endDate', new FormControl(this.data.privateHealthInsuranceToupdate.endDate));
		this.privateHealthInsuranceService.getAllPlansById(this.data.privateHealthInsuranceToupdate.medicalCoverage.id)
			.subscribe(plans =>{
				this.plans = plans;
				this.prepagaForm.setControl('plan', new FormControl(this.data.privateHealthInsuranceToupdate.planId));
			});
		this.prepagaForm.controls.name.disable();
	}

	selectPrivateHealthInsurance(event: MatOptionSelectionChange, privateHealthInsurance: PrivateHealthInsuranceDto): void {
		if (event.isUserInput) {
			this.selectedPlan = null;
			this.privateHealthInsuranceToAdd = this.fromPrivateHealthInsuranceMasterDataToPrivateHealthInsurance(privateHealthInsurance);
			this.privateHealthInsuranceService.getAllPlansById(privateHealthInsurance.id)
				.subscribe(plans =>{
					this.plans = plans;
				});
		}
	}

	private fromPrivateHealthInsuranceMasterDataToPrivateHealthInsurance(privateHealthInsurance: PrivateHealthInsuranceDto): PrivateHealthInsurance {
		const privateHealthInsuranceId = this.privateHealthInsuranceFilteredMasterData
			.filter((s: PrivateHealthInsuranceDto) => s.id === privateHealthInsurance.id)
			.map(s => s.id)[0];
		return new PrivateHealthInsurance(privateHealthInsuranceId, privateHealthInsurance.name, EMedicalCoverageType.PREPAGA, privateHealthInsurance.cuit);
	}

	addPrivateHealthInsurance(): void {
		if (this.privateHealthInsuranceToAdd && this.prepagaForm.valid) {
			const privateHealthInsurance = this.getPrivateHealthInsuranceToAdd();
			if (!this.isPrivateHealthInsuranceToAdd) {
				privateHealthInsurance.id = this.data.privateHealthInsuranceToupdate.id;
				privateHealthInsurance.validDate = this.data.privateHealthInsuranceToupdate.validDate;
			}
			this.dialogRef.close(privateHealthInsurance);
		}
	}

	private getPrivateHealthInsuranceToAdd(): PatientMedicalCoverage {
		const medicalCoverage = new PrivateHealthInsurance(this.privateHealthInsuranceToAdd.id,
			this.privateHealthInsuranceToAdd.name, EMedicalCoverageType.PREPAGA, null);
		const toAdd: PatientMedicalCoverage = {
			medicalCoverage,
			affiliateNumber: this.prepagaForm.value.affiliateNumber,
			validDate: newMoment(),
			condition: this.prepagaForm.value.condition,
			startDate: this.prepagaForm.value.startDate,
			endDate: this.prepagaForm.value.endDate,
			planId: this.prepagaForm.value.plan,
			planName: this.plans.filter(data => data.id == this.prepagaForm.value.plan).map(medicalCoveragePlan => (medicalCoveragePlan.plan))[0],
			active: true
		};
		return toAdd;
	}

	closeModal(): void {
		this.dialogRef.close();
	}

}
