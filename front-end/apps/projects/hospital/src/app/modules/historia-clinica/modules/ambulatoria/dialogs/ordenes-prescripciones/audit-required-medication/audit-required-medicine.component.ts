import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { Observable, of } from 'rxjs';

@Component({
	selector: 'app-audit-required-medicine',
	templateUrl: './audit-required-medicine.component.html',
	styleUrls: ['./audit-required-medicine.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class AuditRequiredMedicationComponent implements OnInit {

	groupAuditRequired$:Observable<string[]>


	@Input() set auditRequiredData( requiredTexts: string[]) {
		this.groupAuditRequired$ =  of(requiredTexts)
	};

	constructor(
	) {}

	ngOnInit(): void {
	}


}

export interface DataMedicationGroup {
	problem: string,
	medication: SnomedDto
}