import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { FormControl, UntypedFormBuilder } from '@angular/forms';
import { AnestheticReportDto, DiagnosisDto, HealthConditionDto, ProcedureDescriptionDto, TimeDto } from '@api-rest/api-model';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { scrollIntoError } from '@core/utils/form.utils';
import { AnalgesicTechniqueData, AnalgesicTechniqueService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/analgesic-technique.service';
import { AnestheticReportAnestheticHistoryService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anesthetic-history.service';
import { AnestheticReportAnthropometricDataService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-anthropometric-data.service';
import { AnestheticReportClinicalEvaluationService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-clinical-evaluation.service';
import { AnestheticReportEndOfAnesthesiaStatusService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-end-of-anesthesia-status.service';
import { AnestheticReportIntrasurgicalAnestheticProceduresService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-intrasurgical-anesthetic-procedures.service';
import { AnestheticReportProposedSurgeryService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-proposed-surgery.service';
import { AnestheticReportRecordService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-record.service';
import { AnestheticTechniqueData, AnestheticTechniqueService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-technique.service';
import { FluidAdministrationData, FluidAdministrationService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/fluid-administration.service';
import { MedicationData, MedicationService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/medicationService';
import { AnestheticReportVitalSignsService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/anesthetic-report-vital-signs.service';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { MedicacionesNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TranslateService } from '@ngx-translate/core';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';

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

	isLoading = false;
    
    anesthesicReportProposedSurgeryService: AnestheticReportProposedSurgeryService;
    anesthesicReportAnthropometricDataService: AnestheticReportAnthropometricDataService;
    anestheticReportClinicalEvaluationService: AnestheticReportClinicalEvaluationService;
    anestheticReportAnestheticHistoryService: AnestheticReportAnestheticHistoryService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
    anestheticReportPremedicationAndFoodIntakeService: MedicationService;
    anestheticReportRecordService: AnestheticReportRecordService;
    anestheticPlan: MedicationService;
    analgesicTechnique: AnalgesicTechniqueService;
    anestheticTechnique: AnestheticTechniqueService
    fluidAdministrationService: FluidAdministrationService
    anestheticReportAnestheticAgent: MedicationService;
    anestheticReportNonAnestheticDrugs: MedicationService;
    anestheticReportIntrasurgicalAnestheticProceduresService: AnestheticReportIntrasurgicalAnestheticProceduresService;
    anestheticReportAntibioticProphylaxisService: MedicationService;
    anestheticReportEndOfAnesthesiaStatusService: AnestheticReportEndOfAnesthesiaStatusService;
    anestheticReportVitalSignsService: AnestheticReportVitalSignsService;

    lastFoodIntakeTimeSelected: TimeDto;

    collapsedAnthropometricDataSection = true;
    collapsedClinicalEvaluationSection = true;
    fluidAdministrationList$: Observable<FluidAdministrationData[]>;
    anestheticPlanList$: Observable<MedicationData[]>
    analgesicTechniqueList$: Observable<AnalgesicTechniqueData[]>
    anestheticTechniqueList$: Observable<AnestheticTechniqueData[]>

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
        this.anestheticTechnique = new AnestheticTechniqueService(this.snomedService, this.snackBarService)
        this.fluidAdministrationService = new FluidAdministrationService(this.snomedService, this.snackBarService)
        this.anestheticReportAnestheticAgent = new MedicationService(this.snomedService, this.snackBarService, this.translateService);
        this.anestheticReportNonAnestheticDrugs = new MedicationService(this.snomedService, this.snackBarService, this.translateService);
        this.anestheticReportIntrasurgicalAnestheticProceduresService = new AnestheticReportIntrasurgicalAnestheticProceduresService();
        this.anestheticReportAntibioticProphylaxisService = new MedicationService(this.snomedService, this.snackBarService, this.translateService);
        this.anestheticReportEndOfAnesthesiaStatusService = new AnestheticReportEndOfAnesthesiaStatusService();
        this.anestheticReportVitalSignsService = new AnestheticReportVitalSignsService(this.translateService, this.snackBarService);
    }

    ngOnInit(): void {
        this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosis;
        this.fluidAdministrationList$ = this.fluidAdministrationService.getFluidAdministrationList()
        this.analgesicTechniqueList$ = this.analgesicTechnique.getAnalgesicTechniqueList()
        this.anestheticPlanList$ = this.anestheticPlan.getMedication()
        this.anestheticTechniqueList$ = this.anestheticTechnique.getAnestheticTechniqueList()
        
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
            foodIntake: this.lastFoodIntakeTimeSelected ? {clockTime: this.lastFoodIntakeTimeSelected} : null,
            histories: this.anestheticReportRecordService.getRecordAsHealthConditionDto(),
            anestheticPlans: this.anestheticPlan.getAnestheticSubstanceDto(),
            analgesicTechniques: this.analgesicTechnique.getAnalgesicTechniqueDto(),
            anestheticTechniques: this.anestheticTechnique.getAnestheticTechniqueDto(),
            fluidAdministrations: this.fluidAdministrationService.getFluidAdministrationDto(),
            anestheticAgents: this.anestheticReportAnestheticAgent.getAnestheticSubstanceDto(),
            nonAnestheticDrugs: this.anestheticReportNonAnestheticDrugs.getAnestheticSubstanceDto(),
            procedureDescription: this.getProcedureDescription(),
            antibioticProphylaxis: this.anestheticReportAntibioticProphylaxisService.getAnestheticSubstanceDto(),
            measuringPoints: this.anestheticReportVitalSignsService.getMeasuringPointsAsMeasuringPointDto(),
            postAnesthesiaStatus: this.anestheticReportEndOfAnesthesiaStatusService.getPostAnesthesiaStatusDto(),
		};
	}

    private getProcedureDescription(): ProcedureDescriptionDto {
        const radioButtonsOptions = this.anestheticReportIntrasurgicalAnestheticProceduresService.getIntrasurgicalAnestheticProceduresData();
        const personRecordForm = this.anestheticReportRecordService.getPersonalRecordData();
        const vitalSignsForm = this.anestheticReportVitalSignsService.getVitalSignsData();
        return {
            nasogastricTube: radioButtonsOptions.nasogastricTube,
            urinaryCatheter: radioButtonsOptions.urinaryCatheter,
            venousAccess: radioButtonsOptions.venousAccess,
            note: personRecordForm.observations,
            asa: personRecordForm.asa,
            anesthesiaStartDate: vitalSignsForm.anesthesiaStartDate ? dateToDateDto(vitalSignsForm.anesthesiaStartDate) : null,
            anesthesiaEndDate: vitalSignsForm.anesthesiaEndDate ? dateToDateDto(vitalSignsForm.anesthesiaEndDate) : null,
            anesthesiaStartTime: vitalSignsForm.anesthesiaStartTime,    
            anesthesiaEndTime: vitalSignsForm.anesthesiaEndTime,
            surgeryStartDate: vitalSignsForm.surgeryStartDate ? dateToDateDto(vitalSignsForm.surgeryStartDate) : null,
            surgeryEndDate: vitalSignsForm.surgeryEndDate ? dateToDateDto(vitalSignsForm.surgeryEndDate) : null,
            surgeryStartTime: vitalSignsForm.surgeryStartTime,
            surgeryEndTime: vitalSignsForm.surgeryEndTime,
        }
    }

    onLastFoodIntakeTimeSelected(newLastFoodIntakeTimeSelected: TimeDto) {
        this.lastFoodIntakeTimeSelected = newLastFoodIntakeTimeSelected;
    }
}

export interface FoodIntakeForm {
    lastFoodIntake: FormControl<TimeDto>;
}