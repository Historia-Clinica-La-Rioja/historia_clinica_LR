import {
    List,
    Datagrid,
    TextField,
    usePermissions,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from "../roles";

const SnomedGroupList = props => {
    const { permissions } = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (
        <List {...props} bulkActionButtons={false} hasCreate={userIsAdminInstitutional}>
            <Datagrid rowClick="show">
                <TextField source="id" />
                <TextField source="description" />
                <TextField source="customId" />
                <SgxDateField source="lastUpdate" />
            </Datagrid>
        </List>
    );
};

export default SnomedGroupList;

