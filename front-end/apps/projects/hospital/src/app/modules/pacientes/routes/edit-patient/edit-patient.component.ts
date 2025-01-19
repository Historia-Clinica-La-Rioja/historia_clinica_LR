import { Component, ElementRef, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
	APatientDto,
	BMPatientDto, BMPersonDto, CompletePatientDto, EAuditType, EducationLevelDto, ERole, EthnicityDto, GenderDto,
	IdentificationTypeDto, PatientMedicalCoverageDto, PatientType, PersonOccupationDto, RoleAssignmentDto, SelfPerceivedGenderDto
} from '@api-rest/api-model';

import { MatDialog } from '@angular/material/dialog';
import { AppFeature } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { AuditPatientService } from '@api-rest/services/audit-patient.service';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PersonService } from '@api-rest/services/person.service';
import { PERSON } from '@core/constants/validation-constants';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { MapperService } from '@core/services/mapper.service';
import { PermissionsService } from '@core/services/permissions.service';
import { hasError, scrollIntoError, updateControlValidator, VALIDATIONS } from '@core/utils/form.utils';
import { dateISOParseDate, newDate } from '@core/utils/moment.utils';
import { PATIENT_TYPE } from '@core/utils/patient.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { EditIdentificationNumberComponent, PersonBasicDataResponseCustom } from '@pacientes/dialogs/edit-identification-number/edit-identification-number.component';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { MessageForAuditComponent } from '@pacientes/dialogs/message-for-audit/message-for-audit.component';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { UserService } from '@api-rest/services/user.service';
import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { anyMatch } from '@core/utils/array.utils';
import { NO_INSTITUTION } from '../../../home/home.component';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';


const ROUTE_PROFILE = 'pacientes/profile/';
const ERROR_ENCOUNTER_ACTIVE_EXISTS = 'ENCOUNTER_ACTIVE_EXISTS';
const REJECTTED = 'Rechazado';

@Component({
	selector: 'app-edit-patient',
	templateUrl: './edit-patient.component.html',
	styleUrls: ['./edit-patient.component.scss']
})
export class EditPatientComponent implements OnInit {

	currentEducationLevelDescription: string;
	currentOccupationDescription: string;
	hasInstitutionalAdministratorRole = false;
	hasToSaveFiles: boolean = false;
	typesPatient: PatientType[];
	hasAuditorRole: boolean = false;
	idTypeDni: number;
	dniWasEdited: boolean = false;
	rejectedId: number;

	readonly PERSON_MAX_LENGTH = PERSON.MAX_LENGTH;
	readonly GENDER_MAX_LENGTH = VALIDATIONS.MAX_LENGTH.gender;
	private readonly NONE_SELF_PERCEIVED_GENDER_SELECTED_ID = 10; // Dato Maestro proveniente de género autopercibido "Ninguna de las anteriores"

	public form: UntypedFormGroup;
	public personResponse: BMPatientDto;
	public formSubmitted = false;
	public today = newDate();
	public hasError = hasError;
	public genders: GenderDto[];
	public selfPerceivedGenders: SelfPerceivedGenderDto[];
	public showOtherGender: boolean;
	public countries: any[];
	public provinces$: Observable<any[]>;
	public departments$: Observable<any[]>;
	public cities$: Observable<any[]>;
	public identificationTypeList: IdentificationTypeDto[];
	private readonly routePrefix;
	public patientType;
	public completeDataPatient: CompletePatientDto;
	public auditablePatientInfo: AuditablePatientInfo;
	private auditableFullDate: Date;
	private auditTypeId: EAuditType = EAuditType.UNAUDITED;
	private wasMarked = false;
	public patientId: any;
	public filesId: number[];
	private medicalCoverages: PatientMedicalCoverage[];
	public ethnicities: EthnicityDto[];
	public occupations: PersonOccupationDto[];
	public educationLevels: EducationLevelDto[];
	public isHabilitarAuditoriaOn = false;

