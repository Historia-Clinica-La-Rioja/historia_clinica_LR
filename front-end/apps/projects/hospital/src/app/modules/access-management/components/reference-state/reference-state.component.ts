import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { EReferenceRegulationState, EReferenceAdministrativeState, ReferenceRegulationDto } from '@api-rest/api-model';
import { AppointmentSummary } from '../appointment-summary/appointment-summary.component';
import { TabsService } from '@access-management/services/tabs.service';
import { SearchAppointmentsInfoService } from '@access-management/services/search-appointment-info.service';

const TAB_OFERTA_POR_REGULACION = 1;

@Component({
    selector: 'app-reference-state',
    templateUrl: './reference-state.component.html',
    styleUrls: ['./reference-state.component.scss']
})
export class ReferenceStateComponent implements OnInit {

    originAudited: boolean;
    destinationApproved = false;

    @Input() referenceRegulation: ReferenceRegulationDto;
    @Input() appointment: AppointmentSummary;
    @Output() closeDialog: EventEmitter<boolean> = new EventEmitter<boolean>();

    constructor(
        public permissionService: ReferencePermissionCombinationService,
        private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly tabsService: TabsService
    ) { }

    ngOnInit(): void {
        this.originAudited = this.isAudited(this.referenceRegulation.state);
    }

    newOriginState(state: EReferenceRegulationState) {
        this.originAudited = this.isAudited(state);
    }

    newDestinationState(state: EReferenceAdministrativeState) {
        this.destinationApproved = this.isApproved(state);
    }

    redirectToOfferByRegulation(): void {
		this.searchAppointmentsInfoService.loadInformation(this.permissionService.reportCompleteData.patient.id, this.permissionService.reportCompleteData.reference);
		this.tabsService.setTabActive(TAB_OFERTA_POR_REGULACION);
		this.closeDialog.emit();
	}

    private isAudited(state: EReferenceRegulationState): boolean {
        return state === this.permissionService.referenceOriginStates.audited || state === this.permissionService.referenceOriginStates.noAuditRequired;
    }

    private isApproved(state: EReferenceAdministrativeState) {
        return true;
    }

}
