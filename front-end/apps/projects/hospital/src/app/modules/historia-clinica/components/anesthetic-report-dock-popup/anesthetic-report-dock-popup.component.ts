import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder } from '@angular/forms';
import { DiagnosisDto, HealthConditionDto, TimeDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AnestheticReportAnestheticHistoryService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { AnestheticReportAnthropometricDataService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anthropometric-data.service';
import { AnestheticReportClinicalEvaluationService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-clinical-evaluation.service';
import { AnestheticReportPremedicationAndFoodIntakeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-premedication-and-food-intake.service';
import { AnestheticReportProposedSurgeryService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-proposed-surgery.service';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { MedicacionesNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
    selector: 'app-anesthetic-report-dock-popup',
    templateUrl: './anesthetic-report-dock-popup.component.html',
    styleUrls: ['./anesthetic-report-dock-popup.component.scss'],
	providers: [ComponentEvaluationManagerService]
})
export class AnestheticReportDockPopupComponent implements OnInit {
    
    mainDiagnosis: HealthConditionDto;
	diagnosis: DiagnosisDto[] = [];
    
    possibleTimesList: TimeDto[];

	isLoading = false;
    
    anesthesicReportProposedSurgeryService: AnestheticReportProposedSurgeryService;
    anesthesicReportAnthropometricDataService: AnestheticReportAnthropometricDataService;
    anestheticReportClinicalEvaluationService: AnestheticReportClinicalEvaluationService;
    anestheticReportAnestheticHistoryService: AnestheticReportAnestheticHistoryService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
    premedicationAndFoodIntakeService: AnestheticReportPremedicationAndFoodIntakeService;

    formFoodIntake: FormGroup;

    constructor(
        @Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
        private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
        private readonly snackBarService: SnackBarService,
        private readonly translateService: TranslateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
    ) {
        this.mainDiagnosis = data.mainDiagnosis;
        this.diagnosis = data.diagnosis;
        
        this.anesthesicReportProposedSurgeryService = new AnestheticReportProposedSurgeryService(this.snomedService, this.snackBarService);
        this.anesthesicReportAnthropometricDataService = new AnestheticReportAnthropometricDataService(this.internacionMasterDataService, this.translateService);
        this.anestheticReportClinicalEvaluationService = new AnestheticReportClinicalEvaluationService(this.translateService);
        this.anestheticReportAnestheticHistoryService = new AnestheticReportAnestheticHistoryService();
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);
		this.premedicationAndFoodIntakeService = new AnestheticReportPremedicationAndFoodIntakeService(this.snomedService, this.snackBarService, this.translateService);

        this.formFoodIntake = new FormGroup<FoodIntakeForm>({
            lastFoodIntake: new FormControl(null),  
        });

        this.possibleTimesList = this.premedicationAndFoodIntakeService.possibleTimesList;
    }

    ngOnInit(): void {
        this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosis;
    }

    save(): void {

	}
}

export interface FoodIntakeForm {
    lastFoodIntake: FormControl<TimeDto>;
}