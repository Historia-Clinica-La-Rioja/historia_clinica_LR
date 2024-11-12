import { EReferenceRegulationState } from "@api-rest/api-model"
import { Color, ColoredLabel } from "@presentation/colored-label/colored-label.component"

export const APPROVAL: ColoredLabel = {
    color: Color.GREEN,
    icon: 'check_circle',
    description: 'access-management.search_references.reference.approval.states.APPROVED'
}

export const AUDIT: ColoredLabel = {
    color: Color.GREEN,
    icon: 'check_circle',
    description: 'access-management.search_references.reference.approval.states.AUDITED'
}

export const SUGGESTED: ColoredLabel = {
    color: Color.YELLOW,
    icon: 'feedback',
    description: 'access-management.search_references.reference.approval.states.SUGGESTED'
}

export const REJECTED: ColoredLabel = {
    color: Color.RED,
    icon: 'cancel',
    description: 'access-management.search_references.reference.approval.states.REJECTED'
}

export const PENDING_APPROVAL: ColoredLabel = {
    color: Color.BLUE,
    icon: 'pending_actions',
    description: 'access-management.search_references.reference.approval.states.PENDING_APPROVAL'
}

export const PENDING_AUDIT: ColoredLabel = {
    color: Color.BLUE,
    icon: 'pending_actions',
    description: 'access-management.search_references.reference.approval.states.PENDING_AUDIT'
}

export const AUDIT_NOT_REQUIRED: ColoredLabel = {
    color: Color.GREY,
    icon: 'check_circle',
    description: 'access-management.search_references.reference.approval.states.AUDIT_NOT_REQUIRED'
}


export const getIconState = {
    [EReferenceRegulationState.WAITING_AUDIT]: PENDING_AUDIT,
    [EReferenceRegulationState.SUGGESTED_REVISION]: SUGGESTED,
    [EReferenceRegulationState.REJECTED]: REJECTED,
    [EReferenceRegulationState.AUDITED]: AUDIT,
    [EReferenceRegulationState.DONT_REQUIRES_AUDIT]: AUDIT_NOT_REQUIRED,
    ['APPROVED']: APPROVAL,
    ['WAITING_APPROVAL']: PENDING_APPROVAL,
}

export enum ReferenceApprovalState {
    WAITING_APPROVAL, APPROVED, REJECTED, SUGGESTED_REVISION
}

export enum ReferenceOriginState {
    PENDING_AUDIT, AUDIT_NOT_REQUIRED, REJECTED, SUGGESTED_REVISION, AUDIT
}
