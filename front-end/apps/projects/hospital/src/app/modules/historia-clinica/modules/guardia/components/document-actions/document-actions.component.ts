import { Component, Input } from '@angular/core';
import { DocumentService } from '@api-rest/services/document.service';
import { Item } from '../emergency-care-evolutions/emergency-care-evolutions.component';
import { EmergencyCareEpisodeAdministrativeDischargeService } from '@api-rest/services/emergency-care-episode-administrative-service.service';
import { Observable } from 'rxjs/internal/Observable';



@Component({
	selector: 'app-document-actions',
	templateUrl: './document-actions.component.html',
	styleUrls: ['./document-actions.component.scss']
})
export class DocumentActionsComponent {

	private _episodeId: number;
	readonly TRIAGE = 'TRIAGE';
	hasAdministrativeDischarge$: Observable<boolean>;
	@Input() document: Item;
	@Input() set episodeId(episodeId: number) {
		this._episodeId = episodeId;
		this.setAdministrativeDischarge();
	};

	constructor(
		private readonly documentService: DocumentService,
		private readonly emergencyCareEpisodeAdministrativeDischargeService: EmergencyCareEpisodeAdministrativeDischargeService
	) { }

	downloadDocument() {
		this.documentService.downloadFile({ filename: this.document.summary.docFileName, id: this.document.summary.docId });
	}

	private setAdministrativeDischarge() {
		this.hasAdministrativeDischarge$ = this.emergencyCareEpisodeAdministrativeDischargeService.hasAdministrativeDischarge(this._episodeId);
	}

}
