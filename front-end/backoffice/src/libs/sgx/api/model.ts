
export interface RoleAssignment {
    role: string;
    institutionId: number;
}
export interface PermissionsDto {
    roleAssignments: RoleAssignment[];
}
