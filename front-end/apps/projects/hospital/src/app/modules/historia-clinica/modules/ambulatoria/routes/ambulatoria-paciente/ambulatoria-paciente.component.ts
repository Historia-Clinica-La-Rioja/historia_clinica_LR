import { MedicalCoverageInfoService } from './../../services/medical-coverage-info.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, } from 'rxjs';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { AppFeature, EMedicalCoverageTypeDto, ERole, EPatientMedicalCoverageCondition } from '@api-rest/api-model';
import { EpicrisisSummaryDto, BasicPatientDto, OrganizationDto, PatientSummaryDto, PatientToMergeDto, PersonPhotoDto, InternmentEpisodeProcessDto, ExternalPatientCoverageDto, EmergencyCareEpisodeInProgressDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { InteroperabilityBusService } from '@api-rest/services/interoperability-bus.service';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { MapperService } from '@presentation/services/mapper.service';
import { UIPageDto } from '@extensions/extensions-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { MenuItem } from '@presentation/components/menu/menu.component';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { OdontogramService } from '@historia-clinica/modules/odontologia/services/odontogram.service';
import { FieldsToUpdate } from "@historia-clinica/modules/odontologia/components/odontology-consultation-dock-popup/odontology-consultation-dock-popup.component";
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';
import { MatDialog } from '@angular/material/dialog';
import { InternmentPatientService } from "@api-rest/services/internment-patient.service";
import { ContextService } from '@core/services/context.service';
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { EmergencyCareEpisodeSummaryService } from "@api-rest/services/emergency-care-episode-summary.service";
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { PatientAllergiesService } from '../../services/patient-allergies.service';
import { SummaryCoverageInformation } from '../../components/medical-coverage-summary-view/medical-coverage-summary-view.component';
import { EMedicalCoverageType } from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { InternmentActionsService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-actions.service";
import { WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { PatientType } from '@historia-clinica/constants/summaries';
import { ComponentCanDeactivate } from '@core/guards/PendingChangesGuard';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { PatientToMergeService } from '@api-rest/services/patient-to-merge.service';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';
import { PatientValidatorPopupComponent } from '../../dialogs/patient-validator-popup/patient-validator-popup.component';
import { PATIENT_TYPE } from '@core/utils/patient.utils';
import { WCParams } from '@extensions/components/ui-external-component/ui-external-component.component';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';
import { HomeRoutes } from 'projects/hospital/src/app/modules/home/constants/menu';

const RESUMEN_INDEX = 0;
const VOLUNTARY_ID = 1;
const FEMENINO = 'Femenino';
const EMERGENCY_CARE_INDEX = 0;
const EMERGENCY_CARE_INDEX_WHEN_INTERNED = 1;
const TAB_INDICACIONES = 1;

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss'],

})
export class AmbulatoriaPacienteComponent implements OnInit, OnDestroy, ComponentCanDeactivate {

	dialogRef: DockPopupRef;
	patient: PatientBasicData;
	patientId: number;
	personId: number;
	extensionTabs$: Observable<{ head: MenuItem, body$: Observable<UIPageDto> }[]>;
	extensionWCTabs$: Observable<WCParams[]>;
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
	hasViolenceRole = false;
	showNursingSection = false;
	femenino = FEMENINO;
	selectedTab = 0;
	previousSelectedTab = 0;
	isHabilitarRecetaDigitalEnabled: boolean = false;
	emergencyCareTabIndex: number;
	showEmergencyCareTab: boolean;
	hasEpisodeToShow: boolean;
	hasPrescriptorRole = false;
	canOnlyViewSelfAddedProblems = false;
	rolesThatCanOnlyViewSelfAddedProblems = [ERole.PRESCRIPTOR];
	isNewConsultationOpen: boolean;
	isNewViolenceSituationOpen= false;
	isEmergencyCareTemporalPatient = false;
	patientType: number;
	isTemporaryPatient: boolean = false;
	showCardTabs: boolean = false;

	emergencyCareEpisode: ResponseEmergencyCareDto;
	emergencyCareEpisodeState: EstadosEpisodio;
	EstadosEpisodio = EstadosEpisodio;
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
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly patientToMergeService: PatientToMergeService,
		private readonly violenceReportFacadeService: ViolenceReportFacadeService
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
						this.personInformation.push({ description: patient.person?.identificationType, data: patient.person?.identificationNumber });
						this.patient = this.mapperService.toPatientBasicData(patient);
						this.personId = patient.person?.id;
						this.patientType = patient.typeId;
						this.isEmergencyCareTemporalPatient = patient.typeId === PatientType.EMERGENCY_CARE_TEMPORARY;

						if (this.isHabilitarRecetaDigitalEnabled && this.patientType === PATIENT_TYPE.TEMPORARY)
								this.isTemporaryPatient = true;
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

						this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgressInTheInstitution(this.patientId).subscribe(
							emergencyCareEpisodeInProgressDto => {

								if (emergencyCareEpisodeInProgressDto.id) {

									this.emergencyCareEpisodeService.getAdministrative(emergencyCareEpisodeInProgressDto.id).subscribe(ec => {
										this.emergencyCareEpisode = ec;
									});
								}

								this.emergencyCareEpisodeInProgress = emergencyCareEpisodeInProgressDto;
								if (emergencyCareEpisodeInProgressDto?.id) {
									this.emergencyCareEpisodeStateService.getState(emergencyCareEpisodeInProgressDto.id).subscribe(
										state => {
											const episodeState = state.id;
											this.emergencyCareEpisodeState = state.id;
											const emergencyEpisodeWithAdminDischarge = (EstadosEpisodio.CON_ALTA_ADMINISTRATIVA === episodeState);
											this.hasEpisodeToShow = (this.emergencyCareEpisodeInProgress?.inProgress && !emergencyEpisodeWithAdminDischarge);
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
		this.ambulatoriaSummaryFacadeService.isNewConsultationOpen$.subscribe((event: boolean) => {
			this.isNewConsultationOpen = event;
		});
		this.violenceReportFacadeService.isNewViolenceSituation$.subscribe((event: boolean) => {
			this.isNewViolenceSituationOpen = event;
		});
	}

	canDeactivate(): Observable<boolean> | boolean {
		return this.isNewConsultationOpen || this.isOpenOdontologyConsultation || this.odontogramService.existActionedTeeth() || this.isNewViolenceSituationOpen;
	};

	openValidatorDialog() {
		this.dialog.open(PatientValidatorPopupComponent, {
			data: {
				patientType: this.patientType,
				patientId: this.patientId
			},
			width: '35%',
		});
	}

	ngOnInit(): void {
		this.setActionsLayout();

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUS_INTEROPERABILIDAD)
			.subscribe(isOn => this.externalInstitutionsEnabled = isOn);

		this.featureFlagService.isActive(AppFeature.HABILITAR_ODONTOLOGY)
			.subscribe(isOn => this.odontologyEnabled = isOn);

		this.extensionWCTabs$ = this.wcExtensionsService.getClinicHistoryComponents(this.patientId);

		this.odontogramService.resetOdontogram();

		this.medicamentStatus$ = this.requestMasterDataService.medicationStatus();

		this.diagnosticReportsStatus$ = this.requestMasterDataService.diagnosticReportStatus();

		this.studyCategories$ = this.requestMasterDataService.categories();

		this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(false);
	}

	patientSelected(patient: Patient) {

		const patientToMergeDto: PatientToMergeDto = {
			activePatientId: patient.basicData.id,
			oldPatientsIds: [this.patient.id],
			registrationDataPerson: {
				middleNames: patient.basicData.middleName,
				otherLastNames: patient.basicData.person.otherLastNames,
				birthDate: patient.basicData.person.birthDate,
				firstName: patient.basicData.firstName,
				genderId: patient.basicData.person.gender.id,
				identificationNumber: patient.basicData.identificationNumber,
				identificationTypeId: patient.basicData.person.identificationTypeId,
				lastName: patient.basicData.person.lastName,
				nameSelfDetermination: patient.basicData.person.nameSelfDetermination,
				phoneNumber: null,
				phonePrefix: null
			}
		}

		this.patientToMergeService.merge(patientToMergeDto).subscribe(
			_ => {
				this.snackBarService.showSuccess('Se ha asignado correctamente el paciente');
				const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/ambulatoria/paciente/${patient.basicData.id}`;
				this.router.navigate([url])
			},
			error => this.snackBarService.showError(error.text || 'No cuenta con los roles suficientes para realizar esta accion'),
		)

	}

	ngOnDestroy() {
		this.medicalCoverageInfo.clearAll();
	}

	private setPermissions(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.canOnlyViewSelfAddedProblems = anyMatch<ERole>(userRoles, this.rolesThatCanOnlyViewSelfAddedProblems);
		});
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
				personalHistories: false,
				familyHistories: false,
				patientProblems: false,
				patientProblemsByRole: true,
				riskFactors: false,
				medications: true,
				anthropometricData: false,
				problems: false
			});
		}
		if (this.isTemporaryPatient && !this.emergencyCareEpisodeInProgress.inProgress && !this.hasInternmentEpisodeInThisInstitution) {
			if (event.index == TAB_INDICACIONES) {
				this.openValidatorDialog();
				this.selectedTab = this.previousSelectedTab;
			}
			this.previousSelectedTab = this.selectedTab;
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
			this.hasOdontologyRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_EN_ODONTOLOGIA]);
			this.hasEmergencyCareRelatedRole = (this.hasMedicalRole || this.hasNurseRole || this.hasHealthProfessionalRole || this.hasOdontologyRole);
			this.hasHealthRelatedRole = anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO_ADULTO_MAYOR]);
			this.hasPicturesStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_IMAGENES]);
			this.hasLaboratoryStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_LABORATORIO]);
			this.hasPharmacyStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_FARMACIA]);
			this.hasPrescriptorRole = anyMatch<ERole>(userRoles, [ERole.PRESCRIPTOR]);
			this.hasViolenceRole = anyMatch<ERole>(userRoles, [ERole.ABORDAJE_VIOLENCIAS]);

			if (this.hasHealthRelatedRole || this.hasPrescriptorRole || this.hasEmergencyCareRelatedRole || 
				this.hasPharmacyStaffRole || this.hasPicturesStaffRole || this.hasLaboratoryStaffRole)	{
					this.showCardTabs = true;
			}
			this.showNursingTab(userRoles);
		});
	}

	goToPatient(): void {
		const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/ambulatoria/${AppRoutes.PortalPaciente}/${this.patientId}/${HomeRoutes.Profile}`;
		this.router.navigate([url]);
	}

	setStateConsultationOdontology(isOpenOdontologyConsultation: boolean): void {
		this.isOpenOdontologyConsultation = isOpenOdontologyConsultation;
	}

	setStateNewViolenceSituationOpen(isViolenceSituationOpen: boolean) {
		this.isNewViolenceSituationOpen = isViolenceSituationOpen;
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

	private showNursingTab(userRoles: ERole[]) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_ENF_EN_DESARROLLO)
			.subscribe(show => {
				if (anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PERSONAL_DE_FARMACIA])
					&& show) 
					this.showNursingSection = show
			});
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

	goToEmergencyCareEpisode(episodeId: number) {
		if (this.emergencyCareEpisodeInProgress.id === episodeId) {
			this.selectedTab = 0;
		} else {
			this.router.navigate([`${AppRoutes.Institucion}/${this.contextService.institutionId}/ambulatoria/episodio/${episodeId}`])
		}
	}

}
