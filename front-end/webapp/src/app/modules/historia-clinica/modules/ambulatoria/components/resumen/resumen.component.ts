import { Component, OnInit } from '@angular/core';
import { AllergyConditionDto } from "@api-rest/api-model";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";

@Component({
	selector: 'app-resumen',
	templateUrl: './resumen.component.html',
	styleUrls: ['./resumen.component.scss']
})
export class ResumenComponent implements OnInit {

	public allergies$: Observable<AllergyConditionDto[]>;
	public patientId: number;

	constructor(private hceGeneralStateService: HceGeneralStateService,
				private route: ActivatedRoute) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('id'));
				this.initSummaries();
			});
	}

	initSummaries() {
		this.allergies$ = this.hceGeneralStateService.getAllergies(this.patientId);
	}

}
