import { getIconState } from '@access-management/constants/approval';
import { Component, EventEmitter, Output, Input } from '@angular/core';
import { EReferenceAdministrativeState, ReferenceAdministrativeStateDto, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-approval',
	templateUrl: './approval.component.html',
	styleUrls: ['./approval.component.scss']
})
export class ApprovalComponent {

	regulationDestinationStates: EReferenceAdministrativeState[] = [
		EReferenceAdministrativeState.WAITING_APPROVAL,
		EReferenceAdministrativeState.SUGGESTED_REVISION,
		EReferenceAdministrativeState.APPROVED,
	]

	regulationState: ColoredLabel;
	referenceAdministrativeDto: ReferenceAdministrativeStateDto;
	
	referenceId: number;
	hideReason = false;
	loggedUserCanDoActions = false;

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

	updateReferenceInfomation(referenceDetails$: Observable<ReferenceCompleteDataDto>) {
		referenceDetails$.subscribe(reference => 
			this.setReferenceInformation(reference)
		)
	}

	onNewState(hasChange : boolean){
		if (hasChange){
			this.institutionalReferenceReportService.getReferenceDetail(this.referenceId).subscribe(
				(reference) => 
					this.setReferenceInformation(reference)
				);
		}
	}

	setReferenceInformation(reference: ReferenceCompleteDataDto) {
		this.permissionService.setReferenceAndReportDataAndVisualPermissions(reference, this.permissionService.reportCompleteData);
		this.referenceAdministrativeDto = reference.administrativeState;
		this.regulationState = getIconState[reference.administrativeState.state];
		this.regulationStateEmmiter.next(reference.administrativeState.state);
	}

	onEditingState(editing: boolean){
		if (editing) 
			this.hideReason = true;
		else 
			this.hideReason = false;
	}
}
