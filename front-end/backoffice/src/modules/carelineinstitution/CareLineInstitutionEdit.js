import React from 'react';
import {
    Edit,
    DeleteButton,
    Toolbar,
    ReferenceField,
    SimpleForm,
    TextField,
    usePermissions,
} from 'react-admin';
import {ADMINISTRADOR, ROOT} from "../roles";

const UserDeleteToolbar = props => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
    return(
        <Toolbar {...props} >
            <DeleteButton disabled={userIsRootOrAdmin} />
        </Toolbar>
    )
};

const CareLineInstitutionEdit = props => {
    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<UserDeleteToolbar />}>
                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name"/>
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines">
                    <TextField source="description"/>
                </ReferenceField>
            </SimpleForm>
        </Edit>
    );
};

export default CareLineInstitutionEdit;
