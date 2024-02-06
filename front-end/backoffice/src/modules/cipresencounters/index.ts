import CipresEncountersList from './CipresEncountersList';
import { ROOT } from "../roles";
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";

const cipresEncounters = (permissions: SGXPermissions) => ({
    list: permissions.isOn('HABILITAR_MONITOREO_CIPRES') && permissions.hasAnyAssignment(ROOT) ? CipresEncountersList: undefined,
    options: {
        submenu: 'more'
    }
});

export default cipresEncounters;
