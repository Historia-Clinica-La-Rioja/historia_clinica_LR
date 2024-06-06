import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import {
	AllergyConditionDto, AppFeature, DiagnosisDto, EvolutionNoteDto,
	HealthConditionDto, HospitalizationProcedureDto, ImmunizationDto, MasterDataInterface, ResponseEvolutionNoteDto
} from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { PermissionsService } from "@core/services/permissions.service";
import { anyMatch } from "@core/utils/array.utils";
import { MIN_DATE } from "@core/utils/date.utils";
import { getError, hasError } from '@core/utils/form.utils';
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { ComponentEvaluationManagerService } from '../../../../services/component-evaluation-manager.service';
import { DocumentActionReasonComponent } from '../document-action-reason/document-action-reason.component';
import { AnthropometricData } from '@historia-clinica/services/patient-evolution-charts.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-evolution-note-dock-popup',
	templateUrl: './evolution-note-dock-popup.component.html',
	styleUrls: ['./evolution-note-dock-popup.component.scss'],
	providers: [
		ComponentEvaluationManagerService,
	]
})
export class EvolutionNoteDockPopupComponent implements OnInit {

	apiErrors: string[] = [];

	getError = getError;
	hasError = hasError;

	form: UntypedFormGroup;

	bloodTypes: MasterDataInterface<string>[];
	mainDiagnosis: HealthConditionDto;
	diagnosticos: DiagnosisDto[] = [];
	allergies: AllergyConditionDto[] = [];
	immunizations: ImmunizationDto[] = [];
	procedures: HospitalizationProcedureDto[] = [];
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	isNursingEvolutionNote: boolean;

