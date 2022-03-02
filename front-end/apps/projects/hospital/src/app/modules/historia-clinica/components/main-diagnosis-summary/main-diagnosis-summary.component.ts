import { Component, OnInit, Input } from '@angular/core';
import { DIAGNOSTICO_PRINCIPAL } from '../../constants/summaries';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { HealthConditionDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-main-diagnosis-summary',
	templateUrl: './main-diagnosis-summary.component.html',
	styleUrls: ['./main-diagnosis-summary.component.scss']
})
export class MainDiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable = true;

	mainDiagnosticosSummary = DIAGNOSTICO_PRINCIPAL;
	mainDiagnosis$: Observable<HealthConditionDto>;
	private routePrefix;

	constructor(
		private internmentStateService: InternmentStateService,
		private router: Router,
		private contextService: ContextService,
		private readonly route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.mainDiagnosis$ = this.internmentStateService.getMainDiagnosis(this.internmentEpisodeId);
		this.route.paramMap.subscribe(
			(params) => {
				const patientId = Number(params.get('idPaciente'));
				this.routePrefix = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${patientId}`;
			});
	}

	goToClinicalEvaluation(id: number): void {
		this.router.navigate([`${this.routePrefix}/eval-clinica-diagnosticos/${id}`]);
	}

	goToChangeMainDiagnosis(): void {
		this.router.navigate([`${this.routePrefix}/cambiar-diag-principal`]);
	}

}
