import { DatePipe } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { dateTimeDtoToStringDate } from "@api-rest/mapper/date-dto.mapper";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { PatientMedicalCoverageService } from "@api-rest/services/patient-medical-coverage.service";
import { PatientService } from "@api-rest/services/patient.service";
import { PersonService } from "@api-rest/services/person.service";
import { ContextService } from "@core/services/context.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { DatePipeFormat } from "@core/utils/date.utils";
import { TIME_PATTERN, hasError, beforeTimeDateValidation, futureTimeValidation } from "@core/utils/form.utils";
import { newMoment } from "@core/utils/moment.utils";
import { PatientBasicData } from "@presentation/components/patient-card/patient-card.component";
import { PatientTypeData } from "@presentation/components/patient-type-logo/patient-type-logo.component";
import { PersonalInformation } from "@presentation/components/personal-information/personal-information.component";
import { MapperService } from "@presentation/services/mapper.service";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import * as moment from "moment";
import { Moment } from "moment";
import {
	CompletePatientDto,
	PatientMedicalCoverageDto,
	PersonalInformationDto,
	PersonPhotoDto
} from "@api-rest/api-model";
import { AppFeature } from "@api-rest/api-model";

const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
	selector: 'app-patient-discharge',
	templateUrl: './patient-discharge.component.html',
	styleUrls: ['./patient-discharge.component.scss']
})
export class PatientDischargeComponent implements OnInit {

	todayDate = new Date();
	TIME_PATTERN = TIME_PATTERN;
	minTime: string;
	minDate: string;
	public dischargeForm: FormGroup;
	public minDischargeDate: Date;
	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public personPhoto: PersonPhotoDto;
	public patientTypeData: PatientTypeData;
	public dischargeTypes: {};
	public formSubmited: boolean;
	private readonly routePrefix;
	public dischargeWithoutEpicrisisDisabled: boolean;
	private patientId: number;
	private internmentId: number;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];

	public hasError = hasError;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly personService: PersonService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly intermentEpisodeService: InternmentEpisodeService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly featureFlagService: FeatureFlagService,
		private readonly datePipe: DatePipe,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {

		this.internacionMasterDataService.getDischargeType()
			.subscribe(dischargeTypes => this.dischargeTypes = dischargeTypes);

		this.loadForm();

		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.internmentId = Number(params.get('idInternacion'));
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
					.subscribe((personPhotoDto: PersonPhotoDto) => {
						this.personPhoto = personPhotoDto;
					});
				this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
					.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);

				this.featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)
					.subscribe(epicrisisNotRequired => {
						if (!epicrisisNotRequired) {
							this.setDischargeFormWithEpicrisisRequired();
						}
					});

				this.setMinimumDateForDischarge();
			}
		);

	}

	private loadForm() {
		this.dischargeForm = this.formBuilder.group({
			date: [moment(), [Validators.required]],
			time: [this.datePipe.transform(this.todayDate, DatePipeFormat.SHORT_TIME)],
			dischargeTypeId: [null, [Validators.required]]
		});
		this.setValidators()
		this.dischargeForm.get('date').valueChanges.subscribe((value: Moment) => {
			if (value?.isSame(newMoment(), 'day')) {
				if (this.minDate === (this.datePipe.transform(new Date(), DatePipeFormat.SHORT_DATE))) {
					this.dischargeForm.get('time').setValidators([Validators.required, beforeTimeDateValidation(this.minTime), futureTimeValidation, Validators.pattern(TIME_PATTERN)])
				} else {
					this.dischargeForm.get('time').setValidators([Validators.required, futureTimeValidation, Validators.pattern(TIME_PATTERN)]);
				}
			} else {
				this.dischargeForm.get('time').removeValidators(futureTimeValidation);
			}
			this.dischargeForm.get('time').updateValueAndValidity();
		});
	}

	setValidators(): void {
		if (this.minDate === (this.datePipe.transform(new Date(), DatePipeFormat.SHORT_DATE))) {
			this.dischargeForm.get('time').setValidators([Validators.required, beforeTimeDateValidation(this.minTime), futureTimeValidation, Validators.pattern(TIME_PATTERN)])
		} else {
			this.dischargeForm.get('time').setValidators([Validators.required, futureTimeValidation, Validators.pattern(TIME_PATTERN)]);
		}
	}

	private setDischargeFormWithEpicrisisRequired() {
		this.dischargeForm.controls.dischargeTypeId.disable();
		this.intermentEpisodeService.getPatientDischarge(this.internmentId)
			.subscribe(discharge => {
				this.dischargeForm.controls.dischargeTypeId.setValue(Number(discharge.dischargeTypeId));
			});
	}

	private setMinimumDateForDischarge() {
		this.intermentEpisodeService.getMinDischargeDate(this.internmentId)
			.subscribe(minDischargeDate => {
				this.minDischargeDate = new Date(dateTimeDtoToStringDate(minDischargeDate));
				this.minTime = this.datePipe.transform(this.minDischargeDate, DatePipeFormat.SHORT_TIME);
				this.minDate = this.datePipe.transform(this.minDischargeDate, DatePipeFormat.SHORT_DATE);
			});
	}

	save(): void {
		this.formSubmited = true;
		if (this.dischargeForm.valid) {
			const request = this.dischargeForm.getRawValue();
			const newDatetime = new Date(this.dischargeForm.value.date);
			newDatetime.setHours(this.dischargeForm.value.time.split(":")[0]);
			newDatetime.setMinutes(this.dischargeForm.value.time.split(":")[1]);
			newDatetime.setSeconds(0);
			request.administrativeDischargeDate = newDatetime.toISOString();
			this.intermentEpisodeService.dischargeInternmentEpisode(request, this.internmentId)
				.subscribe(response => {
					this.snackBarService.showSuccess('internaciones.discharge.messages.SUCCESS');
					this.router.navigate([`${this.routePrefix}${ROUTE_PROFILE}${this.patientId}`]);
				}, _ => this.snackBarService.showError('internaciones.discharge.messages.ERROR'));
		}
	}

	back(): void {
		this.formSubmited = false;
		this.router.navigate([`${this.routePrefix}${ROUTE_PROFILE}${this.patientId}`]);
	}
}
