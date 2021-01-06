import { Component, OnInit } from '@angular/core';
import { EmergencyCareDto, SnomedDto, TriageDto } from "@api-rest/api-model";
import { AdministrativeAdmissionDto, NewEpisodeService } from '../../services/new-episode.service';
import { EmergencyCareEpisodeService } from "@api-rest/services/emergency-care-episode.service";
import { Router } from "@angular/router";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { ContextService } from "@core/services/context.service";

const ROUTE_EMERGENCY_CARE = 'guardia/';

@Component({
	selector: 'app-new-episode-admin-triage',
	templateUrl: './new-episode-admin-triage.component.html',
	styleUrls: ['./new-episode-admin-triage.component.scss']
})
export class NewEpisodeAdminTriageComponent implements OnInit {

	private triage: TriageDto;
	private administrativeAdmissionDto: AdministrativeAdmissionDto;
	private emergencyCareDto: EmergencyCareDto = {
		administrative: {},
		triage: {
			categoryId: -1,
			doctorsOfficeId: -1
		}
	};
	private readonly routePrefix;

	constructor(private readonly newEpisodeService: NewEpisodeService,
	            private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
	            private router: Router,
	            private snackBarService: SnackBarService,
	            private contextService: ContextService) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.administrativeAdmissionDto = this.newEpisodeService.getAdministrativeAdmission();
	}

	confirmEvent(triage: TriageDto): void {
		this.triage = triage;
		this.emergencyCareDto.triage = this.triage;
		//TODO mapper administrativeAdmissionDto to emergencyCareDto
		this.emergencyCareEpisodeService.createAdministrative(this.emergencyCareDto).subscribe(
			emergencyCareId =>
				this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/episodio' +  emergencyCareId]),
			_ => this.snackBarService.showError('Ocurrio un error al intentar crear el episodio')
		);
	}

	cancelEvent(): void {
		//TODO send parameters
		this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/nuevo-episodio/administrativa']);
	}

}
