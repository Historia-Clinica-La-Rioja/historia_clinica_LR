import {
    List,
    Datagrid,
    TextField,
    usePermissions, ReferenceField,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from "../roles";
import React from "react";

const InstitutionPracticesList = props => {
    const { permissions } = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (
        <List {...props} bulkActionButtons={false} hasCreate={userIsAdminInstitutional}>
            <Datagrid rowClick="show">
                <TextField source="id" />
                <ReferenceField source="institutionId" reference="institutions" >
                    <TextField source="name" />
                </ReferenceField>
                <TextField source="description" />
                <TextField source="customId" />
                <SgxDateField source="lastUpdate" />
            </Datagrid>
        </List>
    );
};

export default InstitutionPracticesList;

