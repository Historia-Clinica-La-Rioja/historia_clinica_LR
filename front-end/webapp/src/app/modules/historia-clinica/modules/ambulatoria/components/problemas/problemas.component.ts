import { Component, OnInit } from '@angular/core';
import { PROBLEMAS_ACTIVOS, PROBLEMAS_CRONICOS, PROBLEMAS_RESUELTOS } from '../../../../constants/summaries';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-problemas',
	templateUrl: './problemas.component.html',
	styleUrls: ['./problemas.component.scss']
})
export class ProblemasComponent implements OnInit {

	public readonly cronicos = PROBLEMAS_CRONICOS;
	public readonly activos = PROBLEMAS_ACTIVOS;
	public readonly resueltos = PROBLEMAS_RESUELTOS;
	public activeProblems: HCEPersonalHistoryDto[];
	private patientId: number;


	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private route: ActivatedRoute
	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
			});
	}

	ngOnInit(): void {
		this.hceGeneralStateService.getActiveProblems(this.patientId).subscribe(activeProblems => {
			this.activeProblems = activeProblems
		});
	}

}
