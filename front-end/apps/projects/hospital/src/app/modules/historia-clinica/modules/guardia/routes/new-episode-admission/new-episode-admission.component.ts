import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { AdministrativeAdmission, NewEpisodeService } from '../../services/new-episode.service';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';

@Component({
	selector: 'app-new-episode-admission',
	templateUrl: './new-episode-admission.component.html',
	styleUrls: ['./new-episode-admission.component.scss']
})
export class NewEpisodeAdmissionComponent implements OnInit {

	initData: AdministrativeAdmission;

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	constructor(private readonly newEpisodeService: NewEpisodeService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly router: Router,
		private readonly contextService: ContextService,) { }

	ngOnInit(): void {

		if (window.history.state.commingBack) {
			this.initData = this.newEpisodeService.getAdministrativeAdmission();
		}

	}

	confirm(data): void {
		this.newEpisodeService.setAdministrativeAdmission(data);
		this.goToTriage(data);
	}

	goToTriage(administrativeAdmission: AdministrativeAdmission): void {
		this.newEpisodeService.setAdministrativeAdmission(administrativeAdmission);
		this.triageDefinitionsService.getTriagePath(administrativeAdmission?.emergencyCareTypeId)
			.subscribe(({ url }) => this.router.navigateByUrl(url));
	}

	goBack(): void {
		const url = `${this.routePrefix}/guardia`;
		this.router.navigateByUrl(url);
	}
}
