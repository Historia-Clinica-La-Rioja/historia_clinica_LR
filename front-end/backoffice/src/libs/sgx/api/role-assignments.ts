

import { RoleAssignment, PermissionsDto } from './model';

const isPublicRouter = (path = '') => path.startsWith('#/auth/');

const guestPermissions = (): Promise<RoleAssignment[]> => Promise.resolve([]);

const roleAssignments = (fetchPermissions: () => Promise<PermissionsDto>): Promise<RoleAssignment[]> => 
    isPublicRouter(window.location.hash) ? guestPermissions() : fetchPermissions().then(p => p.roleAssignments);

export default roleAssignments;
