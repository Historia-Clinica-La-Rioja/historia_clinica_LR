import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import {
	PROBLEMAS_ACTIVOS,
	PROBLEMAS_CRONICOS,
	PROBLEMAS_INTERNACION,
	PROBLEMAS_RESUELTOS
} from '../../../../constants/summaries';
import { HCEHospitalizationHistoryDto, HCEPersonalHistoryDto } from '@api-rest/api-model';
import {HceGeneralStateService} from '@api-rest/services/hce-general-state.service';
import {ActivatedRoute, Router} from '@angular/router';
import {DateFormat, momentFormat, momentParseDate} from '@core/utils/moment.utils';
import {map, tap} from 'rxjs/operators';
import {Observable, Subscription} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {SolveProblemComponent} from '../../../../dialogs/solve-problem/solve-problem.component';
import {HistoricalProblems, HistoricalProblemsFacadeService} from '../../services/historical-problems-facade.service';
import { ContextService } from '@core/services/context.service';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';

const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_INTERNMENT_EPISODE_SUFIX = '/paciente/';

@Component({
	selector: 'app-problemas',
	templateUrl: './problemas.component.html',
	styleUrls: ['./problemas.component.scss'],
})
export class ProblemasComponent implements OnInit, OnDestroy {

	@Input()
	set nuevaConsultaRef(nuevaConsultaRef: DockPopupRef) {
		this.nuevaConsultaAmbulatoriaRef = nuevaConsultaRef;
		if (nuevaConsultaRef) {
			this.nuevaConsultaFromProblemaRef?.close();
			delete this.nuevaConsultaFromProblemaRef;
		}
	}
	public hasNewConsultationEnabled$: Observable<boolean>;

	public readonly cronicos = PROBLEMAS_CRONICOS;
	public readonly activos = PROBLEMAS_ACTIVOS;
	public readonly resueltos = PROBLEMAS_RESUELTOS;
	public readonly internacion = PROBLEMAS_INTERNACION;
	private readonly routePrefix;
	public activeProblems$: Observable<HCEPersonalHistoryDto[]>;
	public solvedProblems$: Observable<HCEPersonalHistoryDto[]>;
	public chronicProblems$: Observable<HCEPersonalHistoryDto[]>;
	public hospitalizationProblems$: Observable<HCEHospitalizationHistoryDto[]>;
	public historicalProblemsList: HistoricalProblems[];
	public historicalProblemsAmount: number;
	public hideFilterPanel = false;
	private historicalProblems$: Subscription;
	private patientId: number;
	private nuevaConsultaAmbulatoriaRef: DockPopupRef;
	private nuevaConsultaFromProblemaRef: DockPopupRef;

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private historicalProblemsFacadeService: HistoricalProblemsFacadeService,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private route: ActivatedRoute,
		private readonly router: Router,
		public dialog: MatDialog,
		private contextService: ContextService,
		private dockPopupService: DockPopupService,
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				historicalProblemsFacadeService.setPatientId(this.patientId);
			});
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;
		this.loadActiveProblems();
		this.loadChronicProblems();
		this.loadHospitalizationProblems();
		this.loadSolvedProblems();
		this.loadHistoricalProblems();
	}

	loadActiveProblems() {
		this.activeProblems$ = this.ambulatoriaSummaryFacadeService.activeProblems$.pipe(
			map(this.formatProblemsDates)
		);
	}

	loadSolvedProblems() {
		this.solvedProblems$ = this.hceGeneralStateService.getSolvedProblems(this.patientId).pipe(
			map(this.formatProblemsDates)
		);
	}

	loadChronicProblems() {
		this.chronicProblems$ = this.ambulatoriaSummaryFacadeService.chronicProblems$.pipe(
			map(this.formatProblemsDates)
		);
	}

	private formatProblemsDates(problemas: HCEPersonalHistoryDto[]) {
		return problemas.map((problema: HCEPersonalHistoryDto) => {
			return {
				...problema,
				startDate: momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE)
			};
		});
	}

	loadHospitalizationProblems() {
		this.hospitalizationProblems$ = this.hceGeneralStateService.getHospitalizationHistory(this.patientId).pipe(
			map((problemas: HCEHospitalizationHistoryDto[]) => {
				return problemas.map((problema: HCEHospitalizationHistoryDto) => {
					problema.entryDate = momentFormat(momentParseDate(problema.entryDate), DateFormat.VIEW_DATE);
					problema.dischargeDate = problema.dischargeDate ? momentFormat(momentParseDate(problema.dischargeDate), DateFormat.VIEW_DATE) : undefined;
					return problema;
				});
			})
		);
	}

	loadHistoricalProblems() {
		this.historicalProblems$ = this.historicalProblemsFacadeService.getHistoricalProblems().pipe(
			tap(historicalProblems => this.historicalProblemsAmount = historicalProblems ? historicalProblems.length : 0)
		).subscribe(data => this.historicalProblemsList = data);
	}

	openNuevaConsulta(problema: HCEPersonalHistoryDto): void {
		if (!this.nuevaConsultaFromProblemaRef) {
			if (!this.nuevaConsultaAmbulatoriaRef) {
				this.openDockPopup(problema.id);
			} else {
				const confirmDialog = this.dialog.open(ConfirmDialogComponent, {data: getConfirmDataDialog()});
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
		const idPaciente = this.route.snapshot.paramMap.get('idPaciente');
		this.nuevaConsultaFromProblemaRef =
			this.dockPopupService.open(NuevaConsultaDockPopupComponent, {idPaciente, idProblema});
		this.nuevaConsultaFromProblemaRef.afterClosed().subscribe(fieldsToUpdate => {
			if (fieldsToUpdate) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
			}
			delete this.nuevaConsultaFromProblemaRef;
		});
	}



	solveProblemPopUp(problema: HCEPersonalHistoryDto) {
		this.dialog.open(SolveProblemComponent, {
			data: {
				problema,
				patientId: this.patientId
			}
		}).afterClosed().subscribe(submit => {
			if (!submit) {
				this.loadChronicProblems();
				this.loadSolvedProblems();
				this.loadActiveProblems();
			}});
	}

	onProblemClick(problem: HCEPersonalHistoryDto) {
		this.historicalProblemsFacadeService.sendHistoricalProblemsFilter({
			specialty: null,
			professional: null,
			problem: problem.snomed.sctid,
			consultationDate: null
		});
	}

	hideFilters() {
		this.hideFilterPanel = !this.hideFilterPanel;
	}

	ngOnDestroy(): void {
		this.historicalProblems$.unsubscribe();
	}

	goToHospitalizationEpisode(problema: HCEHospitalizationHistoryDto) {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + problema.sourceId + ROUTE_INTERNMENT_EPISODE_SUFIX + this.patientId]);
	}

}
