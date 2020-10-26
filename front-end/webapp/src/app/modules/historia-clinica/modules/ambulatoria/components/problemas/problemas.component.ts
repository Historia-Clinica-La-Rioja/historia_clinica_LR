import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {
	PROBLEMAS_ACTIVOS,
	PROBLEMAS_CRONICOS,
	PROBLEMAS_INTERNACION,
	PROBLEMAS_RESUELTOS
} from '../../../../constants/summaries';
import {HCEHospitalizationHistoryDto, HCEPersonalHistoryDto} from '@api-rest/api-model';
import {HceGeneralStateService} from '@api-rest/services/hce-general-state.service';
import {ActivatedRoute, Router} from '@angular/router';
import {DateFormat, momentFormat, momentParseDate} from '@core/utils/moment.utils';
import {map, tap} from 'rxjs/operators';
import {Observable, Subscription} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {SolveProblemComponent} from '../../../../dialogs/solve-problem/solve-problem.component';
import {HistoricalProblems, HistoricalProblemsFacadeService} from '../../services/historical-problems-facade.service';
import { ContextService } from '@core/services/context.service';

const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_INTERNMENT_EPISODE_SUFIX = '/paciente/';

@Component({
	selector: 'app-problemas',
	templateUrl: './problemas.component.html',
	styleUrls: ['./problemas.component.scss'],
	providers: [ HistoricalProblemsFacadeService ]
})
export class ProblemasComponent implements OnInit, OnDestroy {

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
	@Input() hasNewConsultationEnabled: boolean;


	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private historicalProblemsFacadeService: HistoricalProblemsFacadeService,
		private route: ActivatedRoute,
		private readonly router: Router,
		public dialog: MatDialog,
		private contextService: ContextService,
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				historicalProblemsFacadeService.setPatientId(this.patientId);
			});
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.loadActiveProblems();
		this.loadChronicProblems();
		this.loadHospitalizationProblems();
		this.loadSolvedProblems();
		this.loadHistoricalProblems();
	}

	loadActiveProblems(){
		this.activeProblems$ = this.hceGeneralStateService.getActiveProblems(this.patientId).pipe(
			map((problemas: HCEPersonalHistoryDto[]) => {
				return problemas.map((problema: HCEPersonalHistoryDto) => {
					problema.startDate = momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE);
					return problema;
				});
			})
		);
	}

	loadSolvedProblems(){
		this.solvedProblems$ = this.hceGeneralStateService.getSolvedProblems(this.patientId).pipe(
			map((problemas: HCEPersonalHistoryDto[]) => {
				return problemas.map((problema: HCEPersonalHistoryDto) => {
					problema.startDate = momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE);
					return problema;
				});
			})
		);
	}

	loadChronicProblems(){
		this.chronicProblems$ = this.hceGeneralStateService.getChronicConditions(this.patientId).pipe(
			map((problemas: HCEPersonalHistoryDto[]) => {
				return problemas.map((problema: HCEPersonalHistoryDto) => {
					problema.startDate = momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE);
					return problema;
				});
			})
		);
	}

	loadHospitalizationProblems(){
		this.hospitalizationProblems$ = this.hceGeneralStateService.getHospitalizationHistory(this.patientId).pipe(
			map((problemas: HCEHospitalizationHistoryDto[]) => {
				return problemas.map((problema: HCEHospitalizationHistoryDto) => {
					problema.entryDate = momentFormat(momentParseDate(problema.entryDate), DateFormat.VIEW_DATE);
					problema.dischargeDate = problema.dischargeDate? momentFormat(momentParseDate(problema.dischargeDate), DateFormat.VIEW_DATE) : undefined;
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

	goToNuevaConsulta(problema: HCEPersonalHistoryDto) {
		this.router.navigateByUrl(`${this.router.url}/nuevaDesdeProblema/${problema.id}`).then();
	}

	solveProblemPopUp(problema: HCEPersonalHistoryDto){
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
			speciality: null,
			professional: null,
			problem: problem.snomed.id,
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
