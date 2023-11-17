import { Component, Inject, OnInit } from '@angular/core';
import { DiagnosisDto, ProfessionalDto } from '@api-rest/api-model';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
	selector: 'app-surgical-report-dock-popup',
	templateUrl: './surgical-report-dock-popup.component.html',
	styleUrls: ['./surgical-report-dock-popup.component.scss'],
	providers: [
		ComponentEvaluationManagerService,
	]
})
export class SurgicalReportDockPopupComponent implements OnInit {

	diagnosis: DiagnosisDto[];
	professionals: ProfessionalDto[];

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly internmentStateService: InternmentStateService,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly healthcareProfessionalByInstitutionService: HealthcareProfessionalByInstitutionService,
	) {
		this.internmentStateService.getDiagnosesGeneralState(this.data.patientInfo.internmentEpisodeId).subscribe(response => {
			if (response)
				this.diagnosis = response;
		});
		this.healthcareProfessionalByInstitutionService.getAllAssociated().subscribe(response => {
			if (response)
				this.professionals = response;
		});
	}

	ngOnInit(): void {
	}

}
