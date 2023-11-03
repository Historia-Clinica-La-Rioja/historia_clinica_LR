import { Component, Injector, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApiErrorMessageDto, AppFeature, HCEPersonalHistoryDto, ProblemInfoDto } from '@api-rest/api-model';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { ActivatedRoute } from '@angular/router';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { Observable, Subject, take } from 'rxjs';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { SolveProblemComponent } from '@historia-clinica/dialogs/solve-problem/solve-problem.component';
import { HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { AmendProblemComponent } from '../../dialogs/amend-problem/amend-problem.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';

@Component({
    selector: 'app-problems-options-menu',
    templateUrl: './problems-options-menu.component.html',
    styleUrls: ['./problems-options-menu.component.scss']
})
export class ProblemsOptionsMenuComponent implements OnInit {

    @Input() problem: HCEPersonalHistoryDto;
    @Input() set nuevaConsultaRef(nuevaConsultaRef: DockPopupRef) {
		this.nuevaConsultaAmbulatoriaRef = nuevaConsultaRef;
		if (nuevaConsultaRef) {
			this.nuevaConsultaFromProblemaRef?.close();
			delete this.nuevaConsultaFromProblemaRef;
		}
	}
    @Output() setProblemOnHistoric = new Subject<HCEPersonalHistoryDto>();
    patientId: number;
    hasNewConsultationEnabled$: Observable<boolean>;
	private nuevaConsultaAmbulatoriaRef: DockPopupRef;
	private nuevaConsultaFromProblemaRef: DockPopupRef;
	isMarkProblemAsErrorActive: boolean;

    // Injected dependencies
    private dockPopupService: DockPopupService;
    constructor(
        private route: ActivatedRoute,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
        public dialog: MatDialog,
		private injector: Injector,
		private historicalProblemsFacadeService: HistoricalProblemsFacadeService,
		private outpatientConsultationService: OutpatientConsultationService,
		private readonly featureFlagService: FeatureFlagService,
        ) {
            this.dockPopupService = this.injector.get<DockPopupService>(DockPopupService);
            this.route.paramMap.subscribe(
                (params) => {
                    this.patientId = Number(params.get('idPaciente'));
                }
			);
			this.featureFlagService.isActive(AppFeature.HABILITAR_RESOLUCION_PROBLEMAS_CARGADOS_COMO_ERROR_EN_DESARROLLO).subscribe(isOn => {
				this.isMarkProblemAsErrorActive = isOn;
			});
        }

    ngOnInit(): void {
		this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;
    }

    openNuevaConsulta(problema: HCEPersonalHistoryDto): void {
		if (!this.nuevaConsultaFromProblemaRef) {
			if (!this.nuevaConsultaAmbulatoriaRef) {
				this.openDockPopup(problema.id);
			} else {
				const confirmDialog = this.dialog.open(ConfirmDialogComponent, { data: getConfirmDataDialog() });
				confirmDialog.afterClosed().subscribe(confirmed => {
					if (confirmed) {
						this.openDockPopup(problema.id);
						this.nuevaConsultaAmbulatoriaRef.close();
					}
				});
			}
		}

		function getConfirmDataDialog() {
			const keyPrefix = 'ambulatoria.paciente.problemas.nueva_opened_confirm_dialog';
			return {
				title: `${keyPrefix}.TITLE`,
				content: `${keyPrefix}.CONTENT`,
				okButtonLabel: `${keyPrefix}.OK_BUTTON`,
				cancelButtonLabel: `${keyPrefix}.CANCEL_BUTTON`,
			};
		}
	}

    private openDockPopup(idProblema: number) {
		this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(true);
		const idPaciente = this.route.snapshot.paramMap.get('idPaciente');
		this.nuevaConsultaFromProblemaRef =
			this.dockPopupService.open(NuevaConsultaDockPopupComponent, { idPaciente, idProblema });
		this.nuevaConsultaFromProblemaRef.afterClosed().pipe(take(1)).subscribe(fieldsToUpdate => {
			if (fieldsToUpdate) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
			}
			this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(false);
			delete this.nuevaConsultaFromProblemaRef;
		});
	}

    solveProblemPopUp(problema: HCEPersonalHistoryDto) {
		this.dialog.open(SolveProblemComponent, {
			data: {
				problema,
				patientId: this.patientId
			}
		}).afterClosed().subscribe(submitted => {
			if (submitted) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({ problems: true, personalHistories: true });
			}
		});
	}

    filterByProblemOnProblemClick(problem: HCEPersonalHistoryDto) {
		this.historicalProblemsFacadeService.sendHistoricalProblemsFilter({
			specialty: null,
			professional: null,
			problem: problem.snomed.sctid,
			consultationDate: null,
			referenceStateId: null,
		});
        this.setProblemOnHistoric.next(problem);
    }

	amendProblem(problem: HCEPersonalHistoryDto) {
		const warnignComponent = this.dialog.open(DiscardWarningComponent,
			{
				disableClose: true,
				data: {
					title: 'ambulatoria.paciente.problemas.amend_problems.TITLE',
					content: 'ambulatoria.paciente.problemas.amend_problems.CONTENT',
					okButtonLabel: 'buttons.CONTINUE',
					cancelButtonLabel: 'buttons.CANCEL',
					buttonClose: true,
				},
				maxWidth: '500px'
			});
		warnignComponent.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.outpatientConsultationService.validateProblemAsError(this.patientId, problem.id).subscribe({
					next: (problemInfo: ProblemInfoDto[]) => this.openAmendProblemDialog(problem, problemInfo),
    				error: (e: ApiErrorMessageDto) => this.openErrorDialog(e.text),
				})
			}
		});
	}

	private openAmendProblemDialog(problem: HCEPersonalHistoryDto, problemInfo: ProblemInfoDto[]) {
		const amendProblemDialog = this.dialog.open(AmendProblemComponent, 
			{
				autoFocus: false,
				minWidth: '500px',
				data: {
					problemId: problem.id,
					patientId: this.patientId,
					problemInfo
				}
			})
		amendProblemDialog.afterClosed().subscribe()
	}

	private openErrorDialog(content: string){
		this.dialog.open(DiscardWarningComponent, { data: getConfirmDataDialog(content) });

		function getConfirmDataDialog(content: string) {
			const keyPrefix = 'ambulatoria.paciente.problemas.amend_problems.error';
			return {
				title: `${keyPrefix}.TITLE`,
				content: `${content}`,
				okButtonLabel: `${keyPrefix}.OK_BUTTON`,
				errorMode: true,
				color: 'warn'
			};
		}
	}			
}
