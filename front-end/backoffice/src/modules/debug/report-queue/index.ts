import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';

import AssessmentIcon from '@material-ui/icons/Assessment';
import ReportQueueShow from './ReportQueueShow';
import ReportQueueList from './ReportQueueList';

import { ROOT, ADMINISTRADOR } from '../../roles';

const check = (permissions: SGXPermissions) => 
    permissions.hasAnyAssignment(ROOT, ADMINISTRADOR);

const reportQueue = (permissions: SGXPermissions) => ({
    icon: AssessmentIcon,
    show: ReportQueueShow,
    list: check(permissions) ? ReportQueueList : undefined,
    options: {
        submenu: 'debug'
    }
});

export default reportQueue;
