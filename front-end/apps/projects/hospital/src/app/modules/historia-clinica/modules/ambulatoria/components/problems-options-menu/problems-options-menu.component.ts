import { Component, Injector, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { ActivatedRoute } from '@angular/router';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { Observable, take } from 'rxjs';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { SolveProblemComponent } from '@historia-clinica/dialogs/solve-problem/solve-problem.component';

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
    patientId: number;
    hasNewConsultationEnabled$: Observable<boolean>;
	private nuevaConsultaAmbulatoriaRef: DockPopupRef;
	private nuevaConsultaFromProblemaRef: DockPopupRef;
    
    // Injected dependencies
    private dockPopupService: DockPopupService;
    constructor(
        private route: ActivatedRoute,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
        public dialog: MatDialog,
		private injector: Injector,
        ) {
            this.dockPopupService = this.injector.get<DockPopupService>(DockPopupService);
            this.route.paramMap.subscribe(
                (params) => {
                    this.patientId = Number(params.get('idPaciente'));
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
}
