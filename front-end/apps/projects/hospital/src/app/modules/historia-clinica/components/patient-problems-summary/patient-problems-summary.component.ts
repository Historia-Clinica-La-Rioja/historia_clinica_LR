import {Component, Injector, Input, OnInit} from '@angular/core';
import {ApiErrorMessageDto, HCEHealthConditionDto, ProblemInfoDto} from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import {SummaryHeader} from '@presentation/components/summary-card/summary-card.component';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';
import { AmbulatoriaSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/services/ambulatoria-summary-facade.service';
import { Observable, take } from 'rxjs';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { ActivatedRoute } from '@angular/router';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { NuevaConsultaDockPopupComponent } from '@historia-clinica/modules/ambulatoria/dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { SolveProblemComponent } from '@historia-clinica/dialogs/solve-problem/solve-problem.component';
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { AmendProblemComponent, AmendProblemData } from '@historia-clinica/modules/ambulatoria/dialogs/amend-problem/amend-problem.component';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';

@Component({
	selector: 'app-patient-problems-summary',
	templateUrl: './patient-problems-summary.component.html',
	styleUrls: ['./patient-problems-summary.component.scss']
})
export class PatientProblemsSummaryComponent implements OnInit{

	readonly MILD_SEVERITY:string = 'LA6752-5';
	readonly MODERATE_SEVERITY:string = 'LA6751-7';
	readonly SEVERE_SEVERITY:string = 'LA6750-9';

	problems: Problem[] = [];
	hasNewConsultationEnabled$: Observable<boolean>;
	private patientId: number;
	private severityTypesMasterData: any[] = [];
	private nuevaConsultaAmbulatoriaRef: DockPopupRef;
	private nuevaConsultaFromProblemaRef: DockPopupRef;
	private dockPopupService: DockPopupService;
	canOnlyViewSelfAddedProblems = false;
	rolesThatCanOnlyViewSelfAddedProblems = [ERole.PRESCRIPTOR];
	@Input() personalProblemsHeader: SummaryHeader;
	@Input()
	set patientProblems(patientProblems: HCEHealthConditionDto[]){
		this.onPatientProblemsChange(patientProblems);
	};

	constructor(
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly permissionsService: PermissionsService,
		public dialog: MatDialog,
		private route: ActivatedRoute,
		private injector: Injector,
		private outpatientConsultationService: OutpatientConsultationService,
	) {
		this.dockPopupService = this.injector.get<DockPopupService>(DockPopupService);
		this.route.paramMap.subscribe(
			(params) => this.patientId = Number(params.get('idPaciente')));
	}

	ngOnInit(): void {
		this.setPermissions();
		this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;
	}

	private async onPatientProblemsChange(patientProblems: HCEHealthConditionDto[]){
		await this.setSeverityMasterData();
		this.setProblems(patientProblems);
	}

	private setPermissions(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.canOnlyViewSelfAddedProblems = anyMatch<ERole>(userRoles, this.rolesThatCanOnlyViewSelfAddedProblems);
		});
	}


	getSeverityTypeDisplayByCode(severityCode): string {
		return (severityCode && this.severityTypesMasterData.length) ?
			this.severityTypesMasterData.find(severityType => severityType.code === severityCode).display
			: '';
	}

	getSeverityColor(severityCode): string {
		switch(severityCode) {
			case this.MILD_SEVERITY:
				return 'grey';
			case this.MODERATE_SEVERITY:
				return 'black';
			case this.SEVERE_SEVERITY:
				return 'warn';
		}
	}


	openNuevaConsulta(problem: Problem): void {
		if (!this.nuevaConsultaFromProblemaRef) {
			if (!this.nuevaConsultaAmbulatoriaRef) {
				this.openDockPopup(problem.data.id);
			} else {
				const confirmDialog = this.dialog.open(ConfirmDialogComponent, { data: getConfirmDataDialog() });
				confirmDialog.afterClosed().subscribe(confirmed => {
					if (confirmed) {
						this.openDockPopup(problem.data.id);
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

	solveProblemPopUp(problem: Problem) {
		this.dialog.open(SolveProblemComponent, {
			data: {
				problema: problem.data,
				patientId: this.patientId
			}
		}).afterClosed().subscribe(submitted => {
			if (submitted) {
				this.canOnlyViewSelfAddedProblems ?
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({ patientProblemsByRole: true, problems: true })
				: this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({ patientProblems: true, problems: true });
			}
		});
	}
	
	amendProblem(problem: HCEHealthConditionDto) {
		const warnignComponent = this.dialog.open(DiscardWarningComponent,
			{
				disableClose: true,
				data: {
					title: 'ambulatoria.paciente.problemas.amend_problems.TITLE',
					content: 'ambulatoria.paciente.problemas.amend_problems.CONTENT',
					okButtonLabel: 'ambulatoria.paciente.problemas.amend_problems.OK_BUTTON',
					cancelButtonLabel : 'ambulatoria.paciente.problemas.amend_problems.BACK_BUTTON',
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

	private openAmendProblemDialog(problem: HCEHealthConditionDto, problemInfo: ProblemInfoDto[]) {
		let amendProblemData: AmendProblemData = {
			problemId: problem.id,
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
		this.dialog.open(DiscardWarningComponent, { data: getConfirmDataDialog() });

		function getConfirmDataDialog() {
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

	private openDockPopup(idProblema: number) {
		this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(true);
		this.nuevaConsultaFromProblemaRef =
			this.dockPopupService.open(NuevaConsultaDockPopupComponent, { idPaciente: this.patientId, idProblema });
		this.nuevaConsultaFromProblemaRef.afterClosed().pipe(take(1)).subscribe(fieldsToUpdate => {
			if (fieldsToUpdate) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
			}
			this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(false);
			delete this.nuevaConsultaFromProblemaRef;
		});
	}

	private setProblems(problems: HCEHealthConditionDto[]){
		this.problems = problems?.map(prob => {
			return this.mapToProblem(prob);
		});
	}

	private async setSeverityMasterData(){
		return new Promise((resolve) => {
			if (!this.severityTypesMasterData.length){
				this.internacionMasterDataService.getHealthSeverity().subscribe(
					severityTypes => {
						this.severityTypesMasterData = severityTypes;
						resolve(true);
					}
				);
			} else {
				resolve(true);
			}
		  });
	}

	private mapToProblem(problem: HCEHealthConditionDto): Problem {
		return {
			data: problem,
			severityName: this.getSeverityTypeDisplayByCode(problem.severity),
			severityColor: this.getSeverityColor(problem.severity)
		}
	}
}

interface Problem {
	data: HCEHealthConditionDto;
	severityName: string;
	severityColor: string;
}
