import { getIconState } from '@access-management/constants/approval';
import { Component, Input, OnInit } from '@angular/core';
import { EReferenceRegulationState, ReferenceCompleteDataDto, ReferenceRegulationDto } from '@api-rest/api-model';
import { AccountService } from '@api-rest/services/account.service';
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

	@Input() referenceCompleteDataDto: ReferenceCompleteDataDto;
	@Input() set approval(value: ReferenceRegulationDto) {
		this.referenceRegulationDto = value;
		this.regulationState = getIconState[value.state];
	};

	constructor(
		private readonly accountService: AccountService,
	) { }

	ngOnInit(): void {
		const hasSuggestedState = this.referenceRegulationDto.state === EReferenceRegulationState.SUGGESTED_REVISION;
		this.accountService.getInfo().subscribe(loggedUserInfo =>
			this.loggedUserCanDoActions = loggedUserInfo.id === this.referenceCompleteDataDto.reference.createdBy && hasSuggestedState
		);
	}

}