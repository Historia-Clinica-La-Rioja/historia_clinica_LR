import { Component, Inject, OnInit } from '@angular/core';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { AnestheticReportAnestheticHistoryService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
    selector: 'app-anesthetic-report-dock-popup',
    templateUrl: './anesthetic-report-dock-popup.component.html',
    styleUrls: ['./anesthetic-report-dock-popup.component.scss'],
	providers: [ComponentEvaluationManagerService]
})
export class AnestheticReportDockPopupComponent implements OnInit {
    
    mainDiagnosis: HealthConditionDto;
	diagnosis: DiagnosisDto[] = [];

	isLoading = false;

    anestheticReportAnestheticHistoryService: AnestheticReportAnestheticHistoryService;

    constructor(
        @Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
    ) {
        this.mainDiagnosis = data.mainDiagnosis;
        this.diagnosis = data.diagnosis;
        this.anestheticReportAnestheticHistoryService = new AnestheticReportAnestheticHistoryService();
    }

    ngOnInit(): void {
        this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosis;
    }

    save(): void {

	}
}
