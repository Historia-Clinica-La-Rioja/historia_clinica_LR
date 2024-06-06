import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    AUDITORIA_DE_ACCESO,
} from '../../roles';

import VClinicHistoryAuditShow from './VClinicHistoryAuditShow';
import VClinicHistoryAuditList from './VClinicHistoryAuditList';

const vclinichistoryaudit = (permissions: SGXPermissions) => ({
    show: permissions.isOn('HABILITAR_AUDITORIA_DE_ACCESO_EN_HC') && permissions.hasAnyAssignment(AUDITORIA_DE_ACCESO) ? VClinicHistoryAuditShow: undefined,
    list: permissions.isOn('HABILITAR_AUDITORIA_DE_ACCESO_EN_HC') &&  permissions.hasAnyAssignment(AUDITORIA_DE_ACCESO) ? VClinicHistoryAuditList: undefined,
    options: {
        submenu: 'more'
    }
});

export default vclinichistoryaudit;
