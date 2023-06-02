import HierarchicalUnitTypeShow from './HierarchicalUnitTypeShow';
import HierarchicalUnitTypeList from './HierarchicalUnitTypeList';
import HierarchicalUnitTypeCreate from './HierarchicalUnitTypeCreate';
import HierarchicalUnitTypeEdit from './HierarchicalUnitTypeEdit';
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";
import { ADMINISTRADOR } from "../roles";

const check = (permissions: SGXPermissions) =>
    permissions.isOn('HABILITAR_UNIDADES_JERARQUICAS_EN_DESARROLLO') && permissions.hasAnyAssignment(ADMINISTRADOR);

const hierarchicalunittypes = (permissions: SGXPermissions) => ({
    list: check(permissions) ? HierarchicalUnitTypeList : undefined,
    show: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeShow : undefined,
    create: permissions.hasAnyAssignment(ADMINISTRADOR) ? HierarchicalUnitTypeCreate : undefined,
    edit: permissions.hasAnyAssignment(ADMINISTRADOR) ?  HierarchicalUnitTypeEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});
export default hierarchicalunittypes;
