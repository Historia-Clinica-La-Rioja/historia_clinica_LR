import { ElementRef, Injectable } from '@angular/core';
import { MedicacionesNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { AnalgesicTechniqueData, AnalgesicTechniqueService } from './analgesic-technique.service';
import { AnestheticReportAnestheticHistoryService } from './anesthetic-report-anesthetic-history.service';
import { AnestheticReportAnthropometricDataService } from './anesthetic-report-anthropometric-data.service';
import { AnestheticReportClinicalEvaluationService } from './anesthetic-report-clinical-evaluation.service';
import { AnestheticReportEndOfAnesthesiaStatusService } from './anesthetic-report-end-of-anesthesia-status.service';
import { AnestheticReportIntrasurgicalAnestheticProceduresService } from './anesthetic-report-intrasurgical-anesthetic-procedures.service';
import { AnestheticReportProposedSurgeryService } from './anesthetic-report-proposed-surgery.service';
import { AnestheticReportRecordService } from './anesthetic-report-record.service';
import { AnestheticReportVitalSignsService } from './anesthetic-report-vital-signs.service';
import { AnestheticTechniqueData, AnestheticTechniqueService } from './anesthetic-technique.service';
import { FluidAdministrationData, FluidAdministrationService } from './fluid-administration.service';
import { MedicationData, MedicationService } from './medicationService';
import { UntypedFormBuilder } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject, Observable, forkJoin } from 'rxjs';
import { scrollIntoError } from '@core/utils/form.utils';
import { AnesthethicReportService } from '@api-rest/services/anesthethic-report.service';
import { AnestheticHistoryDto, AnestheticReportDto, DiagnosisDto, HealthConditionDto, MasterDataDto, PostAnesthesiaStatusDto, ProcedureDescriptionDto, TimeDto } from '@api-rest/api-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { AnestheticReportDocumentSummaryService } from '@historia-clinica/services/anesthetic-report-document-summary.service';

