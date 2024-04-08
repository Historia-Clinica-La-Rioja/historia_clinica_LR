import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import UnitOfMeasureList from './UnitOfMeasureList';
import UnitOfMeasureShow from './UnitOfMeasureShow';
import UnitOfMeasureEdit from './UnitOfMeasureEdit';

import { ROOT, ADMINISTRADOR } from '../roles';

const unitsOfMeasure = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? UnitOfMeasureList : undefined,
    show: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? UnitOfMeasureShow : undefined,
    create: undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? UnitOfMeasureEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default unitsOfMeasure;