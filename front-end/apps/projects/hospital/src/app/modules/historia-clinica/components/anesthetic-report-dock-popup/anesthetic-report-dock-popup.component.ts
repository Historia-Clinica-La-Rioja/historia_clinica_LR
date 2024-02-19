import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder } from '@angular/forms';
import { AnestheticReportDto, DiagnosisDto, HealthConditionDto, TimeDto } from '@api-rest/api-model';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { scrollIntoError } from '@core/utils/form.utils';
import { AnalgesicTechniqueService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/analgesic-technique.service';
import { AnestheticReportAnestheticHistoryService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { AnestheticReportAnthropometricDataService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anthropometric-data.service';
import { AnestheticReportClinicalEvaluationService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-clinical-evaluation.service';
import { AnestheticReportProposedSurgeryService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-proposed-surgery.service';
import { AnestheticReportRecordService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-record.service';
import { MedicationService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/medicationService';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { MedicacionesNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const TIME_OUT = 5000;

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
    anestheticReportPremedicationAndFoodIntakeService: MedicationService;
    anestheticReportRecordService: AnestheticReportRecordService;
    anestheticPlan: MedicationService;
    analgesicTechnique: AnalgesicTechniqueService

    personalRecordForm: FormGroup;
    readonly ASAOptions = [1,2,3,4,5]

    formFoodIntake: FormGroup;

    collapsedAnthropometricDataSection = true;
    collapsedClinicalEvaluationSection = true;

    constructor(
        @Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly el: ElementRef,
        private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
        private readonly snackBarService: SnackBarService,
        private readonly translateService: TranslateService,
        private readonly anesthethicReportService: AnesthethicReportService,
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
		this.anestheticReportPremedicationAndFoodIntakeService = new MedicationService(this.snomedService, this.snackBarService, this.translateService);
        this.anestheticReportRecordService = new AnestheticReportRecordService(this.snomedService, this.snackBarService);
        this.anestheticPlan = new MedicationService(this.snomedService, this.snackBarService, this.translateService);
        this.analgesicTechnique = new AnalgesicTechniqueService(this.snomedService, this.snackBarService, this.translateService);

        this.formFoodIntake = new FormGroup<FoodIntakeForm>({
            lastFoodIntake: new FormControl(null),
        });

        this.personalRecordForm = new FormGroup<PersonalRecordForm>({
            observations: new FormControl(null),
            asa: new FormControl(null)
        });

        this.possibleTimesList = this.anestheticReportPremedicationAndFoodIntakeService.possibleTimesList;
    }

    ngOnInit(): void {
        this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosis;
    }

    save(): void {
		this.isLoading = true;

        const newAnestheticReport: AnestheticReportDto = this.buildAnestheticReportDto();

        if (this.isValidConsultation()) {
            this.createAnestheticReport(newAnestheticReport);
        } else {
            this.checkFormErrors();
        }
	}

    private checkFormErrors() {
        if (this.anesthesicReportAnthropometricDataService.getForm().invalid) {
            this.collapsedAnthropometricDataSection = false;
            setTimeout(() => {
                scrollIntoError(this.anesthesicReportAnthropometricDataService.getForm(), this.el)
                this.isLoading = false;
            }, 300);
        }
        else if (this.anestheticReportClinicalEvaluationService.getForm().invalid) {
            this.collapsedClinicalEvaluationSection = false;
            setTimeout(() => {
                scrollIntoError(this.anestheticReportClinicalEvaluationService.getForm(), this.el)
                this.isLoading = false;
            }, 300);
        }
    }

    private createAnestheticReport(newAnestheticReport: AnestheticReportDto) {
        this.anesthethicReportService.createAnestheticReport(newAnestheticReport, this.data.internmentEpisodeId).subscribe(
			next => {
                this.snackBarService.showSuccess('internaciones.anesthesic-report.SUCCESS', { duration: TIME_OUT });
                this.isLoading = false;
				this.dockPopupRef.close();
			},
			error => {
				this.snackBarService.showError(error.text)
                this.isLoading = false;
			}
        )
    }

    isValidConsultation(): boolean {
        if (this.anesthesicReportAnthropometricDataService.getForm().invalid || this.anestheticReportClinicalEvaluationService.getForm().invalid)
			return false;
		return true;
    }

    buildAnestheticReportDto(): AnestheticReportDto {
        return {
            mainDiagnosis: this.mainDiagnosis,
            diagnosis: this.diagnosis,
            surgeryProcedures: this.anesthesicReportProposedSurgeryService.getProposedSurgeriesList(),
            anthropometricData: this.anesthesicReportAnthropometricDataService.getAnthropomethricData(),
            riskFactors: this.anestheticReportClinicalEvaluationService.getClinicalEvaluationData(),
            anestheticHistory: this.anestheticReportAnestheticHistoryService.getAnestheticHistoryData(),
            medications: this.medicacionesNuevaConsultaService.getMedicationsAsMedicationDto(),
            preMedications: this.anestheticReportPremedicationAndFoodIntakeService.getAnestheticSubstanceDto(),
            foodIntake: this.anestheticReportPremedicationAndFoodIntakeService.getAnestheticSubstanceDto().length > 0 ? {clockTime: this.formFoodIntake.value.lastFoodIntake} : null,
            histories: this.anestheticReportRecordService.getRecordAsHealthConditionDto(),
            anestheticPlans: this.anestheticPlan.getAnestheticSubstanceDto(),
            analgesicTechniques: this.analgesicTechnique.getAnalgesicTechniqueDto()
		};
	}
}

export interface FoodIntakeForm {
    lastFoodIntake: FormControl<TimeDto>;
}

export interface PersonalRecordForm {
    observations: FormControl<string>;
    asa: FormControl<number>;
}