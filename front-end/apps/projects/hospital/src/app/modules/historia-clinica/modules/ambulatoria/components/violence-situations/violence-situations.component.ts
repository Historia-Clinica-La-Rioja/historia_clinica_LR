import { Component, OnInit } from '@angular/core';
import { ViolenceSituationDockPopupComponent } from '../../dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { ActivatedRoute } from '@angular/router';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { VIOLENCE_SITUATION } from '@historia-clinica/constants/summaries';
import { PageDto, ViolenceReportSituationDto } from '@api-rest/api-model';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';

@Component({
	selector: 'app-violence-situations',
	templateUrl: './violence-situations.component.html',
	styleUrls: ['./violence-situations.component.scss']
})
export class ViolenceSituationsComponent implements OnInit {
	
	constructor(private readonly dockPopupService: DockPopupService, 
				private route: ActivatedRoute,
				private violenceSituationReportFacadeService: ViolenceReportFacadeService) { }

	patientId: number;
	header: SummaryHeader = VIOLENCE_SITUATION;
	violenceSituations: PageDto<ViolenceReportSituationDto>;
	showSeeAll: boolean = true;

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
			});
		this.setPatientViolenceSituations(true);
		this.violenceSituationReportFacadeService.violenceSituations$
			.subscribe((result: PageDto<ViolenceReportSituationDto>) => {
				this.violenceSituations = result;
				this.showSeeAll = result?.content.length !== result?.totalElementsAmount;
			});
	}

	openViolenceSituationDockPopUp() {
		this.dockPopupService.open(ViolenceSituationDockPopupComponent, {
			data: {
				patientId: this.patientId,
			}
		});
	}

	setPatientViolenceSituations(mustBeLimited: boolean) {
		this.violenceSituationReportFacadeService.setAllPatientViolenceSituations(this.patientId, mustBeLimited);
	}	
}