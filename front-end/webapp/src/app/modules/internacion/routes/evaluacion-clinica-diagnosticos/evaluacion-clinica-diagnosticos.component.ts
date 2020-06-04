import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { BasicPatientDto, InternmentSummaryDto, DiagnosesGeneralStateDto, MasterDataInterface, EvolutionDiagnosisDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { MapperService } from '@presentation/services/mapper.service';
import { InternacionService } from '@api-rest/services/internacion.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { Observable, forkJoin } from 'rxjs';
import { InternmentEpisodeSummary } from '@presentation/components/internment-episode-summary/internment-episode-summary.component';
import { TableCheckbox } from 'src/app/modules/material/model/table.model';
import { SelectionModel } from '@angular/cdk/collections';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TableService } from '@core/services/table.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { HEALTH_CLINICAL_STATUS } from '../../constants/ids';

const MAIN_DIAGNOSIS_INDEX = 0;

@Component({
	selector: 'app-evaluacion-clinica-diagnosticos',
	templateUrl: './evaluacion-clinica-diagnosticos.component.html',
	styleUrls: ['./evaluacion-clinica-diagnosticos.component.scss']
})
export class EvaluacionClinicaDiagnosticosComponent implements OnInit {

	private internmentEpisodeId: number;
	private patientId: number;
	private healthClinicalStatus: MasterDataInterface<string>[];

	form: FormGroup;
	patient$: Observable<PatientBasicData>;
	internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	diagnostics: TableCheckbox<DiagnosesGeneralStateDto> = {
		data: [],
		columns: [
			{
				def: 'diagnosis',
				header: 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.DIAGNOSIS',
				display: ap => ap.snomed.pt
			},
			{
				def: 'status',
				header: 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.STATUS',
				display: (row) => this.healthClinicalStatus?.find(status => status.id === row.statusId).description
			},
			{
				def: 'verificacion',
				header: 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.VERIFICATION',
				display: row => row.presumptive ? 'Presuntivo' : 'Confirmado'
			},
			{
				def: 'type',
				display: row => row.main ? 'internaciones.clinical-assessment-diagnosis.diagnostics.table.columns.MAIN' : '',
			},
		],
		displayedColumns: [],
		selection: new SelectionModel<DiagnosesGeneralStateDto>(true, [])
	};
	isAllSelected = this.tableService.isAllSelected;
	masterToggle = this.tableService.masterToggle;

	constructor(
		private readonly patientService: PatientService,
		private readonly internmentService: InternacionService,
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly contextService: ContextService,
		private readonly mapperService: MapperService,
		private readonly tableService: TableService,
		private readonly formBuilder: FormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
	) {
		this.diagnostics.displayedColumns = this.diagnostics.columns?.map(c => c.def).concat(['select']);
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params: ParamMap) => {
				const routedDiagnosisId = Number(params.get('idDiagnostico'));

				this.internmentEpisodeId = Number(params.get('idInternacion'));
				this.patientId = Number(params.get('idPaciente'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
					map((internmentEpisodeSummary: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisodeSummary))
				);

				this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisodeId).pipe(
					map(diagnostics => diagnostics.filter(od => od.statusId === HEALTH_CLINICAL_STATUS.ACTIVO))
				).subscribe(
					diagnostics => {
						this.diagnostics.data = diagnostics;
						this.diagnostics.selection.select(diagnostics.find(d => d.id === routedDiagnosisId));
					}
				);
			}
		);

		this.internacionMasterDataService.getHealthClinical().subscribe(healthClinical => {
			this.healthClinicalStatus = healthClinical;
		});

		this.form = this.formBuilder.group({
			currentIllnessNote: [],
			physicalExamNote: [],
			studiesSummaryNote: [],
			evolutionNote: [],
			clinicalImpressionNote: [],
			otherNote: [],
		});

	}

	save(): void {
		if (this.form.valid) {
			const evolutionNote: EvolutionDiagnosisDto = {
				diagnosesId: this.diagnostics.selection.selected.map(diagnosis => diagnosis.id),
				notes: this.form.value
			}
			this.evolutionNoteService.createEvolutionDiagnosis(evolutionNote, this.internmentEpisodeId).subscribe(
				_ => {
					this.snackBarService.showSuccess('internaciones.clinical-assessment-diagnosis.messages.SUCCESS')
					const url = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
					this.router.navigate([url]);
				},
				_ => this.snackBarService.showError('internaciones.clinical-assessment-diagnosis.messages.ERROR')
			);
		}
	}

	back(): void {
		window.history.back();
	}

}
