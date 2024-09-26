import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { MedicineGroupAuditRequiredDto, SnomedDto } from '@api-rest/api-model';
import { MedicineGroupService } from '@api-rest/services/medicine-group.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-audit-required-medicine',
	templateUrl: './audit-required-medicine.component.html',
	styleUrls: ['./audit-required-medicine.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class AuditRequiredMedicationComponent implements OnInit {

	groupAuditRequired$:Observable<MedicineGroupAuditRequiredDto[]>

	@Input() set auditRequiredData( value: DataMedicationGroup) {
		this.groupAuditRequired$ = this.medicineGroupService.getMedicineGroupAuditRequired(value.medication.sctid,value.problem)
	};

	constructor(
		private readonly medicineGroupService: MedicineGroupService,
	) {}

	ngOnInit(): void {
	}


}

export interface DataMedicationGroup {
	problem: string,
	medication: SnomedDto
}