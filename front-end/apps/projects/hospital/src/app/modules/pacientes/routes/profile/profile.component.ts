import { Component, OnInit } from '@angular/core';
import {
	PersonalInformationDto,
	CompletePatientDto,
	PatientMedicalCoverageDto,
	PersonPhotoDto,
	ProfessionalDto,
	ClinicalSpecialtyDto,
	UserDataDto,
	UserRoleDto,
	RoleDto,
	InternmentSummaryDto,
	PatientDischargeDto,
	InstitutionDto,
	EmergencyCareEpisodeInProgressDto,
	HealthcareProfessionalCompleteDto,
	ProfessionalProfessionsDto,
	ProfessionalSpecialtyDto,
	ProfessionalLicenseNumberDto
} from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '../../../presentation/services/mapper.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PersonService } from '@api-rest/services/person.service';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { ContextService } from '@core/services/context.service';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { MatDialog } from "@angular/material/dialog";
import { ReportsComponent } from "@pacientes/dialogs/reports/reports.component";
import { patientCompleteName } from '@core/utils/patient.utils';
import { UserService } from "@api-rest/services/user.service";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { UntypedFormBuilder, UntypedFormGroup } from "@angular/forms";
import { processErrors } from "@core/utils/form.utils";
import { PermissionsService } from "@core/services/permissions.service";
import { UserPasswordResetService } from "@api-rest/services/user-password-reset.service";
import { ProfessionalService } from '@api-rest/services/professional.service';
import { SpecialtyService } from '@api-rest/services/specialty.service';
import { EditRolesComponent } from '@pacientes/dialogs/edit-roles/edit-roles.component';
import { RolesService } from '@api-rest/services/roles.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { INTERNACION } from "@historia-clinica/constants/summaries";
import { InternacionService } from "@api-rest/services/internacion.service";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { EstadosEpisodio, Triages } from "@historia-clinica/modules/guardia/constants/masterdata";
import { EmergencyCareEpisodeSummaryService } from "@api-rest/services/emergency-care-episode-summary.service";
import { AppRoutes } from "../../../../app-routing.module";
import { InternmentDocuments, InternmentEpisodeSummary } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component";
import { EditPrefessionsSpecialtiesComponent } from '@pacientes/dialogs/edit-prefessions-specialties/edit-prefessions-specialties.component';
import { combineLatest, Observable } from 'rxjs';
import { EditLicenseComponent } from '@pacientes/dialogs/edit-license/edit-license.component';
import { ProfessionalLicenseService } from '@api-rest/services/professional-license.service';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { AuditablePatientInfo } from '../edit-patient/edit-patient.component';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { WCParams } from '@extensions/components/ui-external-component/ui-external-component.component';

const ROUTE_NEW_INTERNMENT = 'internaciones/internacion/new';
const ROUTE_EDIT_PATIENT = 'pacientes/edit';
const ROLES_TO_VIEW_USER_DATA: ERole[] = [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR];
const ROLES_TO_VIEW_CLINIC_HISTORY_DATA: ERole[] = [ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES];
const ROLES_TO_ASSIGN_PERSCRIPTOR_ROLE: ERole[] = [ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR];
const ROLES_THAT_CAN_ASSIGN_ANY_ROLE_BUT_PRESCRIPTOR: ERole[] = [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE];
const DIALOG_SIZE = '30%';
const DIALOG_SIZE_HEIGHT = '80%';

export interface ProfessionAndSpecialtyDto {
	professionDescription: string,
	professionId: number,
	specialtyId: number,
	specialtyName: string,
}

