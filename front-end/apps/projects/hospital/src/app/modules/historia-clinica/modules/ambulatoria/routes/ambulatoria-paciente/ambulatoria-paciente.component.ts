import { Component, ComponentFactoryResolver, OnInit, ViewContainerRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { AppFeature, ERole } from '@api-rest/api-model';
import { EvaluationNoteSummaryDto, AnamnesisSummaryDto, EpicrisisSummaryDto, BasicPatientDto, OrganizationDto, PatientSummaryDto, PersonPhotoDto, HCEAnthropometricDataDto, InternmentEpisodeProcessDto, ExternalPatientCoverageDto, EmergencyCareEpisodeInProgressDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { InteroperabilityBusService } from '@api-rest/services/interoperability-bus.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { MapperService } from '@presentation/services/mapper.service';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { UIPageDto } from '@extensions/extensions-model';

import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { NuevaConsultaDockPopupEnfermeriaComponent } from '../../dialogs/nueva-consulta-dock-popup-enfermeria/nueva-consulta-dock-popup-enfermeria.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { MedicacionesService } from '../../services/medicaciones.service';
import { MenuItem } from '@presentation/components/menu/menu.component';
import { ExtensionPatientService } from '@extensions/services/extension-patient.service';
import { AdditionalInfo } from '@pacientes/pacientes.model';
import { OdontogramService } from '@historia-clinica/modules/odontologia/services/odontogram.service';
import { FieldsToUpdate } from "@historia-clinica/modules/odontologia/components/odontology-consultation-dock-popup/odontology-consultation-dock-popup.component";
import { anyMatch, pushIfNotExists } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';
import { ReferenceService } from '@api-rest/services/reference.service';
import { MatDialog } from '@angular/material/dialog';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ReferenceNotificationInfo, ReferenceNotificationService } from '@historia-clinica/services/reference-notification.service';
import { REFERENCE_CONSULTATION_TYPE } from '../../constants/reference-masterdata';
import { InternmentPatientService } from "@api-rest/services/internment-patient.service";
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";
import { ContextService } from '@core/services/context.service';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { AppRoutes } from 'projects/hospital/src/app/app-routing.module';
import { HomeRoutes } from 'projects/hospital/src/app/modules/home/home-routing.module';
import { EmergencyCareEpisodeSummaryService } from "@api-rest/services/emergency-care-episode-summary.service";
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { InternacionPacienteComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/routes/internacion-paciente/internacion-paciente.component";
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { PatientAllergiesService } from '../../services/patient-allergies.service';

const RESUMEN_INDEX = 0;

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss'],
	providers: [HistoricalProblemsFacadeService, AmbulatoriaSummaryFacadeService]

})
export class AmbulatoriaPacienteComponent implements OnInit {

	dialogRef: DockPopupRef;
	patient: PatientBasicData;
	patientId: number;
	extensionTabs$: Observable<{ head: MenuItem, body$: Observable<UIPageDto> }[]>;
	medicamentStatus$: Observable<any>;
	studyCategories$: Observable<any>;
	diagnosticReportsStatus$: Observable<any>;
	public personInformation: AdditionalInfo[];
	public personPhoto: PersonPhotoDto;
	public hasNewConsultationEnabled$: Observable<boolean>;
	public showOrders: boolean;
	public externalInstitutionsEnabled: boolean;
	odontologyEnabled: boolean;
	public externalInstitutions: OrganizationDto[];
	public patientExternalSummary: PatientSummaryDto;
	public externalInstitutionPlaceholder = 'Ninguna';
	public loaded = false;
	public spinner = false;
	private timeOut = 15000;
	public CurrentUserIsAllowedToMakeBothQueries = false;
	referenceNotificationService: ReferenceNotificationService;
	refNotificationInfo: ReferenceNotificationInfo;
	bloodType: string;
	internmentEpisodeProcess: InternmentEpisodeProcessDto;
	internmentEpisodeCoverageInfo: ExternalPatientCoverageDto;
	private isOpenOdontologyConsultation = false;
	emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;
	hasInternmentEpisodeInThisInstitution = undefined;
	anamnesisDoc: AnamnesisSummaryDto;
	epicrisisDoc: EpicrisisSummaryDto;
	lastEvolutionNoteDoc: EvaluationNoteSummaryDto;
	hasMedicalDischarge: boolean;
	currentUserIsAllowedToDoAConsultation = false;
	hasMedicalRole = false;
	internmentAction: InternmentActions;
	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly dockPopupService: DockPopupService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly interoperabilityBusService: InteroperabilityBusService,
		private readonly snackBarService: SnackBarService,
		private readonly medicacionesService: MedicacionesService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly extensionPatientService: ExtensionPatientService,
		private readonly odontogramService: OdontogramService,
		private readonly permissionsService: PermissionsService,
		private readonly referenceService: ReferenceService,
		private readonly dialog: MatDialog,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly contextService: ContextService,
		private readonly router: Router,
		private readonly emergencyCareEpisodeSummaryService: EmergencyCareEpisodeSummaryService,
		private readonly componentFactoryResolver: ComponentFactoryResolver,
		private viewContainerRef: ViewContainerRef,
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		readonly patientAllergies: PatientAllergiesService,

