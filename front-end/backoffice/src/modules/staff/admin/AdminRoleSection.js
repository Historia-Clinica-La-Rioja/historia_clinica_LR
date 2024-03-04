import React from "react";
import {
    Datagrid,
    DeleteButton,
    ReferenceField,
    ReferenceManyField,
    TextField,
} from 'react-admin';

const redirect = (basePath) => {
    return `/admin/${basePath.record.id}/show`;
};

const AdminRoleSection = (props) => (
    <ReferenceManyField
        addLabel={false}
        reference="userroles"
        target="userId"
    >
        <Datagrid rowClick="show">
            <ReferenceField source="roleId"  link={false}  reference="roles">
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField source="institutionId"  link={false}  reference="institutions">
                <TextField source="name" />
            </ReferenceField>
            <DeleteButton redirect={redirect(props)} />
        </Datagrid>
    </ReferenceManyField>
);

export default AdminRoleSection;
