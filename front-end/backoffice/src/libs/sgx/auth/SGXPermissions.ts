import { RoleAssignment } from '../api/model';


const matchInstitutions = (userAssignment: RoleAssignment, neededAssignment: RoleAssignment): boolean => {
    if (userAssignment.institutionId === neededAssignment.institutionId) {
        return true;
    }
    // si es rol institucional, el usuario tiene asignado un id real (no -1) y el que se necesita no es -1
    return (userAssignment.institutionId !== -1) && (!neededAssignment.institutionId);
};

class SGXPermissions {
    roleAssignments: RoleAssignment[];
    featureFlags: string[];
    
    constructor(roleAssignments: RoleAssignment[], featureFlags: string[]) {
        this.roleAssignments = roleAssignments;
        this.featureFlags = featureFlags;
    }

    hasAnyAssignment(...anyAssignments: RoleAssignment[]) {
        if (anyAssignments.length === 0) {
            return true;
        }
        const hasAny = anyAssignments.find(assignment => 
            this.roleAssignments.find(userAssignment => 
                userAssignment.role === assignment.role && matchInstitutions(userAssignment, assignment)
            )
        ) !== undefined;
        return hasAny;
    }

    isOn(featureFlag: string) {
        return this.featureFlags.find(ff => ff === featureFlag) !== undefined;
    }

    hasOnlyOneAssigment(...anyAssignment: RoleAssignment[]) {
        const oneRole = (this.roleAssignments.length === 1 && anyAssignment[0].role === this.roleAssignments[0].role);
        return oneRole;
    }

}
  
export default SGXPermissions;
