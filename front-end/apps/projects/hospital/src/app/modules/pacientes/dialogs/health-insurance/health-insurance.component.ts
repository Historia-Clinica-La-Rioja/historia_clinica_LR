import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from "@angular/forms";
import { MatOptionSelectionChange } from "@angular/material/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { EPatientMedicalCoverageCondition, MedicalCoverageDto, MedicalCoveragePlanDto } from "@api-rest/api-model";
import { HealthInsuranceService } from "@api-rest/services/health-insurance.service";
import { MIN_DATE } from "@core/utils/date.utils";
import { newDate } from "@core/utils/moment.utils";
import {
	EMedicalCoverageType,
	HealthInsurance,
	PatientMedicalCoverage
} from "@pacientes/dialogs/medical-coverage/medical-coverage.component";

@Component({
	selector: 'app-health-insurance',
	templateUrl: './health-insurance.component.html',
	styleUrls: ['./health-insurance.component.scss']
})
export class HealthInsuranceComponent implements OnInit {

	minDate = MIN_DATE;

	healthInsuranceForm: UntypedFormGroup;
	healthInsuranceFilteredMasterData: MedicalCoverageDto[];
	plans: MedicalCoveragePlanDto[];
	selectedPlan: MedicalCoveragePlanDto;
	conditions = [EPatientMedicalCoverageCondition.VOLUNTARIA, EPatientMedicalCoverageCondition.OBLIGATORIA];
	isHealthInsuranceToAdd: boolean;

	private healthInsuranceToAdd: HealthInsurance;

	constructor(public dialogRef: MatDialogRef<HealthInsuranceComponent>,
				@Inject(MAT_DIALOG_DATA) public data: { healthInsuranceMasterData: MedicalCoverageDto[],
					healthInsuranceToUpdate: PatientMedicalCoverage },
				private formBuilder: UntypedFormBuilder,
				public readonly healthInsuranceService: HealthInsuranceService,
	) {
	}

	ngOnInit(): void {
		this.isHealthInsuranceToAdd = this.data.healthInsuranceToUpdate ? false : true;
		this.healthInsuranceFilteredMasterData = this.data.healthInsuranceMasterData;

		this.healthInsuranceForm = this.formBuilder.group({
			healthInsurance: [null, Validators.required],
			affiliateNumber: [],
			condition: EPatientMedicalCoverageCondition.VOLUNTARIA,
			plan: []
		});

		if (!this.isHealthInsuranceToAdd) {
			this.uploadHealthInsuranceForm();
		}

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

	private uploadHealthInsuranceForm() {
		this.healthInsuranceToAdd = this.fromHealthInsuranceMasterDataToHealthInsurance(this.toMedicalCoverageDto(this.data.healthInsuranceToUpdate.medicalCoverage as HealthInsurance));
		this.healthInsuranceForm.setControl('healthInsurance', new UntypedFormControl(this.getFullHealthInsuranceText(this.data.healthInsuranceToUpdate.medicalCoverage)));
		this.healthInsuranceForm.setControl('affiliateNumber', new UntypedFormControl(this.data.healthInsuranceToUpdate.affiliateNumber));
		this.healthInsuranceForm.setControl('condition', new UntypedFormControl(this.data.healthInsuranceToUpdate.condition ? this.data.healthInsuranceToUpdate.condition : EPatientMedicalCoverageCondition.VOLUNTARIA));
		this.healthInsuranceService.getAllPlansById(this.data.healthInsuranceToUpdate.medicalCoverage.id)
			.subscribe(plans => {
				this.plans = plans;
				this.healthInsuranceForm.setControl('plan', new UntypedFormControl(this.data.healthInsuranceToUpdate.planId));
			});
		this.healthInsuranceForm.controls.healthInsurance.disable();
	}

	addHealthInsurance(): void {
		if (this.healthInsuranceToAdd && this.healthInsuranceForm.valid) {
			const healthInsurance = this.getHealthInsuranceToAdd();
			if(this.data.healthInsuranceToUpdate) {
				healthInsurance.id = this.data.healthInsuranceToUpdate.id;
				healthInsurance.validDate = this.data.healthInsuranceToUpdate.validDate;
			}
			this.dialogRef.close(healthInsurance);
		}
	}

	private getHealthInsuranceToAdd(): PatientMedicalCoverage {
		const toAdd: PatientMedicalCoverage = {
			medicalCoverage: this.healthInsuranceToAdd,
			affiliateNumber: this.healthInsuranceForm.value.affiliateNumber,
			validDate: newDate(),
			active: true,
			condition: this.healthInsuranceForm.value.condition,
			planId: this.healthInsuranceForm.value.plan,
			planName: this.healthInsuranceForm.value.plan ? this.plans.filter(data => data.id == this.healthInsuranceForm.value.plan).map(medicalCoveragePlan => (medicalCoveragePlan.plan))[0] : null
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

	getFullHealthInsuranceText(healthInsurance: any): string {
		return [healthInsurance.acronym, healthInsurance.name].filter(Boolean).join(' - ');
	}

	closeModal(): void {
		this.dialogRef.close();
	}

	toMedicalCoverageDto(healthInsurance: HealthInsurance): MedicalCoverageDto {
		return {
			acronym: healthInsurance.acronym,
			id: healthInsurance.id,
			name: healthInsurance.name,
			rnos: healthInsurance.rnos,
			dateQuery: null,
			service: null
		};
	}

}
