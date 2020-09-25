import { Component, OnInit } from '@angular/core';
import {
	MasterDataInterface,
	DiagnosisDto,
	AllergyConditionDto,
	ImmunizationDto,
	EvolutionNoteDto,
	ResponseEvolutionNoteDto
} from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { getError, hasError } from '@core/utils/form.utils';
import { ProcedimientosService } from '../../../../services/procedimientos.service';
import { SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';

@Component({
	selector: 'app-nota-evolucion-form',
	templateUrl: './nota-evolucion-form.component.html',
	styleUrls: ['./nota-evolucion-form.component.scss']
})
export class NotaEvolucionFormComponent implements OnInit {

	private internmentEpisodeId: number;
	private patientId: number;
	apiErrors: string[] = [];

	getError = getError;
	hasError = hasError;

	form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];
	diagnosticos: DiagnosisDto[] = [];
	allergies: AllergyConditionDto[] = [];
	immunizations: ImmunizationDto[] = [];
	procedimientosService: ProcedimientosService;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
		private readonly router: Router,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
	) {
		this.procedimientosService = new ProcedimientosService(formBuilder, snomedService);
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params: ParamMap) => {
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

	}

	save(): void {
		if (this.form.valid) {
			this.apiErrors = [];
			const evolutionNote = this.buildEvolutionNoteDto();
			this.evolutionNoteService.createDocument(evolutionNote, this.internmentEpisodeId)
			.subscribe(() => {
					this.snackBarService.showSuccess('internaciones.nota-evolucion.messages.SUCCESS');
					this.goToInternmentSummary();
				}, errors => {
					Object.getOwnPropertyNames(errors).forEach(val => {
						this.apiErrors.push(errors[val]);
					  });
					this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
				});
		} else {
			this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
		}
	}

	private goToInternmentSummary(): void {
		const url = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
		this.router.navigate([url]);
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

	back(): void {
		window.history.back();
	}

}
