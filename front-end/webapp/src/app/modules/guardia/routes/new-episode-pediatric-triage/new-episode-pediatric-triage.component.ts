import { Component, OnInit } from '@angular/core';
import { TriageDto } from "@api-rest/api-model";

@Component({
	selector: 'app-new-episode-pediatric-triage',
	templateUrl: './new-episode-pediatric-triage.component.html',
	styleUrls: ['./new-episode-pediatric-triage.component.scss']
})
export class NewEpisodePediatricTriageComponent implements OnInit {

	constructor() {
	}

	ngOnInit(): void {
	}

	confirmEvent(triage: TriageDto): void {

	}

	cancelEvent(): void {

	}
}
