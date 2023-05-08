import { Component, Input, OnInit } from '@angular/core';
import { AuditablePatientInfoDto } from '@api-rest/api-model';

@Component({
	selector: 'app-message-flagged-for-audit',
	templateUrl: './message-flagged-for-audit.component.html',
	styleUrls: ['./message-flagged-for-audit.component.scss']
})
export class MessageFlaggedForAuditComponent implements OnInit {
	@Input() auditablePatientInfo: AuditablePatientInfoDto;
	constructor() { }

	ngOnInit(): void {
	}

}
