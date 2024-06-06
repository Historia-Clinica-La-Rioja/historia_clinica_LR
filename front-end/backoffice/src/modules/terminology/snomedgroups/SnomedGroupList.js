import {
    List,
    Datagrid,
    TextField,
    usePermissions,
    Filter,
    TextInput,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

const SnomedGroupListFilter = props =>(
    <Filter {...props}>
        <TextInput source="description" alwaysOn />
    </Filter>
);

const SnomedGroupList = props => {
    const { permissions } = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (
        <List {...props} bulkActionButtons={false} hasCreate={userIsAdminInstitutional} 
            sort={{ field: 'lastUpdate', order: 'DESC' }}
            filters={<SnomedGroupListFilter/>}>
            <Datagrid rowClick="show">
                <TextField source="id" sortable={false} />
                <TextField source="description" />
                <TextField source="customId" />
                <SgxDateField source="lastUpdate" />
            </Datagrid>
        </List>
    );
};

export default SnomedGroupList;

