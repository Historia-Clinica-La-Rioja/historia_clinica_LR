import { ReferenceOriginState, getIconState } from '@access-management/constants/approval';
import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EReferenceRegulationState } from '@api-rest/api-model';
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

	waitingAudit =  EReferenceRegulationState.WAITING_AUDIT;
	selectedOption: EReferenceRegulationState = this.waitingAudit;
	initialOption: EReferenceRegulationState;
	selectedOptionData: ReasonData;
	private selectedOptionId: number;
	showReasonArea: boolean = false;
	private readonly translatePrefixRoute = 'access-management.search_references.reference.discard_popup';
	reason: FormGroup;

	@Input() regulationOriginStates: EReferenceRegulationState[];
	@Input() isOrigin: boolean;
	@Output() newState = new EventEmitter<boolean>();
	@Output() editing = new EventEmitter<boolean>();

	constructor(
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		private readonly permissionService: ReferencePermissionCombinationService,
	) { }

	ngOnInit(): void {
		if (this.permissionService.referenceCompleteData.regulation?.state) 
			this.setSelectedOption(this.permissionService.referenceCompleteData.regulation.state);
		this.reason = new FormGroup<ReasonForm>({
			reason: new FormControl(null, [Validators.required, Validators.pattern(NON_WHITESPACE_REGEX)]),
		});
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
		WAITING_AUDIT: () => {
			this.showReasonArea = false;
			this.selectedOptionId = ReferenceOriginState.PENDING_AUDIT;
		},
		REJECTED: () => {
			this.showReasonArea = true;
			this.selectedOptionData = this.toReasonData('rejected.TITLE', 'rejected.PLACEHOLDER');
			this.selectedOptionId = ReferenceOriginState.REJECTED;
		},
		SUGGESTED_REVISION: () => {
			this.showReasonArea = true;
			this.selectedOptionData = this.toReasonData('suggested-review.TITLE', 'suggested-review.PLACEHOLDER');
			this.selectedOptionId = ReferenceOriginState.SUGGESTED_REVISION;
		},
		AUDITED: () => {
			this.showReasonArea = false;
			this.selectedOptionData = this.toReasonData('approved.TITLE', '');
			this.selectedOptionId = ReferenceOriginState.AUDIT;
		}
	}

	confirm() {
		console.log('ID', this.selectedOption)
		if (this.showReasonArea) {
			if (this.reason.valid)
				this.updateStateAndEmit(this.selectedOptionId);
		}
		else
			this.updateStateAndEmit(this.selectedOptionId);

		this.editing.next(false);
	}

	cancel() {
		this.showReasonArea = false;
		this.editing.next(false);
	}

	private updateStateAndEmit(stateId: number) {
		const reason = this.reason.controls.reason.value;
		let updateState$: Observable<boolean>;
		if (this.permissionService.isRoleGestorInstitucional)
			updateState$ = this.institutionalReferenceReportService.changeReferenceRegulationStateAsGestorInstitucional(
				this.permissionService.referenceCompleteData.reference.id, stateId, reason || null);
		else 
			updateState$ = this.institutionalNetworkReferenceReportService.changeReferenceRegulationState(
				this.permissionService.referenceCompleteData.reference.id, stateId, reason || null);
		
		updateState$.subscribe(_ => this.newState.next(true));
	}

	private toReasonData(titleKey: string, placeholderKey: string): ReasonData {
		return {
			title: `${this.translatePrefixRoute}.${titleKey}`,
			placeholder: `${this.translatePrefixRoute}.${placeholderKey}`,
		}
	}
}

interface ReasonData {
	title: string;
	placeholder: string;
}

interface ReasonForm {
	reason: FormControl<string>;
}
