import HierarchicalUnitShow from './HierarchicalUnitShow';
import HierarchicalUnitList from './HierarchicalUnitList';
import HierarchicalUnitCreate from './HierarchicalUnitCreate';
import HierarchicalUnitEdit from './HierarchicalUnitEdit';
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";

const check = (permissions: SGXPermissions) =>
    permissions.isOn('HABILITAR_UNIDADES_JERARQUICAS_EN_DESARROLLO');

const hierarchicalunits = (permissions: SGXPermissions) => (
    {
        list: check(permissions) ? HierarchicalUnitList : undefined,
        show: HierarchicalUnitShow,
        create: HierarchicalUnitCreate,
        edit: HierarchicalUnitEdit,
        options: {
            submenu: 'facilities'
        }
    });
export default hierarchicalunits;
