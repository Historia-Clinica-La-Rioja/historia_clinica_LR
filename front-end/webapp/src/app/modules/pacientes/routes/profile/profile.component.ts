import { Component, OnInit } from '@angular/core';
import { PersonalInformationDto, CompletePatientDto, PatientMedicalCoverageDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '../../../presentation/services/mapper.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PersonService } from '@api-rest/services/person.service';
import { PatientBasicData } from 'src/app/modules/presentation/components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { ContextService } from '@core/services/context.service';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { DateFormat } from '@core/utils/moment.utils';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';

const ROUTE_NEW_INTERNMENT = 'internaciones/internacion/new';
const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_INTERNMENT_EPISODE_SUFIX = '/paciente/';
const ROUTE_EDIT_PATIENT = 'pacientes/edit';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public personPhoto: PersonPhotoDto;
	public codigoColor: string;
	private patientId: number;
	private readonly routePrefix;
	public internmentEpisode;

	constructor(
		private patientService: PatientService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private personService: PersonService,
		private contextService: ContextService,
		private internmentPatientService: InternmentPatientService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('id'));
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
					.subscribe(completeData => {
						this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
						this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
						this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
							.subscribe(personInformationData => {
								this.personalInformation = this.mapperService.toPersonalInformationData(completeData, personInformationData);
							});
						this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
							.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);
					});

				this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId)
					.subscribe(internmentEpisodeProcessDto => {
						if (internmentEpisodeProcessDto) {
							this.internmentEpisode = internmentEpisodeProcessDto;
						}
					});

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
			});

	}

	goNewInternment(): void {
		this.router.navigate([this.routePrefix + ROUTE_NEW_INTERNMENT],
			{
				queryParams: { patientId: this.patientId }
			});
	}

	goInternmentEpisode(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this.internmentEpisode.id + ROUTE_INTERNMENT_EPISODE_SUFIX + this.patientId]);
	}

	goToEditProfile(): void {
		let person = {
			id: this.patientBasicData.id,
		};
		this.router.navigate([this.routePrefix + ROUTE_EDIT_PATIENT], {
			queryParams: person
		});
	}

}
