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

const RESUMEN_INDEX = 0;
const VOLUNTARY_ID = 1;

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss'],

})
export class AmbulatoriaPacienteComponent implements OnInit, OnDestroy {

	dialogRef: DockPopupRef;
	patient: PatientBasicData;
	patientId: number;
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
	showNursingSection = false;

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
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		readonly patientAllergies: PatientAllergiesService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly internmentActionsService: InternmentActionsService,
		private readonly medicalCoverageInfo: MedicalCoverageInfoService,
		private readonly wcExtensionsService: WCExtensionsService,
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
					})

				this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgress(this.patientId)
					.subscribe(emergencyCareEpisodeInProgressDto => this.emergencyCareEpisodeInProgress = emergencyCareEpisodeInProgressDto);
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
			this.hasOdontologyRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_EN_ODONTOLOGIA]);
			this.hasHealthRelatedRole = anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO_ADULTO_MAYOR]);
			this.hasPicturesStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_IMAGENES]);
			this.hasLaboratoryStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_LABORATORIO]);
			this.hasPharmacyStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_FARMACIA]);
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

}
