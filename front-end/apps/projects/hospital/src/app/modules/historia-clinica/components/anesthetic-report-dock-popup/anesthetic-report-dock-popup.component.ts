import { Component, ElementRef, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder } from '@angular/forms';
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
    analgesicTechnique: AnalgesicTechniqueService;
    anestheticTechnique: AnestheticTechniqueService
    fluidAdministrationService: FluidAdministrationService
    anestheticReportAnestheticAgent: MedicationService;
    anestheticReportNonAnestheticDrugs: MedicationService;
    anestheticReportIntrasurgicalAnestheticProceduresService: AnestheticReportIntrasurgicalAnestheticProceduresService;
    anestheticReportAntibioticProphylaxisService: MedicationService;
    anestheticReportEndOfAnesthesiaStatusService: AnestheticReportEndOfAnesthesiaStatusService;
    anestheticReportVitalSignsService: AnestheticReportVitalSignsService;

    personalRecordForm: FormGroup;
    readonly ASAOptions = [1,2,3,4,5]

    formFoodIntake: FormGroup;
    endOfAnesthesiaStatusForm: FormGroup;
    vitalSignsForm: FormGroup;
    
    isVitalSignSectionEmpty = true;

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

        this.formFoodIntake = new FormGroup<FoodIntakeForm>({
            lastFoodIntake: new FormControl(null),
        });

        this.personalRecordForm = new FormGroup<PersonalRecordForm>({
            observations: new FormControl(null),
            asa: new FormControl(null)
        });

        this.endOfAnesthesiaStatusForm = new FormGroup<EndOfAnesthesiaStatusForm>({
            observations: new FormControl(null),
        })

        this.vitalSignsForm = new FormGroup<VitalSignsForm>({
            anesthesiaStartDate: new FormControl(null),
            anesthesiaEndDate: new FormControl(null),
            anesthesiaStartTime: new FormControl(null),
            anesthesiaEndTime: new FormControl(null),
            surgeryStartDate: new FormControl(null),
            surgeryEndDate: new FormControl(null),
            surgeryStartTime: new FormControl(null),
            surgeryEndTime: new FormControl(null),
        })

        this.checkVitalSignSectionEmptyness();

        this.possibleTimesList = this.anestheticReportPremedicationAndFoodIntakeService.possibleTimesList;
    }

    ngOnInit(): void {
        this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosis;
        this.fluidAdministrationList$ = this.fluidAdministrationService.getFluidAdministrationList()
        this.analgesicTechniqueList$ = this.analgesicTechnique.getAnalgesicTechniqueList()
        this.anestheticPlanList$ = this.anestheticPlan.getMedication()
        this.anestheticTechniqueList$ = this.anestheticTechnique.getAnestheticTechniqueList()
        
    }

    private checkVitalSignSectionEmptyness() {
        this.vitalSignsForm.valueChanges.subscribe(() => {
            if (this.isVitalSignSectionEmpty){
                this.isVitalSignSectionEmpty = this.checkVitalSignsEmptyness();
            }
          });
        this.anestheticReportVitalSignsService.isEmpty$.subscribe(isEmpty => {
            if (this.isVitalSignSectionEmpty){
                this.isVitalSignSectionEmpty = isEmpty;
            }
        })
    }
    
    private checkVitalSignsEmptyness(): boolean {
        for (let controlName in this.vitalSignsForm.controls) {
            if (this.vitalSignsForm.controls.hasOwnProperty(controlName)) {
                if (this.vitalSignsForm.controls[controlName].value) {
                    return false;
                }
            }
        }
        return true;
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
            analgesicTechniques: this.analgesicTechnique.getAnalgesicTechniqueDto(),
            anestheticTechniques: this.anestheticTechnique.getAnestheticTechniqueDto(),
            fluidAdministrations: this.fluidAdministrationService.getFluidAdministrationDto(),
            anestheticAgents: this.anestheticReportAnestheticAgent.getAnestheticSubstanceDto(),
            nonAnestheticDrugs: this.anestheticReportNonAnestheticDrugs.getAnestheticSubstanceDto(),
            procedureDescription: this.getProcedureDescription(),
            antibioticProphylaxis: this.anestheticReportAntibioticProphylaxisService.getAnestheticSubstanceDto(),
		};
	}

    private getProcedureDescription(): ProcedureDescriptionDto {
        const radioButtonsOptions = this.anestheticReportIntrasurgicalAnestheticProceduresService.getIntrasurgicalAnestheticProceduresData();
        return {
            nasogastricTube: radioButtonsOptions.nasogastricTube,
            urinaryCatheter: radioButtonsOptions.urinaryCatheter,
            venousAccess: radioButtonsOptions.venousAccess,
            note: this.personalRecordForm.value.observation,
            asa: this.personalRecordForm.value.asa
        }
    }
        
    setAnesthesiaStartDate(date: Date) {
        this.vitalSignsForm.controls.anesthesiaStartDate.setValue(date);
    }

    setAnesthesiaEndDate(date: Date) {
        this.vitalSignsForm.controls.anesthesiaEndDate.setValue(date);
    }

    setSurgeryStartDate(date: Date) {
        this.vitalSignsForm.controls.surgeryStartDate.setValue(date);
    }

    setSurgeryEndDate(date: Date) {
        this.vitalSignsForm.controls.surgeryEndDate.setValue(date);
    }
}

export interface FoodIntakeForm {
    lastFoodIntake: FormControl<TimeDto>;
}

export interface PersonalRecordForm {
    observations: FormControl<string>;
    asa: FormControl<number>;
}

export interface EndOfAnesthesiaStatusForm {
    observations: FormControl<string>;
}

export interface VitalSignsForm {
    anesthesiaStartDate: FormControl<Date>;
    anesthesiaEndDate: FormControl<Date>;
    anesthesiaStartTime: FormControl<TimeDto>;
    anesthesiaEndTime: FormControl<TimeDto>;
    surgeryStartDate: FormControl<Date>;
    surgeryEndDate: FormControl<Date>;
    surgeryStartTime: FormControl<TimeDto>;
    surgeryEndTime: FormControl<TimeDto>;
}