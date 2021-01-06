import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { TriageService } from "@api-rest/services/triage.service";
import { TriageAdministrativeDto } from "@api-rest/api-model";

@Component({
	selector: 'app-administrative-triage-dialog',
	templateUrl: './administrative-triage-dialog.component.html',
	styleUrls: ['./administrative-triage-dialog.component.scss']
})
export class AdministrativeTriageDialogComponent implements OnInit {

	private triage: TriageAdministrativeDto;

	constructor(private triageService: TriageService,
			@Inject(MAT_DIALOG_DATA) public data: {episodeId: number}) {
	}

	ngOnInit(): void {
	}

	setTriage(triage: TriageAdministrativeDto): void {
		this.triage = triage;
		this.triageService.createAdministrative(this.data.episodeId, this.triage).subscribe();
	}
}