	minDate = MIN_DATE;
	evolutionNote: ResponseEvolutionNoteDto;
	isDisableConfirmButton = false;
	anthropometricDataSubject = new BehaviorSubject<boolean>(true);
	observationsSubject = new BehaviorSubject<boolean>(true);
	anthropometricData: AnthropometricData;
	isEvolutionChartsFFActive = false;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: UntypedFormBuilder,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly permissionsService: PermissionsService,
		private readonly translateService: TranslateService,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.diagnosticos = data.diagnosticos;
		this.mainDiagnosis = data.mainDiagnosis;
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService);
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isNursingEvolutionNote = !anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD]) && anyMatch<ERole>(userRoles, [ERole.ENFERMERO]);
		})
	}

	ngOnInit(): void {
		this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosticos;
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

		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => this.bloodTypes = bloodTypes);


		if (this.data.evolutionNoteId) {
			if (this.data.documentType === "Nota de evolución") {
				this.evolutionNoteService.getEvolutionDiagnosis(this.data.evolutionNoteId, this.data.internmentEpisodeId).subscribe(e => {
					this.evolutionNote = e;
					this.loadEvolutionNoteInfo();
				});
			}
			else {
				this.evolutionNoteService.getEvolutionDiagnosisNursing(this.data.evolutionNoteId, this.data.internmentEpisodeId).subscribe(e => {
					this.evolutionNote = e;
					this.loadEvolutionNoteInfo();
				});
			}
		}

		this.form.get('observations').valueChanges.pipe(
			map(formData => Object.values(formData)),
			map(formValues => formValues.every(value => value === null || value === ''))
		).subscribe(allFormValuesAreNull => {
			this.observationsSubject.next(allFormValuesAreNull);
		});

		this.form.get('anthropometricData').valueChanges.subscribe(formData => {
			const formValues = Object.values(formData);
			const allFormValuesAreNull = formValues.every(value => value === null);
			this.anthropometricDataSubject.next(allFormValuesAreNull);
			this.anthropometricData = formData;
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_GRAFICOS_EVOLUCIONES_ANTROPOMETRICAS_EN_DESARROLLO).subscribe(isEvolutionChartsActive => this.isEvolutionChartsFFActive = isEvolutionChartsActive);
	}

	save(): void {
		if (this.form.valid) {
			this.isDisableConfirmButton = true;

			this.apiErrors = [];
			const evolutionNote = this.buildEvolutionNoteDto();
			if (this.data.evolutionNoteId) {
				this.openEditReason(evolutionNote);
				return;
			}
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId)
				.subscribe(
					() => this.showSuccesAndClosePopup(evolutionNote),
					error => {
						this.isDisableConfirmButton = false;
						this.showError(error);
					});
		} else {
			this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
			this.form.markAllAsTouched();
		}
	}

	private setFieldsToUpdate(evolutionNoteDto: EvolutionNoteDto): InternmentFields {
		return {
			allergies: !!evolutionNoteDto.allergies,
			heightAndWeight: !!evolutionNoteDto.anthropometricData?.weight || !!evolutionNoteDto.anthropometricData?.height,
			bloodType: !!evolutionNoteDto.anthropometricData?.bloodType,
			immunizations: !!evolutionNoteDto.immunizations,
			riskFactors: !!evolutionNoteDto.riskFactors,
			mainDiagnosis: !!evolutionNoteDto.mainDiagnosis,
			diagnosis: !!evolutionNoteDto.diagnosis,
			evolutionClinical: !!evolutionNoteDto.diagnosis,
		}
	}

	private buildEvolutionNoteDto(): EvolutionNoteDto {
		const formValues = this.form.value;
		return {
			confirmed: true,
			allergies: this.allergies,
			anthropometricData: isNull(formValues.anthropometricData) ? undefined : {
				bloodType: formValues.anthropometricData.bloodType ? {
					id: this.evolutionNote ?
						(formValues.anthropometricData.bloodType.description === this.evolutionNote.anthropometricData?.bloodType?.value) ? this.evolutionNote.anthropometricData.bloodType.id : null
						: null,
					value: formValues.anthropometricData.bloodType.description
				} : undefined,
				height: formValues.anthropometricData.height ? {
					id: this.evolutionNote ?
						(formValues.anthropometricData.height === this.evolutionNote.anthropometricData?.height?.value) ? this.evolutionNote.anthropometricData.height.id : null
						: null,
					value: formValues.anthropometricData.height
				} : undefined,
				weight: formValues.anthropometricData.weight ? {
					id: this.evolutionNote ?
						(formValues.anthropometricData.weight === this.evolutionNote.anthropometricData?.weight?.value) ? this.evolutionNote.anthropometricData.weight.id : null
						: null,
					value: formValues.anthropometricData.weight
				} : undefined,
			},
			mainDiagnosis: this.mainDiagnosis?.isAdded ? this.mainDiagnosis : null,
			diagnosis: this.diagnosticos.filter(diagnosis => diagnosis.isAdded),
			immunizations: this.immunizations,
			notes: isNull(formValues.observations) ? undefined : formValues.observations,
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
			procedures: this.procedures,
			isNursingEvolutionNote: (this.data.documentType) ? this.data.documentType === "Nota de evolución de enfermería" : this.isNursingEvolutionNote
		};

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every(el => el === null);
		}

		function getEffectiveValue(controlValue: any) {
			return controlValue.value ? { value: controlValue.value, effectiveTime: controlValue.effectiveTime } : undefined;
		}
	}

	clearBloodType(control): void {
		control.controls.bloodType.reset();
	}

	loadEvolutionNoteInfo() {
		this.componentEvaluationManagerService.evolutionNote = this.evolutionNote;
		this.allergies = this.evolutionNote.allergies;

		let evolutionNoteDiagnosis = this.evolutionNote.diagnosis;
		evolutionNoteDiagnosis?.forEach(d => d.isAdded = true);
		this.diagnosticos = this.diagnosticos.filter(d => !evolutionNoteDiagnosis.some(e => e.snomed.sctid === d.snomed.sctid));
		this.diagnosticos = this.diagnosticos?.concat(evolutionNoteDiagnosis)

		this.immunizations = this.evolutionNote.immunizations;
		this.procedures = this.evolutionNote.procedures;
		this.mainDiagnosis = this.evolutionNote.mainDiagnosis;
		if (this.mainDiagnosis)
			this.mainDiagnosis.isAdded = true;
		if (this.evolutionNote.anthropometricData) {
			const findBloodTypeValue = findBloodType(this.bloodTypes, this.evolutionNote.anthropometricData.bloodType?.value)
			this.form.controls.anthropometricData.setValue({
				bloodType: findBloodTypeValue ? findBloodTypeValue : null,
				height: this.evolutionNote.anthropometricData.height?.value ? this.evolutionNote.anthropometricData.height.value : null,
				weight: this.evolutionNote.anthropometricData.weight?.value ? this.evolutionNote.anthropometricData.weight.value : null
			});
		}
		if (this.evolutionNote.riskFactors) {
			Object.keys(this.evolutionNote.riskFactors).forEach((key: string) => {
				if (this.evolutionNote.riskFactors[key].value != undefined) {
					this.form.controls.riskFactors.patchValue({ [key]: { value: this.evolutionNote.riskFactors[key].value } });
					const date: Date = new Date(this.evolutionNote.riskFactors[key].effectiveTime);
					this.form.controls.riskFactors.patchValue({ [key]: { effectiveTime: date } });
				}
			});
		}
		Object.keys(this.evolutionNote.notes).forEach((key: string) => {
			if (this.evolutionNote.notes[key]) {
				this.form.controls.observations.patchValue({ [key]: this.evolutionNote.notes[key] })
			}
		});

		function findBloodType(bloodTypes: MasterDataInterface<string>[], value: string): MasterDataInterface<string> {
			return bloodTypes.find(bloodType => bloodType.description === value);
		}
	}

	showSuccesAndClosePopup(evolutionNote: EvolutionNoteDto) {
		this.snackBarService.showSuccess('internaciones.nota-evolucion.messages.SUCCESS');
		this.dockPopupRef.close(this.setFieldsToUpdate(evolutionNote));
	}

	showError(error) {
		error.errors?.forEach(val => {
			this.apiErrors.push(val);
		});
		let msg = (error.text) ? error.text : 'internaciones.nota-evolucion.messages.ERROR';
		this.snackBarService.showError(msg);
	}

	private openEditReason(evolutionNote: EvolutionNoteDto) {
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
				evolutionNote.modificationReason = reason;
				this.evolutionNoteService.editEvolutionDiagnosis(evolutionNote, this.data.evolutionNoteId, this.data.internmentEpisodeId).subscribe(
					success => this.showSuccesAndClosePopup(evolutionNote),
					error => {
						this.showError(error);
					});
			}
		});
	}

	getAnthropometricData(): Observable<boolean> {
		return this.anthropometricDataSubject.asObservable();
	}

	getObservations(): Observable<boolean> {
		return this.observationsSubject.asObservable();
	}
}


