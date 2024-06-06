import React from 'react';
import {
    Create,
    SimpleForm,
    required, ReferenceField, TextField, ReferenceInput, SelectInput, AutocompleteInput
} from 'react-admin';
import CustomToolbar from '../../../modules/components/CustomToolbar';

const redirect = (basePath, id, data) => `/users/${data.userId}/show`;


const UserRoleCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <ReferenceField
                label="resources.userroles.fields.userId"
                source="userId" reference="users" link={false}>
                <TextField source="username" />
            </ReferenceField>
            <ReferenceInput
                source="roleId"
                reference="roles"
                sort={{ field: 'description', order: 'ASC' }}
                validate={[required()]}
            >
                <SelectInput optionText="description" optionValue="id"/>
            </ReferenceInput>
            <ReferenceInput
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchText => ({name: searchText})}
            >
                <AutocompleteInput optionText="name" optionValue="id"/>
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default UserRoleCreate;
