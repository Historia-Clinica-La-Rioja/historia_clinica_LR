import { MedicalCoverageInfoService } from './../../services/medical-coverage-info.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { AppFeature, EMedicalCoverageTypeDto, ERole, EPatientMedicalCoverageCondition } from '@api-rest/api-model';
import { EpicrisisSummaryDto, BasicPatientDto, OrganizationDto, PatientSummaryDto, PersonPhotoDto, InternmentEpisodeProcessDto, ExternalPatientCoverageDto, EmergencyCareEpisodeInProgressDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { InteroperabilityBusService } from '@api-rest/services/interoperability-bus.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';
import { UIPageDto } from '@extensions/extensions-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { MenuItem } from '@presentation/components/menu/menu.component';
import { ExtensionPatientService } from '@extensions/services/extension-patient.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { OdontogramService } from '@historia-clinica/modules/odontologia/services/odontogram.service';
import { FieldsToUpdate } from "@historia-clinica/modules/odontologia/components/odontology-consultation-dock-popup/odontology-consultation-dock-popup.component";
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';
import { MatDialog } from '@angular/material/dialog';
import { InternmentPatientService } from "@api-rest/services/internment-patient.service";
import { ContextService } from '@core/services/context.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { HomeRoutes } from 'projects/hospital/src/app/modules/home/home-routing.module';
import { EmergencyCareEpisodeSummaryService } from "@api-rest/services/emergency-care-episode-summary.service";
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { PatientAllergiesService } from '../../services/patient-allergies.service';
import { SummaryCoverageInformation } from '../../components/medical-coverage-summary-view/medical-coverage-summary-view.component';
import { EMedicalCoverageType } from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { InternmentActionsService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-actions.service";
import { Slot, SlotedInfo, WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { PatientType } from '@historia-clinica/constants/summaries';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DialogoAclaracionComponent } from './dialogo-aclaracion/dialogo-aclaracion.component';
import { HCEPersonalHistoryDto } from '@api-rest/api-model';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { MedicationInfoDto } from '@api-rest/api-model';
import { DialogoMayorDe15Component } from './dialogo-mayor-de15/dialogo-mayor-de15.component';
import { DialogoInconsistenciaComponent } from './dialogo-inconsistencia/dialogo-inconsistencia.component';

const RESUMEN_INDEX = 0;
const VOLUNTARY_ID = 1;
const FEMENINO = 'Femenino';
const EMERGENCY_CARE_INDEX = 0;
const EMERGENCY_CARE_INDEX_WHEN_INTERNED = 1;

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss'],

})
export class AmbulatoriaPacienteComponent implements OnInit, OnDestroy {

