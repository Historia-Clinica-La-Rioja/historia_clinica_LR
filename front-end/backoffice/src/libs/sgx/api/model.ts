export interface JWTokenDto { 
    token: string;
    refreshToken: string;
}

export interface LoggedUserDto {
    id: number;
    email: string;
}

export interface RoleAssignment {
    role: string;
    institutionId: number;
}
export interface PermissionsDto {
    roleAssignments: RoleAssignment[];
}

export interface PublicInfoDto {
    features: string[];
}