import { PENDING_ATTENTION_STATE, REFERENCE_ORIGIN_STATES, REFERENCE_STATES } from '@access-management/constants/reference';
import { ReportCompleteData } from '@access-management/dialogs/report-complete-data-popup/report-complete-data-popup.component';
import { Injectable } from '@angular/core';
import { ERole, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { DashboardView } from '@shared-appointment-access-management/components/reference-dashboard-filters/reference-dashboard-filters.component';
import { DashboardService } from './dashboard.service';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

const GESTORES = [ERole.GESTOR_DE_ACCESO_DE_DOMINIO, ERole.GESTOR_DE_ACCESO_LOCAL, ERole.GESTOR_DE_ACCESO_REGIONAL];
const GESTOR_INSTITUCIONAL = ERole.GESTOR_DE_ACCESO_INSTITUCIONAL;
const APPOINTMENT_STATES = REFERENCE_STATES;

@Injectable({
    providedIn: 'root'
})
export class ReferencePermissionCombinationService {

    visualPermissions: ReferenceVisualPermissions;

    referenceCompleteData: ReferenceCompleteDataDto;
	reportCompleteData: ReportCompleteData;

    registerEditorAppointment: RegisterEditor;
    isRoleGestor: boolean;
	isRoleGestorInstitucional: boolean;
    pendingAttentionState = PENDING_ATTENTION_STATE;
    registerEditorCasesDateHour = REGISTER_EDITOR_CASES.DATE_HOUR;
    referenceOriginStates = REFERENCE_ORIGIN_STATES;

    constructor(
        private readonly permissionService: PermissionsService,
        readonly dashboardService: DashboardService,
    ) {
        this.permissionService.hasContextAssignments$(GESTORES).subscribe(hasRole => this.isRoleGestor = hasRole);
		this.permissionService.hasContextAssignments$([GESTOR_INSTITUCIONAL]).subscribe(hasRole => this.isRoleGestorInstitucional = hasRole);    
    }

    setReferenceAndReportDataAndVisualPermissions(reference: ReferenceCompleteDataDto, report: ReportCompleteData) {
        this.referenceCompleteData = reference;
        this.reportCompleteData = report;
        this.visualPermissions = {
            showAssignAppointmentButton: this.showAssignAppointmentButton(),
            showEditReferenceButton: this.showEditReferenceButton(),
            showDeriveRequestButton: this.showDeriveRequestButton(),
            showAdministativeClosureButton: this.showAdministativeClosureButton(),
            showAuditDropdown: this.showAuditDropdown(),
            canEditApproval: this.canEditApproval()
        }
    }

    setEditorAppointment(authorFullName: string, createdOn: Date) {
        this.registerEditorAppointment = {createdBy: authorFullName, date: createdOn}
    }

    showAssignAppointmentButton(): boolean {
		return this.referenceCompleteData.regulation.state
			&& this.isRoleGestor && !this.referenceCompleteData.reference.closureType && !this.reportHasAppointment();
	}

    showEditReferenceButton(): boolean {
        return this.isRoleGestor && !this.reportHasAppointment() && !this.referenceCompleteData.reference.closureType && 
        this.referenceCompleteData.regulation.state !== this.referenceOriginStates.rejected;
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
        return (this.isRoleGestor || this.isRoleGestorInstitucional) && !this.referenceCompleteData.reference.closureType &&
        this.referenceCompleteData.regulation.state === this.referenceOriginStates.waitingAudit && !this.reportHasAppointment();
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
    showDeriveRequestButton: boolean,
    showAdministativeClosureButton: boolean,
    showAuditDropdown: boolean,
    canEditApproval: boolean
}