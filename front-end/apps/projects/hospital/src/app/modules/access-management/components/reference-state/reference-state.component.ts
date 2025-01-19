import { ReferencePermissionCombinationService } from '@access-management/services/reference-permission-combination.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { EReferenceRegulationState, EReferenceAdministrativeState, ReferenceRegulationDto, ReferenceAdministrativeStateDto } from '@api-rest/api-model';
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

    canShowApproval: boolean;
    canShowAppointment: boolean;
    selectedStepperIndex = 0;

    @Input() referenceRegulation: ReferenceRegulationDto;
    @Input() referenceAdministrativeStateDto: ReferenceAdministrativeStateDto;
    @Input() appointment: AppointmentSummary;
    @Output() closeDialog: EventEmitter<boolean> = new EventEmitter<boolean>();

    constructor(
        public permissionService: ReferencePermissionCombinationService,
        private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly tabsService: TabsService
    ) { }

    ngOnInit(): void {
        this.permissionService.getReferenceCompleteData().subscribe(newReference => {
			if (!newReference.administrativeState) {
                this.canShowApproval = false;
                this.canShowAppointment = false;
                this.setStepperIndex();
            }
            if (newReference.administrativeState) {
                this.canShowApproval = true;
                this.canShowAppointment = false;
                this.setStepperIndex();
            }
		})
        if (this.permissionService.referenceCompleteData.reference.institutionDestination.id)
            this.canShowApproval = this.isAudited(this.referenceRegulation?.state);
        else
            this.canShowApproval = false;
        this.canShowAppointment = this.isApproved(this.referenceAdministrativeStateDto?.state);
        this.setStepperIndex();
    }

    newOriginState(state: EReferenceRegulationState) {
        this.canShowApproval = this.isAudited(state);
    }

    newDestinationState(state: EReferenceAdministrativeState) {
        if (!state){
            this.canShowApproval = false;
            this.canShowAppointment = false;
            this.setStepperIndex();
        }
        else
            this.canShowAppointment = this.isApproved(state);
    }

    redirectToOfferByRegulation(): void {
		this.searchAppointmentsInfoService.loadInformation(this.permissionService.reportCompleteData.patient.id, this.permissionService.reportCompleteData.reference);
		this.tabsService.setTabActive(TAB_OFERTA_POR_REGULACION);
		this.closeDialog.emit();
	}

    private setStepperIndex() {
        if (this.canShowApproval) this.selectedStepperIndex = 1;
        if (this.canShowAppointment) this.selectedStepperIndex = 2;
        if (!this.canShowApproval && !this.canShowAppointment) this.selectedStepperIndex = 0;
    }

    private isAudited(state: EReferenceRegulationState): boolean {
        return state === this.permissionService.referenceOriginStates.audited || state === this.permissionService.referenceOriginStates.noAuditRequired
            || this.permissionService.referenceCompleteData.administrativeState?.state === this.permissionService.referenceDestinationState.suggestedRevision;
    }

    private isApproved(state: EReferenceAdministrativeState): boolean {
        return state === this.permissionService.referenceDestinationState.approval;
    }

}
