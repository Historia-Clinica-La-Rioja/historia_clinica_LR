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

import {
    BASIC_BO_ROLES,
} from '../../roles-set';

const UserDeleteToolbar = props => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.hasAnyAssignment(...BASIC_BO_ROLES);
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
