import { getIconState } from '@access-management/constants/approval';
import { Component, Input, OnInit } from '@angular/core';
import { EReferenceRegulationState, ReferenceCompleteDataDto, ReferenceRegulationDto } from '@api-rest/api-model';
import { AccountService } from '@api-rest/services/account.service';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
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

	@Input() referenceCompleteDataDto: ReferenceCompleteDataDto;
	@Input() set approval(value: ReferenceRegulationDto) {
		this.referenceRegulationDto = value;
		this.regulationState = getIconState[value.state];
	};

	constructor(
		private readonly accountService: AccountService,
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
	) { }

	ngOnInit(): void {
		const hasSuggestedState = this.referenceRegulationDto.state === EReferenceRegulationState.SUGGESTED_REVISION;
		this.accountService.getInfo().subscribe(loggedUserInfo =>
			this.loggedUserCanDoActions = loggedUserInfo.id === this.referenceCompleteDataDto.reference.createdBy && hasSuggestedState
		);
	}

	onNewState(hasChange : boolean){
		if (hasChange){
			this.institutionalNetworkReferenceReportService.getReferenceDetail(this.referenceCompleteDataDto.reference.id).subscribe(
				(result) => {
					this.referenceRegulationDto = result.regulation;
					this.regulationState = getIconState[result.regulation.state];
				  });
		}
	}
}
