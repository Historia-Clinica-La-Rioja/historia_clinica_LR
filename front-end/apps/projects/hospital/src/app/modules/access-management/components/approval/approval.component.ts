import { getIconState } from '@access-management/constants/approval';
import { Component, EventEmitter, Output, Input, OnInit } from '@angular/core';
import { EReferenceAdministrativeState, ReferenceAdministrativeStateDto, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { AccountService } from '@api-rest/services/account.service';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
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

	hideReason = false;
	loggedUserCanDoActions = false;

	@Input() referenceCompleteDataDto: ReferenceCompleteDataDto;
	@Input() set approval(value: ReferenceAdministrativeStateDto) {
		this.referenceAdministrativeDto = value;
		this.regulationState = getIconState[value.state];
	};
	@Input() canEditApprovalState: boolean;
	@Output() regulationStateEmmiter: EventEmitter<EReferenceAdministrativeState> = new EventEmitter<EReferenceAdministrativeState>();

	constructor(
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly accountService: AccountService,
		public referencePermissionService: ReferencePermissionCombinationService
	) { }

	ngOnInit(): void {
		const hasSuggestedState = this.referenceAdministrativeDto.state === this.referencePermissionService.referenceDestinationState.suggestedRevision;
		this.accountService.getInfo().subscribe(loggedUserInfo =>
			this.loggedUserCanDoActions = loggedUserInfo.id === this.referenceCompleteDataDto.reference.createdBy && hasSuggestedState
		);
	}

	onNewState(hasChange : boolean){
		if (hasChange){
			if (this.referencePermissionService.isRoleGestorInstitucional)
				this.institutionalReferenceReportService.getReferenceDetail(this.referenceCompleteDataDto.reference.id).subscribe(
					(result) => {
						this.referenceAdministrativeDto = result.administrativeState;
						this.regulationState = getIconState[result.regulation.state];
						this.regulationStateEmmiter.next(result.administrativeState.state);
					});
			else 
				this.institutionalNetworkReferenceReportService.getReferenceDetail(this.referenceCompleteDataDto.reference.id).subscribe(
					(result) => {
						this.referenceAdministrativeDto = result.administrativeState;
						this.regulationState = getIconState[result.regulation.state];
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
