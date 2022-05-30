import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdministrativeDischargeDto, MasterDataInterface, VMedicalDischargeDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { EmergencyCareEntranceType } from '@api-rest/masterdata';
import { EmergencyCareEpisodeAdministrativeDischargeService } from '@api-rest/services/emergency-care-episode-administrative-service.service';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { AMBULANCE } from '@core/constants/validation-constants';
import { ContextService } from '@core/services/context.service';
import { futureTimeValidation, hasError, beforeTimeValidation, TIME_PATTERN } from '@core/utils/form.utils';
import { DateFormat, dateToMoment, momentFormat, newMoment } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';

@Component({
	selector: 'app-administrative-discharge',
	templateUrl: './administrative-discharge.component.html',
	styleUrls: ['./administrative-discharge.component.scss']
})
export class AdministrativeDischargeComponent implements OnInit {

	TIME_PATTERN = TIME_PATTERN;
	hasError = hasError;
	readonly EMERGENCY_CARE_ENTRANCE_TYPE = EmergencyCareEntranceType;
	readonly AMBULANCE = AMBULANCE;

	form: FormGroup;
	hospitalTransports$: Observable<MasterDataInterface<number>[]>;
	administrativeDischarge$: Observable<VMedicalDischargeDto>;
	medicalDischargeOn: Moment;
	today = new Date();

	private episodeId: number;
	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: FormBuilder,
		private readonly contextService: ContextService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly emergencyCareEpisodeAdministrativeDischargeService: EmergencyCareEpisodeAdministrativeDischargeService,
		private readonly emergencyCareEspisodeMedicalDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {

		this.hospitalTransports$ = this.emergencyCareMasterDataService.getEntranceType();

		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [moment(), Validators.required],
				time: [momentFormat(newMoment(), DateFormat.HOUR_MINUTE)],
			}),
			hospitalTransportId: [null],
			ambulanceCompanyId: [null, Validators.maxLength(AMBULANCE.COMPANY_ID.max_length)]
		});

		this.route.paramMap.subscribe(params => {
			this.episodeId = Number(params.get('id'));
			this.administrativeDischarge$ = this.emergencyCareEspisodeMedicalDischargeService.getMedicalDischarge(this.episodeId);
			const medicalDischargeOn$ = this.administrativeDischarge$.pipe(map(s => dateTimeDtoToDate(s.medicalDischargeOn)), map(dateToMoment));
			medicalDischargeOn$.subscribe(medicalDischargeOn => {
				this.medicalDischargeOn = medicalDischargeOn;
				this.setDateTimeValidation(medicalDischargeOn);
			});

		});


	}

	confirm(): void {
		if (this.form.valid) {
			const administrativeDischargeDto: AdministrativeDischargeDto = this.guardiaMapperService.toAdministrativeDischargeDto(this.form.value);
			this.emergencyCareEpisodeAdministrativeDischargeService.newAdministrativeDischarge(this.episodeId, administrativeDischargeDto).subscribe(
				saved => {
					this.snackBarService.showSuccess('guardia.episode.administrative_discharge.messages.SUCCESS');
					this.router.navigateByUrl(`institucion/${this.contextService.institutionId}/guardia`);
				}, error => error?.text ? this.snackBarService.showError(error.text)
					: this.snackBarService.showError('guardia.episode.administrative_discharge.messages.ERROR')
			);
		}
	}

	goToEpisodeDetails(): void {
		this.router.navigateByUrl(`institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}`);
	}

	private setDateTimeValidation(medicalDischargeOn: Moment): void {
		const dateControl: FormGroup = (this.form.controls.dateTime) as FormGroup;
		const timeControl: AbstractControl = dateControl.controls.time;
		timeControl.setValidators([Validators.required, beforeTimeValidation(medicalDischargeOn),
			futureTimeValidation, Validators.pattern(TIME_PATTERN)]);

		this.form.get('dateTime.date').valueChanges.subscribe(
			(selectedDate: Moment) => {
				timeControl.clearValidators();
				requiredAndPatternValidation();
				if (newMoment().isSame(selectedDate, 'day')) {
					beforeTodayValidation();
				}
				if (medicalDischargeOn.isSame(selectedDate, 'day')) {
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
					timeControl.setValidators([existingValidators, beforeTimeValidation(medicalDischargeOn)]);
					timeControl.updateValueAndValidity();
				}
			}
		);
	}

}

export class AdministrativeForm {
	dateTime: {
		date: Moment;
		time: string;
	};
	hospitalTransportId: number;
	ambulanceCompanyId: string;
}
