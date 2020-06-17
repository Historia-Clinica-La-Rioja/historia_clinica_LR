import { Component, OnInit } from '@angular/core';
import { CompletePatientDto, PersonalInformationDto, PatientDischargeDto } from '@api-rest/api-model';
import { Validators, FormGroup, FormBuilder } from '@angular/forms';
import { DateFormat } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import * as moment from 'moment';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { hasError } from '@core/utils/form.utils';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '@presentation/services/mapper.service';
import { PersonService } from '@api-rest/services/person.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { ActivatedRoute, Router } from '@angular/router';

const ROUTE_PROFILE = 'pacientes/profile/';

@Component({
  selector: 'app-medical-discharge',
  templateUrl: './medical-discharge.component.html',
  styleUrls: ['./medical-discharge.component.scss']
})
export class MedicalDischargeComponent implements OnInit {

	public dischargeForm: FormGroup;
	public today: Moment = moment();
	public minMedicalDischargeDate: Date;
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
		this.intermentEpisodeService.getLastUpdateDateOfInternmentEpisode(this.internmentId)
			.subscribe ( lastUpdateDate => this.minMedicalDischargeDate = lastUpdateDate);



	}

	save(): void {
		this.formSubmited = true;
		if (this.dischargeForm.valid) {
			let request: PatientDischargeDto = this.dischargeForm.value;
			request.medicalDischargeDate =this.dischargeForm.value.dischargeDate.format(DateFormat.API_DATE);
			this.route.paramMap.subscribe(
				(params) => {
					 this.intermentEpisodeService.medicalDischargeInternmentEpisode(request,this.internmentId)
					 	.subscribe(response => {
							this.snackBarService.showSuccess('internaciones.discharge.messages.MEDICAL_DISCHARGE_SUCCESS');
							this.router.navigate([this.routePrefix + `internaciones/internacion/` + `${this.internmentId}` + `/paciente/` + `${this.patientId}`]);
						 }, _ => this.snackBarService.showError('internaciones.discharge.messages.MEDICAL_DISCHARGE_ERROR'));
				});
		}
	}

	back(): void {
		this.formSubmited = false;
		this.router.navigate([this.routePrefix + 'internaciones/internacion/'+ this.internmentId + '/paciente/' + this.patientId]);
	}

}