		private readonly requestMasterDataService: RequestMasterDataService,

	) {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).subscribe(
					patient => {
						this.personInformation.push({ description: patient.person.identificationType, data: patient.person.identificationNumber });
						this.patient = this.mapperService.toPatientBasicData(patient);
					}
				);
				this.ambulatoriaSummaryFacadeService.setIdPaciente(this.patientId);
				this.patientAllergies.updateCriticalAllergies(this.patientId);
				this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;
				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
				this.refNotificationInfo = {
					patientId: this.patientId,
					consultationType: REFERENCE_CONSULTATION_TYPE.AMBULATORY
				}
				this.referenceNotificationService = new ReferenceNotificationService(this.refNotificationInfo, this.referenceService, this.dialog, this.clinicalSpecialtyService, this.medicacionesService, this.ambulatoriaSummaryFacadeService, this.dockPopupService);
				this.ambulatoriaSummaryFacadeService.anthropometricData$.subscribe(
					(data: HCEAnthropometricDataDto) => this.bloodType = data?.bloodType?.value
				);

				this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId).subscribe(
					(internmentEpisodeProcess: InternmentEpisodeProcessDto) => {
						this.internmentEpisodeProcess = internmentEpisodeProcess
						if (this.internmentEpisodeProcess.id && this.internmentEpisodeProcess.inProgress) {
							this.hceGeneralStateService.getInternmentEpisodeMedicalCoverage(this.patientId, this.internmentEpisodeProcess.id).subscribe(
								(data: ExternalPatientCoverageDto) => this.internmentEpisodeCoverageInfo = data);
							this.internmentSummaryFacadeService.setInternmentEpisodeInformation(internmentEpisodeProcess.id, false);
							if (this.internmentEpisodeProcess.inProgress) {
								this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
								this.internmentSummaryFacadeService.unifyFamilyHistories(this.patientId);
							}
							this.internmentSummaryFacadeService.anamnesis$.subscribe(a => this.anamnesisDoc = a);
							this.internmentSummaryFacadeService.epicrisis$.subscribe(e => this.epicrisisDoc = e);
							this.internmentSummaryFacadeService.evolutionNote$.subscribe(evolutionNote => this.lastEvolutionNoteDoc = evolutionNote);
							this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(h => this.hasMedicalDischarge = h);
							this.internmentSummaryFacadeService.anthropometricData$.subscribe(
								(data: HCEAnthropometricDataDto) => {
									if (data?.bloodType?.value)
										this.bloodType = data?.bloodType?.value
								});
						}
						this.hasInternmentEpisodeInThisInstitution = internmentEpisodeProcess.inProgress && !!internmentEpisodeProcess.id;
					})

				this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgress(this.patientId)
					.subscribe(emergencyCareEpisodeInProgressDto => this.emergencyCareEpisodeInProgress = emergencyCareEpisodeInProgressDto);
			});
	}

	ngOnInit(): void {
		this.setActionsLayout();
		this.personInformation = [];

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUS_INTEROPERABILIDAD)
			.subscribe(isOn => this.externalInstitutionsEnabled = isOn);

		this.featureFlagService.isActive(AppFeature.HABILITAR_ODONTOLOGY)
			.subscribe(isOn => this.odontologyEnabled = isOn);

		this.extensionTabs$ = this.extensionPatientService.getTabs(this.patientId);

		this.odontogramService.resetOdontogram();

		this.referenceNotificationService.getOpenConsultation().subscribe(type => {
			if (type === REFERENCE_CONSULTATION_TYPE.AMBULATORY) {
				this.openNuevaConsulta();
			}
		})

		this.medicamentStatus$ = this.requestMasterDataService.medicationStatus();

		this.diagnosticReportsStatus$ = this.requestMasterDataService.diagnosticReportStatus();

		this.studyCategories$ = this.requestMasterDataService.categories();

	}

	loadExternalInstitutions() {
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

	loadExternalSummary(organization: OrganizationDto) {
		this.spinner = true;

		const info = this.interoperabilityBusService.getPatientInfo(this.patientId.toString(), organization.custodian)
			.subscribe(summary => {
				this.patientExternalSummary = summary;
				this.spinner = false;
			});

		this.showTimeOutMessages(info);
	}

	externalInstitutionsClicked() {
		if (!this.loaded) {
			this.loaded = true;
			this.loadExternalInstitutions();
			this.externalInstitutionPlaceholder = ' ';
		}
	}

	showTimeOutMessages(subscription) {
		setTimeout(() => {
			if (this.spinner) {
				subscription.unsubscribe();
				this.snackBarService.showError('ambulatoria.bus-interoperabilidad.TIMEOUT-MESSAGE');
				this.spinner = false;
			}
		}, this.timeOut);
	}

	openNuevaConsulta(): void {
		if (!this.dialogRef) {
			this.patientId = Number(this.route.snapshot.paramMap.get('idPaciente'));
			this.dialogRef = this.dockPopupService.open(NuevaConsultaDockPopupComponent, { idPaciente: this.patientId });
			this.dialogRef.afterClosed().subscribe(fieldsToUpdate => {
				delete this.dialogRef;
				this.medicacionesService.updateMedication();
				if (fieldsToUpdate) {
					this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
				if (this.internmentEpisodeProcess?.inProgress) {
					if (fieldsToUpdate?.allergies)
						this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
					if (fieldsToUpdate?.familyHistories)
						this.internmentSummaryFacadeService.unifyFamilyHistories(this.patientId);
				}

				this.patientAllergies.updateCriticalAllergies(this.patientId);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openNuevaConsultaEnfermeria(): void {
		if (!this.dialogRef) {
			this.patientId = Number(this.route.snapshot.paramMap.get('idPaciente'));
			this.dialogRef = this.dockPopupService.open(NuevaConsultaDockPopupEnfermeriaComponent, { idPaciente: this.patientId });
			this.dialogRef.afterClosed().subscribe(fieldsToUpdate => {
				delete this.dialogRef;
				this.medicacionesService.updateMedication();
				if (fieldsToUpdate) {
					this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
				this.patientAllergies.updateCriticalAllergies(this.patientId);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}

	}

	onTabChanged(event: MatTabChangeEvent) {
		// TODO Utilizar este método para actualizar componentes asociados a Tabs

		if (event.index == RESUMEN_INDEX) {
			this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({
				allergies: false,
				familyHistories: false,
				personalHistories: false,
				riskFactors: false,
				medications: true,
				anthropometricData: false,
				problems: false
			});
		}
	}

	updateFields(fieldsToUpdate: FieldsToUpdate) {
		this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
	}

	setActionsLayout() {
		this.CurrentUserIsAllowedToMakeBothQueries = false
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.CurrentUserIsAllowedToMakeBothQueries = (anyMatch<ERole>(userRoles, [ERole.ENFERMERO]) &&
				(anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO])))
			this.currentUserIsAllowedToDoAConsultation = (anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO]));
			this.hasMedicalRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO]);
		});
	}

	goToPatient() {
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

	openInternmentAction(internmentActionId: number) {
		const component = this.componentFactoryResolver.resolveComponentFactory(InternacionPacienteComponent);
		const internmentComponent = this.viewContainerRef.createComponent(component);
		this.viewContainerRef.clear();
		internmentComponent.instance.internmentEpisodeId = this.internmentEpisodeProcess.id;
		internmentComponent.instance.patientId = this.patientId;

		if (InternmentActions.anamnesis === internmentActionId) {
			internmentComponent.instance.openAnamnesis();
			return;
		}

		if (InternmentActions.evolutionNote === internmentActionId) {
			internmentComponent.instance.openEvolutionNote();
			return;
		}

		if (InternmentActions.epicrisis === internmentActionId) {
			internmentComponent.instance.openEpicrisis();
			return;
		}

		if (InternmentActions.medicalDischarge === internmentActionId) {
			internmentComponent.instance.openMedicalDischarge();
		}
	}
}

export enum InternmentActions {
	anamnesis, evolutionNote, epicrisis, medicalDischarge
}
