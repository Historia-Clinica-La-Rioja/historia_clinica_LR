import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AMedicalDischargeDto, DiagnosisDto, MasterDataInterface } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DischargeTypes } from '@api-rest/masterdata';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { ContextService } from '@core/services/context.service';
import { sortBy } from '@core/utils/array.utils';
import { futureTimeValidation, hasError, beforeTimeValidation, TIME_PATTERN } from '@core/utils/form.utils';
import { DateFormat, dateToMoment, momentFormat, newMoment } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { ProblemasService } from '../../../../services/problemas.service';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';
import { MIN_DATE } from "@core/utils/date.utils";
import * as moment from 'moment';

@Component({
	selector: 'app-medical-discharge',
	templateUrl: './medical-discharge.component.html',
	styleUrls: ['./medical-discharge.component.scss']
})
export class MedicalDischargeComponent implements OnInit {

	TIME_PATTERN = TIME_PATTERN;
	hasError = hasError;

	form: FormGroup;
	diagnosticos: DiagnosisDto[] = [];
	dischargeTypes$: Observable<MasterDataInterface<number>[]>;

	problemasService: ProblemasService;
	severityTypes: any[];
	today = new Date();
	episodeCreatedOn: Moment;
	formSubmited = false;
	private episodeId: number;

	minDate = MIN_DATE;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: FormBuilder,
		private readonly contextService: ContextService,
		private readonly emergencyCareEspisodeDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly internacionMasterDataService: InternacionMasterDataService,

	) {
		this.problemasService = new ProblemasService(formBuilder, this.snomedService,this.snackBarService);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [moment(), Validators.required],
				time: [momentFormat(newMoment(), DateFormat.HOUR_MINUTE)]
			}),
			autopsy: [null],
			dischargeTypeId: [DischargeTypes.ALTA_MEDICA, Validators.required]
		});

		this.route.paramMap.subscribe(params => {
			this.episodeId = Number(params.get('id'));
			const episodeCreatedOn$ = this.emergencyCareEpisodeService.getCreationDate(this.episodeId)
				.pipe(map(dateTimeDtoToDate), map(dateToMoment));

			episodeCreatedOn$.subscribe(episodeCreatedOn => {
				this.episodeCreatedOn = episodeCreatedOn;
				this.setDateTimeValidation(episodeCreatedOn);
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

	confirm(): void {
		this.formSubmited = true;
		if (this.form.valid && this.problemasService.getProblemas().length) {
			const s: MedicalDischargeForm = { ... this.form.value, problems: this.problemasService.getProblemas() };
			const medicalCoverageDto: AMedicalDischargeDto = this.guardiaMapperService.formToAMedicalDischargeDto(s);
			this.emergencyCareEspisodeDischargeService.newMedicalDischarge
				(this.episodeId, medicalCoverageDto).subscribe(
					saved => {
						if (saved) {
							this.goToEpisodeDetails();
							this.snackBarService.showSuccess('guardia.episode.medical_discharge.messages.SUCCESS');
						}
					}, _ => this.snackBarService.showError('guardia.episode.medical_discharge.messages.ERROR')
				);
		}
	}

	goToEpisodeDetails(): void {
		this.router.navigateByUrl(`institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}`);
	}

	private setDateTimeValidation(episodeCreatedOn: Moment): void {
		const dateControl: FormGroup = (this.form.controls.dateTime) as FormGroup;
		const timeControl: AbstractControl = dateControl.controls.time;
		timeControl.setValidators([Validators.required, beforeTimeValidation(episodeCreatedOn),
			futureTimeValidation, Validators.pattern(TIME_PATTERN)]);

		this.form.get('dateTime.date').valueChanges.subscribe(
			(selectedDate: Moment) => {
				timeControl.clearValidators();
				requiredAndPatternValidation();
				if (newMoment().isSame(selectedDate, 'day')) {
					beforeTodayValidation();
				}
				if (episodeCreatedOn.isSame(selectedDate, 'day')) {
					afterEpisodeCreationValidation();
				}

				function requiredAndPatternValidation(): void {
					timeControl.setValidators([Validators.required, Validators.pattern(TIME_PATTERN)]);
					timeControl.updateValueAndValidity();
				}

				function beforeTodayValidation(): void {
					const existingValidators = timeControl.validator;
					timeControl.setValidators([existingValidators, futureTimeValidation]);
					timeControl.updateValueAndValidity();
				}

				function afterEpisodeCreationValidation(): void {
					const existingValidators = timeControl.validator;
					timeControl.setValidators([existingValidators, beforeTimeValidation(episodeCreatedOn)]);
					timeControl.updateValueAndValidity();
				}
			}
		);
	}
}

export class MedicalDischargeForm {
	dateTime: {
		date: Moment,
		time: string
	};
	autopsy: boolean;
	dischargeTypeId: number;
	problems: any[];
}
