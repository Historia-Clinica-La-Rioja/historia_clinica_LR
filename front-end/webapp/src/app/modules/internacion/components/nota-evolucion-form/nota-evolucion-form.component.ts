import { Component, OnInit } from '@angular/core';
import { MasterDataInterface, DiagnosisDto, AllergyConditionDto, InmunizationDto, EvolutionNoteDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
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
				bloodType: [null, Validators.required],
				height: [null, Validators.required],
				weight: [null, Validators.required],
			}),
			vitalSigns: this.formBuilder.group({
				heartRate: [null, Validators.required],
				respiratoryRate: [null, Validators.required],
				temperature: [null, Validators.required],
				bloodOxygenSaturation: [null, Validators.required],
				systolicBloodPressure: [null, Validators.required],
				diastolicBloodPressure: [null, Validators.required],
			}),
			observations: this.formBuilder.group ({
				currentIllnessNote: [null, Validators.required],
				physicalExamNote: [null, Validators.required],
				studiesSummaryNote: [null, Validators.required],
				evolutionNote: [null, Validators.required],
				clinicalImpressionNote: [null, Validators.required],
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
					this.evolutionNoteService.getPDF(evolutionNoteResponse.id, this.internmentEpisodeId).subscribe(
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
		return {
			confirmed: true,
			allergies: this.allergies,
			anthropometricData: {
				bloodType: {
					id: this.form.value.anthropometricData.bloodType.id,
					value: this.form.value.anthropometricData.bloodType.description
				},
				height: {id: null, value: this.form.value.anthropometricData.height},
				weight: {id: null, value: this.form.value.anthropometricData.weight},
				bmi: null,
			},
			diagnosis: this.diagnosticos,
			inmunizations: this.inmunizations,
			notes: this.form.value.observations,
			vitalSigns: {
				bloodOxygenSaturation: {id: null, value: this.form.value.vitalSigns.bloodOxygenSaturation},
				diastolicBloodPressure: {id: null, value: this.form.value.vitalSigns.diastolicBloodPressure},
				heartRate: {id: null, value: this.form.value.vitalSigns.heartRate},
				respiratoryRate: {id: null, value: this.form.value.vitalSigns.respiratoryRate},
				systolicBloodPressure: {id: null, value: this.form.value.vitalSigns.systolicBloodPressure},
				temperature: {id: null, value: this.form.value.vitalSigns.temperature},
			}
		};
	}

	back(): void {
		window.history.back();
	}

}
