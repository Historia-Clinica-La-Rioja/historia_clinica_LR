import { REFERENCE_DESTINATION_STATES, REFERENCE_ORIGIN_STATES, REFERENCE_STATES } from '@access-management/constants/reference';
import { ReportCompleteData } from '@access-management/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { Injectable, OnDestroy } from '@angular/core';
import { ERole, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { DashboardView } from '@shared-appointment-access-management/components/reference-dashboard-filters/reference-dashboard-filters.component';
import { DashboardService } from './dashboard.service';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { Observable, Subject } from 'rxjs';

const GESTORES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_LOCAL, ERole.GESTOR_DE_ACCESO_REGIONAL];
const GESTOR_INSTITUCIONAL = ERole.GESTOR_DE_ACCESO_INSTITUCIONAL;
const APPOINTMENT_STATES = REFERENCE_STATES;

@Injectable({
    providedIn: 'root'
})
export class ReferencePermissionCombinationService implements OnDestroy {

    visualPermissions: ReferenceVisualPermissions;

    referenceCompleteData$: Subject<ReferenceCompleteDataDto> = new Subject<ReferenceCompleteDataDto>();
    referenceCompleteData: ReferenceCompleteDataDto;
	reportCompleteData: ReportCompleteData;

    registerEditorAppointment: RegisterEditor;
    isRoleGestor: boolean;
	isRoleGestorInstitucional: boolean;
    pendingAttentionState = APPOINTMENT_STATES.PENDING;
    registerEditorCasesDateHour = REGISTER_EDITOR_CASES.DATE_HOUR;
    referenceOriginStates = REFERENCE_ORIGIN_STATES;
    referenceDestinationState = REFERENCE_DESTINATION_STATES;

    constructor(
        private readonly permissionService: PermissionsService,
        readonly dashboardService: DashboardService,
    ) {
        this.permissionService.hasContextAssignments$(GESTORES).subscribe(hasRole => this.isRoleGestor = hasRole);
		this.permissionService.hasContextAssignments$([GESTOR_INSTITUCIONAL]).subscribe(hasRole => this.isRoleGestorInstitucional = hasRole);
    }

    ngOnDestroy(): void {
        this.isRoleGestor = null;
        this.isRoleGestorInstitucional = null;
    }

    setReferenceAndReportDataAndVisualPermissions(reference: ReferenceCompleteDataDto, report: ReportCompleteData) {
        this.referenceCompleteData$.next(reference);
        this.referenceCompleteData = reference;
        this.reportCompleteData = report;
        this.visualPermissions = {
            showAssignAppointmentButton: this.showAssignAppointmentButton(),
            showEditReferenceButton: this.showEditReferenceButton(),
            showEditReferenceInDestinationState: this.showEditReferenceInDestinationState(),
            showDeriveRequestButton: this.showDeriveRequestButton(),
            showAdministativeClosureButton: this.showAdministativeClosureButton(),
            showAuditDropdown: this.showAuditDropdown(),
            showApprovalDropdown: this.showApprovalDropdown(),
            canEditApproval: this.canEditApproval()
        }
    }

    resetEditorAppontment() {
        this.registerEditorAppointment = null;
    }

    setEditorAppointment(authorFullName: string, createdOn: Date) {
        this.registerEditorAppointment = {createdBy: authorFullName, date: createdOn}
    }

    getReferenceCompleteData(): Observable<ReferenceCompleteDataDto>{
        return this.referenceCompleteData$;
    }

    showAssignAppointmentButton(): boolean {
		return this.referenceCompleteData.administrativeState?.state === this.referenceDestinationState.approval
			&& this.isRoleGestor && !this.referenceCompleteData.reference.closureType && !this.reportHasAppointment();
	}

    showEditReferenceButton(): boolean {
        return this.isRoleGestor && !this.reportHasAppointment() && !this.referenceCompleteData.reference.closureType
            && this.referenceCompleteData.regulation.state !== this.referenceOriginStates.rejected
            && this.referenceCompleteData.administrativeState?.state !== this.referenceDestinationState.suggestedRevision;
    }

    showEditReferenceInDestinationState(): boolean {
        return (this.isRoleGestor || (this.isRoleGestorInstitucional && this.dashboardService.dashboardView == DashboardView.REQUESTED))
            && this.referenceCompleteData.regulation.state === this.referenceOriginStates.waitingAudit && !this.referenceCompleteData.reference.closureType;
    }

    showDeriveRequestButton(): boolean {
        return (this.referenceCompleteData.regulation.state && this.referenceCompleteData.regulation.state !== this.referenceOriginStates.rejected
            || this.referenceCompleteData.regulation.state) && this.isRoleGestor;
    }

    showAdministativeClosureButton(): boolean {
        return !this.reportHasAppointment() && !this.referenceCompleteData.reference.closureType && (this.isRoleGestor ||
            (this.isRoleGestorInstitucional && this.dashboardService.dashboardView == DashboardView.REQUESTED)) && 
            this.referenceCompleteData.regulation.state !== this.referenceOriginStates.rejected;
    }

    showAuditDropdown(): boolean {
        return (this.isRoleGestor || (this.isRoleGestorInstitucional && this.dashboardService.dashboardView == DashboardView.REQUESTED))
        && !this.referenceCompleteData.reference.closureType && this.referenceCompleteData.regulation.state === this.referenceOriginStates.waitingAudit
        && !this.reportHasAppointment();
    }

    showApprovalDropdown(): boolean {
        return this.isRoleGestorInstitucional && this.dashboardService.dashboardView == DashboardView.RECEIVED
        && this.referenceCompleteData.administrativeState?.state === this.referenceDestinationState.waitingApproval
        && !this.referenceCompleteData.reference.closureType;
    }

    canEditApproval(): boolean {
        return !this.referenceCompleteData.reference?.closureType && !this.reportHasAppointment();
    }

    private reportHasAppointment(): boolean {
		return this.reportCompleteData.appointment.state.description === (APPOINTMENT_STATES.ASSIGNED || APPOINTMENT_STATES.SERVED);
	}
}

interface ReferenceVisualPermissions {
    showAssignAppointmentButton: boolean,
    showEditReferenceButton: boolean,
    showEditReferenceInDestinationState: boolean,
    showDeriveRequestButton: boolean,
    showAdministativeClosureButton: boolean,
    showAuditDropdown: boolean,
    showApprovalDropdown: boolean,
    canEditApproval: boolean
}