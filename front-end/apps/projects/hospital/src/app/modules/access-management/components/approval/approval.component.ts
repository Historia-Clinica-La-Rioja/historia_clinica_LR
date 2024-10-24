import { getIconState } from '@access-management/constants/approval';
import { Component, Input, OnInit } from '@angular/core';
import { EReferenceRegulationState, ERole, ReferenceCompleteDataDto, ReferenceRegulationDto } from '@api-rest/api-model';
import { AccountService } from '@api-rest/services/account.service';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-approval',
	templateUrl: './approval.component.html',
	styleUrls: ['./approval.component.scss']
})
export class ApprovalComponent implements OnInit {

	regulationState: ColoredLabel;
	referenceRegulationDto: ReferenceRegulationDto;
	loggedUserCanDoActions = false;
	referenceApprovalState = {
		approved: EReferenceRegulationState.APPROVED,
		pending: EReferenceRegulationState.WAITING_APPROVAL,
	}
	hasGestorRole = false;
	hasGestorInstitucionalRole = false;
	hideReason = false;

	@Input() referenceCompleteDataDto: ReferenceCompleteDataDto;
	@Input() set approval(value: ReferenceRegulationDto) {
		this.referenceRegulationDto = value;
		this.regulationState = getIconState[value.state];
	};
	@Input() canEditApprovalState: boolean;

	constructor(
		private readonly accountService: AccountService,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly permissionsService: PermissionsService,
	) { }

	ngOnInit(): void {
		const hasSuggestedState = this.referenceRegulationDto.state === EReferenceRegulationState.SUGGESTED_REVISION;
		this.accountService.getInfo().subscribe(loggedUserInfo =>
			this.loggedUserCanDoActions = loggedUserInfo.id === this.referenceCompleteDataDto.reference.createdBy && hasSuggestedState
		);
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasGestorRole = anyMatch<ERole>(userRoles, [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_REGIONAL, ERole.GESTOR_DE_ACCESO_LOCAL]);
			this.hasGestorInstitucionalRole = anyMatch<ERole>(userRoles, [ERole.GESTOR_DE_ACCESO_INSTITUCIONAL]);
		});
	}

	onNewState(hasChange : boolean){
		if (hasChange){
			if (this.hasGestorInstitucionalRole)
				this.institutionalReferenceReportService.getReferenceDetail(this.referenceCompleteDataDto.reference.id).subscribe(
					(result) => {
						this.referenceRegulationDto = result.regulation;
						this.regulationState = getIconState[result.regulation.state];
					});
			else 
				this.institutionalNetworkReferenceReportService.getReferenceDetail(this.referenceCompleteDataDto.reference.id).subscribe(
					(result) => {
						this.referenceRegulationDto = result.regulation;
						this.regulationState = getIconState[result.regulation.state];
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
