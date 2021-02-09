import { MainDiagnosisDto, PersonPhotoDto } from '@api-rest/api-model';
import { MainDiagnosesService } from '@api-rest/services/main-diagnoses.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { BasicPatientDto, InternmentSummaryDto, HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { InternmentEpisodeSummary } from '@presentation/components/internment-episode-summary/internment-episode-summary.component';
import { PatientService } from '@api-rest/services/patient.service';
import { InternacionService } from '@api-rest/services/internacion.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ContextService } from '@core/services/context.service';
import { MapperService } from '@presentation/services/mapper.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ActivatedRoute, Router, ParamMap } from '@angular/router';
import { map } from 'rxjs/operators';
import { SnomedSemanticSearch, SnomedService } from '../../../../services/snomed.service';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { MatSelectionList } from '@angular/material/list';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';

@Component({
	selector: 'app-cambiar-diagnostico-principal',
	templateUrl: './cambiar-diagnostico-principal.component.html',
	styleUrls: ['./cambiar-diagnostico-principal.component.scss']
})
export class CambiarDiagnosticoPrincipalComponent implements OnInit {

	@ViewChild(MatSelectionList) selection: MatSelectionList;

	private readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;
	private internmentEpisodeId: number;
	private patientId: number;

	newMainDiagnosis: HealthConditionDto;

	panelOpenState = true;
	form: FormGroup;
	currentMainDiagnosis: HealthConditionDto;
	patient$: Observable<PatientBasicData>;
	public personPhoto: PersonPhotoDto;
	internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	diagnostics$: Observable<HealthConditionDto[]>

	constructor(
		private readonly patientService: PatientService,
		private readonly internmentService: InternacionService,
		private readonly internmentStateService: InternmentStateService,
		private readonly snomedService: SnomedService,
		private readonly mainDiagnosesService: MainDiagnosesService,
		private readonly contextService: ContextService,
		private readonly mapperService: MapperService,
		private readonly formBuilder: FormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly dialog: MatDialog,
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params: ParamMap) => {

				this.internmentEpisodeId = Number(params.get('idInternacion'));
				this.patientId = Number(params.get('idPaciente'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto; });

				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
					map((internmentEpisodeSummary: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisodeSummary))
				);

				this.internmentStateService.getMainDiagnosis(this.internmentEpisodeId).subscribe(
					mainDiagnosis => this.currentMainDiagnosis = mainDiagnosis
				);

				this.diagnostics$ = this.internmentStateService.getActiveAlternativeDiagnosesGeneralState(this.internmentEpisodeId);
			}
		);

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
		if (this.form.valid && this.newMainDiagnosis) {
			this.openConfirmDialog().subscribe(accept => {
				if (accept) {
					const mainDiagnosisDto: MainDiagnosisDto = {
						mainDiagnosis: this.newMainDiagnosis,
						notes: this.form.value
					}
					this.mainDiagnosesService.addMainDiagnosis(this.internmentEpisodeId, mainDiagnosisDto).subscribe(
						_ => {
							this.snackBarService.showSuccess('internaciones.clinical-assessment-diagnosis.messages.SUCCESS')
							const url = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}`;
							this.router.navigate([url]);
						},
						_ => this.snackBarService.showError('internaciones.clinical-assessment-diagnosis.messages.ERROR')
					);
				}
			});
		}
	}

	openConfirmDialog(): Observable<any> {
		const previousDiagnosisHTML = `<strong>${this.currentMainDiagnosis.snomed.pt}</strong>`;
		const newDiagnosisHTML = `<strong>${this.newMainDiagnosis.snomed.pt}</strong>`;
		const dialogRef = this.dialog.open(ConfirmDialogComponent, {
			data: {
				// TODO pasar a archivo de traducción
				title: 'Confirmar operación',
				content: `Se va a cambiar el diagnóstico principal ${previousDiagnosisHTML} por ${newDiagnosisHTML}`,
				okButtonLabel: 'Aceptar'
			}
		});

		return dialogRef.afterClosed();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.diagnosis
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => {
					if (selectedConcept) {
						this.newMainDiagnosis = {
							snomed: selectedConcept
						}
					}
				});
		}
	}

	setMainDiagnosisFromList(): void {
		this.newMainDiagnosis = this.selection.selectedOptions.selected[0].value;
	}

	deleteMainDiagnosis(): void {
		delete this.newMainDiagnosis;
		if (this.selection) {
			this.selection.deselectAll();
		}
	}

	back(): void {
		window.history.back();
	}

}
