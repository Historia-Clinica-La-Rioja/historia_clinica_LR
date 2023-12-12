import { Component, Injector, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApiErrorMessageDto, HCEHealthConditionDto, ProblemInfoDto } from '@api-rest/api-model';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { Observable, Subject, take } from 'rxjs';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { SolveProblemComponent } from '@historia-clinica/dialogs/solve-problem/solve-problem.component';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { AmendProblemComponent, AmendProblemData } from '../../dialogs/amend-problem/amend-problem.component';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';

@Component({
    selector: 'app-problems-options-menu',
    templateUrl: './problems-options-menu.component.html',
    styleUrls: ['./problems-options-menu.component.scss']
})
export class ProblemsOptionsMenuComponent implements OnInit {

    @Input() problem: HCEHealthConditionDto;
    @Input() set nuevaConsultaRef(nuevaConsultaRef: DockPopupRef) {
		this.nuevaConsultaAmbulatoriaRef = nuevaConsultaRef;
		if (nuevaConsultaRef) {
			this.nuevaConsultaFromProblemaRef?.close();
			delete this.nuevaConsultaFromProblemaRef;
		}
	}
	@Input() patientId: number;
    @Output() setProblemOnHistoric = new Subject<HCEHealthConditionDto>();
    hasNewConsultationEnabled$: Observable<boolean>;
	private nuevaConsultaAmbulatoriaRef: DockPopupRef;
	private nuevaConsultaFromProblemaRef: DockPopupRef;

    // Injected dependencies
    private dockPopupService: DockPopupService;
    constructor(
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
        public dialog: MatDialog,
		private injector: Injector,
		private readonly outpatientConsultationService: OutpatientConsultationService,
        ) {
            this.dockPopupService = this.injector.get<DockPopupService>(DockPopupService);
        }

    ngOnInit(): void {
		this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;
    }

    openNuevaConsulta(): void {
		if (!this.nuevaConsultaFromProblemaRef) {
			if (!this.nuevaConsultaAmbulatoriaRef) {
				this.openDockPopup(this.problem.id);
			} else {
				const confirmDialog = this.dialog.open(ConfirmDialogComponent, { data: getConfirmDataDialog() });
				confirmDialog.afterClosed().subscribe(confirmed => {
					if (confirmed) {
						this.openDockPopup(this.problem.id);
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
		const idPaciente = this.patientId
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

    solveProblemPopUp() {
		this.dialog.open(SolveProblemComponent, {
			data: {
				problema: this.problem,
				patientId: this.patientId
			}
		}).afterClosed().subscribe(submitted => {
			if (submitted) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({ problems: true, patientProblems: true });
			}
		});
	}

    viewProblemDetails() {
        this.setProblemOnHistoric.next(this.problem);
    }

	amendProblem() {
		const warnignComponent = this.dialog.open(DiscardWarningComponent,
			{
				disableClose: true,
				data: {
					title: 'ambulatoria.paciente.problemas.amend_problems.TITLE',
					content: 'ambulatoria.paciente.problemas.amend_problems.CONTENT',
					okButtonLabel: 'ambulatoria.paciente.problemas.amend_problems.OK_BUTTON',
					cancelButtonLabel: 'ambulatoria.paciente.problemas.amend_problems.BACK_BUTTON',
					buttonClose: true,
				},
				maxWidth: '500px'
			});
		warnignComponent.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.outpatientConsultationService.validateProblemAsError(this.patientId, this.problem.id).subscribe({
					next: (problemInfo: ProblemInfoDto[]) => this.openAmendProblemDialog(problemInfo),
    				error: (e: ApiErrorMessageDto) => this.openErrorDialog(e.text),
				})
			}
		});
	}

	private openAmendProblemDialog(problemInfo: ProblemInfoDto[]) {
		let amendProblemData: AmendProblemData = {
			problemId: this.problem.id,
			patientId: this.patientId,
			problemInfo
		}
		this.dialog.open(AmendProblemComponent, 
			{
				autoFocus: false,
				minWidth: '500px',
				data: { amendProblemData }
			})
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
