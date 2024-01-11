import { Component, OnInit } from '@angular/core';
import { ViolenceSituationDockPopupComponent } from '../../dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { ActivatedRoute } from '@angular/router';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { VIOLENCE_SITUATION } from '@historia-clinica/constants/summaries';

@Component({
	selector: 'app-violence-situations',
	templateUrl: './violence-situations.component.html',
	styleUrls: ['./violence-situations.component.scss']
})
export class ViolenceSituationsComponent implements OnInit {
	
	constructor(private readonly dockPopupService: DockPopupService, private route: ActivatedRoute) { }

	patientId: number;
	header: SummaryHeader = VIOLENCE_SITUATION;
	violenceSituations = [
		{
			id: 'Situación #1',
			type: 'Física',
			modality: 'Doméstica',
			riskLevel: 'Alto',
			initDate: '11/08/23',
			lastUpdate: '18/08/23'
		},
		{
			id: 'Situación #2',
			type: 'Física',
			modality: 'Doméstica',
			riskLevel: 'Alto',
			initDate: '11/08/23',
			lastUpdate: '18/08/23'
		},
	];

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
			})
	}

	openViolenceSituationDockPopUp() {
		this.dockPopupService.open(ViolenceSituationDockPopupComponent,this.patientId);
	}
}