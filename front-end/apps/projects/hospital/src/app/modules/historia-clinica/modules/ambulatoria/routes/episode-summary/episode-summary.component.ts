import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BasicPatientDto, EmergencyCareEpisodeInProgressDto, PersonPhotoDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { PatientService } from '@api-rest/services/patient.service';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { MapperService } from '@presentation/services/mapper.service';
import { IDENTIFIER_CASES } from 'projects/hospital/src/app/modules/hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-episode-summary',
	templateUrl: './episode-summary.component.html',
	styleUrls: ['./episode-summary.component.scss']
})
export class EpisodeSummaryComponent implements OnInit {

	responseEmergencyCareDto: ResponseEmergencyCareDto;
	identiferCases = IDENTIFIER_CASES;
	
	readonly filterBy = {
		source: 'Guardia',
		id: null
	}
	readonly emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto = {
		inProgress: false,
		id: null
	};

	personPhoto: PersonPhotoDto;
	patient: PatientBasicData;
	internmentEpisodeProcess = false;

	fullInfo$;

	diagnosticReportsStatus$ = this.requestMasterDataService.diagnosticReportStatus();
	studyCategories$ = this.requestMasterDataService.categories();

	constructor(
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly route: ActivatedRoute,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService
	) { }

	ngOnInit(): void {

		this.route.paramMap.subscribe(params => {

			const episodeId = Number(params.get('idEpisodio'));
			this.filterBy.id = episodeId;
			this.emergencyCareEpisodeInProgress.id = episodeId;

			this.emergencyCareEpisodeService.getAdministrative(episodeId).subscribe(
				ad => {
					this.responseEmergencyCareDto = ad;

					this.patientService.getPatientBasicData<BasicPatientDto>(this.responseEmergencyCareDto.patient.id).subscribe(
						patient => {
							this.patient = this.mapperService.toPatientBasicData(patient);
						}
					);

					this.patientService.getPatientPhoto(this.responseEmergencyCareDto.patient.id)
						.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
				}
			);
		})

	}

}
