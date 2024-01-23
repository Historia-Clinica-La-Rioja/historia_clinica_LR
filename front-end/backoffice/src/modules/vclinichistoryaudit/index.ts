import VClinicHistoryAuditShow from './VClinicHistoryAuditShow';
import VClinicHistoryAuditList from './VClinicHistoryAuditList';
import {AUDITORIA_DE_ACCESO} from "../roles";
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";

const vclinichistoryaudit = (permissions: SGXPermissions) => ({
    show: permissions.hasAnyAssignment(AUDITORIA_DE_ACCESO) ?VClinicHistoryAuditShow: undefined,
    list: permissions.hasAnyAssignment(AUDITORIA_DE_ACCESO) ? VClinicHistoryAuditList: undefined,
    options: {
        submenu: 'more'
    }
});

export default vclinichistoryaudit;
