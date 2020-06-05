import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {
	AllergyConditionDto, AnamnesisDto, DiagnosisDto,
	HealthHistoryConditionDto, InmunizationDto,
	MasterDataInterface,
	MedicationDto, ResponseAnamnesisDto, HealthConditionDto
} from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { AnamnesisReportService } from '@api-rest/services/anamnesis-report.service';
import { forkJoin } from 'rxjs';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	private internmentEpisodeId: number;
	private patientId: number;
	anamnesisId: number;

	mainDiagnosisError: string = '';
	anamnesis: ResponseAnamnesisDto;
	form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];
	diagnosticos: DiagnosisDto[] = [];
	mainDiagnosis: HealthConditionDto;
	personalHistories: HealthHistoryConditionDto[] = [];
	familyHistories: HealthHistoryConditionDto[] = [];
	allergies: AllergyConditionDto[] = [];
	inmunizations: InmunizationDto[] = [];
	medications: MedicationDto[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService,
		private anamnesisService: AnamnesisService,
		private anamnesisReportService: AnamnesisReportService,
		private route: ActivatedRoute,
		private router: Router,
		private contextService: ContextService,
		private snackBarService: SnackBarService
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params: ParamMap) => {
				this.anamnesisId = Number(params.get('anamnesisId'));
				this.internmentEpisodeId = Number(params.get('idInternacion'));
				this.patientId = Number(params.get('idPaciente'));
			}
		);

		this.form = this.formBuilder.group({
			anthropometricData: this.formBuilder.group({
				bloodType: [null],
				height: [null],
				weight: [null],
			}),
			vitalSigns: this.formBuilder.group({
				heartRate: this.formBuilder.group({
					value: [null],
					effectiveTime: [newMoment()],
				}),
				respiratoryRate: this.formBuilder.group({
					value: [null],
					effectiveTime: [newMoment()],
				}),
				temperature: this.formBuilder.group({
					value: [null],
					effectiveTime: [newMoment()],
				}),
				bloodOxygenSaturation: this.formBuilder.group({
					value: [null],
					effectiveTime: [newMoment()],
				}),
				systolicBloodPressure: this.formBuilder.group({
					value: [null],
					effectiveTime: [newMoment()],
				}),
				diastolicBloodPressure: this.formBuilder.group({
					value: [null],
					effectiveTime: [newMoment()],
				}),
			}),
			observations: this.formBuilder.group ({
				currentIllnessNote: [null],
				physicalExamNote: [null],
				studiesSummaryNote: [null],
				evolutionNote: [null],
				clinicalImpressionNote: [null],
				otherNote: [null]
			})
		});

		const anamnesis$ = this.anamnesisService.getAnamnesis(this.anamnesisId, this.internmentEpisodeId);
		const bloodTypes$ = this.internacionMasterDataService.getBloodTypes();

		if (this.anamnesisId) {
			forkJoin([anamnesis$, bloodTypes$]).subscribe(results => {
				const anamnesis = results[0];
				this.bloodTypes = results[1];

				this.allergies = anamnesis.allergies;
				this.diagnosticos = anamnesis.diagnosis;
				this.familyHistories = anamnesis.familyHistories;
				this.inmunizations = anamnesis.inmunizations;
				this.medications = anamnesis.medications;
				this.personalHistories = anamnesis.personalHistories;

				this.mainDiagnosis = anamnesis.mainDiagnosis;

				this.form.controls.anthropometricData.setValue({
					bloodType: findBloodType(this.bloodTypes, anamnesis.anthropometricData.bloodType.id),
					height: anamnesis.anthropometricData.height.value,
					weight: anamnesis.anthropometricData.weight.value
				});

				this.form.controls.observations.setValue(anamnesis.notes);

				this.form.controls.vitalSigns.setValue({
					heartRate: anamnesis.vitalSigns.heartRate,
					respiratoryRate: anamnesis.vitalSigns.respiratoryRate,
					temperature: anamnesis.vitalSigns.temperature,
					bloodOxygenSaturation: anamnesis.vitalSigns.bloodOxygenSaturation,
					systolicBloodPressure: anamnesis.vitalSigns.systolicBloodPressure,
					diastolicBloodPressure: anamnesis.vitalSigns.diastolicBloodPressure
				});

			});
		} else {
			bloodTypes$.subscribe(bloodTypes => this.bloodTypes = bloodTypes);
		}

		function findBloodType(bloodTypes: MasterDataInterface<string>[], id): MasterDataInterface<string> {
			return bloodTypes.find(bloodType => bloodType.id === id);
		}
	}

	setMainDiagnosis(newDiagnosis: HealthConditionDto) {
		this.mainDiagnosis = newDiagnosis;
		if (newDiagnosis) {
			delete this.mainDiagnosisError;
		}
	}

	back(): void {
		window.history.back();
	}

	save(): void {
		if (this.form.valid) {
			const anamnesis: AnamnesisDto = this.buildAnamnesisDto();

			if (this.anamnesisId) {
				this.anamnesisService.updateAnamnesis(this.anamnesisId, anamnesis, this.internmentEpisodeId)
					.subscribe((anamnesisResponse: ResponseAnamnesisDto) => {
							const url = `internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
							this.router.navigate([url]);
					});
			} else {
				this.anamnesisService.createAnamnesis(anamnesis, this.internmentEpisodeId)
				.subscribe((anamnesisResponse: ResponseAnamnesisDto) => {
						this.snackBarService.showSuccess('internaciones.anamnesis.messages.SUCCESS');
						this.goToInternmentSummary();
					}, errors => {
						//TODO imlementar estrategia para mostrar error
						this.mainDiagnosisError = errors.mainDiagnosis;
						this.snackBarService.showError('internaciones.anamnesis.messages.ERROR')
					});
				}
			} else {
			this.snackBarService.showError('internaciones.anamnesis.messages.ERROR');
		}
	}

	private goToInternmentSummary(): void {
		const url = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
		this.router.navigate([url]);
	}

	private buildAnamnesisDto(): AnamnesisDto {
		const formValues = this.form.value;
		return {
			confirmed: true,
			allergies: this.allergies,
			anthropometricData: isNull(formValues.anthropometricData) ? undefined : {
				bloodType: formValues.anthropometricData.bloodType ? {
					id: formValues.anthropometricData.bloodType.id,
					value: formValues.anthropometricData.bloodType.description
				} : undefined,
				height: getValue(formValues.anthropometricData.height),
				weight: getValue(formValues.anthropometricData.weight),
			},
			mainDiagnosis: this.mainDiagnosis,
			diagnosis: this.diagnosticos,
			familyHistories: this.familyHistories,
			inmunizations: this.inmunizations,
			medications: this.medications,
			notes: isNull(formValues.observations) ? undefined : formValues.observations,
			personalHistories: this.personalHistories,
			vitalSigns: isNull(formValues.vitalSigns) ? undefined : {
				bloodOxygenSaturation: getEffectiveValue(formValues.vitalSigns.bloodOxygenSaturation),
				diastolicBloodPressure: getEffectiveValue(formValues.vitalSigns.diastolicBloodPressure),
				heartRate: getEffectiveValue(formValues.vitalSigns.heartRate),
				respiratoryRate: getEffectiveValue(formValues.vitalSigns.respiratoryRate),
				systolicBloodPressure: getEffectiveValue(formValues.vitalSigns.systolicBloodPressure),
				temperature: getEffectiveValue(formValues.vitalSigns.temperature)
			}
		};

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every(el => el === null);
		}

		function getValue(controlValue: any) {
			return controlValue ? { value: controlValue } : undefined;
		}

		function getEffectiveValue(controlValue: any) {
			return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
		}

	}

	setVitalSignEffectiveTime(newEffectiveTime: Moment, formField: string): void {
		(<FormGroup>(<FormGroup>this.form.controls['vitalSigns']).controls[formField]).controls['effectiveTime'].setValue(newEffectiveTime);
	}

}
