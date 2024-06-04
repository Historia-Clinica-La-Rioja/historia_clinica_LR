import { Component, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AMedicalDischargeDto, DiagnosisDto, MasterDataInterface, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DischargeTypes } from '@api-rest/masterdata';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { ContextService } from '@core/services/context.service';
import { sortBy } from '@core/utils/array.utils';
import { futureTimeValidationDate, hasError, beforeTimeValidationDate, TIME_PATTERN } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { ProblemasService } from '../../../../services/problemas.service';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormat, MIN_DATE } from "@core/utils/date.utils";
import { format, isSameDay } from 'date-fns';

@Component({
	selector: 'app-medical-discharge',
	templateUrl: './medical-discharge.component.html',
	styleUrls: ['./medical-discharge.component.scss']
})
export class MedicalDischargeComponent implements OnInit {

	TIME_PATTERN = TIME_PATTERN;
	hasError = hasError;

	form: UntypedFormGroup;
	diagnosticos: DiagnosisDto[] = [];
	dischargeTypes$: Observable<MasterDataInterface<number>[]>;

	problemasService: ProblemasService;
	severityTypes: any[];
	today = new Date();
	episodeCreatedOn: Date;
	formSubmited = false;
	isLoading = false;
	private episodeId: number;
	private patientId: number;

	minDate = MIN_DATE;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly contextService: ContextService,
		private readonly emergencyCareEspisodeDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly internacionMasterDataService: InternacionMasterDataService,

	) {
		this.problemasService = new ProblemasService(formBuilder, this.snomedService, this.snackBarService);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [new Date(), Validators.required],
				time: [format(new Date(), DateFormat.HOUR_MINUTE)],
			}),
			autopsy: [null],
			dischargeTypeId: [DischargeTypes.ALTA_MEDICA, Validators.required]
		});

		this.route.paramMap.subscribe(params => {
			this.episodeId = Number(params.get('id'));
			const episodeCreatedOn$ = this.emergencyCareEpisodeService.getCreationDate(this.episodeId)
				.pipe(map(dateTimeDtoToDate));

			episodeCreatedOn$.subscribe(episodeCreatedOn => {
				this.episodeCreatedOn = episodeCreatedOn;
				this.setDateTimeValidation(episodeCreatedOn);
			});

			this.emergencyCareEpisodeService.getAdministrative(this.episodeId).subscribe((dto: ResponseEmergencyCareDto) => {
				this.patientId = dto.patient ? dto.patient.id : null;
			});
		});
		const sortByDescription = sortBy('description');
		this.dischargeTypes$ = this.emergencyCareMasterDataService.getDischargeType()
			.pipe(
				map((dischargeTypes) => sortByDescription(dischargeTypes))
			);

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.problemasService.setSeverityTypes(healthConditionSeverities);
		});
	}

	dischargedDateChanged(date: Date) {
		this.form.controls.dateTime.get('date').setValue(date);
	}

	problemStartDateChanged(date: Date) {
		this.problemasService.getForm().controls.fechaInicio.setValue(date);
	}

	problemEndDateChanged(date: Date) {
		this.problemasService.getForm().controls.fechaFin.setValue(date);
	}

	confirm(): void {
		this.formSubmited = true;
		this.isLoading = true;
		if (this.form.valid && this.problemasService.getProblemas().length) {
			const s: MedicalDischargeForm = { ... this.form.value, problems: this.problemasService.getProblemas() };
			const medicalCoverageDto: AMedicalDischargeDto = this.guardiaMapperService.formToAMedicalDischargeDto(s);
			this.emergencyCareEspisodeDischargeService.newMedicalDischarge
				(this.episodeId, medicalCoverageDto).subscribe(
					saved => {
						this.isLoading = false
						if (saved) {
							this.goToEpisodeDetails();
							this.snackBarService.showSuccess('guardia.episode.medical_discharge.messages.SUCCESS');
						}
					}, error => {
						this.isLoading = false
						this.snackBarService.showError(error.message ? error.message : 'guardia.episode.medical_discharge.messages.ERROR');
					}
				);
		}
	}

	goToEpisodeDetails(): void {
		const url = `institucion/${this.contextService.institutionId}/ambulatoria/paciente/${this.patientId}`;
		this.router.navigateByUrl(url, { state: { toEmergencyCareTab: true } });

	}

	private setDateTimeValidation(episodeCreatedOn: Date): void {
		const dateControl: UntypedFormGroup = (this.form.controls.dateTime) as UntypedFormGroup;
		const timeControl: AbstractControl = dateControl.controls.time;
		timeControl.setValidators([Validators.required, beforeTimeValidationDate(episodeCreatedOn),
			futureTimeValidationDate, Validators.pattern(TIME_PATTERN)]);

		this.form.get('dateTime.date').valueChanges.subscribe(
			selectedDate => {
				timeControl.clearValidators();
				requiredAndPatternValidation();
				if (isSameDay(new Date(), new Date(selectedDate))) {
					beforeTodayValidation();
				}
				if (isSameDay(episodeCreatedOn, new Date(selectedDate))) {
					afterEpisodeCreationValidation();
				}

				function requiredAndPatternValidation(): void {
					timeControl.setValidators([Validators.required, Validators.pattern(TIME_PATTERN)]);
					timeControl.updateValueAndValidity();
				}

				function beforeTodayValidation(): void {
					const existingValidators = timeControl.validator;
					timeControl.setValidators([existingValidators, futureTimeValidationDate]);
					timeControl.updateValueAndValidity();
				}

				function afterEpisodeCreationValidation(): void {
					const existingValidators = timeControl.validator;
					timeControl.setValidators([existingValidators, beforeTimeValidationDate(episodeCreatedOn)]);
					timeControl.updateValueAndValidity();
				}
			}
		);
	}
}

export class MedicalDischargeForm {
	dateTime: {
		date: Date,
		time: string
	};
	autopsy: boolean;
	dischargeTypeId: number;
	problems: any[];
}
