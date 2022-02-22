import { Component, Inject, OnInit } from '@angular/core';
import {
	MasterDataInterface,
	DiagnosisDto,
	AllergyConditionDto,
	ImmunizationDto,
	EvolutionNoteDto
} from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { getError, hasError } from '@core/utils/form.utils';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { MIN_DATE } from "@core/utils/date.utils";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { ProcedimientosService } from "@historia-clinica/services/procedimientos.service";
import {
	InternmentFields
} from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

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
	diagnosticos: DiagnosisDto[] = [];
	allergies: AllergyConditionDto[] = [];
	immunizations: ImmunizationDto[] = [];
	procedimientosService: ProcedimientosService;

	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;

	minDate = MIN_DATE;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
	) {
		this.procedimientosService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			anthropometricData: this.formBuilder.group({
				bloodType: [null],
				height: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^[0-9]+$')]],
				weight: [null, [Validators.min(0), Validators.max(1000), Validators.pattern('^\\d*\\.?\\d+$')]]
			}),
			riskFactors: this.formBuilder.group({
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

	}

	save(): void {
		if (this.form.valid) {
			this.apiErrors = [];
			const evolutionNote = this.buildEvolutionNoteDto();
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId)
				.subscribe(() => {
					this.snackBarService.showSuccess('internaciones.nota-evolucion.messages.SUCCESS');
					this.dockPopupRef.close(setFieldsToUpdate(evolutionNote));
				}, error => {
					error.errors.forEach(val => {
						this.apiErrors.push(val);
					});
					this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
				});
		} else {
			this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
			this.form.markAllAsTouched();
		}

		function setFieldsToUpdate(evolutionNoteDto: EvolutionNoteDto): InternmentFields {
			return {
				allergies: !!evolutionNoteDto.allergies,
				anthropometricData: !!evolutionNoteDto.anthropometricData,
				immunizations: !!evolutionNoteDto.immunizations,
				riskFactors: !!evolutionNoteDto.riskFactors,
				diagnosis: !!evolutionNoteDto.diagnosis,
				evolutionClinical: !!evolutionNoteDto.diagnosis,
			}
		}
	}

	private buildEvolutionNoteDto(): EvolutionNoteDto {
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
			diagnosis: this.diagnosticos,
			immunizations: this.immunizations,
			notes: isNull(formValues.observations) ? undefined : formValues.observations,
			riskFactors: isNull(formValues.riskFactors) ? undefined : {
				bloodOxygenSaturation: getEffectiveValue(formValues.riskFactors.bloodOxygenSaturation),
				diastolicBloodPressure: getEffectiveValue(formValues.riskFactors.diastolicBloodPressure),
				heartRate: getEffectiveValue(formValues.riskFactors.heartRate),
				respiratoryRate: getEffectiveValue(formValues.riskFactors.respiratoryRate),
				systolicBloodPressure: getEffectiveValue(formValues.riskFactors.systolicBloodPressure),
				temperature: getEffectiveValue(formValues.riskFactors.temperature)
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

	setRiskFactorEffectiveTime(newEffectiveTime: Moment, formField: string): void {
		((this.form.controls.riskFactors as FormGroup).controls[formField] as FormGroup).controls.effectiveTime.setValue(newEffectiveTime);
	}

	clearBloodType(control): void {
		control.controls.bloodType.reset();
	}
}
