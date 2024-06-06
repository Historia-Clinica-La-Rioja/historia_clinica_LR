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
const RenderModality = (data ) => {
    const modality = data.record;
    return  (<>{modality.acronym} ({modality.description})</>);
};

const EquipmentList = props => {

    return (
        <List {...props} bulkActionButtons={false}  hasCreate={false}>
            <Datagrid rowClick={UserIsAdmin()?"show":""}>
                <TextField source="name" />
                <TextField source="aeTitle" />
                <ReferenceField link={false}  source="sectorId" reference="sectors">
                    <TextField source="description"/>
                </ReferenceField>
                <ReferenceField link={false} source="pacServerId"  reference="pacserversimagelvl">
                    <TextField  source="name" />
                </ReferenceField>
                <ReferenceField link={false}source="orchestratorId" reference="orchestrator">
                    <TextField  source="name" />
                </ReferenceField>
                <ReferenceField link={false}source="modalityId" reference="modality">
                    <RenderModality/>
                </ReferenceField>
            </Datagrid>
        </List>
    );
};

export default EquipmentList;
