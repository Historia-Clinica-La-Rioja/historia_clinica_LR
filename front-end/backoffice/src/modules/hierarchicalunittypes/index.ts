import HierarchicalUnitTypeShow from './HierarchicalUnitTypeShow';
import HierarchicalUnitTypeList from './HierarchicalUnitTypeList';
import HierarchicalUnitTypeCreate from './HierarchicalUnitTypeCreate';
import HierarchicalUnitTypeEdit from './HierarchicalUnitTypeEdit';
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";
import { ADMINISTRADOR } from "../roles";


const hierarchicalunittypes = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeShow : undefined,
    list: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeList : undefined,
    create: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeCreate : undefined,
    edit: permissions.hasAnyAssignment(ADMINISTRADOR) ?  HierarchicalUnitTypeEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});
export default hierarchicalunittypes;
