import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { AnestheticReportDto, DiagnosisDto, HealthConditionDto, TimeDto } from '@api-rest/api-model';
import { AnestheticReportService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report.service';
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

    constructor(
        @Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly el: ElementRef,
        readonly anesthethicReportHandlerService: AnestheticReportService,
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
    ) {
        this.mainDiagnosis = data.mainDiagnosis;
        this.diagnosis = data.diagnosis;
        
        this.anesthethicReportHandlerService.createAnestheticReportServiceInstances();
    }

    ngOnInit(): void {
        this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosis;
        this.anesthethicReportHandlerService.getIsAnestheticReportLoading().subscribe(isLoading=> {
            this.isLoading = isLoading;
        })
    }

    save(): void {
		this.isLoading = true;

        const newAnestheticReport: AnestheticReportDto = this.anesthethicReportHandlerService.buildAnestheticReportDto(this.mainDiagnosis, this.diagnosis);

        if (this.anesthethicReportHandlerService.isValidConsultation()) {
            this.anesthethicReportHandlerService.createAnestheticReport(newAnestheticReport, this.data.internmentEpisodeId, this.dockPopupRef);
        } else {
            this.anesthethicReportHandlerService.checkFormErrors(this.el);
        }
	}

    onLastFoodIntakeTimeSelected(newLastFoodIntakeTimeSelected: TimeDto) {
        this.anesthethicReportHandlerService.setLastFoodIntakeTime(newLastFoodIntakeTimeSelected);
    }
}

export interface FoodIntakeForm {
    lastFoodIntake: FormControl<TimeDto>;
}