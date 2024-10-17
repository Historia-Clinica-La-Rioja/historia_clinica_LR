import { ReferenceApprovalState, getIconState } from '@access-management/constants/approval';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EReferenceRegulationState, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { NON_WHITESPACE_REGEX } from '@core/utils/form.utils';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-institutional-network-actions-dropdown',
	templateUrl: './institutional-network-actions-dropdown.component.html',
	styleUrls: ['./institutional-network-actions-dropdown.component.scss']
})
export class InstitutionalNetworkActionsDropdownComponent implements OnInit {

	regulationStates: EReferenceRegulationState[] = [
		EReferenceRegulationState.WAITING_APPROVAL,
		EReferenceRegulationState.SUGGESTED_REVISION,
		EReferenceRegulationState.REJECTED,
		EReferenceRegulationState.APPROVED,
	];
	waitingApproval =  EReferenceRegulationState.WAITING_APPROVAL;
	selectedOption: EReferenceRegulationState = this.waitingApproval;
	initialOption: EReferenceRegulationState;
	selectedOptionData: ReasonData;
	private selectedOptionId: number;
	showReasonArea: boolean = false;
	private readonly translatePrefixRoute = 'access-management.search_references.reference.discard_popup';
	reason: FormGroup;

	@Input() reportCompleteData: ReferenceCompleteDataDto;
	@Input() hasGestorInstitucionalRole: boolean;
	@Output() newState = new EventEmitter<boolean>();

	constructor(
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService
	) { }

	ngOnInit(): void {
		this.initialOption = this.reportCompleteData.regulation.state? this.reportCompleteData.regulation?.state : this.waitingApproval;
		if (this.reportCompleteData.regulation?.state) this.setSelectedOption(this.reportCompleteData.regulation.state);
		this.reason = new FormGroup<ReasonForm>({
			reason: new FormControl(null, [Validators.required, Validators.pattern(NON_WHITESPACE_REGEX)]),
		});
		if (this.initialOption !== this.waitingApproval) this.regulationStates.shift();
	}

	get selectedStateOption() {
		return this.getSelectedOption();
	}

	private getSelectedOption(): ColoredLabel {
		return this.getOptionIconState(this.selectedOption);
	}

	getOptionIconState(state: EReferenceRegulationState): ColoredLabel {
		return getIconState[state];
	}

	setSelectedOption(option: EReferenceRegulationState) {
		this.selectedOption = option;
		this.setValuesInOptions[option]();
	}

	private setValuesInOptions = {
		WAITING_APPROVAL: () => {
			this.showReasonArea = false;
			this.selectedOptionId = ReferenceApprovalState.WAITING_APPROVAL;
		},
		REJECTED: () => {
			this.showReasonArea = true;
			this.selectedOptionData = this.toReasonData('rejected.TITLE', 'rejected.SUBTITLE', 'rejected.PLACEHOLDER');
			this.selectedOptionId = ReferenceApprovalState.REJECTED;
		},
		SUGGESTED_REVISION: () => {
			this.showReasonArea = true;
			this.selectedOptionData = this.toReasonData('suggested-review.TITLE', 'suggested-review.SUBTITLE', 'suggested-review.PLACEHOLDER');
			this.selectedOptionId = ReferenceApprovalState.SUGGESTED_REVISION;
		},
		APPROVED: () => {
			this.showReasonArea = false;
			this.selectedOptionData = this.toReasonData('approved.TITLE', 'approved.SUBTITLE', '');
			this.selectedOptionId = ReferenceApprovalState.APPROVED;
		}
	}

	confirm() {
		if (this.showReasonArea) {
			if (this.reason.valid)
				this.updateStateAndEmit(this.selectedOptionId);
		}
		else
			this.updateStateAndEmit(this.selectedOptionId);
	}

	cancel() {
		this.selectedOption = this.waitingApproval;
		this.selectedOptionId = ReferenceApprovalState.WAITING_APPROVAL;
		this.showReasonArea = false;
	}

	private updateStateAndEmit(stateId: number) {
		const reason = this.reason.controls.reason.value;
		let updateState$: Observable<boolean>;
		if (this.hasGestorInstitucionalRole)
			updateState$ = this.institutionalReferenceReportService.changeReferenceRegulationStateAsGestorInstitucional(this.reportCompleteData.reference.id, stateId, reason || null);
		else 
			updateState$ = this.institutionalNetworkReferenceReportService.changeReferenceRegulationState(this.reportCompleteData.reference.id, stateId, reason || null);
		
		updateState$.subscribe(_ => this.newState.next(true));
		this.initialOption = this.selectedOption;
	}

	private toReasonData(titleKey: string, subtitleKey: string, placeholderKey: string): ReasonData {
		return {
			title: `${this.translatePrefixRoute}.${titleKey}`,
			subtitle: `${this.translatePrefixRoute}.${subtitleKey}`,
			placeholder: `${this.translatePrefixRoute}.${placeholderKey}`,
		}
	}
}

interface ReasonData {
	title: string;
	subtitle: string;
	placeholder: string;
}

interface ReasonForm {
	reason: FormControl<string>;
}
