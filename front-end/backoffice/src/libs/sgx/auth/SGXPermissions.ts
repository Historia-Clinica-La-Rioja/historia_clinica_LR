import { RoleAssignment } from '../api/model';

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
            this.roleAssignments.find(userAssignment => userAssignment.role === assignment.role && userAssignment.institutionId === assignment.institutionId)
        ) !== undefined;

        return hasAny;
    }

    isOn(featureFlag: string) {
        return this.featureFlags.find(ff => ff === featureFlag) !== undefined;
    }

}
  
export default SGXPermissions;
