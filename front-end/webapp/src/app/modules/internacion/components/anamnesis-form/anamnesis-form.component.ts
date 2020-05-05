import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
	AllergyConditionDto, AnamnesisDto, DiagnosisDto,
	HealthHistoryConditionDto, InmunizationDto,
	MasterDataInterface,
	MedicationDto, ResponseAnamnesisDto
} from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { forkJoin } from 'rxjs';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	anamnesis: ResponseAnamnesisDto;
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

		const docId = 19; //todo obtener de params
		const anamnesis$ = this.anamnesisService.getAnamnesis(docId);
		const bloodTypes$ = this.internacionMasterDataService.getBloodTypes();

		if (docId) {
			forkJoin([anamnesis$, bloodTypes$]).subscribe(results => {
				const anamnesis = results[0];
				this.bloodTypes = results[1];

				this.allergies = anamnesis.allergies;
				this.diagnosticos = anamnesis.diagnosis;
				this.familyHistories = anamnesis.familyHistories;
				this.inmunizations = anamnesis.inmunizations;
				this.medications = anamnesis.medications;
				this.personalHistories = anamnesis.personalHistories;

				this.form.controls.anthropometricData.setValue({
					bloodType: findBloodType(this.bloodTypes, anamnesis.anthropometricData.bloodType.id),
					height: anamnesis.anthropometricData.height.value,
					weight: anamnesis.anthropometricData.weight.value
				});

				this.form.controls.observations.setValue(anamnesis.notes);

				this.form.controls.vitalSigns.setValue({
					heartRate: anamnesis.vitalSigns.heartRate.value,
					respiratoryRate: anamnesis.vitalSigns.respiratoryRate.value,
					temperature: anamnesis.vitalSigns.temperature.value,
					bloodOxygenSaturation: anamnesis.vitalSigns.bloodOxygenSaturation.value,
					systolicBloodPressure: anamnesis.vitalSigns.systolicBloodPressure.value,
					diastolicBloodPressure: anamnesis.vitalSigns.diastolicBloodPressure.value
				});

			});
		} else {
			bloodTypes$.subscribe(bloodTypes => this.bloodTypes = bloodTypes);
		}

		function findBloodType(bloodTypes: MasterDataInterface<string>[], id): MasterDataInterface<string> {
			return bloodTypes.find(bloodType => bloodType.id === id);
		}
	}

	save(event): void {
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
