import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { InternmentEpisodeProcessDto } from "@api-rest/api-model";
import { AppRoutes } from "../../../../../../../../app-routing.module";
import { ContextService } from "@core/services/context.service";
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

const ROUTE_HISTORY_CLINIC = 'ambulatoria/paciente/';

@Component({
	selector: 'app-internment-summary',
	templateUrl: './internment-summary.component.html',
	styleUrls: ['./internment-summary.component.scss']
})
export class InternmentSummaryComponent implements OnInit {

	internmentEpisodeInfo: InternmentEpisodeProcessDto;
	patientId: number;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(params => {
			this.internmentEpisodeInfo = { id: Number(params.get('idInternacion')), inProgress: false, patientHospitalized: false };
			this.patientId = Number(params.get('idPaciente'));
			this.internmentSummaryFacadeService.setInternmentEpisodeInformation(this.internmentEpisodeInfo.id, true, true);
		})
	}

	goBack(): void {
		const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/${ROUTE_HISTORY_CLINIC}/${this.patientId}`;
		this.router.navigate([url]);
	}

}