const TIME_OUT = 5000;

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportService {

    anesthesicReportProposedSurgeryService: AnestheticReportProposedSurgeryService;
    anesthesicReportAnthropometricDataService: AnestheticReportAnthropometricDataService;
    anestheticReportClinicalEvaluationService: AnestheticReportClinicalEvaluationService;
    anestheticReportAnestheticHistoryService: AnestheticReportAnestheticHistoryService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
    anestheticReportPremedicationAndFoodIntakeService: MedicationService;
    anestheticReportRecordService: AnestheticReportRecordService;
    anestheticPlanService: MedicationService;
    analgesicTechniqueService: AnalgesicTechniqueService;
    anestheticTechniqueService: AnestheticTechniqueService
    fluidAdministrationService: FluidAdministrationService
    anestheticReportAnestheticAgentService: MedicationService;
    anestheticReportNonAnestheticDrugsService: MedicationService;
    anestheticReportIntrasurgicalAnestheticProceduresService: AnestheticReportIntrasurgicalAnestheticProceduresService;
    anestheticReportAntibioticProphylaxisService: MedicationService;
    anestheticReportEndOfAnesthesiaStatusService: AnestheticReportEndOfAnesthesiaStatusService;
    anestheticReportVitalSignsService: AnestheticReportVitalSignsService;

    fluidAdministrationList$: Observable<FluidAdministrationData[]>;
    anestheticPlanList$: Observable<MedicationData[]>
    analgesicTechniqueList$: Observable<AnalgesicTechniqueData[]>
    anestheticTechniqueList$: Observable<AnestheticTechniqueData[]>

	premedicationViasDescription: MasterDataDto[]

    private collapsedAnthropometricDataSectionSource = new BehaviorSubject<boolean>(true);
    collapsedAnthropometricDataSection$: Observable<boolean> = this.collapsedAnthropometricDataSectionSource.asObservable();
    private collapsedClinicalEvaluationSectionSource = new BehaviorSubject<boolean>(true);
    collapsedClinicalEvaluationSection$: Observable<boolean> = this.collapsedClinicalEvaluationSectionSource.asObservable();
    private isAnestheticReportLoadingSource = new BehaviorSubject<boolean>(false);
	isAnestheticReportLoading$: Observable<boolean> = this.isAnestheticReportLoadingSource.asObservable();
	private isAnestheticReportLoadingDraftSource = new BehaviorSubject<boolean>(false);
	isAnestheticReportLoadingDraft$: Observable<boolean> = this.isAnestheticReportLoadingDraftSource.asObservable();

    lastFoodIntakeTimeSelected: TimeDto;

    private isProposedSurgeryEmptySource = new BehaviorSubject<boolean>(true);
	isProposedSurgeryEmpty$ = this.isProposedSurgeryEmptySource.asObservable();
    private isAnthropometricDataEmptySource = new BehaviorSubject<boolean>(true);
	isAnthropometricDataEmpty$ = this.isAnthropometricDataEmptySource.asObservable();
    private isClinicalEvaluationEmptySource = new BehaviorSubject<boolean>(true);
	isClinicalEvaluationEmpty$ = this.isClinicalEvaluationEmptySource.asObservable();
    private isAnestheticHistoryEmptySource = new BehaviorSubject<boolean>(true);
	isAnestheticHistoryEmpty$ = this.isAnestheticHistoryEmptySource.asObservable();
    private isMedicationEmptySource = new BehaviorSubject<boolean>(true);
	isMedicationEmpty$ = this.isMedicationEmptySource.asObservable();
    private isPremedicationAndFoodIntakeEmptySource = new BehaviorSubject<boolean>(true);
	isPremedicationAndFoodIntakeEmpty$ = this.isPremedicationAndFoodIntakeEmptySource.asObservable();
    private isRecordEmptySource = new BehaviorSubject<boolean>(true);
	isRecordEmpty$ = this.isRecordEmptySource.asObservable();
    private isAnestheticPlanEmptySource = new BehaviorSubject<boolean>(true);
	isAnestheticPlanEmpty$ = this.isAnestheticPlanEmptySource.asObservable();
    private isAnalgesicTechniqueEmptySource = new BehaviorSubject<boolean>(true);
	isAnalgesicTechniqueEmpty$ = this.isAnalgesicTechniqueEmptySource.asObservable();
    private isAnestheticTechniqueEmptySource = new BehaviorSubject<boolean>(true);
	isAnestheticTechniqueEmpty$ = this.isAnestheticTechniqueEmptySource.asObservable();
    private isFluidAdministrationEmptySource = new BehaviorSubject<boolean>(true);
	isFluidAdministrationEmpty$ = this.isFluidAdministrationEmptySource.asObservable();
    private isAnestheticAgentEmptySource = new BehaviorSubject<boolean>(true);
	isAnestheticAgentEmpty$ = this.isAnestheticAgentEmptySource.asObservable();
    private isNonAnestheticDrugsSource = new BehaviorSubject<boolean>(true);
	isNonAnestheticDrugsEmpty$ = this.isNonAnestheticDrugsSource.asObservable();
    private isIntrasurgicalAnestheticProceduresEmptySource = new BehaviorSubject<boolean>(true);
	isIntrasurgicalAnestheticProceduresEmpty$ = this.isIntrasurgicalAnestheticProceduresEmptySource.asObservable();
    private isAntibioticProphylaxisEmptySource = new BehaviorSubject<boolean>(true);
	isAntibioticProphylaxisEmpty$ = this.isAntibioticProphylaxisEmptySource.asObservable();
    private isVitalSignsEmptySource = new BehaviorSubject<boolean>(true);
	isVitalSignsEmpty$ = this.isVitalSignsEmptySource.asObservable();
    private isEndOfAnesthesiaStatusEmptySource = new BehaviorSubject<boolean>(true);
	isEndOfAnesthesiaStatusEmpty$ = this.isEndOfAnesthesiaStatusEmptySource.asObservable();

	private anestheticHistorySubject: BehaviorSubject<AnestheticHistoryDto> = new BehaviorSubject<AnestheticHistoryDto>(null);
    anestheticHistory$ = this.anestheticHistorySubject.asObservable();

	private lastIntakeSubject: BehaviorSubject<TimeDto> = new BehaviorSubject<TimeDto>(null);
    lastIntake$ = this.lastIntakeSubject.asObservable();

	private intrasurgicalAnestheticProceduresSubject: BehaviorSubject<ProcedureDescriptionDto> = new BehaviorSubject<ProcedureDescriptionDto>(null);
    intrasurgicalAnestheticProcedures$ = this.intrasurgicalAnestheticProceduresSubject.asObservable();

	private postAnesthesiaSubject: BehaviorSubject<PostAnesthesiaStatusDto> = new BehaviorSubject<PostAnesthesiaStatusDto>(null);
    postAnesthesia$ = this.postAnesthesiaSubject.asObservable();

    constructor(
        private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
        private readonly snackBarService: SnackBarService,
        private readonly translateService: TranslateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly anestheticReportDocumentSummaryService: AnestheticReportDocumentSummaryService,
        readonly anesthethicReportService: AnesthethicReportService,
    ) { }

	loadAnestheticPreviousData(dialogData: any) {
		this.resetDraftSubjects()
		this.setAnestheticPreviousData(dialogData)
	}

	private setAnestheticPreviousData(dialogData: any) {
		if (dialogData.isDraft) {
			forkJoin([
				this.internacionMasterDataService.getViasPremedication(),
				this.anesthethicReportService.getAnestheticReport(dialogData.anestheticPartId, dialogData.internmentEpisodeId)
			]).subscribe((result: [MasterDataDto[], AnestheticReportDto]) => {
				const dataPremedication = result[0]
				const data = result[1]

				this.anesthesicReportProposedSurgeryService.setData(data.surgeryProcedures);
				this.anesthesicReportAnthropometricDataService.setData(data.anthropometricData);
				this.anestheticReportClinicalEvaluationService.setData(data.riskFactors);
				this.anestheticReportAnestheticHistoryService.setData(data.anestheticHistory);
				this.anestheticHistorySubject.next(data.anestheticHistory);
				this.medicacionesNuevaConsultaService.setData(data.medications);
				this.lastIntakeSubject.next(data.foodIntake?.clockTime)
				this.anestheticReportPremedicationAndFoodIntakeService.setData(data.preMedications, dataPremedication);
				this.anestheticReportRecordService.setData(data.histories, data.procedureDescription);
				this.anestheticPlanService.setData(data.anestheticPlans, dataPremedication);
				this.analgesicTechniqueService.setData(data.analgesicTechniques);
				this.anestheticTechniqueService.setData(data.anestheticTechniques);
				this.fluidAdministrationService.setData(data.fluidAdministrations);
				this.anestheticReportAnestheticAgentService.setData(data.anestheticAgents, dataPremedication);
				this.anestheticReportNonAnestheticDrugsService.setData(data.nonAnestheticDrugs, dataPremedication);
				this.anestheticReportIntrasurgicalAnestheticProceduresService.setData(data.procedureDescription);
				this.anestheticReportAntibioticProphylaxisService.setData(data.antibioticProphylaxis, dataPremedication);
				this.anestheticReportVitalSignsService.setDataVitalSigns(data.procedureDescription);
				this.anestheticReportVitalSignsService.setMeasuringPoints(data.measuringPoints);
				this.intrasurgicalAnestheticProceduresSubject.next(data.procedureDescription)
				this.anestheticReportEndOfAnesthesiaStatusService.setDataEndOfAnesthesiaStatus(data.postAnesthesiaStatus);
				this.postAnesthesiaSubject.next(data.postAnesthesiaStatus)
			})
		}
	}

	private resetDraftSubjects() {
		this.anestheticHistorySubject.next(null);
		this.lastIntakeSubject.next(null);
		this.intrasurgicalAnestheticProceduresSubject.next(null);
		this.postAnesthesiaSubject.next(null);
	}

    createAnestheticReportServiceInstances() {
        this.anesthesicReportProposedSurgeryService = new AnestheticReportProposedSurgeryService(this.snomedService, this.snackBarService);
        this.anesthesicReportAnthropometricDataService = new AnestheticReportAnthropometricDataService(this.internacionMasterDataService, this.translateService);
        this.anestheticReportClinicalEvaluationService = new AnestheticReportClinicalEvaluationService(this.translateService);
        this.anestheticReportAnestheticHistoryService = new AnestheticReportAnestheticHistoryService();
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(this.formBuilder, this.snomedService, this.snackBarService);
		this.anestheticReportPremedicationAndFoodIntakeService = new MedicationService(this.snomedService, this.snackBarService, this.translateService, this.anestheticReportDocumentSummaryService);
        this.anestheticReportRecordService = new AnestheticReportRecordService(this.snomedService, this.snackBarService);
        this.anestheticPlanService = new MedicationService(this.snomedService, this.snackBarService, this.translateService, this.anestheticReportDocumentSummaryService);
        this.analgesicTechniqueService = new AnalgesicTechniqueService(this.snomedService, this.snackBarService, this.translateService);
        this.anestheticTechniqueService = new AnestheticTechniqueService(this.snomedService, this.snackBarService, this.anestheticReportDocumentSummaryService)
        this.fluidAdministrationService = new FluidAdministrationService(this.snomedService, this.snackBarService)
        this.anestheticReportAnestheticAgentService = new MedicationService(this.snomedService, this.snackBarService, this.translateService, this.anestheticReportDocumentSummaryService);
        this.anestheticReportNonAnestheticDrugsService = new MedicationService(this.snomedService, this.snackBarService, this.translateService, this.anestheticReportDocumentSummaryService);
        this.anestheticReportIntrasurgicalAnestheticProceduresService = new AnestheticReportIntrasurgicalAnestheticProceduresService();
        this.anestheticReportAntibioticProphylaxisService = new MedicationService(this.snomedService, this.snackBarService, this.translateService, this.anestheticReportDocumentSummaryService);
        this.anestheticReportEndOfAnesthesiaStatusService = new AnestheticReportEndOfAnesthesiaStatusService();
        this.anestheticReportVitalSignsService = new AnestheticReportVitalSignsService(this.translateService, this.snackBarService);
        this.initializeData();
        this.isEmptySubscribeHandler();
    }

    private isEmptySubscribeHandler() {
        this.anesthesicReportProposedSurgeryService.isEmpty$.subscribe(isEmpty => this.isProposedSurgeryEmptySource.next(isEmpty));
        this.anesthesicReportAnthropometricDataService.isEmpty$.subscribe(isEmpty => this.isAnthropometricDataEmptySource.next(isEmpty));
        this.anestheticReportClinicalEvaluationService.isEmpty$.subscribe(isEmpty => this.isClinicalEvaluationEmptySource.next(isEmpty));
        this.anestheticReportAnestheticHistoryService.isEmpty$.subscribe(isEmpty => this.isAnestheticHistoryEmptySource.next(isEmpty));
        this.medicacionesNuevaConsultaService.isEmpty$.subscribe(isEmpty => this.isMedicationEmptySource.next(isEmpty));
        this.anestheticReportPremedicationAndFoodIntakeService.isEmpty$.subscribe(isEmpty => this.isPremedicationAndFoodIntakeEmptySource.next(isEmpty && !this.lastFoodIntakeTimeSelected));
        this.anestheticReportRecordService.isEmpty$.subscribe(isEmpty => this.isRecordEmptySource.next(isEmpty));
        this.anestheticPlanList$.subscribe(anestheticPlanList => this.isAnestheticPlanEmptySource.next(!anestheticPlanList.length));
        this.analgesicTechniqueList$.subscribe(analgesicTechnique => this.isAnalgesicTechniqueEmptySource.next(!analgesicTechnique.length));
        this.anestheticTechniqueList$.subscribe(anestheticTechnique => this.isAnestheticTechniqueEmptySource.next(!anestheticTechnique.length));
        this.fluidAdministrationList$.subscribe(fluidAdministration => this.isFluidAdministrationEmptySource.next(!fluidAdministration.length));
        this.anestheticReportAnestheticAgentService.isEmpty$.subscribe(isEmpty => this.isAnestheticAgentEmptySource.next(isEmpty));
        this.anestheticReportNonAnestheticDrugsService.isEmpty$.subscribe(isEmpty => this.isNonAnestheticDrugsSource.next(isEmpty));
        this.anestheticReportIntrasurgicalAnestheticProceduresService.isEmpty$.subscribe(isEmpty => this.isIntrasurgicalAnestheticProceduresEmptySource.next(isEmpty));
        this.anestheticReportAntibioticProphylaxisService.isEmpty$.subscribe(isEmpty => this.isAntibioticProphylaxisEmptySource.next(isEmpty));
        this.anestheticReportVitalSignsService.isSectionEmpty$.subscribe(isEmpty => this.isVitalSignsEmptySource.next(isEmpty));
        this.anestheticReportEndOfAnesthesiaStatusService.isEmpty$.subscribe(isEmpty => this.isEndOfAnesthesiaStatusEmptySource.next(isEmpty));
    }

    private initializeData() {
        this.fluidAdministrationList$ = this.fluidAdministrationService.getFluidAdministrationList();
        this.analgesicTechniqueList$ = this.analgesicTechniqueService.getAnalgesicTechniqueList();
        this.anestheticPlanList$ = this.anestheticPlanService.getMedication();
        this.anestheticTechniqueList$ = this.anestheticTechniqueService.getAnestheticTechniqueList();
    }

    checkFormErrors(elementRef: ElementRef) {
        if (this.anesthesicReportAnthropometricDataService.getForm().invalid) {
            this.collapsedAnthropometricDataSectionSource.next(false);
            setTimeout(() => {
                scrollIntoError(this.anesthesicReportAnthropometricDataService.getForm(), elementRef)
                this.setIsLoading(false)
            }, 300);
        }
        else if (this.anestheticReportClinicalEvaluationService.getForm().invalid) {
            this.collapsedClinicalEvaluationSectionSource.next(false);
            setTimeout(() => {
                scrollIntoError(this.anestheticReportClinicalEvaluationService.getForm(), elementRef)
                this.setIsLoading(false)
            }, 300);
        }
    }

    createAnestheticReport(newAnestheticReport: AnestheticReportDto, internmentEpisodeId: number, dockPopupRef: DockPopupRef, isDraft: boolean) {
		const service = isDraft ?
			this.anesthethicReportService.createAnestheticReportDraft(newAnestheticReport, internmentEpisodeId) :
			this.anesthethicReportService.createAnestheticReport(newAnestheticReport, internmentEpisodeId)
		const successMessage = isDraft ? 'internaciones.anesthesic-report.SUCCESS_DRAFT' : 'internaciones.anesthesic-report.SUCCESS';

        service.subscribe({
			next: () => {
                this.snackBarService.showSuccess(successMessage, { duration: TIME_OUT });
                this.setIsLoading(false)
				dockPopupRef.close({
                    evolutionClinical: true
                });
			},
			error: (error) => {
				this.snackBarService.showError(error.text)
                this.setIsLoading(false)
			},
            complete: () => {
                this.resetValues();
            }
        })
    }

    getIsAnestheticReportLoading(): Observable<boolean> {
        return this.isAnestheticReportLoading$;
    }

	getIsAnestheticReportLoadingDraft(): Observable<boolean> {
        return this.isAnestheticReportLoadingDraft$;
    }

    isValidConsultation(): boolean {
        if (this.anesthesicReportAnthropometricDataService.getForm().invalid || this.anestheticReportClinicalEvaluationService.getForm().invalid)
			return false;
		return true;
    }

    buildAnestheticReportDto(mainDiagnosis: HealthConditionDto, diagnosis: DiagnosisDto[], isDraft: boolean): AnestheticReportDto {
        return {
            mainDiagnosis: mainDiagnosis,
            diagnosis: diagnosis,
            surgeryProcedures: this.anesthesicReportProposedSurgeryService.getProposedSurgeriesList(),
            anthropometricData: this.anesthesicReportAnthropometricDataService.getAnthropomethricData(),
            riskFactors: this.anestheticReportClinicalEvaluationService.getClinicalEvaluationData(),
            anestheticHistory: this.anestheticReportAnestheticHistoryService.getAnestheticHistoryData(),
            medications: this.medicacionesNuevaConsultaService.getMedicationsAsMedicationDto(),
            preMedications: this.anestheticReportPremedicationAndFoodIntakeService.getAnestheticSubstanceDto(),
            foodIntake: this.lastFoodIntakeTimeSelected ? {clockTime: this.lastFoodIntakeTimeSelected} : null,
            histories: this.anestheticReportRecordService.getRecordAsHealthConditionDto(),
            anestheticPlans: this.anestheticPlanService.getAnestheticSubstanceDto(),
            analgesicTechniques: this.analgesicTechniqueService.getAnalgesicTechniqueDto(),
            anestheticTechniques: this.anestheticTechniqueService.getAnestheticTechniqueDto(),
            fluidAdministrations: this.fluidAdministrationService.getFluidAdministrationDto(),
            anestheticAgents: this.anestheticReportAnestheticAgentService.getAnestheticSubstanceDto(),
            nonAnestheticDrugs: this.anestheticReportNonAnestheticDrugsService.getAnestheticSubstanceDto(),
            procedureDescription: this.getProcedureDescription(),
            antibioticProphylaxis: this.anestheticReportAntibioticProphylaxisService.getAnestheticSubstanceDto(),
            measuringPoints: this.anestheticReportVitalSignsService.getMeasuringPointsAsMeasuringPointDto(),
            postAnesthesiaStatus: this.anestheticReportEndOfAnesthesiaStatusService.getPostAnesthesiaStatusDto(),
			confirmed: !isDraft,
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

    setLastFoodIntakeTime(newLastFoodIntakeTimeSelected: TimeDto) {
        this.lastFoodIntakeTimeSelected = newLastFoodIntakeTimeSelected;
		this.lastIntakeSubject.next(this.lastFoodIntakeTimeSelected)
    }

    private resetValues() {
        this.setLastFoodIntakeTime(null);
    }

	private setIsLoading(loading: boolean): void {
		this.isAnestheticReportLoadingDraftSource.next(loading)
		this.isAnestheticReportLoadingSource.next(loading)
	}
}