	constructor(
		private formBuilder: UntypedFormBuilder,
		private router: Router,
		private el: ElementRef,
		private patientService: PatientService,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService,
		private addressMasterDataService: AddressMasterDataService,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
		private personService: PersonService,
		private featureFlagService: FeatureFlagService,
		private dialog: MatDialog,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private permissionsService: PermissionsService,
		private readonly dateFormatPipe: DateFormatPipe,
		private patientMasterDataService: PatientMasterDataService,
		private auditPatientService: AuditPatientService,
		private readonly userService: UserService,
		private loggedUserService: LoggedUserService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_AUDITORIA).subscribe(isOn => this.isHabilitarAuditoriaOn = isOn);
		this.loggedUserService.assignments$.subscribe((roleAssignment: RoleAssignmentDto[]) => this.hasAuditorRole = anyMatch<ERole>(roleAssignment.map(roleAssignment => roleAssignment.role), [ERole.AUDITOR_MPI]));
		this.route.queryParams
			.subscribe(params => {
				this.patientId = params.id;
				this.formBuild();
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
					.subscribe(completeData => {
						this.completeDataPatient = completeData;
						if (completeData?.auditablePatientInfo) {
							this.wasMarked = true;
							this.auditTypeId = EAuditType.TO_AUDIT;
							this.auditableFullDate = dateTimeDtotoLocalDate(
								{
									date: this.completeDataPatient.auditablePatientInfo.createdOn.date,
									time: this.completeDataPatient.auditablePatientInfo.createdOn.time
								}
							);
							this.auditablePatientInfo = {
								message: this.completeDataPatient.auditablePatientInfo.message,
								createdBy: this.completeDataPatient.auditablePatientInfo.createdBy,
								createdOn: this.dateFormatPipe.transform(this.auditableFullDate, 'datetime'),
								institutionName: this.completeDataPatient.auditablePatientInfo.institutionName
							};
						}
						this.patientType = completeData.patientType.id;
						this.personService.getCompletePerson<BMPersonDto>(completeData.person.id)
							.subscribe(personInformationData => {
								if (personInformationData.identificationTypeId) {
									this.form.setControl('identificationTypeId', new UntypedFormControl(Number(personInformationData.identificationTypeId), Validators.required));
								}
								this.form.setControl('identificationNumber', new UntypedFormControl(personInformationData.identificationNumber, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]));
								// person
								this.form.setControl('firstName', new UntypedFormControl(completeData.person.firstName, Validators.required));
								this.form.setControl('middleNames', new UntypedFormControl(personInformationData.middleNames));
								this.form.setControl('lastName', new UntypedFormControl(completeData.person.lastName, Validators.required));
								this.form.setControl('otherLastNames', new UntypedFormControl(personInformationData.otherLastNames));
								this.form.setControl('mothersLastName', new UntypedFormControl(personInformationData.mothersLastName));
								this.form.setControl('patientId', new UntypedFormControl(completeData.id, Validators.required));
								this.form.setControl('stateId', new UntypedFormControl(completeData.patientType.id, Validators.required));

								if (completeData.person.gender.id) {
									this.form.setControl('genderId', new UntypedFormControl(Number(completeData.person.gender.id), Validators.required));
								}
								this.form.setControl('genderSelfDeterminationId', new UntypedFormControl(Number(personInformationData.genderSelfDeterminationId)));

								const OTHER_GENDER_VALUE = personInformationData.otherGenderSelfDetermination ? personInformationData.otherGenderSelfDetermination : null;
								this.form.setControl('otherGenderSelfDetermination', new UntypedFormControl(OTHER_GENDER_VALUE, [Validators.required, Validators.maxLength(this.GENDER_MAX_LENGTH)]));
								if (personInformationData.genderSelfDeterminationId !== this.NONE_SELF_PERCEIVED_GENDER_SELECTED_ID) {
									this.form.get('otherGenderSelfDetermination').disable();
									this.showOtherGender = false;
								}
								else
									this.showOtherGender = true;

								this.form.setControl('nameSelfDetermination', new UntypedFormControl(personInformationData.nameSelfDetermination));
								this.form.setControl('birthDate', new UntypedFormControl(new Date(personInformationData.birthDate), Validators.required));
								this.form.setControl('cuil', new UntypedFormControl(personInformationData.cuil, [Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)]));
								this.form.setControl('email', new UntypedFormControl(personInformationData.email, Validators.email));
								this.form.setControl('phonePrefix', new UntypedFormControl(personInformationData.phonePrefix, [Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]));
								this.form.setControl('phoneNumber', new UntypedFormControl(personInformationData.phoneNumber, [Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]));
								if (personInformationData.phoneNumber) {
									updateControlValidator(this.form, 'phoneNumber', [Validators.required]);
									updateControlValidator(this.form, 'phonePrefix', [Validators.required]);
								}
								this.form.setControl('religion', new UntypedFormControl(personInformationData.religion));
								this.form.setControl('ethnicityId', new UntypedFormControl(personInformationData.ethnicityId));
								this.form.setControl('occupationId', new UntypedFormControl(personInformationData.occupationId));
								this.form.setControl('educationLevelId', new UntypedFormControl(personInformationData.educationLevelId));
								// address
								if (personInformationData.countryId) {
									this.form.setControl('addressCountryId', new UntypedFormControl(personInformationData.countryId));
									this.provinces$ = this.addressMasterDataService.getByCountry(personInformationData.countryId);
								}
								if (personInformationData.provinceId) {
									this.form.setControl('addressProvinceId', new UntypedFormControl(personInformationData.provinceId));
									this.departments$ = this.addressMasterDataService.getDepartmentsByProvince(personInformationData.provinceId);
								}
								if (personInformationData.departmentId) {
									this.form.setControl('addressDepartmentId', new UntypedFormControl(personInformationData.departmentId));
									this.cities$ = this.addressMasterDataService.getCitiesByDepartment(personInformationData.departmentId);
								}
								this.form.setControl('addressCityId', new UntypedFormControl(personInformationData.cityId));
								this.form.setControl('addressStreet', new UntypedFormControl(personInformationData.street));
								this.form.setControl('addressNumber', new UntypedFormControl(personInformationData.number, [Validators.pattern(PATTERN_INTEGER_NUMBER)]));
								this.form.setControl('addressFloor', new UntypedFormControl(personInformationData.floor));
								this.form.setControl('addressApartment', new UntypedFormControl(personInformationData.apartment));
								this.form.setControl('addressQuarter', new UntypedFormControl(personInformationData.quarter));
								this.form.setControl('addressPostcode', new UntypedFormControl(personInformationData.postcode));
								// doctors
								this.form.setControl('generalPractitioner', new UntypedFormControl(completeData.generalPractitioner?.fullName));
								this.form.setControl('generalPractitionerPhoneNumber', new UntypedFormControl(completeData.generalPractitioner?.phoneNumber));
								this.form.setControl('pamiDoctor', new UntypedFormControl(completeData.pamiDoctor?.fullName));
								this.form.setControl('pamiDoctorPhoneNumber', new UntypedFormControl(completeData.pamiDoctor?.phoneNumber));
								this.restrictFormEdit();
								if (this.hasAuditorRole) {
									this.form.controls.patientId.disable();
									if (this.form.controls.identificationTypeId.value === this.idTypeDni) {
										this.form.controls.identificationNumber.disable();
									}
								}

								this.form.get("addressCountryId").valueChanges.subscribe(
									countryId => {
										this.form.controls.addressProvinceId.reset();
										this.provinces$ = countryId ? this.addressMasterDataService.getByCountry(countryId) : null;
									}
								);

								this.form.get("addressProvinceId").valueChanges.subscribe(
									provinceId => {
										this.form.controls.addressDepartmentId.reset();
										this.departments$ = provinceId ? this.addressMasterDataService.getDepartmentsByProvince(provinceId) : null;
									}
								);

								this.form.get("addressDepartmentId").valueChanges.subscribe(
									departmentId => {
										this.form.controls.addressCityId.reset();
										this.cities$ = departmentId ? this.addressMasterDataService.getCitiesByDepartment(departmentId) : null;
									}
								);

								this.form.get("addressCityId").valueChanges.subscribe(
									_ => {
										([
											'addressNumber',
											'addressFloor',
											'addressApartment',
											'addressQuarter',
											'addressStreet',
											'addressPostcode'
										]).forEach(
											controlName => {
												this.form.controls[controlName].reset();
											}
										);
									}
								);

								if (this.hasAuditorRole) {
									this.auditPatientService.getTypesPatient().subscribe(res => {
										this.typesPatient = res;
										this.rejectedId = this.typesPatient.find(type => type.description === REJECTTED).id;
										if (this.form.controls.stateId.value === this.rejectedId) {
											this.form.controls.stateId.disable();
											this.form.controls.identificationNumber.disable();
											this.form.controls.identificationTypeId.disable();
										}
									})
								} else {
									this.patientMasterDataService.getTypesPatient().subscribe(res => {
										this.typesPatient = res;
									})
								}
								//Tooltips
								this.currentOccupationDescription = this.occupations.find(occupation => occupation.id === personInformationData.occupationId)?.description;
								this.currentEducationLevelDescription = this.educationLevels.find(educationLevel => educationLevel.id === personInformationData.educationLevelId)?.description;
								if (this.contextService.institutionId != NO_INSTITUTION) {
									this.personService.canEditUserData(completeData.person.id).subscribe(canEditUserData => {
										if (!canEditUserData)
											this.form.controls.email.disable();
									});
								}
								this.permissionsService.hasContextAssignments$([ERole.AUDITOR_MPI]).subscribe(canEditPatientStatus => {
									this.form.controls.patientId.disable();
									if (!canEditPatientStatus)
										this.form.controls.stateId.disable();
								});
							});
					});
			});

		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});

		this.personMasterDataService.getSelfPerceivedGenders()
			.subscribe(
				genders => this.selfPerceivedGenders = genders
			);

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
				this.idTypeDni = this.identificationTypeList.find(type => type.description === 'DNI').id;
			});

		this.personMasterDataService.getEthnicities()
			.subscribe(ethnicities => {
				this.ethnicities = ethnicities;
			});

		this.personMasterDataService.getOccupations()
			.subscribe(occupations => {
				this.occupations = occupations;
			});

		this.personMasterDataService.getEducationLevels()
			.subscribe(educationLevels => {
				this.educationLevels = educationLevels;
			});

		this.addressMasterDataService.getAllCountries()
			.subscribe(countries => {
				this.countries = countries;
			});

		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
		ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR]).subscribe(hasInstitutionalAdministratorRole =>
			this.hasInstitutionalAdministratorRole = hasInstitutionalAdministratorRole);



	}

	updatePhoneValidators() {
		if (this.form.controls.phoneNumber.value || this.form.controls.phonePrefix.value) {
			updateControlValidator(this.form, 'phonePrefix', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phonePrefix)]);
			updateControlValidator(this.form, 'phoneNumber', [Validators.required, Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.maxLength(VALIDATIONS.MAX_LENGTH.phone)]);
		} else {
			updateControlValidator(this.form, 'phoneNumber', []);
			updateControlValidator(this.form, 'phonePrefix', []);
		}

	}

	formBuild() {
		this.form = this.formBuilder.group({
			firstName: [null, [Validators.required]],
			middleNames: [null],
			lastName: [null, [Validators.required]],
			otherLastNames: [null],
			genderId: [null, [Validators.required]],
			identificationNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			identificationTypeId: [null, [Validators.required]],
			birthDate: [null, [Validators.required]],
			patientId: [null],
			stateId: [null],

			// Person extended
			cuil: [null, [Validators.maxLength(VALIDATIONS.MAX_LENGTH.cuil)]],
			mothersLastName: [],
			phonePrefix: [],
			phoneNumber: [],
			email: [null, Validators.email],
			ethnicityId: [],
			occupationId: [],
			educationLevelId: [],
			religion: [],
			nameSelfDetermination: [],
			genderSelfDeterminationId: [],

			// Address
			addressStreet: [],
			addressNumber: [],
			addressFloor: [],
			addressApartment: [],
			addressQuarter: [],
			addressCityId: [],
			addressPostcode: [],
			addressProvinceId: [],
			addressCountryId: [],
			addressDepartmentId: [],

			// doctors
			generalPractitioner: [],
			generalPractitionerPhoneNumber: [],
			pamiDoctor: [],
			pamiDoctorPhoneNumber: []
		});
	}

	save(): void {
		this.formSubmitted = true;
		if (this.form.valid) {
			if (this.wasMarked && this.hasAuditorRole) {
				const dialogRef = this.dialog.open(DiscardWarningComponent, {
					data: {
						title: 'pacientes.audit.QUESTION_MARK_AUDIT_RESOLVED',
						okButtonLabel: 'pacientes.audit.BUTTON_SAVE_RESOLVED',
						cancelButtonLabel: 'pacientes.audit.BUTTON_SAVE_NOT_RESOLVED',
						buttonClose: true,
					},
					disableClose: true,
					width: '35%',
					autoFocus: false,
				})
				dialogRef.afterClosed().subscribe(saveAndResolved => {
					if (saveAndResolved) {
						this.auditTypeId = EAuditType.AUDITED;
						this.hasToSaveFiles = true;
					} else if (saveAndResolved !== '') {
						this.auditTypeId = EAuditType.TO_AUDIT;
						this.hasToSaveFiles = true;
					}
				})
			} else if (this.form.controls.stateId.value === this.rejectedId) {
				this.userService.getUserData(this.completeDataPatient.person.id)
					.subscribe(userDataDto => {
						if (userDataDto) {
							const dialogRef = this.dialog.open(DiscardWarningComponent, {
								data: {
									title: 'pacientes.audit.TITLE_ERROR_REJECTED_PATIENT_HAS_USER',
									content: 'pacientes.audit.SUBTITLE_ERROR_REJECTED_PATIENT_HAS_USER',
									contentBold: 'pacientes.audit.QUESTION_ERROR_REJECTED_PATIENT_HAS_USER',
									okButtonLabel: 'buttons.YES_CONTINUE',
									cancelButtonLabel: 'buttons.NO_CANCEL',
									buttonClose: true,
									color: 'warn',
									okBottonColor: 'warn'
								},
								disableClose: true,
								width: '35%',
								autoFocus: false,
							})
							dialogRef.afterClosed().subscribe(isContinue => {
								if (isContinue) {
									this.hasToSaveFiles = true;
								}
							})
						}
					});
			} else {
				this.hasToSaveFiles = true;
			}
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.form.getRawValue().genderId,
				identificationNumber: this.form.getRawValue().identificationNumber,
				identificationTypeId: this.form.getRawValue().identificationTypeId,
				initValues: this.medicalCoverages,
				patientId: this.patientId,
			}
		});
		dialogRef.afterClosed().subscribe(medicalCoverages => {
			if (medicalCoverages) {
				this.medicalCoverages = medicalCoverages.patientMedicalCoverages;
			}
		});
	}

	goBack(): void {
		this.formSubmitted = false;
		if (this.hasAuditorRole) {
			window.history.back();
		} else {
			this.router.navigate([this.routePrefix + ROUTE_PROFILE + `${this.patientId}`]);
		}

	}

	showOtherSelfPerceivedGender(): void {
		this.showOtherGender = (this.form.value.genderSelfDeterminationId === this.NONE_SELF_PERCEIVED_GENDER_SELECTED_ID);
		this.form.get('otherGenderSelfDetermination').setValue(null);
		if (this.showOtherGender)
			this.form.get('otherGenderSelfDetermination').enable();
		else
			this.form.get('otherGenderSelfDetermination').disable();
	}

	clearGenderSelfDetermination(): void {
		this.form.controls.genderSelfDeterminationId.reset();
		this.showOtherSelfPerceivedGender();
	}

	openAuditDialog(initialMessage?: string): void {
		const dialogRef = this.dialog.open(MessageForAuditComponent, {
			disableClose: true,
			width: '40%',
			autoFocus: false,
			data: {
				initialMessage: initialMessage,
			}
		});
		dialogRef.afterClosed().subscribe(message => {
			if (message) {
				this.auditTypeId = EAuditType.TO_AUDIT;
				this.auditableFullDate = new Date();
				this.auditablePatientInfo = {
					message: message,
					createdBy: null,
					createdOn: 'pacientes.edit.A_MOMENT_AGO',
					institutionName: null,
				}
			}
		});
	}

	openUnmarkPatientWarning(): void {
		const dialogRef = this.dialog.open(DiscardWarningComponent, {
			data: {
				content: 'pacientes.edit.unmark-patient-for-audit.DESCRIPTION',
				contentBold: 'pacientes.edit.unmark-patient-for-audit.QUESTION',
				okButtonLabel: 'pacientes.edit.unmark-patient-for-audit.CONFIRM',
				cancelButtonLabel: 'pacientes.edit.unmark-patient-for-audit.CANCEL',
			},
			disableClose: true,
			width: '35%',
			autoFocus: false
		});
		dialogRef.afterClosed().subscribe((unmark: boolean) => {
			if (unmark) {
				this.auditTypeId = EAuditType.UNAUDITED;
				this.auditablePatientInfo = undefined;
				this.auditableFullDate = undefined;
			}
		});
	}

	private getMessagesSuccess(): string {
		if (this.auditTypeId === EAuditType.AUDITED) {
			return 'pacientes.audit.SAVE_OK_EDIT_AUDIT';
		} else {
			return this.hasInstitutionalAdministratorRole ? 'pacientes.edit.messages.SUCCESS_PERSON' : 'pacientes.edit.messages.SUCCESS_PATIENT';
		}

	}
	private getMessagesError(): string {
		return this.hasInstitutionalAdministratorRole ? 'pacientes.edit.messages.ERROR_PERSON' : 'pacientes.edit.messages.ERROR_PATIENT';
	}

	savePatient(idFiles: Observable<number[]>) {
		if (idFiles) {
			idFiles.subscribe(idsFiles => {
				this.filesId = idsFiles;
				const personRequest: APatientDto = this.mapToPersonRequest();
				this.patientService.editPatient(personRequest, this.patientId)
					.subscribe(patientId => {
						if (this.medicalCoverages) {
							const patientMedicalCoveragesDto: PatientMedicalCoverageDto[] =
								this.medicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));
							this.patientMedicalCoverageService.addPatientMedicalCoverages(this.patientId, patientMedicalCoveragesDto)
								.subscribe();
						}
						if (this.hasAuditorRole) {
							window.history.back();
						} else {
							this.router.navigate([this.routePrefix + ROUTE_PROFILE + patientId]);
						}
						this.snackBarService.showSuccess(this.getMessagesSuccess());
					}, error => {
						if (error.code === ERROR_ENCOUNTER_ACTIVE_EXISTS) {
							this.dialog.open(DiscardWarningComponent, {
								data: {
									title: 'pacientes.audit.TITLE_ERROR_REJECTED',
									content: 'pacientes.audit.LABEL_ERROR_REJECTED',
									okButtonLabel: 'buttons.CLOSE',
									buttonClose: true,
									errorMode: true,
								},
								disableClose: true,
								width: '35%',
								autoFocus: false
							});
							this.hasToSaveFiles = null;
						} else {
							this.snackBarService.showError(this.getMessagesError());
						}
					})
			})
		}
	}

	private mapToPersonRequest(): APatientDto {
		let patient: APatientDto = {
			message: this.auditablePatientInfo?.message ? this.auditablePatientInfo.message : '',
			auditType: this.auditTypeId,
			birthDate: this.form.controls.birthDate.value,
			firstName: this.form.controls.firstName.value,
			genderId: this.form.controls.genderId.value,
			identificationTypeId: this.form.controls.identificationTypeId.value,
			identificationNumber: this.form.controls.identificationNumber.value,
			lastName: this.form.controls.lastName.value,
			middleNames: this.form.controls.middleNames.value && this.form.controls.middleNames.value.length ? this.form.controls.middleNames.value : null,
			otherLastNames: this.form.controls.otherLastNames.value && this.form.controls.otherLastNames.value.length ? this.form.controls.otherLastNames.value : null,
			personAge: null,
			// Person extended
			cuil: this.form.controls.cuil.value,
			email: this.form.controls.email.value && this.form.controls.email.value.length ? this.form.controls.email.value : null,
			ethnicityId: this.form.controls.ethnicityId.value,
			occupationId: this.form.controls.occupationId.value,
			educationLevelId: this.form.controls.educationLevelId.value,
			genderSelfDeterminationId: this.form.controls.genderSelfDeterminationId.value,
			mothersLastName: this.form.controls.mothersLastName.value,
			nameSelfDetermination: this.form.controls.nameSelfDetermination.value ? this.form.controls.nameSelfDetermination.value : null,
			phonePrefix: this.form.controls.phonePrefix.value,
			phoneNumber: this.form.controls.phoneNumber.value,
			religion: this.form.controls.religion.value,
			// Address
			apartment: this.form.controls.addressApartment.value,
			cityId: this.form.controls.addressCityId.value,
			floor: this.form.controls.addressFloor.value,
			number: this.form.controls.addressNumber.value,
			postcode: this.form.controls.addressPostcode.value,
			quarter: this.form.controls.addressQuarter.value,
			street: this.form.controls.addressStreet.value,
			countryId: this.form.controls.addressCountryId.value,
			departmentId: this.form.controls.addressDepartmentId.value,
			provinceId: this.form.controls.addressProvinceId.value,
			// Patient
			typeId: this.form.controls.stateId.value,
			comments: null,
			identityVerificationStatusId: null,
			// Doctors
			generalPractitioner: {
				id: this.completeDataPatient.generalPractitioner?.id,
				fullName: this.form.controls.generalPractitioner.value,
				phoneNumber: this.form.controls.generalPractitionerPhoneNumber.value,
				generalPractitioner: true
			},
			pamiDoctor: {
				id: this.completeDataPatient.pamiDoctor?.id,
				fullName: this.form.controls.pamiDoctor.value,
				phoneNumber: this.form.controls.pamiDoctorPhoneNumber.value,
				generalPractitioner: false

			},
			fileIds: this.filesId,
		};

		if (patient.genderSelfDeterminationId === this.NONE_SELF_PERCEIVED_GENDER_SELECTED_ID)
			patient.otherGenderSelfDetermination = this.form.value.otherGenderSelfDetermination;

		return patient;
	}

	private disableFormField() {
		this.form.controls.identificationNumber.disable();
		this.form.controls.identificationTypeId.disable();
		this.form.controls.genderId.disable();
		this.form.controls.firstName.disable();
		this.form.controls.middleNames.disable();
		this.form.controls.lastName.disable();
		this.form.controls.otherLastNames.disable();
		this.form.controls.birthDate.disable();
		this.form.controls.patientId.disable();
		this.form.controls.stateId.disable();
	}

	private isLockablePatientType(): boolean {
		if (!this.hasAuditorRole) {
			return (this.patientType === PATIENT_TYPE.PERMANENT_INVALID || this.patientType === PATIENT_TYPE.VALID || this.patientType === PATIENT_TYPE.PERMANENT);
		} else {
			return false;
		}
	}

	private restrictFormEdit(): void {
		this.featureFlagService.isActive(AppFeature.RESTRINGIR_DATOS_EDITAR_PACIENTE)
			.subscribe(result => {
				if (result && this.isLockablePatientType()) {
					this.disableFormField();
				}
			});
	}

	setIdentificationType(value: any) {
		const button = document.getElementById('updateDNI');
		if (this.hasAuditorRole) {
			if (value !== this.idTypeDni) {
				this.form.controls.identificationNumber.enable();
				button.style.display = "none";
			} else {
				this.form.controls.identificationNumber.disable();
				button.style.display = "block";
			}
		}
	}

	setDNI() {
		const dialogRef = this.dialog.open(EditIdentificationNumberComponent, {
			disableClose: true,
			autoFocus: false
		});
		dialogRef.afterClosed().subscribe(personData => {
			if (personData) {
				this.setValuesAndDisabled(personData);
			}
		})

	}
	setStateAndDisabledInputs(value: any) {
		if (value === this.rejectedId) {
			this.form.controls.identificationNumber.disable();
			this.form.controls.identificationTypeId.disable();

		} else if (!(this.form.controls.identificationTypeId.value === this.idTypeDni)) {
			this.form.controls.identificationNumber.enable();
			this.form.controls.identificationTypeId.enable();
		} else {
			this.form.controls.identificationTypeId.enable();
		}
	}
	setValuesAndDisabled(data: PersonBasicDataResponseCustom) {
		this.dniWasEdited = true;

		this.form.controls.genderId.setValue(data.genderId);
		this.form.controls.genderId.disable();

		this.form.controls.stateId.setValue(PATIENT_TYPE.VALID);
		this.form.controls.stateId.disable();

		this.form.controls.identificationNumber.setValue(data.identificationNumber);
		this.form.controls.identificationNumber.disable();

		this.form.controls.identificationTypeId.disable();

		if (data.personData.firstName && data.personData.lastName) {
			const splitedFirstName = this.splitStringByFirstSpaceCharacter(data.personData.firstName);
			const splitedLastName = this.splitStringByFirstSpaceCharacter(data.personData.lastName);

			this.form.controls.lastName.setValue(splitedLastName.firstSubstring);
			this.form.controls.firstName.setValue(splitedFirstName.firstSubstring);

			this.form.controls.middleNames.setValue(splitedFirstName.secondSubstring);
			this.form.controls.otherLastNames.setValue(splitedLastName.secondSubstring);

			this.form.controls.firstName.disable();
			this.form.controls.middleNames.disable();
			this.form.controls.lastName.disable();
			this.form.controls.otherLastNames.disable();
		} else {
			this.form.controls.lastName.setValue(null);
			this.form.controls.firstName.setValue(null);
			this.form.controls.middleNames.setValue(null);
			this.form.controls.otherLastNames.setValue(null);
		}

		this.form.controls.stateId.setValue(PATIENT_TYPE.VALID);
		this.form.controls.stateId.disable();

		this.form.controls.identificationNumber.setValue(data.identificationNumber);
		this.form.controls.identificationNumber.disable();


		if (data.personData.birthDate) {
			this.form.controls.birthDate.setValue(dateISOParseDate(data.personData.birthDate));
			this.form.controls.birthDate.disable();
		} else {
			this.form.controls.birthDate.setValue(null);
		}

		if (data.personData.cuil) {
			this.form.controls.cuil.setValue(data.personData.cuil);
		} else {
			this.form.controls.cuil.setValue(null);
		}
	}

	private splitStringByFirstSpaceCharacter(text: string): any {
		const spaceIndex: number = text.indexOf(' ');
		if (spaceIndex === 0) {
			return this.splitStringByFirstSpaceCharacter(text.substr(1));
		} else {
			return (spaceIndex !== -1) ?
				{
					firstSubstring: text.substr(0, spaceIndex),
					secondSubstring: text.substr(spaceIndex + 1)
				}
				:
				{ firstSubstring: text };
		}
	}
}

export interface AuditablePatientInfo {
	createdBy: string;
	createdOn: string;
	institutionName: string;
	message: string;
}
