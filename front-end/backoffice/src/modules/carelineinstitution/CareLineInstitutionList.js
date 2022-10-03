import {
    List,
    Datagrid,
    TextField,
    usePermissions, ReferenceField,
} from 'react-admin';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from "../roles";
import React from "react";

const CareLineInstitutionList = props => {
    const {permissions} = usePermissions();
    const userIsInstitutionalAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (
        <List {...props} bulkActionButtons={false} hasCreate={userIsInstitutionalAdmin}>
            <Datagrid rowClick="show">
                <TextField source="id"/>
                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines">
                    <TextField source="description" />
                </ReferenceField>
            </Datagrid>
        </List>
    );
};

export default CareLineInstitutionList;