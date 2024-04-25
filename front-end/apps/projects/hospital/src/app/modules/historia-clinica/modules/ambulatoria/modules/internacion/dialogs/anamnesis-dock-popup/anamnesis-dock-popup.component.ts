import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import {
	AllergyConditionDto,
	AnamnesisDto,
	DiagnosisDto, HealthConditionDto, HealthHistoryConditionDto, HospitalizationProcedureDto, ImmunizationDto,
	MasterDataInterface,
	MedicationDto,
	ResponseAnamnesisDto
} from '@api-rest/api-model';
import { AnamnesisService } from '@api-rest/services/anamnesis.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { getError, hasError } from '@core/utils/form.utils';
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { TranslateService } from '@ngx-translate/core';
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';
import { ComponentEvaluationManagerService } from '../../../../services/component-evaluation-manager.service';
import { DocumentActionReasonComponent } from '../document-action-reason/document-action-reason.component';
import { AnthropometricData } from '@historia-clinica/services/patient-evolution-charts.service';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';

@Component({
	selector: 'app-anamnesis-dock-popup',
	templateUrl: './anamnesis-dock-popup.component.html',
	styleUrls: ['./anamnesis-dock-popup.component.scss'],
	providers: [
		ComponentEvaluationManagerService,
	]
})
export class AnamnesisDockPopupComponent implements OnInit {

	anamnesisId: number;

	getError = getError;
	hasError = hasError;

	mainDiagnosisError = '';
	anamnesis: ResponseAnamnesisDto;
	form: UntypedFormGroup;

