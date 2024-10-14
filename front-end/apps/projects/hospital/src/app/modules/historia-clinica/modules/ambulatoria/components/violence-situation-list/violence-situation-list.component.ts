import { Component, Input, OnInit } from '@angular/core';
import { EViolenceEvaluationRiskLevel, ViolenceReportSituationDto } from '@api-rest/api-model';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { ViolenceSituationDockPopupComponent } from '../../dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-violence-situation-list',
	templateUrl: './violence-situation-list.component.html',
	styleUrls: ['./violence-situation-list.component.scss']
})
export class ViolenceSituationListComponent implements OnInit {

	constructor(private readonly dockPopupService: DockPopupService,
				private readonly route: ActivatedRoute) {}
	
	@Input() violenceSituations: ViolenceReportSituationDto[] = [];

	patientId: number;
	
	LOW = EViolenceEvaluationRiskLevel.LOW;
	MEDIUM = EViolenceEvaluationRiskLevel.MEDIUM;
	HIGH = EViolenceEvaluationRiskLevel.HIGH;
	
	ngOnInit(): void {
		this.setPatientId();
	}

	setPatientId() {
		this.route.paramMap.subscribe((params) => this.patientId = Number(params.get('idPaciente')));
	}

	continue = (situationId: number) => {
		this.dockPopupService.open(ViolenceSituationDockPopupComponent, {
			data: {
				patientId: this.patientId,
				situationId
			}
		});
	}
}
