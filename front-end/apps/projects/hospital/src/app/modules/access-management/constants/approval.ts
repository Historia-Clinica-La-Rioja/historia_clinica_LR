import { EReferenceRegulationState } from "@api-rest/api-model"
import { Color, ColoredLabel } from "@presentation/colored-label/colored-label.component"

export const APPROVAL: ColoredLabel = {
    color: Color.GREEN,
    icon: 'check_circle',
    description: 'access-management.search_references.reference.approval.states.APPROVED'
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

export const PENDING: ColoredLabel = {
    color: Color.BLUE,
    icon: 'pending_actions',
    description: 'access-management.search_references.reference.approval.states.PENDING'
}

export const getIconState = {
    [EReferenceRegulationState.WAITING_AUDIT]: PENDING,
    [EReferenceRegulationState.SUGGESTED_REVISION]: SUGGESTED,
    [EReferenceRegulationState.REJECTED]: REJECTED,
    [EReferenceRegulationState.AUDITED]: APPROVAL,
}

export enum ReferenceApprovalState {
    WAITING_APPROVAL, APPROVED, REJECTED, SUGGESTED_REVISION,
}
