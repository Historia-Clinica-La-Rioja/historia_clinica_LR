import { ReferenceOriginState, ReferenceApprovalState, getIconState } from '@access-management/constants/approval';
import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EReferenceAdministrativeState, EReferenceRegulationState } from '@api-rest/api-model';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { NON_WHITESPACE_REGEX } from '@core/utils/form.utils';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { Observable } from 'rxjs';

const WAITING_AUDIT = "WAITING_AUDIT";
const WAITING_APPROVAL = "WAITING_APPROVAL";

@Component({
	selector: 'app-institutional-network-actions-dropdown',
	templateUrl: './institutional-network-actions-dropdown.component.html',
	styleUrls: ['./institutional-network-actions-dropdown.component.scss']
})
export class InstitutionalNetworkActionsDropdownComponent implements OnInit {

	selectedOption: EReferenceRegulationState | EReferenceAdministrativeState;
	selectedOptionColoredLabel: ColoredLabel;
	selectedOptionData: ReasonData;
	reason: FormGroup;

	private selectedOptionId: number;
	private readonly translatePrefixRoute = 'access-management.search_references.reference.discard_popup';
	showReasonArea: boolean = false;
	isWaitingAudit = WAITING_AUDIT;
	isWaitingAproval = WAITING_APPROVAL;
	
	@Input() regulationStates: EReferenceAdministrativeState[] | EReferenceRegulationState[];
	@Input() isOrigin: boolean;
	@Output() newState = new EventEmitter<boolean>();
	@Output() editing = new EventEmitter<boolean>();

	constructor(
		private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
		private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
		public permissionService: ReferencePermissionCombinationService,
	) { }

	ngOnInit(): void {
		if (this.isOrigin) {
			if (this.permissionService.referenceCompleteData.regulation?.state) 
				this.setSelectedOption(this.permissionService.referenceCompleteData.regulation.state);
			else 
				this.selectedOption = this.permissionService.referenceOriginStates.waitingAudit;
		}
		else {
			if (this.permissionService.referenceCompleteData.administrativeState?.state)
				this.setSelectedOption(this.permissionService.referenceCompleteData.administrativeState.state)
			else
				this.selectedOption = this.permissionService.referenceDestinationState.waitingApproval;
		}
		this.selectedOptionColoredLabel = this.getSelectedOption();
		this.reason = new FormGroup<ReasonForm>({
			reason: new FormControl(null, [Validators.required, Validators.pattern(NON_WHITESPACE_REGEX)]),
		});
	}

	private getSelectedOption(): ColoredLabel {
		return this.getOptionIconState(this.selectedOption);
	}

	getOptionIconState(state: EReferenceRegulationState | EReferenceAdministrativeState): ColoredLabel {
		return getIconState[state];
	}

	setSelectedOption(option: EReferenceRegulationState | EReferenceAdministrativeState) {
		this.selectedOption = option;
		this.selectedOptionColoredLabel = this.getSelectedOption();
		if (this.isOrigin)
			this.setOriginValuesInOptions[option]();
		else
			this.setDestinationValuesInOptions[option]();
	}

	private setOriginValuesInOptions = {
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
			this.selectedOptionData = this.toReasonData('audit.TITLE', '');
			this.selectedOptionId = ReferenceOriginState.AUDIT;
		}
	}

	private setDestinationValuesInOptions = {
		WAITING_APPROVAL: () => {
			this.showReasonArea = false;
			this.selectedOptionId = ReferenceApprovalState.WAITING_APPROVAL;
		},
		SUGGESTED_REVISION: () => {
			this.showReasonArea = true;
			this.selectedOptionData = this.toReasonData('suggested-review.TITLE', 'suggested-review.PLACEHOLDER');
			this.selectedOptionId = ReferenceApprovalState.SUGGESTED_REVISION;
		},
		APPROVED: () => {
			this.showReasonArea = false;
			this.selectedOptionData = this.toReasonData('approved.TITLE', '');
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

		this.editing.next(false);
	}

	cancel() {
		this.selectedOption = this.permissionService.referenceDestinationState.waitingApproval;
		this.selectedOptionColoredLabel = this.getSelectedOption();
		this.showReasonArea = false;
		this.editing.next(false);
	}

	private updateStateAndEmit(stateId: number) {
		const reason = this.reason.controls.reason.value;
		let updateState$: Observable<boolean>;
		if (this.isOrigin) {
			if (this.permissionService.isRoleGestorInstitucional) 
				updateState$ = this.institutionalReferenceReportService.changeReferenceRegulationStateAsGestorInstitucional(
					this.permissionService.referenceCompleteData.reference.id, stateId, reason || null);
			else
				updateState$ = this.institutionalNetworkReferenceReportService.changeReferenceRegulationState(
					this.permissionService.referenceCompleteData.reference.id, stateId, reason || null);
		}
		else {
			updateState$ = this.institutionalReferenceReportService.changeReferenceApprovalStateAsGestorInstitucional(
				this.permissionService.referenceCompleteData.reference.id, stateId, reason || null);
		}
		updateState$.subscribe(_ => {
			this.newState.next(true)
		});
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
