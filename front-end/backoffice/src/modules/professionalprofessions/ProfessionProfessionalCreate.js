import React from 'react';
import {AutocompleteInput, Create, ReferenceInput, required, SimpleForm} from 'react-admin';

import PersonReferenceField from "../person/PersonReferenceField";

const redirect = (basePath, id, data) => `/person/${data.personId}/show/2`;

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

        </SimpleForm>
    </Create>
);

export default ProfessionProfessionalCreate;
