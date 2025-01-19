import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    BASIC_BO_ROLES,
    DEFAULT_BO_ROLES,
} from '../../roles-set';

import HolidayShow from './show';
import HolidayList from './list';
import HolidayCreate from './create';
import HolidayEdit from './edit';


const holidays = (permissions: SGXPermissions) => ({
    show: HolidayShow,
    list: permissions.hasAnyAssignment(...DEFAULT_BO_ROLES) ? HolidayList : undefined,
    create: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? HolidayCreate : undefined,
    edit: permissions.hasAnyAssignment(...BASIC_BO_ROLES) ? HolidayEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default holidays;