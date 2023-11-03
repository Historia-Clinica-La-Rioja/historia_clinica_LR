import { Component, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdministrativeDischargeDto, MasterDataInterface, ResponseEmergencyCareDto, VMedicalDischargeDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { EmergencyCareEntranceType } from '@api-rest/masterdata';
import { EmergencyCareEpisodeAdministrativeDischargeService } from '@api-rest/services/emergency-care-episode-administrative-service.service';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { AMBULANCE } from '@core/constants/validation-constants';
import { ContextService } from '@core/services/context.service';
import { futureTimeValidation, hasError, beforeTimeValidation, TIME_PATTERN } from '@core/utils/form.utils';
import { DateFormat, dateToMomentTimeZone, momentFormat, newMoment } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { ERole } from '@api-rest/api-model';
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';

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

	form: UntypedFormGroup;
	hospitalTransports$: Observable<MasterDataInterface<number>[]>;
	administrativeDischarge$: Observable<VMedicalDischargeDto>;
	medicalDischargeOn: Moment;
	today = new Date();

	private episodeId: number;
	private patientId: number;
	private hasEmergencyCareRelatedRole: boolean;
	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly contextService: ContextService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly emergencyCareEpisodeAdministrativeDischargeService: EmergencyCareEpisodeAdministrativeDischargeService,
		private readonly emergencyCareEspisodeMedicalDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly snackBarService: SnackBarService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly permissionsService: PermissionsService,
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

			this.emergencyCareEspisodeMedicalDischargeService.hasMedicalDischarge(this.episodeId).subscribe((hasMedicalDischarge) => {
				if (hasMedicalDischarge) {
					this.administrativeDischarge$ = this.emergencyCareEspisodeMedicalDischargeService.getMedicalDischarge(this.episodeId);
					const medicalDischargeOn$ = this.administrativeDischarge$.pipe(map(s => dateTimeDtotoLocalDate(s.medicalDischargeOn)), map(dateToMomentTimeZone));
					medicalDischargeOn$.subscribe(medicalDischargeOn => {
						this.medicalDischargeOn = medicalDischargeOn;
						this.setDateTimeValidation(medicalDischargeOn);
					});
				}
			});
			this.emergencyCareEpisodeService.getAdministrative(this.episodeId).subscribe((dto: ResponseEmergencyCareDto) => {
				this.patientId = dto.patient ? dto.patient.id : null;
			});
		});

		this.permissionsService.contextAssignments$().subscribe(
			(userRoles: ERole[]) => {
				this.hasEmergencyCareRelatedRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD]);
			}
		);

	}

	confirm(): void {
		if (this.form.valid) {
			const administrativeDischargeDto: AdministrativeDischargeDto = this.guardiaMapperService.toAdministrativeDischargeDto(this.form.value);
			this.emergencyCareEpisodeAdministrativeDischargeService.newAdministrativeDischarge(this.episodeId, administrativeDischargeDto).subscribe(
				saved => {
					this.snackBarService.showSuccess('guardia.episode.administrative_discharge.messages.SUCCESS');
					if (this.patientId) {
						if (this.hasEmergencyCareRelatedRole) {
							const url = `${this.routePrefix}/ambulatoria/paciente/${this.patientId}`;
							this.router.navigateByUrl(url);
						}
						else {
							const url = `${this.routePrefix}/pacientes/profile/${this.patientId}`;
							this.router.navigateByUrl(url);
						}
					}
					else {
						// TO DO .. This "else" code block can be removed as it is not possible to "Medical Discharge" or "Administrative Discharge" to the emergency care episode without linking the patient
						this.router.navigateByUrl(`${this.routePrefix}/guardia`);
					}
				}, error => error?.text ? this.snackBarService.showError(error.text)
					: this.snackBarService.showError('guardia.episode.administrative_discharge.messages.ERROR')
			);
		}
	}

	goToEpisodeDetails(): void {
		if (this.hasEmergencyCareRelatedRole) {
			const url = `${this.routePrefix}/ambulatoria/paciente/${this.patientId}`;
			this.router.navigateByUrl(url, { state: { toEmergencyCareTab: true } });
		}
		else {
			const url = `${this.routePrefix}/pacientes/profile/${this.patientId}`;
			this.router.navigateByUrl(url);
		}

	}

	private setDateTimeValidation(medicalDischargeOn: Moment): void {
		const dateControl: UntypedFormGroup = (this.form.controls.dateTime) as UntypedFormGroup;
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
