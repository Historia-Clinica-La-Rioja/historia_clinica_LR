import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
	AllergyConditionDto,
	AnamnesisDto,
	DiagnosisDto,
	HealthHistoryConditionDto,
	ImmunizationDto,
	MasterDataInterface,
	MedicationDto,
	ResponseAnamnesisDto,
	HealthConditionDto
} from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { forkJoin } from 'rxjs';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { getError, hasError } from '@core/utils/form.utils';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { ViewChild } from "@angular/core";
import { ElementRef } from "@angular/core";
import { FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';

@Component({
	selector: 'app-anamnesis-dock-popup',
	templateUrl: './anamnesis-dock-popup.component.html',
	styleUrls: ['./anamnesis-dock-popup.component.scss']
})
export class AnamnesisDockPopupComponent implements OnInit {

	anamnesisId: number;

	getError = getError;
	hasError = hasError;

	mainDiagnosisError = '';
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
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;

	minDate = MIN_DATE;
	@ViewChild('errorsView') errorsView: ElementRef;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly anamnesisService: AnamnesisService,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
	) {
		this.mainDiagnosis = data.mainDiagnosis;
		this.diagnosticos = data.diagnosticos;
		this.procedimientosService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			anthropometricData: this.formBuilder.group({
				bloodType: [null],
				height: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^[0-9]+$')]],
				weight: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^\\d*\\.?\\d+$')]]
			}),
			riskFactors: this.factoresDeRiesgoFormService.getForm(),
			observations: this.formBuilder.group({
				currentIllnessNote: [null],
				physicalExamNote: [null],
				studiesSummaryNote: [null],
				evolutionNote: [null],
				clinicalImpressionNote: [null],
				otherNote: [null]
			})
		});
		const anamnesis$ = this.anamnesisService.getAnamnesis(this.data.patientInfo.anamnesisId, this.data.patientInfo.internmentEpisodeId);
		const bloodTypes$ = this.internacionMasterDataService.getBloodTypes();

		if (this.data.patientInfo.anamnesisId) {
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

				this.form.controls.riskFactors.setValue({
					heartRate: anamnesis.riskFactors.heartRate,
					respiratoryRate: anamnesis.riskFactors.respiratoryRate,
					temperature: anamnesis.riskFactors.temperature,
					bloodOxygenSaturation: anamnesis.riskFactors.bloodOxygenSaturation,
					systolicBloodPressure: anamnesis.riskFactors.systolicBloodPressure,
					diastolicBloodPressure: anamnesis.riskFactors.diastolicBloodPressure
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

	save(): void {
		this.apiErrors = [];
		if (!this.mainDiagnosis) {
			this.apiErrors.push("Diagnóstico principal obligatorio");
			this.snackBarService.showError('internaciones.anamnesis.messages.ERROR');
			setTimeout(() => {
				this.errorsView.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
			}, 500);
			return;
		}
		if (this.form.valid) {
			const anamnesis: AnamnesisDto = this.buildAnamnesisDto();
			this.anamnesisService.createAnamnesis(anamnesis, this.data.patientInfo.internmentEpisodeId)
				.subscribe((anamnesisResponse: ResponseAnamnesisDto) => {
					this.snackBarService.showSuccess('internaciones.anamnesis.messages.SUCCESS');
					this.dockPopupRef.close(fieldsToUpdate(anamnesis));
				}, responseErrors => {
					this.apiErrorsProcess(responseErrors);
					this.snackBarService.showError('internaciones.anamnesis.messages.ERROR');
				});
		} else {
			this.snackBarService.showError('internaciones.anamnesis.messages.ERROR');
		}

		function fieldsToUpdate(anamnesisDto: AnamnesisDto): InternmentFields {
			return {
				allergies: !!anamnesisDto.allergies,
				heightAndWeight: !!anamnesisDto.anthropometricData?.weight || !!anamnesisDto.anthropometricData?.height,
				bloodType: !!anamnesisDto.anthropometricData?.bloodType,
				mainDiagnosis: !!anamnesisDto.mainDiagnosis,
				diagnosis: !!anamnesisDto.diagnosis,
				riskFactors: !!anamnesisDto.riskFactors,
				immunizations: !!anamnesisDto.immunizations,
				medications: !!anamnesisDto.medications,
				familyHistories: !!anamnesisDto.familyHistories,
				personalHistories: !!anamnesisDto.personalHistories,
				evolutionClinical: !!anamnesisDto.mainDiagnosis,
			}
		}
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
			mainDiagnosis: this.mainDiagnosis?.isAdded ? this.mainDiagnosis : null,
			diagnosis: this.diagnosticos.filter(diagnosis => diagnosis.isAdded),
			familyHistories: this.familyHistories,
			immunizations: this.immunizations,
			medications: this.medications,
			notes: isNull(formValues.observations) ? undefined : formValues.observations,
			personalHistories: this.personalHistories,
			riskFactors: isNull(formValues.riskFactors) ? undefined : {
				bloodOxygenSaturation: getEffectiveValue(formValues.riskFactors.bloodOxygenSaturation),
				diastolicBloodPressure: getEffectiveValue(formValues.riskFactors.diastolicBloodPressure),
				heartRate: getEffectiveValue(formValues.riskFactors.heartRate),
				respiratoryRate: getEffectiveValue(formValues.riskFactors.respiratoryRate),
				systolicBloodPressure: getEffectiveValue(formValues.riskFactors.systolicBloodPressure),
				temperature: getEffectiveValue(formValues.riskFactors.temperature),
				bloodGlucose: getEffectiveValue(formValues.riskFactors.bloodGlucose),
				glycosylatedHemoglobin: getEffectiveValue(formValues.riskFactors.glycosylatedHemoglobin),
				cardiovascularRisk: getEffectiveValue(formValues.riskFactors.cardiovascularRisk)
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

	private apiErrorsProcess(responseErrors): void {
		this.mainDiagnosisError = responseErrors.mainDiagnosis;
		Object.getOwnPropertyNames(responseErrors).forEach(val => {
			if (val !== 'mainDiagnosis' && val !== 'message') {
				const error = responseErrors[val];
				if (Array.isArray(error)) {
					error.forEach(elementError =>
						this.apiErrors.push(elementError)
					);
				} else {
					this.apiErrors.push(error);
				}
			}
		});
	}

}
