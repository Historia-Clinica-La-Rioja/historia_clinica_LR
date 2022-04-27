import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef } from "@angular/material/dialog";
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

	privateHealthInsuranceFilteredMasterData: PrivateHealthInsuranceDto[];
	private privateHealthInsuranceMasterData: PrivateHealthInsuranceDto[];
	private privateHealthInsuranceToAdd : PrivateHealthInsurance;

	constructor(private formBuilder: FormBuilder,
				private readonly privateHealthInsuranceService: PrivateHealthInsuranceService,
				public dialogRef: MatDialogRef<PrivateHealthInsuranceComponent>) {
	}

	ngOnInit(): void {
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

		this.prepagaForm.controls.name.valueChanges.subscribe((newValue: string) => {
			if (newValue) {
				this.privateHealthInsuranceFilteredMasterData = this.privateHealthInsuranceMasterData
					.filter(data => data.name.toLowerCase().includes(newValue.toLowerCase()));
			} else {
				this.privateHealthInsuranceFilteredMasterData = this.privateHealthInsuranceMasterData;
			}
		});
	}

	selectPrivateHealthInsurance(event: MatOptionSelectionChange, privateHealthInsurance: PrivateHealthInsuranceDto): void {
		if (event.isUserInput) {
			this.selectedPlan = null;
			this.privateHealthInsuranceToAdd = this.fromPrivateHealthInsuranceMasterDataToPrivateHealthInsurance(privateHealthInsurance);
			this.privateHealthInsuranceService.getAllPlansById(privateHealthInsurance.id)
				.subscribe(plans =>{
					this.plans = plans;
				});
			this.prepagaForm.setControl('cuit', new FormControl(this.privateHealthInsuranceToAdd.cuit));
			this.prepagaForm.controls.cuit.disable();
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
			planName: this.plans.filter(data => data.id == this.prepagaForm.value.plan).map(medicalCoveragePlan => (medicalCoveragePlan.plan))[0]
			,
			active: true
		};
		return toAdd;
	}

	closeModal(): void {
		this.dialogRef.close();
	}

}
