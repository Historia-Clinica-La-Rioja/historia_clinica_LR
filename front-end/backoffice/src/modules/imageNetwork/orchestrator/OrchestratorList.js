import React from 'react';
import { Datagrid, List, TextField,ReferenceField,usePermissions} from 'react-admin';
import { 
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
    ADMINISTRADOR,
} from '../../roles';

const UserIsAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin= permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role ||
                    roleAssignment.role === ADMINISTRADOR.role )).length > 0;
 return userAdmin;
}


const OrchestratorList = props => {

    return (
        <List {...props} bulkActionButtons={false}  hasCreate={false}>
            <Datagrid rowClick={UserIsAdmin()?"show":""}>
                <TextField source="name" />
                <TextField source="baseTopic" />
                <ReferenceField link={false}  source="sectorId" reference="sectors">
                    <TextField source="description"/>
                </ReferenceField>
            </Datagrid>
        </List>
    );
};

export default OrchestratorList;
