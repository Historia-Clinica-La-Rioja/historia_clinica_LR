
import PropertyShow from './PropertyShow';
import PropertyList from './PropertyList';
import SGXPermissions from "../../libs/sgx/auth/SGXPermissions";

const properties = (permissions: SGXPermissions) => ({
    show: permissions.isOn('HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA') ? PropertyShow : undefined,
    list: permissions.isOn('HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA') ? PropertyList : undefined,
});

export default properties;
