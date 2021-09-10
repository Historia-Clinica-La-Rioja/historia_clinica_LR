
class SGXPermissions {

    constructor(permissions) {
        this.roleAssignments = permissions.roleAssignments;
        this.featureFlags = permissions.featureFlags;
    }

    hasAnyAssignment(...anyAssignments) {
        if (anyAssignments.length === 0) {
            return true;
        }
        const hasAny = anyAssignments.find(assignment =>
            this.roleAssignments.find(userAssignment => userAssignment.role === assignment.role && userAssignment.institutionId === assignment.institutionId)
        ) !== undefined;

        return hasAny;
    }

    hasAnyAuthority(...anyAuthority){
        if (anyAuthority.length === 0) {
            return true;
        }
        const hasAny = anyAuthority.find(assignment =>
            this.roleAssignments.find(userAssignment => userAssignment.role === assignment.role)
        ) !== undefined;

        return hasAny;
    }

    isOn(featureFlag) {
        return this.featureFlags.find(ff => ff === featureFlag) !== undefined;
    }



}

export default SGXPermissions;
