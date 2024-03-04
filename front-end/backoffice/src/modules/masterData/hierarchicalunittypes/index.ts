import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMINISTRADOR,
} from '../../roles';
import HierarchicalUnitTypeShow from './HierarchicalUnitTypeShow';
import HierarchicalUnitTypeList from './HierarchicalUnitTypeList';
import HierarchicalUnitTypeCreate from './HierarchicalUnitTypeCreate';
import HierarchicalUnitTypeEdit from './HierarchicalUnitTypeEdit';

const hierarchicalunittypes = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeList : undefined,
    show: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeShow : undefined,
    create: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeCreate : undefined,
    edit: permissions.hasAnyAssignment(ADMINISTRADOR) ?  HierarchicalUnitTypeEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});
export default hierarchicalunittypes;
