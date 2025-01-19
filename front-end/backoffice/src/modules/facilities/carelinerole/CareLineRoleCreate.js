import React from 'react';
import {
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const redirect = (basePath, id, data) => `/carelines/${data.careLineId}/show`;

const CareLineRoleCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <ReferenceInput
                source="careLineId"
                reference="carelines"
                label="resources.carelinerole.fields.careLineId"
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>

            <ReferenceInput
                source="roleId"   
                reference="roles"
                label="resources.carelinerole.fields.roleId"
                filterToQuery={searchText => ({ term: searchText})}
                perPage={5}
            >
                <AutocompleteInput resettable={false} optionText="description" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default CareLineRoleCreate;
