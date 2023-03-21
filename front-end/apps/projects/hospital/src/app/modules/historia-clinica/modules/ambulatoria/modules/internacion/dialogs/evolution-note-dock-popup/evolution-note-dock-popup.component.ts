import { Component, Inject, OnInit } from '@angular/core';
import {
	MasterDataInterface,
	DiagnosisDto,
	AllergyConditionDto,
	ImmunizationDto,
	EvolutionNoteDto,
	HealthConditionDto, ResponseEvolutionNoteDto
} from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { getError, hasError } from '@core/utils/form.utils';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { MIN_DATE } from "@core/utils/date.utils";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { Procedimiento, ProcedimientosService } from "@historia-clinica/services/procedimientos.service";
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { PermissionsService } from "@core/services/permissions.service";
import { anyMatch } from "@core/utils/array.utils";
import { dateToMoment } from "@core/utils/moment.utils";
import { TranslateService } from '@ngx-translate/core';
import { DocumentActionReasonComponent } from '../document-action-reason/document-action-reason.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
	selector: 'app-evolution-note-dock-popup',
	templateUrl: './evolution-note-dock-popup.component.html',
	styleUrls: ['./evolution-note-dock-popup.component.scss']
})
export class EvolutionNoteDockPopupComponent implements OnInit {

	apiErrors: string[] = [];

	getError = getError;
	hasError = hasError;

	form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];
	mainDiagnosis: HealthConditionDto;
	diagnosticos: DiagnosisDto[] = [];
	allergies: AllergyConditionDto[] = [];
	immunizations: ImmunizationDto[] = [];
	procedimientosService: ProcedimientosService;
	factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
	isNursingEvolutionNote: boolean;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	minDate = MIN_DATE;
	evolutionNote: ResponseEvolutionNoteDto;
	isDisableConfirmButton = false;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly permissionsService: PermissionsService,
		private readonly translateService: TranslateService,
		private readonly dialog: MatDialog,
	) {
		this.mainDiagnosis = data.mainDiagnosis;
		this.diagnosticos = data.diagnosticos;
		this.procedimientosService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(formBuilder, translateService);
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isNursingEvolutionNote = !anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD]) && anyMatch<ERole>(userRoles, [ERole.ENFERMERO]);
		})
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
				currentIllnessNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
				physicalExamNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
				studiesSummaryNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
				evolutionNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
				clinicalImpressionNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
				otherNote: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]]
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
			procedures: isNull(this.procedimientosService.getProcedimientos()) ? undefined : this.procedimientosService.getProcedimientos(),
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
		this.allergies = this.evolutionNote.allergies;
		this.diagnosticos = this.evolutionNote.diagnosis;
		this.diagnosticos.forEach(d => d.isAdded = true);
		this.immunizations = this.evolutionNote.immunizations;
		const procedure: Procedimiento[] = this.evolutionNote.procedures.map(p => {
			return { snomed: p.snomed, performedDate: p.performedDate }
		});
		procedure.forEach(p => this.procedimientosService.add(p));
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
					this.form.controls.riskFactors.patchValue({ [key]: { effectiveTime: dateToMoment(date) } });
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
		this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
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
			if (reason) {
				evolutionNote.modificationReason = reason;
				this.evolutionNoteService.editEvolutionDiagnosis(evolutionNote, this.data.evolutionNoteId, this.data.internmentEpisodeId).subscribe(
					success => this.showSuccesAndClosePopup(evolutionNote),
					error => {
						this.isDisableConfirmButton = false;
						this.showError(error);
					});
			}
		});
	}
}


