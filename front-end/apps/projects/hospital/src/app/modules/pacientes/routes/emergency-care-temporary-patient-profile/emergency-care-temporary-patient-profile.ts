
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmergencyCareEpisodeSummaryService } from '@api-rest/services/emergency-care-episode-summary.service';
import { map } from 'rxjs/operators';

@Component({
	selector: 'app-emergency-care-temporary-patient-profile',
	templateUrl: './emergency-care-temporary-patient-profile.html',
	styleUrls: ['./emergency-care-temporary-patient-profile.scss']
})
export class EmergencyCareTemporaryPatientProfile {


	episodeId: number;
	constructor(
		private readonly route: ActivatedRoute,
		private readonly emergencyCareEpisodeSummaryService: EmergencyCareEpisodeSummaryService,
	) {
		this.route.paramMap.subscribe(
			(params) => {
				const patientId = Number(params.get('id'));
				this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgress(patientId)
					.pipe(map(r => r.id)).subscribe(rr => this.episodeId = rr)
			}
		)

	}

	patientSelected(patient) {

	}
}