export interface ProfessionalSpecialties {
	profession: ProfessionalSpecialtyDto,
	specialties: ClinicalSpecialtyDto[],
}

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
	userRoles: string[] = [];
	roles: RoleDto[] = [];
	assignableRoles: RoleDto[] = [];
	hasRoleToViewUserData = false;
	userId: number = null;
	rolesByUser: UserRoleDto[] = [];
	patientId: number;
	showDischarge = false;
	internmentDocuments: InternmentDocuments;
	canLoadProbableDischargeDate: boolean;
	allProfessions: ProfessionalSpecialtyDto[] = [];
	allSpecialties: ClinicalSpecialtyDto[] = [];
	ownProfessionsAndSpecialties: ProfessionalProfessionsDto[] = [];
	ownProfessionsAndSpecialties$: Observable<ProfessionalProfessionsDto[]>;
	professionsWithLicense$: Observable<ProfessionalLicenseNumberDto[]>;
	isProfessional = false;
	institutionName: string;
	license: string = '';
	hasPhysicalDischarge = false;
	private healthcareProfessionalId: number;
	private institution: number[] = [];
	private rolesAdmin = false;
	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public personPhoto: PersonPhotoDto;
	public codigoColor: string;
	public internacionSummary = INTERNACION;
	public internmentEpisodeSummary: InternmentEpisodeSummary;
	private readonly routePrefix;
	private clinicalHistoryRoute;
	public internmentEpisode;
	public userData: UserDataDto;
	public personId: number;
	private professionalId: number;
	private professionalSpecialtyId: number[] = [];
	public form: UntypedFormGroup;

	public downloadReportIsEnabled: boolean;
	public createUsersIsEnable: boolean;
	hideProfessions: boolean;

	emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;
	episodeId: number;
	showEmergencyCareSummary = false;
	readonly triages = Triages;
	readonly episodeStates = EstadosEpisodio;

	public auditablePatientInfo: AuditablePatientInfo;
	private auditableFullDate: Date;

	extensions$: Observable<WCParams[]>;

	isHabilitarInternacionOn = false;

	constructor(
		private patientService: PatientService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private personService: PersonService,
		private contextService: ContextService,
		private userPasswordResetService: UserPasswordResetService,
		private internmentPatientService: InternmentPatientService,
		private internmentEpisodeService: InternmentEpisodeService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly featureFlagService: FeatureFlagService,
		public dialog: MatDialog,
		private readonly userService: UserService,
		private readonly snackBarService: SnackBarService,
		private readonly professionalService: ProfessionalService,
		private readonly professionalLicenseService: ProfessionalLicenseService,
		private readonly specialtyService: SpecialtyService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly rolesService: RolesService,
		private readonly institutionService: InstitutionService,
		private readonly permissionService: PermissionsService,
		private readonly internmentService: InternacionService,
		private readonly emergencyCareEpisodeSummaryService: EmergencyCareEpisodeSummaryService,
		private readonly dateFormatPipe: DateFormatPipe,
		private readonly wcExtensionsService: WCExtensionsService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
		this.featureFlagService.isActive(AppFeature.HABILITAR_INFORMES).subscribe(isOn => this.downloadReportIsEnabled = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_CREACION_USUARIOS).subscribe(isOn => this.createUsersIsEnable = isOn);
		this.featureFlagService.isActive(AppFeature.OCULTAR_LISTADO_PROFESIONES_WEBAPP).subscribe(isOn => this.hideProfessions = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_INTERNACION).subscribe(isOn => this.isHabilitarInternacionOn = isOn);
	}

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('id'));
				this.clinicalHistoryRoute = 'institucion/' + this.contextService.institutionId + '/ambulatoria/paciente/' + this.patientId;
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
					.subscribe(completeData => {
						if (completeData?.auditablePatientInfo) {
							this.auditableFullDate = dateTimeDtotoLocalDate(
								{
									date: completeData.auditablePatientInfo.createdOn.date,
									time: completeData.auditablePatientInfo.createdOn.time
								}
							);
							this.auditablePatientInfo = {
								message: completeData.auditablePatientInfo.message,
								createdBy: completeData.auditablePatientInfo.createdBy,
								createdOn: this.dateFormatPipe.transform(this.auditableFullDate, 'datetime'),
								institutionName: completeData.auditablePatientInfo.institutionName
							};
						}
						this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
						this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
						this.personId = completeData.person.id;
						this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
							.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);
						if (this.personId)
							this.personService.getPersonalInformation<PersonalInformationDto>(this.personId)
								.subscribe(personInformationData => {
									this.personalInformation = this.mapperService.toPersonalInformationData(completeData, personInformationData);
								});
						this.permissionService.hasContextAssignments$(ROLES_TO_VIEW_USER_DATA).subscribe(hasRoleToViewUserData => {
							if (hasRoleToViewUserData) {
								this.hasRoleToViewUserData = true;
								this.checkIfProfessional();
								if (this.createUsersIsEnable) {
									this.userService.getUserData(this.personId)
										.subscribe(userDataDto => {
											this.userData = userDataDto
											this.form.controls.enable.setValue(this.userData.enable);
										});
								}
								this.userService.getUserData(completeData.person.id).subscribe((userData: UserDataDto) => {
									this.userData = userData;
									if (userData?.id) {
										this.rolesService.getRolesByUser(this.userData?.id).subscribe((roles: UserRoleDto[]) => {
											this.rolesByUser = roles;
											roles.forEach(e => this.userRoles.push(e.roleDescription));
										})
										this.rolesService.hasBackofficeRole(userData?.id).subscribe((hasRolesAdmin: boolean) => {
											this.rolesAdmin = hasRolesAdmin;
										});
									}
								});
								this.rolesService.getAllInstitutionalRoles().subscribe((roles: RoleDto[]) => {
									this.roles = roles;
									combineLatest([this.permissionService.hasContextAssignments$(ROLES_TO_ASSIGN_PERSCRIPTOR_ROLE),
									this.permissionService.hasContextAssignments$(ROLES_THAT_CAN_ASSIGN_ANY_ROLE_BUT_PRESCRIPTOR)])
										.subscribe(([hasRoleToAssignPrescriptorRole, hasRoleThatCanAssignAnyRoleButPrescriptor]) => {
											if (!hasRoleToAssignPrescriptorRole)
												this.assignableRoles = this.roles.filter(role => role.id !== 21);
											if (hasRoleThatCanAssignAnyRoleButPrescriptor && hasRoleToAssignPrescriptorRole)
												this.assignableRoles = this.roles;
											if (!hasRoleThatCanAssignAnyRoleButPrescriptor && hasRoleToAssignPrescriptorRole)
												this.assignableRoles = this.roles.filter(role => role.id === 21);
										});
								});

								this.institution.push(this.contextService.institutionId);
								this.institutionService.getInstitutions(this.institution).subscribe((institutions: InstitutionDto[]) => {
									this.institutionName = institutions[0].name;
								});

								this.professionalService.getList().subscribe((allProfessions: ProfessionalSpecialtyDto[]) => {
									this.allProfessions = allProfessions;
								});

								this.specialtyService.getAll().subscribe((allSpecialties: ClinicalSpecialtyDto[]) => {
									this.allSpecialties = allSpecialties;
								});
							}
						})

					});
				this.featureFlagService.isActive(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA).subscribe(isOn => {
					this.canLoadProbableDischargeDate = isOn;
				});
				this.permissionService.hasContextAssignments$(ROLES_TO_VIEW_CLINIC_HISTORY_DATA).subscribe(hasRoleToViewClinicHistoryData => {
					if (hasRoleToViewClinicHistoryData) {
						this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId)
							.subscribe(internmentEpisodeProcessDto => {
								if (internmentEpisodeProcessDto) {
									this.internmentEpisode = internmentEpisodeProcessDto;
									if (internmentEpisodeProcessDto.id) {
										this.internmentService.getInternmentEpisodeSummary(internmentEpisodeProcessDto.id)
											.subscribe((internmentEpisode: InternmentSummaryDto) => {
												this.internmentEpisodeSummary = this.mapperService.toInternmentEpisodeSummary(internmentEpisode)
												this.internmentDocuments = {
													hasAnamnesis: !!internmentEpisode.documents?.anamnesis,
													hasEpicrisis: !!internmentEpisode.documents?.epicrisis,
												}
											});
										this.internmentEpisodeService.getPatientDischarge(internmentEpisodeProcessDto.id)
											.subscribe((patientDischarge: PatientDischargeDto) => {
												this.featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS).subscribe(isOn => {
													this.showDischarge = !!(isOn || (patientDischarge.medicalDischargeDate));
												});
												this.hasPhysicalDischarge = !!patientDischarge.physicalDischargeDate;
											});
									}
								}
							});


						this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_GUARDIA).subscribe(isOn => {
							this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgressInTheInstitution(this.patientId)
								.subscribe(emergencyCareEpisodeInProgressDto => {
									this.emergencyCareEpisodeInProgress = emergencyCareEpisodeInProgressDto;
									this.episodeId = this.emergencyCareEpisodeInProgress?.id;
									this.showEmergencyCareSummary = isOn && emergencyCareEpisodeInProgressDto?.inProgress;
								});
						});
					}
				})
				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => {
						this.personPhoto = personPhotoDto;
					});

				this.extensions$ = this.wcExtensionsService.getPatientProfileComponents(this.patientId);
			});

		this.form = this.formBuilder.group({
			enable: [null]
		});

	}

	isInstitutionalAdministrator(): boolean {
		return this.rolesAdmin;
	}

	checkIfProfessional(): void {
		this.professionalService.get(this.personId).subscribe((profession: ProfessionalDto) => {
			if (profession) {
				this.license = profession.licenceNumber;
				this.professionalId = profession.id;
				this.isProfessional = true;
				this.setProfessionsAndSpecialties();
			}
		})
	}

	setProfessionsAndSpecialties() {
		if (this.professionalId) {
			this.ownProfessionsAndSpecialties$ = this.professionalService.getProfessionsByProfessional(this.professionalId);
			this.setLicenses();

		}
	}

	private setLicenses(): void {
		this.ownProfessionsAndSpecialties$.subscribe((professionalsByClinicalSpecialty: ProfessionalProfessionsDto[]) => {
			this.ownProfessionsAndSpecialties = professionalsByClinicalSpecialty;
			this.healthcareProfessionalId = professionalsByClinicalSpecialty[0]?.healthcareProfessionalId;
			if (this.healthcareProfessionalId)
				this.professionsWithLicense$ =
					this.professionalLicenseService.getLicenseNumberByProfessional(this.healthcareProfessionalId);
		})
	}

	goNewInternment(): void {
		this.router.navigate([this.routePrefix + ROUTE_NEW_INTERNMENT],
			{
				queryParams: { patientId: this.patientId }
			});
	}

	goToClinicalHistory(): void {
		this.router.navigate([this.clinicalHistoryRoute]);
	}

	goToEditProfile(): void {
		const person = {
			id: this.patientBasicData.id,
		};
		this.router.navigate([this.routePrefix + ROUTE_EDIT_PATIENT], {
			queryParams: person
		});
	}
	roleAccordingToId(roleId: number): string {

		return this.roles.find(rol => rol.id === roleId)?.description;
	}

	editRoles(): void {
		const dialog = this.dialog.open(EditRolesComponent, {
			width: DIALOG_SIZE,
			disableClose: true,
			data: {
				personId: this.personId,
				isProfessional: this.isProfessional,
				roles: this.assignableRoles,
				userId: this.userData.id,
				rolesByUser: this.rolesByUser
			}
		});
		dialog.afterClosed().subscribe((userRoles: UserRoleDto[]) => {
			if (userRoles) {
				this.rolesService.updateRoles(this.userData.id, userRoles).subscribe(_ => {
					this.snackBarService.showSuccess('pacientes.edit_roles.messages.SUCCESS');
					this.userRoles = [];
					this.rolesByUser = [];

					userRoles.forEach(e => {
						if (!this.userRoles.includes(this.roleAccordingToId(e.roleId))) {
							this.userRoles.push(this.roles.find(rol => rol.id === e.roleId)?.description);
							this.rolesByUser.push(e);
						}
					});
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('pacientes.edit_roles.messages.ERROR');
					});
			}
		});
	}

	editProfessions(): void {

		const dialog = this.dialog.open(EditPrefessionsSpecialtiesComponent, {
			width: DIALOG_SIZE,
			height: DIALOG_SIZE_HEIGHT,
			disableClose: true,
			data: {
				personId: this.personId, professionalId: this.professionalId,
				id: this.professionalSpecialtyId, allSpecialties: this.allSpecialties, allProfessions: this.allProfessions, ownProfessionsAndSpecialties: this.ownProfessionsAndSpecialties
			}
		});
		dialog.afterClosed().subscribe((professional: HealthcareProfessionalCompleteDto) => {
			if (professional) {
				this.professionalService.addProfessional(professional).subscribe(_ => {
					this.snackBarService.showSuccess('pacientes.edit_professions.messages.SUCCESS');
					this.professionalId ? this.setProfessionsAndSpecialties() : this.checkIfProfessional();
				},
					error => {
						error?.text ?
							this.snackBarService.showError(error.text) : this.snackBarService.showError('pacientes.edit_professions.messages.ERROR');
					})
			}
		});
	}

	editLicense(): void {
		const professionSpecialties = this.ownProfessionsAndSpecialties.map((e: ProfessionalProfessionsDto) => {
			return {
				profession: { description: e.profession.description, id: e.id },
				specialties: e.specialties.map(specialty => { return { name: specialty.clinicalSpecialty.name, id: specialty.id } })
			}
		});

		const dialog = this.dialog.open(EditLicenseComponent, {
			width: DIALOG_SIZE,
			disableClose: true,
			height: '80%',
			data: {
				personId: this.personId, id: this.professionalSpecialtyId, professionSpecialties: professionSpecialties, healthcareProfessionalId: this.healthcareProfessionalId
			}
		});
		dialog.afterClosed().subscribe((license) => {
			if (license) {
				this.professionalLicenseService.saveProfessionalLicensesNumber(this.healthcareProfessionalId, license).subscribe(_ => {
					this.snackBarService.showSuccess('pacientes.edit_professions.messages.SUCCESS');
					this.setLicenses();
				},
					error => {
						this.snackBarService.showError(error.text || 'pacientes.edit_professions.messages.ERROR');
					}
				)
			}
		});
	}

	reports(): void {
		this.dialog.open(ReportsComponent, {
			width: '700px',
			data: { patientId: this.patientId, patientName: patientCompleteName(this.patientBasicData) }
		});
	}

	addUser() {
		this.userService.addUser(this.personId)
			.subscribe(userId => {
				this.userService.getUserData(this.personId).subscribe(userDataDto => this.userData = userDataDto);
				this.snackBarService.showSuccess('pacientes.user_data.messages.SUCCESS');
				this.userId = userId;
				this.rolesService.getRolesByUser(userId).subscribe((roles: UserRoleDto[]) => {
					this.rolesByUser = roles;
				});
			}, _ => this.snackBarService.showError('pacientes.user_data.messages.ERROR'));
	}

	enableUser() {
		this.userService.enableUser(this.userData.id, this.form.value.enable)
			.subscribe(userId => {
				this.snackBarService.showSuccess('pacientes.user_data.messages.UPDATE_SUCCESS');
				this.userData.enable = this.form.value.enable;
			}, error => {
				processErrors(error, (msg) => this.snackBarService.showError(msg));
				this.form.controls.enable.setValue(this.userData.enable);
			});
	}

	goResetAccessData() {
		this.userPasswordResetService.createTokenPasswordReset(this.userData.id)
			.subscribe(token => {
				window.open("/auth/access-data-reset/" + token, "_blank")
			}, (error) => {
				processErrors(JSON.parse(error), (msg) => this.snackBarService.showError(msg));
			});
	}

	newAppointment() {
		const url = `${AppRoutes.Institucion}/${this.contextService.institutionId}/turnos`;
		this.router.navigate([url], { queryParams: { idPaciente: this.patientId } });
	}

	getHealthcareProfessionalId(): number {
		return this.healthcareProfessionalId;
	}

	reloadLicenses(removedLicense: ProfessionalLicenseNumberDto) {
		this.professionsWithLicense$ = this.professionalLicenseService.getLicenseNumberByProfessional(this.healthcareProfessionalId);
	}

}
