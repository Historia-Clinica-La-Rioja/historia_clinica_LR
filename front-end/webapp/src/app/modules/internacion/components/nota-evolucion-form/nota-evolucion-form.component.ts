import { Component, OnInit } from '@angular/core';
import { MasterDataInterface, DiagnosisDto, AllergyConditionDto, InmunizationDto, EvolutionNoteDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { EvolutionNoteReportService } from '@api-rest/services/evolution-note-report.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

@Component({
	selector: 'app-nota-evolucion-form',
	templateUrl: './nota-evolucion-form.component.html',
	styleUrls: ['./nota-evolucion-form.component.scss']
})
export class NotaEvolucionFormComponent implements OnInit {

	private internmentEpisodeId: number;
	private patientId: number;

	form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];
	diagnosticos: DiagnosisDto[] = [];
	allergies: AllergyConditionDto[] = [];
	inmunizations: InmunizationDto[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService,
		private evolutionNoteService: EvolutionNoteService,
		private route: ActivatedRoute,
		private contextService: ContextService,
		private router: Router,
		private snackBarService: SnackBarService
	) {
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
			const evolutionNote = this.buildEvolutionNoteDto();

			this.evolutionNoteService.createDocument(evolutionNote, this.internmentEpisodeId).subscribe(
				(evolutionNoteResponse: ResponseEvolutionNoteDto) => {
					this.snackBarService.showSuccess('internaciones.nota-evolucion.messages.SUCCESS');
					this.goToInternmentSummary();
				}, _ => this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR'));
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
			inmunizations: this.inmunizations,
			notes: isNull(formValues.observations) ? undefined : formValues.observations,
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

	back(): void {
		window.history.back();
	}

}
