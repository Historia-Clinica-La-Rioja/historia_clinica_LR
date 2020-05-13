import { Component, OnInit } from '@angular/core';
import { MasterDataInterface, DiagnosisDto, AllergyConditionDto, InmunizationDto, EvolutionNoteDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { EvolutionNoteReportService } from '@api-rest/services/evolution-note-report.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

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
		private evolutionNoteReportService: EvolutionNoteReportService,
		private route: ActivatedRoute,
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
				heartRate: [null],
				respiratoryRate: [null],
				temperature: [null],
				bloodOxygenSaturation: [null],
				systolicBloodPressure: [null],
				diastolicBloodPressure: [null],
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

		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => this.bloodTypes = bloodTypes);

	}

	save(): void {
		if (this.form.valid) {
			const evolutionNote = this.buildEvolutionNoteDto();

			this.evolutionNoteService.createDocument(evolutionNote, this.internmentEpisodeId).subscribe(
				(evolutionNoteResponse: ResponseEvolutionNoteDto) => {
					this.evolutionNoteReportService.getPDF(evolutionNoteResponse.id, this.internmentEpisodeId).subscribe(
						_ => this.goToInternmentSummary(), _ => this.goToInternmentSummary()
					);
					this.snackBarService.showSuccess('internaciones.nota-evolucion.messages.SUCCESS');
				}, _ => this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR'));
		} else {
			this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
		}
	}

	private goToInternmentSummary(): void {
		const url = `internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
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
				bloodOxygenSaturation: getValue(formValues.vitalSigns.bloodOxygenSaturation),
				diastolicBloodPressure: getValue(formValues.vitalSigns.diastolicBloodPressure),
				heartRate: getValue(formValues.vitalSigns.heartRate),
				respiratoryRate: getValue(formValues.vitalSigns.respiratoryRate),
				systolicBloodPressure: getValue(formValues.vitalSigns.systolicBloodPressure),
				temperature: getValue(formValues.vitalSigns.temperature)
			}
		};

		function isNull(formGroupValues: any): boolean {
			return Object.values(formGroupValues).every(el => el === null);
		}

		function getValue(controlValue: any) {
			return controlValue ? { value: controlValue } : undefined;
		}
	}

	back(): void {
		window.history.back();
	}

}
