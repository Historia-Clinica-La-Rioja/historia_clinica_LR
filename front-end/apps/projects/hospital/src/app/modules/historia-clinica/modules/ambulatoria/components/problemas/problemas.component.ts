import { Component, Injector, Input, OnDestroy, OnInit, Output, ViewEncapsulation } from '@angular/core';
import {
	PROBLEMAS_ACTIVOS,
	PROBLEMAS_CRONICOS,
	PROBLEMAS_INTERNACION,
	PROBLEMAS_RESUELTOS
} from '../../../../constants/summaries';
import {
	ExternalClinicalHistorySummaryDto,
	HCEDocumentDataDto,
	HCEHospitalizationHistoryDto,
	HCEHealthConditionDto,
	InternmentEpisodeProcessDto,
	ReferenceCounterReferenceFileDto
} from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ActivatedRoute, Router } from '@angular/router';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { map, tap } from 'rxjs/operators';
import { Observable, Subject, Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { HistoricalProblems, HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';
import { ContextService } from '@core/services/context.service';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { ExternalClinicalHistoryFacadeService } from '../../services/external-clinical-history-facade.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { dateDtoToDate, dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { CounterreferenceFileService } from '@api-rest/services/counterreference-file.service';
import { DocumentService } from "@api-rest/services/document.service";
import { PatientNameService } from "@core/services/patient-name.service";
import { Color } from '@presentation/colored-label/colored-label.component';
import { dateToDateTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { Position } from '@presentation/components/identifier/identifier.component';
import { buildProblemHeaderInformation } from '@historia-clinica/mappers/problems.mapper';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { compare } from '@core/utils/date.utils';

const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_INTERNMENT_EPISODE_SUFIX = '/paciente/';

@Component({
	selector: 'app-problemas',
	templateUrl: './problemas.component.html',
	styleUrls: ['./problemas.component.scss'],
	encapsulation: ViewEncapsulation.None
})
export class ProblemasComponent implements OnInit, OnDestroy {

	@Input() nuevaConsultaRef: DockPopupRef;
	Color = Color;
	Position = Position;
	public readonly cronicos = PROBLEMAS_CRONICOS;
	public readonly activos = PROBLEMAS_ACTIVOS;
	public readonly resueltos = PROBLEMAS_RESUELTOS;
	public readonly internacion = PROBLEMAS_INTERNACION;
	private readonly routePrefix;
	public activeProblems$: Observable<HCEHealthConditionDto[]>;
	public solvedProblems$: Observable<HCEHealthConditionDto[]>;
	public chronicProblems$: Observable<HCEHealthConditionDto[]>;
	public hospitalizationProblems$: Observable<HCEHospitalizationHistoryDto[]>;
	public historicalProblemsList: HistoricalProblems[];
	public historicalProblemsAmount: number;
	public hideFilterPanel = false;
	private historicalProblems$: Subscription;
	patientId: number;
	private severityTypeMasterData: any[];

	public selectedTab: number = 0;

	// External clinical history attributes
	public externalClinicalHistoryList: ExternalClinicalHistorySummaryDto[];
	public externalClinicalHistoryAmount: number = 0;
	public showExternalFilters: boolean = false;
	public showExternalClinicalHistoryTab: boolean = false;
	public externalHistoriesInformation: boolean = false;

	// Injected dependencies
	private contextService: ContextService;
	private readonly internacionMasterDataService: InternacionMasterDataService;
	private readonly externalClinicalHistoryService: ExternalClinicalHistoryFacadeService;
	private readonly featureFlagService: FeatureFlagService;
	@Input() internmentInProcess: InternmentEpisodeProcessDto;
	@Output() goToEmergencyCareEpisode = new Subject<number>();

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
		private readonly dateFormatPipe: DateFormatPipe
	) {
		this.contextService = this.injector.get<ContextService>(ContextService);
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
			map(p => this.formatProblemsDates(p,this.dateFormatPipe))
		);
	}

	setChronicProblems$() {
		this.chronicProblems$ = this.ambulatoriaSummaryFacadeService.chronicProblems$.pipe(
			map(p => this.formatProblemsDates(p, this.dateFormatPipe))
		);
	}

	setSolvedProblems$() {
		this.solvedProblems$ = this.ambulatoriaSummaryFacadeService.solvedProblems$.pipe(
			map(p => this.formatProblemsDates(p, this.dateFormatPipe))
		);
	}

	private formatProblemsDates(problemas: HCEHealthConditionDto[], dateFormatPipe: DateFormatPipe) {
		return problemas.map(p => this.toProblems(p, dateFormatPipe));
	}

	private toProblems(problema: HCEHealthConditionDto, dateFormatPipe: DateFormatPipe) {
		return {
			...problema,
			startDate: problema.startDate ? dateFormatPipe.transform(dateISOParseDate(problema.startDate), 'date') : undefined,
			inactivationDate: problema.inactivationDate ? dateFormatPipe.transform(dateISOParseDate(problema.inactivationDate), 'date') : undefined
		};
	}

	loadHospitalizationProblems() {
		this.hospitalizationProblems$ = this.hceGeneralStateService.getHospitalizationHistory(this.patientId).pipe(
			map((problemas: HCEHospitalizationHistoryDto[]) => {
				return problemas.map((problema: HCEHospitalizationHistoryDto) => {
					problema.entryDate = dateToDateTimeDto(dateTimeDtotoLocalDate(problema.entryDate));
					problema.dischargeDate = problema.dischargeDate ? dateToDateTimeDto(dateTimeDtotoLocalDate(problema.dischargeDate)) : undefined;
					return problema;
				});
			})
		);
	}

	loadHistoricalProblems() {
		this.historicalProblems$ = this.historicalProblemsFacadeService.getHistoricalProblems().pipe(
			tap(historicalProblems => this.historicalProblemsAmount = historicalProblems ? historicalProblems.length : 0)
		).subscribe(data => {
			this.historicalProblemsList = data.map(problem => {
				problem.headerInfoDetails = buildProblemHeaderInformation(problem);
				return problem;
			})
		})
	}

	scrollToHistoric() {
		let historic = document.getElementById("historical-problems");
		historic.scrollIntoView({ behavior: 'smooth' });
	}

	filterByProblemOnProblemClick(problem: HCEHealthConditionDto) {
		this.historicalProblemsFacadeService.sendHistoricalProblemsFilter({
			specialty: null,
			professional: null,
			problem: problem.snomed.sctid,
			consultationDate: null,
			referenceStateId: null,
		});
		this.selectedTab = 0;
		this.scrollToHistoric()
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
			tap((filteredHistories: ExternalClinicalHistorySummaryDto[]) => this.externalClinicalHistoryAmount = filteredHistories ? filteredHistories.length : 0)
		).subscribe(
			(filteredHistories: ExternalClinicalHistorySummaryDto[]) =>
				this.externalClinicalHistoryList = [...filteredHistories].sort(this.orderDesc)
		);

		this.externalClinicalHistoryService.hasInformation$().subscribe(
			(hasInfo: boolean) => this.externalHistoriesInformation = hasInfo
		);
	}

	public orderDesc(h1: ExternalClinicalHistorySummaryDto, h2: ExternalClinicalHistorySummaryDto) {
		const date1: Date = dateDtoToDate(h1.consultationDate);
		const date2: Date = dateDtoToDate(h2.consultationDate);
		return compare(date2, date1)
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

	goToEpisode(episodeId: number) {
		this.goToEmergencyCareEpisode.next(episodeId);
	}
}
