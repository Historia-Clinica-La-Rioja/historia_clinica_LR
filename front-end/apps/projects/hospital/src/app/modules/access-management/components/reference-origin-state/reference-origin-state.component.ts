import { getIconState } from '@access-management/constants/approval';
import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { EReferenceRegulationState, ReferenceCompleteDataDto, ReferenceRegulationDto } from '@api-rest/api-model';
import { InstitutionalNetworkReferenceReportService } from '@api-rest/services/institutional-network-reference-report.service';
import { InstitutionalReferenceReportService } from '@api-rest/services/institutional-reference-report.service';
import { ColoredLabel } from '@presentation/colored-label/colored-label.component';

@Component({
    selector: 'app-reference-origin-state',
    templateUrl: './reference-origin-state.component.html',
    styleUrls: ['./reference-origin-state.component.scss']
})
export class ReferenceOriginStateComponent implements OnInit {

    regulationState: ColoredLabel;
    referenceRegulationData: ReferenceRegulationDto;

    regulationOriginStates: EReferenceRegulationState[] = [
		EReferenceRegulationState.WAITING_AUDIT,
		EReferenceRegulationState.SUGGESTED_REVISION,
		EReferenceRegulationState.REJECTED,
		EReferenceRegulationState.AUDITED,
	];

    referenceId: number;
    hideReason = false;

    @Input() set referenceCompleteData (reference: ReferenceCompleteDataDto) {
        this.referenceRegulationData = reference.regulation;
        this.referenceId = reference.reference.id;
    };
    @Output() regulationStateEmmiter: EventEmitter<EReferenceRegulationState> = new EventEmitter<EReferenceRegulationState>();

    constructor(
        public permissionService: ReferencePermissionCombinationService,
        private readonly institutionalReferenceReportService: InstitutionalReferenceReportService,
        private readonly institutionalNetworkReferenceReportService: InstitutionalNetworkReferenceReportService,
    ) { }

    ngOnInit(): void {
        this.regulationState = getIconState[this.referenceRegulationData.state];
    }

    onNewState(hasChange : boolean){
		if (hasChange){
			if (this.permissionService.isRoleGestorInstitucional)
				this.institutionalReferenceReportService.getReferenceDetail(this.referenceId).subscribe(
					(result) => {
                        this.permissionService.setReferenceAndReportDataAndVisualPermissions(result, this.permissionService.reportCompleteData);
						this.referenceRegulationData = result.regulation;
						this.regulationState = getIconState[result.regulation.state];
						this.regulationStateEmmiter.next(result.regulation.state);
					});
			else 
				this.institutionalNetworkReferenceReportService.getReferenceDetail(this.referenceId).subscribe(
					(result) => {
                        this.permissionService.setReferenceAndReportDataAndVisualPermissions(result, this.permissionService.reportCompleteData);
						this.referenceRegulationData = result.regulation;
						this.regulationState = getIconState[result.regulation.state];
						this.regulationStateEmmiter.next(result.regulation.state);
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
