import React from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const redirect = (basePath, id, data) => `/sectors/${data.sectorId}/show`;

const noWhitespace = (value) => {
    if (value && value.trim() === '') {
        return 'Este campo no puede estar vacío o solo contener espacios en blanco';
    }
};

const validateDescription = [required(), noWhitespace];
const searchToFilter = searchText => ({name: searchText ? searchText : -1});
const ClinicalServiceSectorCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <TextInput source="description" validate={validateDescription} />

            <ReferenceInput
                source="sectorId"
                reference="sectors"
                sort={{ field: 'description', order: 'ASC' }}
                label="resources.clinicalspecialtysectors.fields.sectorId"
            >
                <AutocompleteInput optionText="description" optionValue="id" validate={[required()]} options={{ disabled: true }} />
            </ReferenceInput>

            <ReferenceInput
                source="clinicalSpecialtyId"
                reference="clinicalservices"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchToFilter}
                perPage={100}
            >
                <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default ClinicalServiceSectorCreate;
