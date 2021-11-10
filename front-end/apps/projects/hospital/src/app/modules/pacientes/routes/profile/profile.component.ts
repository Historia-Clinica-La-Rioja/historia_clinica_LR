import { Component, OnInit } from '@angular/core';
import {
	PersonalInformationDto,
	CompletePatientDto,
	PatientMedicalCoverageDto,
	PersonPhotoDto,
	UserDataDto
} from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '../../../presentation/services/mapper.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PersonService } from '@api-rest/services/person.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
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
import { FormBuilder, FormGroup } from "@angular/forms";
import { processErrors } from "@core/utils/form.utils";
import { take } from "rxjs/operators";
import { Observable } from "rxjs";
import { PermissionsService } from "@core/services/permissions.service";
import {UserPasswordResetService} from "@api-rest/services/user-password-reset.service";

const ROUTE_NEW_INTERNMENT = 'internaciones/internacion/new';
const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_INTERNMENT_EPISODE_SUFIX = '/paciente/';
const ROUTE_EDIT_PATIENT = 'pacientes/edit';
const ROLES_TO_VIEW_USER_DATA: ERole[] = [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE];

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public personPhoto: PersonPhotoDto;
	public codigoColor: string;
	private patientId: number;
	private readonly routePrefix;
	public internmentEpisode;
	public userData: UserDataDto;
	private personId: number;
	public form: FormGroup;

	public downloadReportIsEnabled: boolean;
	public createUsersIsEnable: boolean;

	constructor(
		private patientService: PatientService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private personService: PersonService,
		private contextService: ContextService,
		private userPasswordResetService : UserPasswordResetService,
		private internmentPatientService: InternmentPatientService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly featureFlagService: FeatureFlagService,
		public dialog: MatDialog,
		private readonly userService: UserService,
		private readonly snackBarService: SnackBarService,
		private readonly formBuilder: FormBuilder,
		private readonly permissionService: PermissionsService
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
		this.featureFlagService.isActive(AppFeature.HABILITAR_INFORMES).subscribe(isOn => this.downloadReportIsEnabled = isOn);
		this.featureFlagService.isActive(AppFeature.HABILITAR_CREACION_USUARIOS).subscribe(isOn => this.createUsersIsEnable = isOn);
	}

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('id'));
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
					.subscribe(completeData => {
						this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
						this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
						this.personId = completeData.person.id;
						this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
							.subscribe(personInformationData => {
								this.personalInformation = this.mapperService.toPersonalInformationData(completeData, personInformationData);
							});
						this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
							.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);

						this.permissionService.hasContextAssignments$(ROLES_TO_VIEW_USER_DATA).subscribe(hasRoleToViewUserData => {
							if (this.createUsersIsEnable && hasRoleToViewUserData) {
								this.userService.getUserData(this.personId)
									.subscribe(userDataDto => {
										this.userData = userDataDto
										this.form.controls.enable.setValue(this.userData.enable);
									});
							}
						})
					});

				this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId)
					.subscribe(internmentEpisodeProcessDto => {
						if (internmentEpisodeProcessDto) {
							this.internmentEpisode = internmentEpisodeProcessDto;
						}
					});

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
			});

		this.form = this.formBuilder.group({
			enable: [null]
		});

	}

	goNewInternment(): void {
		this.router.navigate([this.routePrefix + ROUTE_NEW_INTERNMENT],
			{
				queryParams: { patientId: this.patientId }
			});
	}

	goInternmentEpisode(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this.internmentEpisode.id + ROUTE_INTERNMENT_EPISODE_SUFIX + this.patientId]);
	}

	goToEditProfile(): void {
		const person = {
			id: this.patientBasicData.id,
		};
		this.router.navigate([this.routePrefix + ROUTE_EDIT_PATIENT], {
			queryParams: person
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
			}, _ => this.snackBarService.showError('pacientes.user_data.messages.ERROR'));
	}

	enableUser() {
		this.userService.enableUser(this.userData.id, this.form.value.enable)
			.subscribe(userId => {
				this.snackBarService.showSuccess('pacientes.user_data.messages.UPDATE_SUCCESS');
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
}
