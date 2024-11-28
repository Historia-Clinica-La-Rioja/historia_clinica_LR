import { getIconState } from '@access-management/constants/approval';
import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { EReferenceAdministrativeState, ReferenceAdministrativeStateDto, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
@Component({
	selector: 'app-approval',
	templateUrl: './approval.component.html',
	styleUrls: ['./approval.component.scss']
})
export class ApprovalComponent implements OnInit {

	regulationState: ColoredLabel;
	referenceAdministrativeDto: ReferenceAdministrativeStateDto;
	
	referenceId: number;
	hideReason = false;
	loggedUserCanDoActions = false;

	regulationDestinationStates: EReferenceAdministrativeState[] = [
		EReferenceAdministrativeState.WAITING_APPROVAL,
		EReferenceAdministrativeState.SUGGESTED_REVISION,
		EReferenceAdministrativeState.APPROVED,
	]

	@Input() set referenceCompleteDataDto(reference: ReferenceCompleteDataDto) {
		this.referenceAdministrativeDto = reference.administrativeState;
		this.referenceId = reference.reference.id;
		if (reference.administrativeState)
			this.regulationState = getIconState[reference.administrativeState.state];
		else
			this.regulationState = getIconState[this.regulationDestinationStates[1]];		
	};
	@Input() canEditApprovalState: boolean;
	@Output() regulationStateEmmiter: EventEmitter<EReferenceAdministrativeState> = new EventEmitter<EReferenceAdministrativeState>();

	constructor(
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		public permissionService: ReferencePermissionCombinationService
	) { }

	ngOnInit(): void {
	}

	onNewState(hasChange : boolean){
		if (hasChange){
			this.institutionalReferenceReportService.getReferenceDetail(this.referenceId).subscribe(
				(result) => {
					this.permissionService.setReferenceAndReportDataAndVisualPermissions(result, this.permissionService.reportCompleteData);
					this.referenceAdministrativeDto = result.administrativeState;
					this.regulationState = getIconState[result.administrativeState.state];
					this.regulationStateEmmiter.next(result.administrativeState.state);
				});
		}
	}

	onEditingState(editing: boolean){
		if (editing) 
			this.hideReason = true;
		else 
			this.hideReason = false;
	}
}
