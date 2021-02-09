import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
	AllergyConditionDto, AnamnesisDto, DiagnosisDto,
	HealthHistoryConditionDto, ImmunizationDto,
	MasterDataInterface,
	MedicationDto, ResponseAnamnesisDto, HealthConditionDto
} from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { forkJoin } from 'rxjs';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { getError, hasError } from '@core/utils/form.utils';
import { ProcedimientosService } from '../../../../services/procedimientos.service';
import { SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	private internmentEpisodeId: number;
	private patientId: number;
	anamnesisId: number;

	getError = getError;
	hasError = hasError;

	mainDiagnosisError: string = '';
	anamnesis: ResponseAnamnesisDto;
	form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];
	diagnosticos: DiagnosisDto[] = [];
	mainDiagnosis: HealthConditionDto;
	personalHistories: HealthHistoryConditionDto[] = [];
	familyHistories: HealthHistoryConditionDto[] = [];
	allergies: AllergyConditionDto[] = [];
	immunizations: ImmunizationDto[] = [];
	medications: MedicationDto[] = [];
	apiErrors: string[] = [];
	procedimientosService: ProcedimientosService;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly anamnesisService: AnamnesisService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
	) {
		this.procedimientosService = new ProcedimientosService(formBuilder, snomedService);
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
				height: [null, [Validators.min(0), Validators.max(1000)]],
				weight: [null, [Validators.min(0), Validators.max(1000)]]
			}),
			vitalSigns: this.formBuilder.group({
				heartRate: this.formBuilder.group({
					value: [null, Validators.min(0)],
					effectiveTime: [newMoment()],
				}),
				respiratoryRate: this.formBuilder.group({
					value: [null, Validators.min(0)],
					effectiveTime: [newMoment()],
				}),
				temperature: this.formBuilder.group({
					value: [null, Validators.min(0)],
					effectiveTime: [newMoment()],
				}),
				bloodOxygenSaturation: this.formBuilder.group({
					value: [null, Validators.min(0)],
					effectiveTime: [newMoment()],
				}),
				systolicBloodPressure: this.formBuilder.group({
					value: [null, Validators.min(0)],
					effectiveTime: [newMoment()],
				}),
				diastolicBloodPressure: this.formBuilder.group({
					value: [null, Validators.min(0)],
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
				this.immunizations = anamnesis.immunizations;
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
			console.warn('form valid')
			const anamnesis: AnamnesisDto = this.buildAnamnesisDto();
			this.apiErrors = [];
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
					}, responseErrors => {
						this.apiErrorsProcess(responseErrors);
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
			immunizations: this.immunizations,
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
			},
			procedures: isNull(this.procedimientosService.getProcedimientos()) ? undefined : this.procedimientosService.getProcedimientos()
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

	private apiErrorsProcess(responseErrors): void {
		this.mainDiagnosisError = responseErrors.mainDiagnosis;
		Object.getOwnPropertyNames(responseErrors).forEach(val => {
			if (val!=='mainDiagnosis' && val!=='message'){
				let error = responseErrors[val];
				if (Array.isArray(error)){
					error.forEach(elementError =>
						this.apiErrors.push(elementError)
					);
				}else
				this.apiErrors.push(error);
			}
		});
	}

}
