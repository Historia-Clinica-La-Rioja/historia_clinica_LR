import {Component, Input, OnInit} from '@angular/core';
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
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {MatDialog} from "@angular/material/dialog";
import {SolveProblemComponent} from "../../../../dialogs/solve-problem/solve-problem.component";

@Component({
	selector: 'app-problemas',
	templateUrl: './problemas.component.html',
	styleUrls: ['./problemas.component.scss']
})
export class ProblemasComponent implements OnInit {

	public readonly cronicos = PROBLEMAS_CRONICOS;
	public readonly activos = PROBLEMAS_ACTIVOS;
	public readonly resueltos = PROBLEMAS_RESUELTOS;
	public readonly internacion = PROBLEMAS_INTERNACION;
	public activeProblems$: Observable<HCEPersonalHistoryDto[]>;
	public solvedProblems$: Observable<HCEPersonalHistoryDto[]>;
	public chronicProblems$: Observable<HCEPersonalHistoryDto[]>;
	public hospitalizationProblems$: Observable<HCEHospitalizationHistoryDto[]>;
	private patientId: number;
	@Input() hasConfirmedAppointment: boolean;


	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private route: ActivatedRoute,
		private readonly router: Router,
		public dialog: MatDialog
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
			});
	}

	ngOnInit(): void {
		this.activeProblems$ = this.hceGeneralStateService.getActiveProblems(this.patientId).pipe(
			map((problemas: HCEPersonalHistoryDto[]) => {
				return problemas.map((problema: HCEPersonalHistoryDto) => {
					problema.startDate = momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE);
					return problema;
				})
			})
		);
		this.solvedProblems$ = this.hceGeneralStateService.getSolvedProblems(this.patientId).pipe(
			map((problemas: HCEPersonalHistoryDto[]) => {
				return problemas.map((problema: HCEPersonalHistoryDto) => {
					problema.startDate = momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE);
					return problema;
				})
			})
		);
		this.chronicProblems$ = this.hceGeneralStateService.getChronicConditions(this.patientId).pipe(
			map((problemas: HCEPersonalHistoryDto[]) => {
				return problemas.map((problema: HCEPersonalHistoryDto) => {
					problema.startDate = momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE);
					return problema;
				})
			})
		);
		this.hospitalizationProblems$ = this.hceGeneralStateService.getHospitalizationHistory(this.patientId).pipe(
			map((problemas: HCEHospitalizationHistoryDto[]) => {
				return problemas.map((problema: HCEHospitalizationHistoryDto) => {
					problema.entryDate = momentFormat(momentParseDate(problema.entryDate), DateFormat.VIEW_DATE);
					problema.dischargeDate = problema.dischargeDate? momentFormat(momentParseDate(problema.dischargeDate), DateFormat.VIEW_DATE) : undefined;
					return problema;
				})
			})
		);
	}

	goToNuevaConsulta(problema: HCEPersonalHistoryDto){
		this.router.navigateByUrl(`${this.router.url}/nuevaDesdeProblema/${problema.id}`).then();
	}

	solveProblemPopUp(problema: HCEPersonalHistoryDto){
		this.dialog.open(SolveProblemComponent, {
			data: {
				problema,
				patientId: this.patientId
			}
		});
	}
}
