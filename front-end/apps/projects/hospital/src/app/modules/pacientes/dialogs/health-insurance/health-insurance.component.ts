import { Component, Inject, Input, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { EPatientMedicalCoverageCondition } from "@api-rest/api-model";
import { MedicalCoverageDto } from "@api-rest/api-model";
import { MedicalCoveragePlanDto } from "@api-rest/api-model";
import { MIN_DATE } from "@core/utils/date.utils";
import { FormBuilder, FormGroup, FormGroupDirective, Validators } from "@angular/forms";
import {
	EMedicalCoverageType,
	HealthInsurance,
	PatientMedicalCoverage
} from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { newMoment } from "@core/utils/moment.utils";
import { MatOptionSelectionChange } from "@angular/material/core";
import { HealthInsuranceService } from "@api-rest/services/health-insurance.service";

@Component({
	selector: 'app-health-insurance',
	templateUrl: './health-insurance.component.html',
	styleUrls: ['./health-insurance.component.scss']
})
export class HealthInsuranceComponent implements OnInit {

	minDate = MIN_DATE;

	healthInsuranceForm: FormGroup;
	healthInsuranceFilteredMasterData: MedicalCoverageDto[]
	plans: MedicalCoveragePlanDto[];
	selectedPlan: MedicalCoveragePlanDto;
	conditions = [EPatientMedicalCoverageCondition.VOLUNTARIA, EPatientMedicalCoverageCondition.OBLIGATORIA];

	private healthInsuranceToAdd: HealthInsurance;

	constructor(public dialogRef: MatDialogRef<HealthInsuranceComponent>,
				@Inject(MAT_DIALOG_DATA) public data: { healthInsuranceMasterData: MedicalCoverageDto[] },
				private formBuilder: FormBuilder,
				public readonly healthInsuranceService: HealthInsuranceService,
	) {
	}

	ngOnInit(): void {
		this.healthInsuranceFilteredMasterData = this.data.healthInsuranceMasterData;

		this.healthInsuranceForm = this.formBuilder.group({
			healthInsurance: [null, Validators.required],
			affiliateNumber: [],
			condition: EPatientMedicalCoverageCondition.VOLUNTARIA,
			plan: []
		});

		this.healthInsuranceForm.controls.healthInsurance.valueChanges.subscribe((newValue: string) => {
			if (newValue) {
				this.healthInsuranceFilteredMasterData =
					this.data.healthInsuranceMasterData
						.filter(data => this.getFullHealthInsuranceText(data).toLowerCase().includes(newValue.toLowerCase()));
			} else {
				this.healthInsuranceFilteredMasterData = this.data.healthInsuranceMasterData;
			}
		});
	}

	addHealthInsurance(): void {
		if (this.healthInsuranceToAdd && this.healthInsuranceForm.valid) {
			const healthInsurance = this.getHealthInsuranceToAdd();
			this.dialogRef.close(healthInsurance);
		}
	}

	private getHealthInsuranceToAdd(): PatientMedicalCoverage {
		const toAdd: PatientMedicalCoverage = {
			medicalCoverage: this.healthInsuranceToAdd,
			affiliateNumber: this.healthInsuranceForm.value.affiliateNumber,
			validDate: newMoment(),
			active: true,
			condition: this.healthInsuranceForm.value.condition,
			planId: this.healthInsuranceForm.value.plan,
			planName: this.plans.filter(data => data.id == this.healthInsuranceForm.value.plan).map(medicalCoveragePlan => (medicalCoveragePlan.plan))[0]
		};
		return toAdd;
	}

	selectHealthInsurance(event: MatOptionSelectionChange, healthInsurance: MedicalCoverageDto): void {
		if (event.isUserInput) {
			this.selectedPlan = null;
			this.healthInsuranceToAdd = this.fromHealthInsuranceMasterDataToHealthInsurance(healthInsurance);
			this.healthInsuranceService.getAllPlansById(healthInsurance.id)
				.subscribe(plans => {
					this.plans = plans;
				});
		}
	}

	private fromHealthInsuranceMasterDataToHealthInsurance(healthInsurance: MedicalCoverageDto): HealthInsurance {
		const healthInsuranceId = this.healthInsuranceFilteredMasterData
			.filter((s: MedicalCoverageDto) => s.rnos ? s.rnos === healthInsurance.rnos : s.id === healthInsurance.id)
			.map(s => s.id)[0];
		return new HealthInsurance(healthInsurance.rnos, healthInsurance.acronym, healthInsuranceId, healthInsurance.name, EMedicalCoverageType.OBRASOCIAL);
	}

	getFullHealthInsuranceText(healthInsurance: MedicalCoverageDto): string {
		return [healthInsurance.acronym, healthInsurance.name].filter(Boolean).join(' - ');
	}

	closeModal(): void {
		this.dialogRef.close();
	}

}
