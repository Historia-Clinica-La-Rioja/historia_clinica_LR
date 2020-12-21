import { Component, OnInit } from '@angular/core';
import { TriageDto } from "@api-rest/api-model";

@Component({
	selector: 'app-new-episode-admin-triage',
	templateUrl: './new-episode-admin-triage.component.html',
	styleUrls: ['./new-episode-admin-triage.component.scss']
})
export class NewEpisodeAdminTriageComponent implements OnInit {

	private triage: TriageDto;

	constructor() {
	}

	ngOnInit(): void {
	}

	setTriage(triage: TriageDto): void {
		this.triage = triage;
	}

}
