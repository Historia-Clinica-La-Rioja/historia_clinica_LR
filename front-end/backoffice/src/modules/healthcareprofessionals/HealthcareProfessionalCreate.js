import React from 'react';
import {AutocompleteInput, Create, ReferenceInput, required, SimpleForm, TextInput} from 'react-admin';

import PersonReferenceInput from '../person/PersonReferenceInput';

const redirect = (basePath, id, data) => `/person/${data.personId}/show/2`;

const searchToFilter = searchText => ({description: searchText ? searchText : -1});
const searchSpecialtyToFilter = searchText => ({name: searchText ? searchText : -1});
const renderSpecialty = (choice) => choice ? `${choice.description} - ${choice.descriptionProfessionRef}` : '';
const HealthcareProfessionalCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} >
            <PersonReferenceInput source="personId" validate={[required()]} />

            <TextInput source="licenseNumber" validate={[required()]} />

            <ReferenceInput
                source="professionalSpecialtyId"
                reference="professionalspecialties"
                sort={{ field: 'description', order: 'ASC' }}
                filterToQuery={searchToFilter}
            >
                <AutocompleteInput optionText={renderSpecialty} optionValue="id" validate={[required()]} />
            </ReferenceInput>

            <ReferenceInput
                source="clinicalSpecialtyId"
                reference="clinicalspecialties"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchSpecialtyToFilter}
            >
                <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
            </ReferenceInput>

        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalCreate;
