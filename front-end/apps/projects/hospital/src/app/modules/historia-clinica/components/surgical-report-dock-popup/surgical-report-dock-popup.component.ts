import { Component, Inject, OnInit } from '@angular/core';
import { DiagnosisDto } from '@api-rest/api-model';
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

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly internmentStateService: InternmentStateService,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
	) {
		this.internmentStateService.getDiagnosesGeneralState(this.data.patientInfo.internmentEpisodeId).subscribe(response => {
			if (response)
				this.diagnosis = response;
		});
	}

	ngOnInit(): void {
	}

}
