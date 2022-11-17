import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';

import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { futureTimeValidation, hasError, scrollIntoError, TIME_PATTERN } from '@core/utils/form.utils';

import { PersonService } from '@api-rest/services/person.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { PatientService } from '@api-rest/services/patient.service';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import {
	CompletePatientDto,
	HealthcareProfessionalDto,
	PersonalInformationDto,
	PersonPhotoDto,
	BedInfoDto, PatientMedicalCoverageDto, BasicPatientDto,
} from '@api-rest/api-model';

import {
	AppFeature,
} from '@api-rest/api-model';

import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { MapperService } from '@presentation/services/mapper.service';
import { MapperService as CoreMapperService } from '@core/services/mapper.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';

import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { DatePipe } from '@angular/common';
import { DatePipeFormat } from '@core/utils/date.utils';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import * as moment from 'moment';
import { map } from 'rxjs/operators';
import { BedAssignmentComponent } from '@historia-clinica/dialogs/bed-assignment/bed-assignment.component';
import { PatientNameService } from "@core/services/patient-name.service";
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

const ROUTE_PROFILE = 'pacientes/profile/';

const MIN_YEAR = 1900;
const MIN_MONTH = 0;
const MIN_DAY = 1;

const MIDDLE_DASH_SYMBOL = '-';
const SLASH_SYMBOL = '/	';

export const MIN_DATE = new Date(MIN_YEAR, MIN_MONTH, MIN_DAY);

@Component({
	selector: 'app-new-internment',
	templateUrl: './new-internment.component.html',
	styleUrls: ['./new-internment.component.scss']
})
export class NewInternmentComponent implements OnInit {
	TIME_PATTERN = TIME_PATTERN;
	today = new Date();
	validPreviousDays = new Date(this.today);
	patientMedicalCoverages: PatientMedicalCoverage[] = [];
	hasError = hasError;
	submitForm = false;
	public form: FormGroup;
	public doctors: HealthcareProfessionalDto[];
	public doctorsTypehead: TypeaheadOption<HealthcareProfessionalDto>[] = [];
	public patientId: number;
	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public patientTypeData: PatientTypeData;
	public personPhoto: PersonPhotoDto;
	public selectedBedInfo: BedInfoDto;
	private readonly routePrefix;
	private patientData: BasicPatientDto;
	
