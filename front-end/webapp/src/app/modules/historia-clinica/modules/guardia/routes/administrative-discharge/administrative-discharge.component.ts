import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AdministrativeDischargeDto, MasterDataInterface, VMedicalDischargeDto } from '@api-rest/api-model';
import { EmergencyCareEntranceType } from '@api-rest/masterdata';
import { EmergencyCareEspisodeDischargeService } from '@api-rest/services/emergency-care-espisode-discharge.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { AMBULANCE } from '@core/constants/validation-constants';
import { ContextService } from '@core/services/context.service';
import { hasError, TIME_PATTERN } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
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

	private episodeId: number;
	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: FormBuilder,
		private readonly contextService: ContextService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly emergencyCareEspisodeDischargeService: EmergencyCareEspisodeDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {

		this.hospitalTransports$ = this.emergencyCareMasterDataService.getEntranceType();

		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [null, Validators.required],
				time: [null, Validators.required],
			}),
			hospitalTransportId: [null],
			ambulanceCompanyId: [null]
		});

		this.route.paramMap.subscribe(params => {
			this.episodeId = Number(params.get('id'));
		});

		this.administrativeDischarge$ = this.emergencyCareEspisodeDischargeService.getMedicalDischarge(this.episodeId);
	}

	cancel(): void {
		this.goToEpisodeDetails();
	}


	confirm(): void {
		if (this.form.valid) {
			const administrativeDischargeDto: AdministrativeDischargeDto = this.guardiaMapperService.toAdministrativeDischargeDto(this.form.value);
			this.emergencyCareEspisodeDischargeService.newAdministrativeDischarge(this.episodeId, administrativeDischargeDto).subscribe(
				saved => {
					this.snackBarService.showSuccess('guardia.episode.administrative_discharge.messages.SUCCESS');
					this.router.navigateByUrl(`institucion/${this.contextService.institutionId}/guardia`);
				}, _ => this.snackBarService.showSuccess('guardia.episode.administrative_discharge.messages.ERROR')
			);
		}
	}

	private goToEpisodeDetails(): void {
		this.router.navigateByUrl(`institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}`);
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
