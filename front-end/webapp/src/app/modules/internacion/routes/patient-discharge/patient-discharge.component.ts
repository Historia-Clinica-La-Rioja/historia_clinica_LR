import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { Moment } from 'moment';
import * as moment from 'moment';
import { PatientService } from '@api-rest/services/patient.service';
import { CompletePatientDto, PersonalInformationDto, PatientDischargeDto } from '@api-rest/api-model';
import { MapperService } from '@presentation/services/mapper.service';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PersonService } from '@api-rest/services/person.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { ActivatedRoute, Router } from '@angular/router';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormat } from '@core/utils/moment.utils';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { ContextService } from '@core/services/context.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
  selector: 'app-patient-discharge',
  templateUrl: './patient-discharge.component.html',
  styleUrls: ['./patient-discharge.component.scss']
})
export class PatientDischargeComponent implements OnInit {

	public dischargeForm: FormGroup;
	public today: Moment = moment();
	public internmentEpisodeEntryDate: Date;
	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;
	public dischargeTypes: {};
	public formSubmited: boolean;
	private readonly routePrefix;

	private patientId: number;
	private internmentId: number;

	public hasError = hasError;
	constructor(
		private formBuilder: FormBuilder,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly personService: PersonService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly intermentEpisodeService: InternmentEpisodeService,
		private readonly snackBarService: SnackBarService,
		private contextService: ContextService,
		private route: ActivatedRoute,
		private router: Router
		) {
			this.routePrefix = 'institucion/' + this.contextService.institutionId + '/'; }

	ngOnInit(): void {

		this.internacionMasterDataService.getDischargeType()
			.subscribe(dischargeTypes => this.dischargeTypes = dischargeTypes);

		this.dischargeForm = this.formBuilder.group({
			dischargeDate: [null, [Validators.required]],
			dischargeTypeId: [null, [Validators.required]]
		});
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
			}
		);
		this.intermentEpisodeService.getInternmentEpisode(this.internmentId)
			.subscribe ( internmentEpisode => this.internmentEpisodeEntryDate = internmentEpisode.entryDate)

	}

	save(): void {
		this.formSubmited = true;
		if (this.dischargeForm.valid) {
			let request = this.dischargeForm.value;
			request.dischargeDate =this.dischargeForm.value.dischargeDate.format(DateFormat.API_DATE);
			this.route.paramMap.subscribe(
				(params) => {
					 this.intermentEpisodeService.dischargeInternmentEpisode<PatientDischargeDto>(request,this.internmentId)
					 	.subscribe(response => {
							this.snackBarService.showSuccess('internaciones.discharge.messages.SUCCESS');
							this.router.navigate([this.routePrefix + ROUTE_PROFILE + `${this.patientId}`]);
						 }, _ => this.snackBarService.showError('internaciones.discharge.messages.ERROR'));
				});
		}
	}

	back(): void {
		this.formSubmited = false;
		this.router.navigate([this.routePrefix + 'internaciones/internacion/'+ this.internmentId + '/paciente/' + this.patientId]);
	}
}
