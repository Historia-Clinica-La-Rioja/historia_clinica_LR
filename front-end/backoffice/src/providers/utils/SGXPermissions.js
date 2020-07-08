
class SGXPermissions {

    constructor(permissions) {
        this.roleAssignments = permissions.roleAssignments;
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

}

export default SGXPermissions;
