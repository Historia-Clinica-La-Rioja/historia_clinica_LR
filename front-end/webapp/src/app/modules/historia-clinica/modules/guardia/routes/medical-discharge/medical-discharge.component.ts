import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DiagnosisDto, MasterDataInterface, MedicalDischargeDto } from '@api-rest/api-model';
import { EmergencyCareEspisodeDischargeService } from '@api-rest/services/emergency-care-espisode-discharge.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { ContextService } from '@core/services/context.service';
import { hasError, TIME_PATTERN } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
import { SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';
import { ProblemasService } from '../../../../services/problemas-nueva-consulta.service';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';

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
	today = new Date();
	formSubmited = false;
	private episodeId: number;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: FormBuilder,
		private readonly contextService: ContextService,
		private readonly emergencyCareEspisodeDischargeService: EmergencyCareEspisodeDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,

	) {
		this.problemasService = new ProblemasService(formBuilder, this.snomedService);
	 }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [null, Validators.required],
				time: [null, Validators.required],
			}),
			autopsy: [null],
			dischargeTypeId: [null, Validators.required]
		});

		this.route.paramMap.subscribe(params => {
			this.episodeId = Number(params.get('id'));
		});

		this.dischargeTypes$ = this.emergencyCareMasterDataService.getDischargeType();
	}

	confirm(): void {
		this.formSubmited = true;
		if (this.form.valid && this.problemasService.getProblemas().length) {
			const s: MedicalDischargeForm = {... this.form.value, problems: this.problemasService.getProblemas()};
			const medicalCoverageDto: MedicalDischargeDto = this.guardiaMapperService.formToMedicalDischargeDto(s);
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
