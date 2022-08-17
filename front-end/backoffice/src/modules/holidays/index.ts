import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import HolidayShow from './show';
import HolidayList from './list';
import HolidayCreate from './create';
import HolidayEdit from './edit';

import { ROOT, ADMINISTRADOR } from '../roles';

const holidays = (permissions: SGXPermissions) => ({
    show: HolidayShow,
    list: HolidayList,
    create: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? HolidayCreate : undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? HolidayEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default holidays;