	bloodTypes: MasterDataInterface<string>[];
	diagnosticos: DiagnosisDto[] = [];
	mainDiagnosis: HealthConditionDto;
	personalHistories: HealthHistoryConditionDto[] = [];
	familyHistories: HealthHistoryConditionDto[] = [];
	procedures: HospitalizationProcedureDto[] = [];
	allergies: AllergyConditionDto[] = [];
	immunizations: ImmunizationDto[] = [];
	medications: MedicationDto[] = [];
	apiErrors: string[] = [];
	procedimientosService: ProcedimientosService;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	isDisableConfirmButton = false;
	anthropometricDataSubject = new BehaviorSubject<boolean>(true);
	observationsSubject = new BehaviorSubject<boolean>(true);
	minDate = MIN_DATE;
	anthropometricData: AnthropometricData;
	@ViewChild('errorsView') errorsView: ElementRef;
	alergiasNuevaConsultaService: AlergiasNuevaConsultaService;
	isAllergyNoRefer: boolean = true;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly anamnesisService: AnamnesisService,
		private readonly snackBarService: SnackBarService,
		private readonly translateService: TranslateService,
		private readonly dialog: MatDialog,
		private readonly snomedService: SnomedService,
	) {
		this.mainDiagnosis = data.mainDiagnosis;
		this.diagnosticos = data.diagnosticos;
		this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosticos;
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService, this.internacionMasterDataService);
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
				this.anamnesis = results[0];
				this.bloodTypes = results[1];
				this.loadAnamnesisInformation();
			})
		} else {
			bloodTypes$.subscribe(bloodTypes => this.bloodTypes = bloodTypes);
		}

		this.form.get('anthropometricData').valueChanges.subscribe(formData => {
			const formValues = Object.values(formData);
			const allFormValuesAreNull = formValues.every(value => value === null);
			this.anthropometricDataSubject.next(allFormValuesAreNull);
			this.anthropometricData = formData;
		});

		this.form.get('observations').valueChanges.pipe(
			map(formData => Object.values(formData)),
			map(formValues => formValues.every(value => value === null || value === ''))
		).subscribe(allFormValuesAreNull => {
			this.observationsSubject.next(allFormValuesAreNull);
		});
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
			this.isDisableConfirmButton = true;

			const anamnesis: AnamnesisDto = this.buildAnamnesisDto();
			if (this.data.patientInfo.anamnesisId) {
				this.openEditReason(anamnesis);
				return;
			}
			this.anamnesisService.createAnamnesis(anamnesis, this.data.patientInfo.internmentEpisodeId)
				.subscribe(
					() => this.showSuccesAndClosePopup(anamnesis),
					responseErrors => this.showError(responseErrors)
				);
		} else {
			this.snackBarService.showError('internaciones.anamnesis.messages.ERROR');
		}
	}

	fieldsToUpdate(anamnesisDto: AnamnesisDto): InternmentFields {
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

	private buildAnamnesisDto(): AnamnesisDto {
		const formValues = this.form.value;
		return {
			confirmed: true,
			allergies: {
				isReferred: (this.isAllergyNoRefer && this.allergies.length === 0) ? null: this.isAllergyNoRefer,
				content: this.allergies
			},
			anthropometricData: isNull(formValues.anthropometricData) ? undefined : {
				bloodType: formValues.anthropometricData.bloodType ? {
					id: this.anamnesis ?
						(formValues.anthropometricData.bloodType.description === this.anamnesis.anthropometricData?.bloodType?.value) ? this.anamnesis.anthropometricData.bloodType.id : null
						: null,
					value: formValues.anthropometricData.bloodType.description
				} : undefined,
				height: formValues.anthropometricData.height ? {
					id: this.anamnesis ?
						(formValues.anthropometricData.height === this.anamnesis.anthropometricData?.height?.value) ? this.anamnesis.anthropometricData.height.id : null
						: null,
					value: formValues.anthropometricData.height
				} : undefined,
				weight: formValues.anthropometricData.weight ? {
					id: this.anamnesis ?
						(formValues.anthropometricData.weight === this.anamnesis.anthropometricData?.weight?.value) ? this.anamnesis.anthropometricData.weight.id : null
						: null,
					value: formValues.anthropometricData.weight
				} : undefined,
			},
			mainDiagnosis: this.mainDiagnosis?.isAdded ? this.mainDiagnosis : null,
			diagnosis: this.diagnosticos.filter(diagnosis => diagnosis.isAdded),
			familyHistories: this.familyHistories,
			immunizations: this.immunizations,
			medications: this.medications,
			notes: isNull(formValues.observations) ? undefined : formValues.observations,
			personalHistories: this.personalHistories,
			riskFactors: isNull(formValues.riskFactors) ? undefined : {
				bloodOxygenSaturation: this.getEffectiveValue(formValues.riskFactors.bloodOxygenSaturation),
				diastolicBloodPressure: this.getEffectiveValue(formValues.riskFactors.diastolicBloodPressure),
				heartRate: this.getEffectiveValue(formValues.riskFactors.heartRate),
				respiratoryRate: this.getEffectiveValue(formValues.riskFactors.respiratoryRate),
				systolicBloodPressure: this.getEffectiveValue(formValues.riskFactors.systolicBloodPressure),
				temperature: this.getEffectiveValue(formValues.riskFactors.temperature),
				bloodGlucose: this.getEffectiveValue(formValues.riskFactors.bloodGlucose),
				glycosylatedHemoglobin: this.getEffectiveValue(formValues.riskFactors.glycosylatedHemoglobin),
				cardiovascularRisk: this.getEffectiveValue(formValues.riskFactors.cardiovascularRisk)
			},
			procedures: this.procedures
		};

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every(el => el === null);
		}
	}

	private getEffectiveValue(controlValue: any): any {
		return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
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

	private loadAnamnesisInformation() {
		this.componentEvaluationManagerService.anamnesis = this.anamnesis;
		this.allergies = this.anamnesis.allergies.content;
		this.diagnosticos = this.anamnesis.diagnosis;
		this.diagnosticos.forEach(d => d.isAdded = true);
		this.familyHistories = this.anamnesis.familyHistories;
		this.immunizations = this.anamnesis.immunizations;
		this.medications = this.anamnesis.medications;
		this.personalHistories = this.anamnesis.personalHistories;
		this.procedures = this.anamnesis?.procedures || null;
		this.mainDiagnosis = this.anamnesis.mainDiagnosis;
		this.mainDiagnosis.isAdded = true;
		if (this.anamnesis.anthropometricData) {
			const findBloodTypeValue = findBloodType(this.bloodTypes, this.anamnesis.anthropometricData.bloodType?.value)
			this.form.controls.anthropometricData.setValue({
				bloodType: findBloodTypeValue ? findBloodTypeValue : null,
				height: this.anamnesis.anthropometricData.height?.value ? this.anamnesis.anthropometricData.height.value : null,
				weight: this.anamnesis.anthropometricData.weight?.value ? this.anamnesis.anthropometricData.weight.value : null
			});
		}
		if (this.anamnesis.riskFactors) {
			Object.keys(this.anamnesis.riskFactors).forEach((key: string) => {
				if (this.anamnesis.riskFactors[key].value != undefined) {
					this.form.controls.riskFactors.patchValue({ [key]: { value: this.anamnesis.riskFactors[key].value } });
					const date: Date = new Date(this.anamnesis.riskFactors[key].effectiveTime);
					this.form.controls.riskFactors.patchValue({ [key]: { effectiveTime: date } });
				}
			});
		}
		Object.keys(this.anamnesis.notes).forEach((key: string) => {
			if (this.anamnesis.notes[key]) {
				this.form.controls.observations.patchValue({ [key]: this.anamnesis.notes[key] })
			}
		});

		function findBloodType(bloodTypes: MasterDataInterface<string>[], value: string): MasterDataInterface<string> {
			return bloodTypes.find(bloodType => bloodType.description === value);
		}
	}

	showSuccesAndClosePopup(anamnesis: AnamnesisDto) {
		this.snackBarService.showSuccess('internaciones.anamnesis.messages.SUCCESS');
		this.dockPopupRef.close(this.fieldsToUpdate(anamnesis));
	}

	showError(responseErrors) {
		this.isDisableConfirmButton = false;
		this.apiErrorsProcess(responseErrors);
		this.snackBarService.showError('internaciones.anamnesis.messages.ERROR');
	}

	private openEditReason(anamnesis: AnamnesisDto) {
		const dialogRef = this.dialog.open(DocumentActionReasonComponent, {
			data: {
				title: 'internaciones.dialogs.actions-document.EDIT_TITLE',
				subtitle: 'internaciones.dialogs.actions-document.SUBTITLE',
			},
			width: "50vh",
			autoFocus: false,
			disableClose: true
		});
		dialogRef.afterClosed().subscribe(reason => {
			this.isDisableConfirmButton = false;
			if (reason) {
				anamnesis.modificationReason = reason;
				this.anamnesisService.editAnamnesis(anamnesis, this.data.patientInfo.anamnesisId, this.data.patientInfo.internmentEpisodeId).subscribe(success => {
					this.showSuccesAndClosePopup(anamnesis);
				}, responseErrors => this.showError(responseErrors));
			}
		});
	}

	getAnthropometricData(): Observable<boolean> {
		return this.anthropometricDataSubject.asObservable();
	}

	getObservations(): Observable<boolean> {
		return this.observationsSubject.asObservable();
	}

	setIsAllergyNoRefer = ($event) => {
		this.isAllergyNoRefer = $event;
	}
}
