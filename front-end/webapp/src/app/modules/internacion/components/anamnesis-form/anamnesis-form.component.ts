import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
	AllergyConditionDto, AnamnesisDto, DiagnosisDto,
	HealthHistoryConditionDto, InmunizationDto,
	MasterDataInterface,
	MedicationDto
} from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];
	diagnosticos: DiagnosisDto[] = [];
	personalHistories: HealthHistoryConditionDto[] = [];
	familyHistories: HealthHistoryConditionDto[] = [];
	allergies: AllergyConditionDto[] = [];
	inmunizations: InmunizationDto[] = [];
	medications: MedicationDto[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService,
		private anamnesisService: AnamnesisService
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
				bloodOxygenSaturation: [null, Validators.required],
				systolicBloodPressure: [null, Validators.required],
				diastolicBloodPressure: [null, Validators.required],
			}),
			observations: this.formBuilder.group ({
				currentIllnessNote: [null, Validators.required],
				physicalExamNote: [null, Validators.required],
				studiesSummaryNote: [null, Validators.required],
				evolutionNote: [null, Validators.required],
				clinicalImpressionNote: [null, Validators.required],
				otherNote: [null]
			}),
			attachSignature: [false]
		});

		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => {
			this.bloodTypes = bloodTypes;
		});
	}

	save(): void {
		if (this.form.valid && this.form.value.attachSignature) {
			const anamnesis = this.buildAnamnesisDto();
			this.anamnesisService.createAnamnesis(anamnesis)
				.subscribe(anamnesisResponse => console.log('POST anamnesis', anamnesisResponse));
		}
	}

	private buildAnamnesisDto(): AnamnesisDto {
		return {
			confirmed: false,
			allergies: this.allergies,
			anthropometricData: {
				bloodType: {
					id: this.form.value.anthropometricData.bloodType.id,
					value: this.form.value.anthropometricData.bloodType.description
				},
				height: {id: null, value: this.form.value.anthropometricData.height},
				weight: {id: null, value: this.form.value.anthropometricData.weight},
				bmi: null,
			},
			diagnosis: this.diagnosticos,
			familyHistories: this.familyHistories,
			inmunizations: this.inmunizations,
			medications: this.medications,
			notes: this.form.value.observations,
			personalHistories: this.personalHistories,
			vitalSigns: {
				bloodOxygenSaturation: {id: null, value: this.form.value.vitalSigns.bloodOxygenSaturation},
				diastolicBloodPressure: {id: null, value: this.form.value.vitalSigns.diastolicBloodPressure},
				heartRate: {id: null, value: this.form.value.vitalSigns.heartRate},
				respiratoryRate: {id: null, value: this.form.value.vitalSigns.respiratoryRate},
				systolicBloodPressure: {id: null, value: this.form.value.vitalSigns.systolicBloodPressure},
				temperature: {id: null, value: this.form.value.vitalSigns.temperature},
			}
		};
	}

	back(): void {
		window.history.back();
	}

}
