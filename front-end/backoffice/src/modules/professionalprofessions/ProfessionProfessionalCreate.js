import React from 'react';
import {AutocompleteInput, Create, ReferenceInput, required, SimpleForm} from 'react-admin';

import PersonReferenceField from "../person/PersonReferenceField";

const redirect = (basePath, id, data) => `/person/${data.personId}/show/2`;

const searchSpecialtyToFilter = searchText => ({name: searchText ? searchText : -1});
const searchToFilter = searchText => ({description: searchText ? searchText : -1});
const renderSpecialty = (choice) => choice ? `${choice.description} - ${choice.descriptionProfessionRef}` : '';

const ProfessionProfessionalCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} >
            <PersonReferenceField source="personId" />
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

export default ProfessionProfessionalCreate;
