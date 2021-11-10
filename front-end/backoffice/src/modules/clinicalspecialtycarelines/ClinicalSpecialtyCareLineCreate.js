import React from 'react';
import {
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const redirect = (basePath, id, data) => `/carelines/${data.careLineId}/show`;

const searchToFilter = searchText => ({name: searchText ? searchText : -1});

const ClinicalSpecialtyCareLineCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <ReferenceInput
                source="careLineId"
                reference="carelines"
                sort={{ field: 'description', order: 'ASC' }}
                label="resources.clinicalspecialtycarelines.fields.careLineId"
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>

            <ReferenceInput
                source="clinicalSpecialtyId"
                reference="clinicalspecialties"
                sort={{ field: 'name', order: 'ASC' }}
                label="resources.clinicalspecialtycarelines.fields.clinicalSpecialtyId"
                filterToQuery={searchToFilter}
            >
                <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default ClinicalSpecialtyCareLineCreate;
