import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
	AllergyConditionDto, DiagnosisDto,
	HealthHistoryConditionDto, InmunizationDto,
	MasterDataInterface,
	MedicationDto
} from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	public form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];
	diagnosticos: DiagnosisDto[] = [];
	personalHistories: HealthHistoryConditionDto[] = [];
	familyHistories: HealthHistoryConditionDto[] = [];
	allergies: AllergyConditionDto[] = [];
	inmunizations: InmunizationDto[] = [];
	medications: MedicationDto[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			anthropometricData: this.formBuilder.group({
				bloodType: [null, Validators.required],
				height: [null, Validators.required],
				weight: [null, Validators.required],
			}),
			vitalSigns: this.formBuilder.group({
				heartRate: [null, Validators.required],
				respiratoryRate: [null, Validators.required],
				temperature: [null, Validators.required],
				bloodOxigenSaturation: [null, Validators.required],
				systolicBloodPressure: [null, Validators.required],
				diastolicBloodPressure: [null, Validators.required],
			}),
			observations: this.formBuilder.group ({
				current_disease: [null, Validators.required],
				physical_examination: [null, Validators.required],
				studies_procedures: [null, Validators.required],
				patient_progress: [null, Validators.required],
				clinical_impression: [null, Validators.required],
				others: [null]
			})
		});

		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => {
			this.bloodTypes = bloodTypes;
		});
	}

	save(): void {
		console.log('diagnosticos: ', this.diagnosticos);
		console.log('personalHistories:', this.personalHistories);
		console.log('familyHistories:', this.familyHistories);
		console.log('allergies:', this.allergies);
		console.log('inmunizations:', this.inmunizations);
		console.log('medications:', this.medications);

		console.log('form: ', this.form);
	}

	back(): void {
		window.history.back();
	}

}