	@ViewChild('errorDoctor') errorDoctor: ElementRef;
	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly el: ElementRef,
		private readonly router: Router,
		private readonly healthcareProfessionalService: HealthcareProfessionalByInstitutionService,
		private readonly patientService: PatientService,
		private readonly personService: PersonService,
		private readonly mapperService: MapperService,
		private readonly coreMapperService: CoreMapperService,
		private readonly route: ActivatedRoute,
		private readonly internmentEpisodeService: InternmentEpisodeService,
		public dialog: MatDialog,
		public translator: TranslateService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly datePipe: DatePipe,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly patientNameService: PatientNameService,) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {
		this.validPreviousDays.setDate(this.validPreviousDays.getDate() - 1);

		this.route.queryParams.subscribe(params => {
			this.patientId = Number(params.patientId);
			this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
				.subscribe(completeData => {
					this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
					this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
					this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
						.subscribe(personInformationData => {
							this.personalInformation =
								this.mapperService.toPersonalInformationData(completeData, personInformationData);
						});
				});
			this.patientService.getPatientPhoto(this.patientId)
				.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });


			this.setMedicalCoverages();
		});

		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [moment(), [Validators.required]],
				time: [this.datePipe.transform(this.today, DatePipeFormat.SHORT_TIME), [Validators.required, futureTimeValidation,
				Validators.pattern(TIME_PATTERN)]]
			}),
			patientMedicalCoverage: [null, [Validators.required]],
			doctorId: [null, [Validators.required]],
			contactName: [null],
			contactPhoneNumber: [null],
			contactRelationship: [null],
		});

		this.form.controls.dateTime.get('date').valueChanges.subscribe((value: Moment) => {
			if (value?.isSame(newMoment(), 'day')) {
				this.form.controls.dateTime.get('time').setValidators([Validators.required, futureTimeValidation,
				Validators.pattern(TIME_PATTERN)]);
			} else {
				this.form.controls.dateTime.get('time').removeValidators(futureTimeValidation);
			}
			this.form.controls.dateTime.get('time').updateValueAndValidity();
		});

		this.healthcareProfessionalService.getAllDoctors().subscribe(data => {
			this.doctors = data;
			this.doctorsTypehead = this.doctors.map(d => this.toDoctorDtoTypeahead(d));
		});

		this.featureFlagService.isActive(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED).subscribe(isOn => {
			if (!isOn) {
				this.form.controls.doctorId.clearValidators();
				this.form.controls.doctorId.reset();
			}
		});

		this.patientService.getPatientBasicData(Number(this.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
		});
	}

	save(): void {
		this.submitForm = true;
		if (this.form.valid && this.selectedBedInfo) {
			this.openDialog();
		} else {
			if (!this.selectedBedInfo) {
				this.snackBarService.showError('internaciones.new-internment.messages.ERROR_BED_ASSIGNMENT');
			}
			if (!this.form.value.doctorId) 
				this.errorDoctor.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
			if (!this.form.value.patientMedicalCoverage)
				scrollIntoError(this.form, this.el);
		}
	}

	openDialog(): void {
		this.translator.get('internaciones.internacion-paciente.confirmacion').subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: 'Nueva internación',
					content: `${res}${this.patientBasicData.id}?`,
					okButtonLabel: 'Confirmar internación'
				}
			});

			dialogRef.afterClosed().subscribe(result => {
				if (result) {
					const intenmentEpisodeReq = this.mapToPersonInternmentEpisodeRequest();
					this.internmentEpisodeService.setNewInternmentEpisode(intenmentEpisodeReq)
						.subscribe(data => {
							if (data && data.id) {
								const url = this.routePrefix + ROUTE_PROFILE + this.patientId;
								this.router.navigate([url]);
								this.snackBarService.showSuccess('internaciones.new-internment.messages.SUCCESS');
							}
						}, _ => this.snackBarService.showError('internaciones.new-internment.messages.ERROR'));
				}
			});
		});

	}

	openBedAssignmentDialog(): void {

		const dialogRef = this.dialog.open(BedAssignmentComponent, {
			width: '80%'
		});

		dialogRef.afterClosed().subscribe((bedInfo: BedInfoDto) => {
			if (bedInfo) {
				this.selectedBedInfo = bedInfo;
			}
		});

	}

	private mapToPersonInternmentEpisodeRequest() {
		const newTime: string = this.form.controls.dateTime.value.time;
		const newDatetime = new Date(this.form.controls.dateTime.value.date);
		const medicalCoverageId = (this.form.controls?.patientMedicalCoverage?.value?.id) ? this.form.controls.patientMedicalCoverage.value.id : null;
		newDatetime.setHours(+newTime.substr(0, 2), +newTime.substr(3, 2), 0, 0);

		const response = {
			patientId: this.patientId,
			bedId: this.selectedBedInfo.bed.id,
			entryDate: newDatetime.toISOString(),
			patientMedicalCoverageId: medicalCoverageId,
			responsibleDoctorId: this.form.controls.doctorId.value,
			responsibleContact: null
		};

		const fullname = this.form.controls.contactName.value;
		const phoneNumber = this.form.controls.contactPhoneNumber.value;
		const relationship = this.form.controls.contactRelationship.value;
		if (fullname || phoneNumber || relationship) {
			response.responsibleContact = {};
			if (fullname) {
				response.responsibleContact.fullName = fullname;
			}
			if (phoneNumber) {
				response.responsibleContact.phoneNumber = phoneNumber;
			}
			if (relationship) {
				response.responsibleContact.relationship = relationship;
			}
		}
		return response;
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const condition = (patientMedicalCoverage.condition) ? patientMedicalCoverage.condition.toLowerCase() : null;
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name]
			.filter(Boolean).join(MIDDLE_DASH_SYMBOL);
		return [medicalCoverageText, patientMedicalCoverage.affiliateNumber, condition].filter(Boolean).join(SLASH_SYMBOL);
	}

	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
			.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(Number(this.patientId))
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.coreMapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => this.patientMedicalCoverages = patientMedicalCoverages);
	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.patientData.person.gender.id,
				identificationNumber: this.patientData.person.identificationNumber,
				identificationTypeId: this.patientData.person.identificationTypeId,
				initValues: this.patientMedicalCoverages,
				patientId: this.patientId
			}

		});

		dialogRef.afterClosed().subscribe(
			values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.coreMapperService.toPatientMedicalCoverageDto(s));
					this.patientMedicalCoverageService.addPatientMedicalCoverages(Number(this.patientId), patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('internaciones.new-internment.toast_messages.POST_UPDATE_COVERAGE_SUCCESS');
						},
						_ => this.snackBarService.showError('internaciones.new-internment.toast_messages.POST_UPDATE_COVERAGE_ERROR')
					);
				}
			}
		);
	}

	setDoctor($event: HealthcareProfessionalDto) {
		if ($event)
			this.form.controls['doctorId'].setValue($event.id);
		else
			this.form.controls.doctorId.reset();
	}

	toDoctorDtoTypeahead(doctorDto: HealthcareProfessionalDto): TypeaheadOption<HealthcareProfessionalDto> {
		return {
			compareValue: `${this.patientNameService.getPatientName(doctorDto.person.firstName, doctorDto.nameSelfDetermination)}` + " " + doctorDto.person.lastName,
			value: doctorDto
		};

	}
}
