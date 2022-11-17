export interface JWTokenDto { 
    token: string;
    refreshToken: string;
}

export interface GetListResponse {
    content: any;
    totalElements: any;
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

export interface OauthConfigDto {
    clientId: string;
    enabled: boolean;
    issuerUrl: string;
    logoutUrl: string;
}

export interface FileInputData {
    file: {rawFile: File, title: string},
}

export interface SupportedLanguages {
    [key: string]: any;
}
