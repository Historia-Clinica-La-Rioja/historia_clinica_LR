import { Component, Injector, Input, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import {
	PROBLEMAS_ACTIVOS,
	PROBLEMAS_CRONICOS,
	PROBLEMAS_INTERNACION,
	PROBLEMAS_RESUELTOS
} from '../../../../constants/summaries';
import {
	ExternalClinicalHistoryDto,
	HCEDocumentDataDto,
	HCEHospitalizationHistoryDto,
	HCEPersonalHistoryDto,
	InternmentEpisodeProcessDto,
	ReferenceCounterReferenceFileDto
} from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DateFormat, dateToMoment, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { map, tap } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { SolveProblemComponent } from '../../../../dialogs/solve-problem/solve-problem.component';
import { HistoricalProblems, HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';
import { ContextService } from '@core/services/context.service';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ExternalClinicalHistoryFacadeService } from '../../services/external-clinical-history-facade.service';
import { Moment } from 'moment';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { CounterreferenceFileService } from '@api-rest/services/counterreference-file.service';
import { DocumentService } from "@api-rest/services/document.service";
import {PatientNameService} from "@core/services/patient-name.service";
import { Color } from '@presentation/colored-label/colored-label.component';

const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_INTERNMENT_EPISODE_SUFIX = '/paciente/';

@Component({
	selector: 'app-problemas',
	templateUrl: './problemas.component.html',
	styleUrls: ['./problemas.component.scss'],
	encapsulation: ViewEncapsulation.None
})
export class ProblemasComponent implements OnInit, OnDestroy {

	@Input()
	set nuevaConsultaRef(nuevaConsultaRef: DockPopupRef) {
		this.nuevaConsultaAmbulatoriaRef = nuevaConsultaRef;
		if (nuevaConsultaRef) {
			this.nuevaConsultaFromProblemaRef?.close();
			delete this.nuevaConsultaFromProblemaRef;
		}
	}
	Color = Color;
	public hasNewConsultationEnabled$: Observable<boolean>;

	public readonly cronicos = PROBLEMAS_CRONICOS;
	public readonly activos = PROBLEMAS_ACTIVOS;
	public readonly resueltos = PROBLEMAS_RESUELTOS;
	public readonly internacion = PROBLEMAS_INTERNACION;
	private readonly routePrefix;
	public activeProblems$: Observable<HCEPersonalHistoryDto[]>;
	public solvedProblems$: Observable<HCEPersonalHistoryDto[]>;
	public chronicProblems$: Observable<HCEPersonalHistoryDto[]>;
	public hospitalizationProblems$: Observable<HCEHospitalizationHistoryDto[]>;
	public historicalProblemsList: HistoricalProblems[];
	public historicalProblemsAmount: number;
	public hideFilterPanel = false;
	private historicalProblems$: Subscription;
	private patientId: number;
	private nuevaConsultaAmbulatoriaRef: DockPopupRef;
	private nuevaConsultaFromProblemaRef: DockPopupRef;
	private severityTypeMasterData: any[];

	public selectedTab: number = 0;

	// External clinical history attributes
	public externalClinicalHistoryList: ExternalClinicalHistoryDto[];
	public externalClinicalHistoryAmount: number = 0;
	public showExternalFilters: boolean = false;
	public showExternalClinicalHistoryTab: boolean = false;
	public externalHistoriesInformation: boolean = false;

	// Injected dependencies
	private contextService: ContextService;
	private dockPopupService: DockPopupService;
	private readonly internacionMasterDataService: InternacionMasterDataService;
	private readonly externalClinicalHistoryService: ExternalClinicalHistoryFacadeService;
	private readonly featureFlagService: FeatureFlagService;
	@Input() internmentInProcess: InternmentEpisodeProcessDto;

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private historicalProblemsFacadeService: HistoricalProblemsFacadeService,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private route: ActivatedRoute,
		private readonly router: Router,
		public dialog: MatDialog,
		private injector: Injector,
		private readonly referenceFileService: ReferenceFileService,
		private readonly counterreferenceFileService: CounterreferenceFileService,
		private readonly documentService: DocumentService,
		private readonly patientNameService: PatientNameService,
	) {
		this.contextService = this.injector.get<ContextService>(ContextService);
		this.dockPopupService = this.injector.get<DockPopupService>(DockPopupService);
		this.internacionMasterDataService = this.injector.get<InternacionMasterDataService>(InternacionMasterDataService);
		this.externalClinicalHistoryService = this.injector.get<ExternalClinicalHistoryFacadeService>(ExternalClinicalHistoryFacadeService);
		this.featureFlagService = this.injector.get<FeatureFlagService>(FeatureFlagService);

		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.externalClinicalHistoryService.loadInformation(this.patientId);
				historicalProblemsFacadeService.setPatientId(this.patientId);
			});
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;
		this.setActiveProblems$();
		this.setChronicProblems$();
		this.setSolvedProblems$();
		this.loadHospitalizationProblems();
		this.loadHistoricalProblems();

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypeMasterData = healthConditionSeverities;
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_HISTORIA_CLINICA_EXTERNA).subscribe(
			(show) => {
				this.showExternalClinicalHistoryTab = show;
				if (this.showExternalClinicalHistoryTab) this.loadExternalClinicalHistoryList();
			}
		);
	}

	ngOnDestroy(): void {
		this.historicalProblems$.unsubscribe();
	}

	getSeverityTypeDisplayByCode(severityCode): string {
		return (severityCode && this.severityTypeMasterData) ?
			this.severityTypeMasterData.find(severityType => severityType.code === severityCode).display
			: '';
	}

	setActiveProblems$() {
		this.activeProblems$ = this.ambulatoriaSummaryFacadeService.activeProblems$.pipe(
			map(this.formatProblemsDates)
		);
	}

	setChronicProblems$() {
		this.chronicProblems$ = this.ambulatoriaSummaryFacadeService.chronicProblems$.pipe(
			map(this.formatProblemsDates)
		);
	}

	setSolvedProblems$() {
		this.solvedProblems$ = this.ambulatoriaSummaryFacadeService.solvedProblems$.pipe(
			map(this.formatProblemsDates)
		);
	}

	private formatProblemsDates(problemas: HCEPersonalHistoryDto[]) {
		return problemas.map((problema: HCEPersonalHistoryDto) => {
			return {
				...problema,
				startDate: problema.startDate ? momentFormat(momentParseDate(problema.startDate), DateFormat.VIEW_DATE) : undefined,
				inactivationDate: problema.inactivationDate ? momentFormat(momentParseDate(problema.inactivationDate), DateFormat.VIEW_DATE) : undefined
			};
		});
	}

	loadHospitalizationProblems() {
		this.hospitalizationProblems$ = this.hceGeneralStateService.getHospitalizationHistory(this.patientId).pipe(
			map((problemas: HCEHospitalizationHistoryDto[]) => {
				return problemas.map((problema: HCEHospitalizationHistoryDto) => {
					problema.entryDate = momentFormat(momentParseDate(problema.entryDate), DateFormat.VIEW_DATE);
					problema.dischargeDate = problema.dischargeDate ? momentFormat(momentParseDate(problema.dischargeDate), DateFormat.VIEW_DATE) : undefined;
					return problema;
				});
			})
		);
	}

	loadHistoricalProblems() {
		this.historicalProblems$ = this.historicalProblemsFacadeService.getHistoricalProblems().pipe(
			tap(historicalProblems => this.historicalProblemsAmount = historicalProblems ? historicalProblems.length : 0)
		).subscribe(data => this.historicalProblemsList = data);
	}

	openNuevaConsulta(problema: HCEPersonalHistoryDto): void {
		if (!this.nuevaConsultaFromProblemaRef) {
			if (!this.nuevaConsultaAmbulatoriaRef) {
				this.openDockPopup(problema.id);
			} else {
				const confirmDialog = this.dialog.open(ConfirmDialogComponent, { data: getConfirmDataDialog() });
				confirmDialog.afterClosed().subscribe(confirmed => {
					if (confirmed) {
						this.openDockPopup(problema.id);
						this.nuevaConsultaAmbulatoriaRef.close();
					}
				});
			}
		}

		function getConfirmDataDialog() {
			const keyPrefix = 'ambulatoria.paciente.problemas.nueva_opened_confirm_dialog';
			return {
				title: `${keyPrefix}.TITLE`,
				content: `${keyPrefix}.CONTENT`,
				okButtonLabel: `${keyPrefix}.OK_BUTTON`,
				cancelButtonLabel: `${keyPrefix}.CANCEL_BUTTON`,
			};
		}
	}

	private openDockPopup(idProblema: number) {
		const idPaciente = this.route.snapshot.paramMap.get('idPaciente');
		this.nuevaConsultaFromProblemaRef =
			this.dockPopupService.open(NuevaConsultaDockPopupComponent, { idPaciente, idProblema });
		this.nuevaConsultaFromProblemaRef.afterClosed().subscribe(fieldsToUpdate => {
			if (fieldsToUpdate) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
			}
			delete this.nuevaConsultaFromProblemaRef;
		});
	}

	solveProblemPopUp(problema: HCEPersonalHistoryDto) {
		this.dialog.open(SolveProblemComponent, {
			data: {
				problema,
				patientId: this.patientId
			}
		}).afterClosed().subscribe(submitted => {
			if (submitted) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({ problems: true });
			}
		});
	}

	filterByProblemOnProblemClick(problem: HCEPersonalHistoryDto) {
		this.historicalProblemsFacadeService.sendHistoricalProblemsFilter({
			specialty: null,
			professional: null,
			problem: problem.snomed.sctid,
			consultationDate: null,
			referenceStateId: null,
		});
		this.selectedTab = 0;
	}

	hideFilters() {
		this.hideFilterPanel = !this.hideFilterPanel;
	}

	goToHospitalizationEpisode(problema: HCEHospitalizationHistoryDto) {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + problema.sourceId + ROUTE_INTERNMENT_EPISODE_SUFIX + this.patientId]);
	}

	// External clinical History methods

	public toggleExternalFilters(): void {
		this.showExternalFilters = !this.showExternalFilters;
	}

	private loadExternalClinicalHistoryList(): void {
		this.externalClinicalHistoryService.getFilteredHistories$().pipe(
			tap((filteredHistories: ExternalClinicalHistoryDto[]) => this.externalClinicalHistoryAmount = filteredHistories ? filteredHistories.length : 0)
		).subscribe(
			(filteredHistories: ExternalClinicalHistoryDto[]) =>
				this.externalClinicalHistoryList = [...filteredHistories].sort(this.compareByDate)
		);

		this.externalClinicalHistoryService.hasInformation$().subscribe(
			(hasInfo: boolean) => this.externalHistoriesInformation = hasInfo
		);
	}

	private compareByDate(h1: ExternalClinicalHistoryDto, h2: ExternalClinicalHistoryDto) {
		// function used to sort External Clinical Histories by descending date
		const moment1: Moment = dateToMoment(dateDtoToDate(h1.consultationDate));
		const moment2: Moment = dateToMoment(dateDtoToDate(h2.consultationDate));
		if (moment1.isSame(moment2)) return 0;
		else if (moment1.isBefore(moment2)) return 1;
		return -1;
	}

	downloadReferenceFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

	downloadCounterreferenceFile(file: ReferenceCounterReferenceFileDto) {
		this.counterreferenceFileService.downloadCounterreferenceFiles(file.fileId, file.fileName);
	}


	downloadDocument(document: HCEDocumentDataDto) {
		this.documentService.downloadFile(document);
	}

	getFullName(firstName: string, nameSelfDetermination: string): string {
		return `${this.patientNameService.getPatientName(firstName, nameSelfDetermination)}`;
	}
}
