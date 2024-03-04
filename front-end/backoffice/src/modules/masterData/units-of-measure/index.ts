import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import UnitOfMeasureList from './UnitOfMeasureList';
import UnitOfMeasureShow from './UnitOfMeasureShow';
import UnitOfMeasureEdit from './UnitOfMeasureEdit';

const unitsOfMeasure = (permissions: SGXPermissions) => ({
    list: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? UnitOfMeasureList : undefined,
    show: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? UnitOfMeasureShow : undefined,
    create: undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? UnitOfMeasureEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default unitsOfMeasure;