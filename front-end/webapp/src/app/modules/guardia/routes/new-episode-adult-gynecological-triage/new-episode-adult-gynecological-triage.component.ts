import { Component, OnInit } from '@angular/core';
import { TriageDto } from "@api-rest/api-model";

@Component({
	selector: 'app-new-episode-adult-gynecological-triage',
	templateUrl: './new-episode-adult-gynecological-triage.component.html',
	styleUrls: ['./new-episode-adult-gynecological-triage.component.scss']
})
export class NewEpisodeAdultGynecologicalTriageComponent implements OnInit {

	constructor() {
	}

	ngOnInit(): void {
	}

	confirmEvent(triage: TriageDto): void {

	}

	cancelEvent(): void {

	}

}