	dialogRef: DockPopupRef;
	patient: PatientBasicData;
	patientId: number;
	personId: number;
	extensionTabs$: Observable<{ head: MenuItem, body$: Observable<UIPageDto> }[]>;
	extensionWCTabs$: Observable<SlotedInfo[]>;
	medicamentStatus$: Observable<any>;
	studyCategories$: Observable<any>;
	diagnosticReportsStatus$: Observable<any>;
	personInformation: AdditionalInfo[] = [];
	personPhoto: PersonPhotoDto;
	showOrders: boolean;
	externalInstitutionsEnabled: boolean;
	odontologyEnabled: boolean;
	externalInstitutions: OrganizationDto[];
	patientExternalSummary: PatientSummaryDto;
	externalInstitutionPlaceholder = 'Ninguna';
	loaded = false;
	spinner = false;
	bloodType: string;
	internmentEpisodeProcess: InternmentEpisodeProcessDto;
	emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;
	hasInternmentEpisodeInThisInstitution = undefined;
	epicrisisDoc: EpicrisisSummaryDto;
	hasMedicalRole = false;
	hasNurseRole = false;
	hasHealthProfessionalRole = false;
	hasOdontologyRole = false;
	hasHealthRelatedRole = false;
	hasPicturesStaffRole = false;
	hasLaboratoryStaffRole = false;
	hasPharmacyStaffRole = false;
	hasEmergencyCareRelatedRole = false;
	showNursingSection = false;
	femenino = FEMENINO;
	selectedTab = 0;
	isNoValidatedOrTemporary: boolean = false;
	isHabilitarRecetaDigitalEnabled: boolean = false;
	emergencyCareTabIndex: number;
	showEmergencyCareTab: boolean;
	hasEpisodeToShow: boolean;
	hasPrescriptorRole = false;
	canOnlyViewSelfAddedProblems = false;
	rolesThatCanOnlyViewSelfAddedProblems = [ERole.PRESCRIPTOR];
	defaultProblem: HCEPersonalHistoryDto;
	activeProblemsList: {
		id: number;
		description: string;
		sctId: string;
	}[];
	private _embarazoAdolescente: boolean;
	private _anticonceptivos: boolean;
	private medicationList;
	private timeOut = 15000;
	private isOpenOdontologyConsultation = false;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly interoperabilityBusService: InteroperabilityBusService,
		private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly extensionPatientService: ExtensionPatientService,
		private readonly odontogramService: OdontogramService,
		private readonly permissionsService: PermissionsService,
		private readonly dialog: MatDialog,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly contextService: ContextService,
		private readonly router: Router,
		private readonly emergencyCareEpisodeSummaryService: EmergencyCareEpisodeSummaryService,
		private readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService,
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		readonly patientAllergies: PatientAllergiesService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly internmentActionsService: InternmentActionsService,
		private readonly medicalCoverageInfo: MedicalCoverageInfoService,
		private readonly wcExtensionsService: WCExtensionsService,
		readonly hceGeneralStateService: HceGeneralStateService,
		private medicationRequestService: MedicationRequestService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECETA_DIGITAL)
			.subscribe((result: boolean) => this.isHabilitarRecetaDigitalEnabled = result)

		const toEmergencyCareTab = this.router.getCurrentNavigation()?.extras?.state?.toEmergencyCareTab;
		this.setPermissions();
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).subscribe(
					patient => {
						if (this.isHabilitarRecetaDigitalEnabled && (patient.typeId === PatientType.TEMPORARY || patient.typeId === PatientType.PERMANENT_NO_VALIDATED)) {
							this.isNoValidatedOrTemporary = true
							this.snackBarService.showError('indicacion.INDICACIONES_DISABLED');
						}

						this.personInformation.push({ description: patient.person.identificationType, data: patient.person.identificationNumber });
						this.patient = this.mapperService.toPatientBasicData(patient);
						this.personId = patient.person.id;
					}
				);
				this.ambulatoriaSummaryFacadeService.setIdPaciente(this.patientId);
				this.patientAllergies.updateCriticalAllergies(this.patientId);
				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
				this.ambulatoriaSummaryFacadeService.bloodType$.subscribe(blood => this.bloodType = blood);

				this.medicalCoverageInfo.setAppointmentConfirmedCoverageInfo(this.patientId);

				this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId).subscribe(
					(internmentEpisodeProcess: InternmentEpisodeProcessDto) => {
						this.internmentEpisodeProcess = internmentEpisodeProcess
						if (this.internmentEpisodeProcess.id && this.internmentEpisodeProcess.inProgress) {
							this.internmentActionsService.setInternmentInformation(this.patientId, this.internmentEpisodeProcess.id);
							if (!this.medicalCoverageInfo.summaryCoverageInfo || !this.medicalCoverageInfo.appointmentConfirmedCoverageInfo) {
								this.medicalCoverageInfo.setInternmentMCoverage(this.patientId, this.internmentEpisodeProcess.id);
							}
							this.internmentSummaryFacadeService.setInternmentEpisodeInformation(internmentEpisodeProcess.id, false, true);
							if (this.internmentEpisodeProcess.inProgress) {
								this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
								this.internmentSummaryFacadeService.unifyFamilyHistories(this.patientId);
							}
							this.internmentSummaryFacadeService.epicrisis$.subscribe(e => this.epicrisisDoc = e);
							this.internmentSummaryFacadeService.bloodTypeData$.subscribe(
								bloodType => {
									if (bloodType)
										this.bloodType = bloodType
								});
						}
						this.hasInternmentEpisodeInThisInstitution = internmentEpisodeProcess.inProgress && !!internmentEpisodeProcess.id;
						this.emergencyCareTabIndex = this.hasInternmentEpisodeInThisInstitution ? EMERGENCY_CARE_INDEX_WHEN_INTERNED : EMERGENCY_CARE_INDEX;

						this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgress(this.patientId).subscribe(
							emergencyCareEpisodeInProgressDto => {
								this.emergencyCareEpisodeInProgress = emergencyCareEpisodeInProgressDto;
								if (emergencyCareEpisodeInProgressDto?.id) {
									this.emergencyCareEpisodeStateService.getState(emergencyCareEpisodeInProgressDto.id).subscribe(
										state => {
											const episodeState = state.id;
											const emergencyEpisodeWithMedicalDischarge = (EstadosEpisodio.CON_ALTA_MEDICA === episodeState);
											this.hasEpisodeToShow = (this.emergencyCareEpisodeInProgress?.inProgress && !emergencyEpisodeWithMedicalDischarge);
											this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_GUARDIA)
												.subscribe(isOn => {
													this.showEmergencyCareTab = this.hasEpisodeToShow && isOn;
													if (toEmergencyCareTab) {
														this.selectedTab = this.emergencyCareTabIndex;
													}
												}
												);

										}
									);
								}
								else {
									this.showEmergencyCareTab = false;
								}
							}
						);


					}
				);
			}
		);
	}

	ngOnInit(): void {
		this.setActionsLayout();

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUS_INTEROPERABILIDAD)
			.subscribe(isOn => this.externalInstitutionsEnabled = isOn);

		this.featureFlagService.isActive(AppFeature.HABILITAR_ODONTOLOGY)
			.subscribe(isOn => this.odontologyEnabled = isOn);

		this.extensionTabs$ = this.extensionPatientService.getTabs(this.patientId);


		this.extensionWCTabs$ = this.wcExtensionsService.getComponentsFromSlot(Slot.CLINIC_HISTORY_TAB);

		this.odontogramService.resetOdontogram();

		this.medicamentStatus$ = this.requestMasterDataService.medicationStatus();

		this.diagnosticReportsStatus$ = this.requestMasterDataService.diagnosticReportStatus();

		this.studyCategories$ = this.requestMasterDataService.categories();

		this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_ENF_EN_DESARROLLO)
			.subscribe(show => this.showNursingSection = show);
	}

	ngOnDestroy() {
		this.medicalCoverageInfo.clearAll();
	}

	private setPermissions(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.canOnlyViewSelfAddedProblems = anyMatch<ERole>(userRoles, this.rolesThatCanOnlyViewSelfAddedProblems);});
	}

	loadExternalInstitutions(): void {
		const externalInstitutions = this.interoperabilityBusService.getPatientLocation(this.patientId.toString())
			.subscribe(
				location => {
					if (location.length === 0) {
						this.snackBarService.showError('ambulatoria.bus-interoperabilidad.PACIENTE-NO-FEDERADO');
						this.loaded = false;
					} else { this.externalInstitutions = location; }
				},
				error => {
					this.snackBarService.showError('ambulatoria.bus-interoperabilidad.INSTITUTION_LOADING_ERROR');
					this.loaded = false;
				});
		this.showTimeOutMessages(externalInstitutions);
	}

	loadExternalSummary(organization: OrganizationDto): void {
		this.spinner = true;

		const info = this.interoperabilityBusService.getPatientInfo(this.patientId.toString(), organization.custodian)
			.subscribe(summary => {
				this.patientExternalSummary = summary;
				this.spinner = false;
			});

		this.showTimeOutMessages(info);
	}

	externalInstitutionsClicked(): void {
		if (!this.loaded) {
			this.loaded = true;
			this.loadExternalInstitutions();
			this.externalInstitutionPlaceholder = ' ';
		}
	}

	showTimeOutMessages(subscription): void {
		setTimeout(() => {
			if (this.spinner) {
				subscription.unsubscribe();
				this.snackBarService.showError('ambulatoria.bus-interoperabilidad.TIMEOUT-MESSAGE');
				this.spinner = false;
			}
		}, this.timeOut);
	}

	onTabChanged(event: MatTabChangeEvent): void {
		// TODO Utilizar este método para actualizar componentes asociados a Tabs

		if (event.index == RESUMEN_INDEX) {
			this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({
				allergies: false,
				familyHistories: false,
				personalHistories: false,
				personalHistoriesByRole: true,
				riskFactors: false,
				medications: true,
				anthropometricData: false,
				problems: false
			});
		}
	}

	updateFields(fieldsToUpdate: FieldsToUpdate): void {
		this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
	}

	setActionsLayout(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasMedicalRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO]);
			this.hasNurseRole = anyMatch<ERole>(userRoles, [ERole.ENFERMERO]);
			this.hasHealthProfessionalRole = anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD]);
			this.hasEmergencyCareRelatedRole = (this.hasMedicalRole || this.hasNurseRole || this.hasHealthProfessionalRole);
			this.hasOdontologyRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_EN_ODONTOLOGIA]);
			this.hasHealthRelatedRole = anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO_ADULTO_MAYOR]);
			this.hasPicturesStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_IMAGENES]);
			this.hasLaboratoryStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_LABORATORIO]);
			this.hasPharmacyStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_FARMACIA]);
			this.hasPrescriptorRole = anyMatch<ERole>(userRoles, [ERole.PRESCRIPTOR]);
		});
	}

	goToPatient(): void {
		const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/ambulatoria/${AppRoutes.PortalPaciente}/${this.patientId}/${HomeRoutes.Profile}`;

		if (this.dialogRef || this.isOpenOdontologyConsultation || this.odontogramService.existActionedTeeth()) {
			const dialog = this.dialog.open(DiscardWarningComponent,
				{
					data: {
						content: 'ambulatoria.screen_change_warning_dialog.CONTENT',
						contentBold: `ambulatoria.screen_change_warning_dialog.ANSWER_CONTENT`,
						okButtonLabel: 'ambulatoria.screen_change_warning_dialog.CONFIRM_BUTTON',
						cancelButtonLabel: 'ambulatoria.screen_change_warning_dialog.CANCEL_BUTTON',
					}
				});
			dialog.afterClosed().subscribe((result: boolean) => {
				if (result) {
					this.dialogRef?.close();
					this.router.navigate([url]);
				}
			});
		}
		else {
			this.router.navigate([url]);
		}
	}

	setStateConsultationOdontology(isOpenOdontologyConsultation: boolean): void {
		this.isOpenOdontologyConsultation = isOpenOdontologyConsultation;
	}

	getSummaryCoverageInfo(): SummaryCoverageInformation {
		if (this.medicalCoverageInfo.summaryCoverageInfo) {
			return this.medicalCoverageInfo.summaryCoverageInfo;
		}
		let summaryInfo: SummaryCoverageInformation =
			this.medicalCoverageInfo.appointmentConfirmedCoverageInfo ?
				this.mapToSummaryCoverage(this.medicalCoverageInfo.appointmentConfirmedCoverageInfo) :
				this.mapToSummaryCoverage(this.medicalCoverageInfo.internmentEpisodeCoverageInfo);
		return summaryInfo;
	}

	private mapToSummaryCoverage(patientCoverage: ExternalPatientCoverageDto): SummaryCoverageInformation {
		if (!patientCoverage) {
			return null;
		}

		let summaryInfo: SummaryCoverageInformation = {};

		if (patientCoverage?.medicalCoverage?.name) {
			summaryInfo.name = patientCoverage.medicalCoverage.name;
		}

		if (patientCoverage?.affiliateNumber) {
			summaryInfo.affiliateNumber = patientCoverage.affiliateNumber;
		}

		if (patientCoverage.medicalCoverage?.plan) {
			summaryInfo.plan = patientCoverage.medicalCoverage.plan;
		}

		if (patientCoverage.condition) {
			summaryInfo.condition = (patientCoverage.condition === VOLUNTARY_ID) ? EPatientMedicalCoverageCondition.VOLUNTARIA : EPatientMedicalCoverageCondition.OBLIGATORIA;
		}

		if (patientCoverage.medicalCoverage?.type) {
			summaryInfo.type = this.getMedicalCoverageType(patientCoverage.medicalCoverage.type);
		}

		if (patientCoverage.medicalCoverage?.cuit) {
			summaryInfo.cuit = patientCoverage.medicalCoverage.cuit;
		}
		return summaryInfo;
	}

	getMedicalCoverageType(type: EMedicalCoverageTypeDto) {
		switch (type) {
			case EMedicalCoverageTypeDto.OBRASOCIAL: return EMedicalCoverageType.OBRASOCIAL;
			case EMedicalCoverageTypeDto.PREPAGA: return EMedicalCoverageType.PREPAGA;
			case EMedicalCoverageTypeDto.ART: return EMedicalCoverageType.ART;
		}
	}

	thereIsAppointmentCovarageInformation(): boolean {
		if (this.medicalCoverageInfo.summaryCoverageInfo || this.medicalCoverageInfo.appointmentConfirmedCoverageInfo)
			return true;
		return false;
	}

	embarazoAdolescente(){
		if (this._embarazoAdolescente === undefined) {
			if (this.patient.age >= 10 && this.patient.age < 19){
				this.hceGeneralStateService.getActiveProblems(this.patientId).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
					this.activeProblemsList = activeProblems.map(problem => ({id: problem.id, description: problem.snomed.pt, sctId: problem.snomed.sctid}));
					console.log(this.activeProblemsList);
				},
					(error) => {
						console.error(error);
					}
				);

				const sctIds = [
					"72892002",
					"237244005",
					"77386006",
					"95212006",
					"127368005",
					"127369002",
					"47200007",
					"166434005",
					"252160004",
					"252161000",
					"271903000",
					"146799005",
					"127366009",
					"135881001",
					"127370001",
					"127367000",
					"182947002",
					"169567006",
					"58532003",
					"307534009",
					"199158007",
					"151901000119101",
					"184005004",
					"250423000",
					"169565003",
					"428252001",
					"237233002",
					"691000221104",
					"74036000",
					"39406005",
					"110081000119109",
					"171000119107",
					"349971000221103",
					"169574001",
					"10761591000119105",
					"152231000119106",
					"305875008",
					"57797005",
					"710973002",
					"309737007",
					"15750121000119108",
					"386322007",
					"386456000",
					"702738006",
					"237627000",
					"10750111000119108",
					"386639001",
					"386394001",
					"237240001",
					"134781000119106",
					"199154009",
					"160401003",
					"14418008",
					"127373004",
					"702758007",
					"94641000119109",
					"169587007",
					"225327004",
					"83074005",
					"250426008",
					"235888006",
					"276367008",
					"169566002",
					"55543007",
					"82771000119102",
					"46101000119101",
					"191733007",
					"274116002",
					"65147003"
				]

				for (let activeProblem of this.activeProblemsList) {
					if (sctIds.includes(activeProblem.sctId)) {
						this._embarazoAdolescente = true;
						break;
					}
				}

				if (this._embarazoAdolescente === undefined) {
					this._embarazoAdolescente = false;
			  	}
				// for (let i = 0; i < this.activeProblemsList.length; i++){
				// 	if (this.activeProblemsList[i]['id'] == 79 || this.activeProblemsList[i]['id'] == 3369 || this.activeProblemsList[i]['id'] == 3368 || this.activeProblemsList[i]['id'] == 3367 || this.activeProblemsList[i]['id'] == 3279 || this.activeProblemsList[i]['id'] == 152 || this.activeProblemsList[i]['id'] == 309 || this.activeProblemsList[i]['id'] == 738 || this.activeProblemsList[i]['id'] == 1215 || this.activeProblemsList[i]['id'] == 1250 || this.activeProblemsList[i]['id'] == 1880 || this.activeProblemsList[i]['id'] == 2232 || this.activeProblemsList[i]['id'] == 2379 || this.activeProblemsList[i]['id'] == 2424 || this.activeProblemsList[i]['id'] == 2527 || this.activeProblemsList[i]['id'] == 2558 || this.activeProblemsList[i]['id'] == 2697 || this.activeProblemsList[i]['id'] == 3295 || this.activeProblemsList[i]['id'] == 3328 || this.activeProblemsList[i]['id'] == 3403 || this.activeProblemsList[i]['id'] == 3665 || this.activeProblemsList[i]['id'] == 3874 || this.activeProblemsList[i]['id'] == 3914 || this.activeProblemsList[i]['id'] == 3956 || this.activeProblemsList[i]['id'] == 4004 || this.activeProblemsList[i]['id'] == 4047 || this.activeProblemsList[i]['id'] == 4069 || this.activeProblemsList[i]['id'] == 4134 || this.activeProblemsList[i]['id'] == 4175 || this.activeProblemsList[i]['id'] == 4176 || this.activeProblemsList[i]['id'] == 4211 || this.activeProblemsList[i]['id'] == 4391 || this.activeProblemsList[i]['id'] == 3107 ||this.activeProblemsList[i]['id'] == 4410 || this.activeProblemsList[i]['id'] == 4479 || this.activeProblemsList[i]['id'] == 4488 || this.activeProblemsList[i]['id'] == 4648 || this.activeProblemsList[i]['id'] == 4665 || this.activeProblemsList[i]['id'] == 4826 || this.activeProblemsList[i]['id'] == 4932 || this.activeProblemsList[i]['id'] == 4989 || this.activeProblemsList[i]['id'] == 5010 || this.activeProblemsList[i]['id'] == 5011 || this.activeProblemsList[i]['id'] == 5066 || this.activeProblemsList[i]['id'] == 5127 || this.activeProblemsList[i]['id'] == 5245 || this.activeProblemsList[i]['id'] == 5246 || this.activeProblemsList[i]['id'] == 5313 || this.activeProblemsList[i]['id'] == 5316 || this.activeProblemsList[i]['id'] == 5362 || this.activeProblemsList[i]['id'] == 5369 || this.activeProblemsList[i]['id'] == 5378 || this.activeProblemsList[i]['id'] == 5480 || this.activeProblemsList[i]['id'] == 5522 || this.activeProblemsList[i]['id'] == 5596 || this.activeProblemsList[i]['id'] == 5974 || this.activeProblemsList[i]['id'] == 6050 || this.activeProblemsList[i]['id'] == 6322 || this.activeProblemsList[i]['id'] == 6445 || this.activeProblemsList[i]['id'] == 6484 || this.activeProblemsList[i]['id'] == 6529 || this.activeProblemsList[i]['id'] == 6659 || this.activeProblemsList[i]['id'] == 6700 || this.activeProblemsList[i]['id'] == 6790 || this.activeProblemsList[i]['id'] == 6804 || this.activeProblemsList[i]['id'] == 6861 || this.activeProblemsList[i]['id'] == 6895 || this.activeProblemsList[i]['id'] == 6909 || this.activeProblemsList[i]['id'] == 6979 || this.activeProblemsList[i]['id'] == 7382 || this.activeProblemsList[i]['id'] == 7462 || this.activeProblemsList[i]['id'] == 7559 || this.activeProblemsList[i]['id'] == 7638 || this.activeProblemsList[i]['id'] == 7780){
				// 		this._embarazoAdolescente = true;
				// 	}
				// }
			}
			else{
			   	this._embarazoAdolescente = false;
			}
		}
		return this._embarazoAdolescente;
	}

	openDialogAclaracion() {
		this.dialog.open(DialogoAclaracionComponent);
	}

	openDialogAclaracionMayorDe15() {
		this.dialog.open(DialogoMayorDe15Component);
	}

	openDialogInconsistencia() {
		this.dialog.open(DialogoInconsistenciaComponent);
	}

	pacienteMenorDe15(){
		const patientAge = this.patient.age;

		if (patientAge < 15){
			return true;
		}
		else{
			return false;
		}
	}

	openAppropriateDialog() {
		const patientAge = this.patient.age;
	  
		if (patientAge < 15) {
		  this.openDialogAclaracion();
		} else if (patientAge >= 10 && patientAge < 19) {
		  this.openDialogAclaracionMayorDe15();
		}
	}	  

	birth_control_pills(){
		if (this._anticonceptivos === undefined)
		{
			if (this.patient.age < 15){
				this.medicationRequestService.medicationRequestList(this.patient.id, null, null, null).subscribe((data: MedicationInfoDto[]) => {
					this.medicationList = data;
					console.log(this.medicationList);
				const idList = [
					"275811000",
					"425890004",
					"169554008",
					"169553002",
					"706507009",
					"326324002",
					"326425002",
					"333751000221106",
					"333891000221106",
					"414606006",
					"714594000",
					"268460000",
					"333651000221104",
					"326361006",
					"400419002",
					"356296007",
					"327311006",
					"375191001",
					"325533008",
					"318058009",
					"352501000221106",
					"326146004",
					"317288008",
					"1204114003",
					"785088001",
					"418556001",
					"156931000221100",
					"150841000221103",
					"150851000221101",
					"128191000221104",
					"128201000221101",
					"98671000221106",
					"98681000221109",
					"143341000221102",
					"143351000221100",
					"343101000221107",
					"345331000221104",
					"343111000221105",
					"343121000221100",
					"324311000221109",
					"348851000221108",
					"180221000221101",
					"180231000221103",
					"192961000221103",
					"192971000221107",
					"200041000221104",
					"200051000221102",
					"97241000221100",
					"97251000221103",
					"321801000221100",
					"145861000221105",
					"145871000221101",
					"144191000221108",
					"144201000221106",
					"100601000221105",
					"100611000221108",
					"190191000221106",
					"100601000221105",
					"100611000221108",
					"190191000221106",
					"146861000221103",
					"146871000221107",
					"193441000221103",
					"193451000221101",
					"193441000221103",
					"193451000221101",
					"435321000221107",
					"140151000221107",
					"118101000221100",
					"140161000221109",
					"118111000221102",
					"150121000221104",
					"150131000221101",
					"366681000221108",
					"253281000221103",
					"294151000221107",
					"260171000221109",
					"191021000221103",
					"191031000221100",
					"174321000221109",
					"174331000221107",
					"98221000221108",
					"99721000221105",
					"98231000221106",
					"95121000221107",
					"95131000221105",
					"142311000221105",
					"142321000221100",
					"427731000221104",
					"107351000221104",
					"343221000221107",
					"348941000221104",
					"399411000221105",
					"399401000221107",
					"341841000221100",
					"399391000221105",
					"399361000221100",
					"405041000221109",
					"428291000221106",
					"211861000221100",
					"211851000221102"
					];

				for (let medication of this.medicationList) {
					if (idList.includes(medication.snomed.sctid)) {
						this._anticonceptivos = true;
						break;
					}
				}

				if (this._anticonceptivos === undefined) {
          			this._anticonceptivos = false;
        		}
			});
		}
		else{
			this._anticonceptivos = false;
		}
		}
		return this._anticonceptivos;
	}

	inconsistenciaDeAlertas(){
		if (this.birth_control_pills() && this.embarazoAdolescente()){
			return true;
		}
	}
}
