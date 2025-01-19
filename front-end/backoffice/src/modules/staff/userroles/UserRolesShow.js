import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
} from 'react-admin';

const UserRolesShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <ReferenceField
                label="resources.userroles.fields.roleId"
                source="roleId"  link={false}  reference="roles">
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField
                label="resources.userroles.fields.institutionId"
                source="institutionId"  link={false}  reference="institutions">
                <TextField source="name" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default UserRolesShow;
