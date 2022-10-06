import {
    List,
    Datagrid,
    TextField,
    usePermissions,
    ReferenceField,
    CreateButton,
    TopToolbar,
} from 'react-admin';
import {ADMINISTRADOR, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ROOT} from "../roles";
import React from "react";

const ListActions = () => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
    return(
        <TopToolbar>
            <CreateButton disabled={userIsRootOrAdmin} />
        </TopToolbar>
    );
};

const CareLineInstitutionList = props => {
    const {permissions} = usePermissions();
    const userIsInstitutionalAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (
        <List {...props} actions={<ListActions/>} bulkActionButtons={false} hasCreate={userIsInstitutionalAdmin} >
            <Datagrid rowClick="show">
                <TextField source="id"/>
                <ReferenceField source="institutionId" reference="institutions" >
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines" >
                    <TextField source="description" />
                </ReferenceField>
            </Datagrid>
        </List>
    );
};

export default CareLineInstitutionList;