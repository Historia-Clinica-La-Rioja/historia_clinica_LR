import { Component, OnInit } from '@angular/core';
import { SnomedDto, TriageDto } from "@api-rest/api-model";
import { NewEpisodeService } from '../../services/new-episode.service';

@Component({
	selector: 'app-new-episode-admin-triage',
	templateUrl: './new-episode-admin-triage.component.html',
	styleUrls: ['./new-episode-admin-triage.component.scss']
})
export class NewEpisodeAdminTriageComponent implements OnInit {

	private triage: TriageDto;

	constructor(
		private readonly newEpisodeService: NewEpisodeService
	) {
	}

	ngOnInit(): void {
	}

	setTriage(triage: TriageDto): void {
		this.triage = triage;
	}

}

export class AdministrativeAdmisionDto {
	patient: {
		id: number;
		patientMedicalCoverageId: number;
	};
	reasons: SnomedDto[];
	typeId: number;
	entranceTypeId: number;
	ambulanceCompanyId: number;
	policeIntervention: {
		dateCall: string;
		timeCall: string;
		plateNumber: string;
		firstName: string;
		lastName: string;
	}
}
