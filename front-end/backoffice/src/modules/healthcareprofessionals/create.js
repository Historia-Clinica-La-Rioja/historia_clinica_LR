import React from 'react';
import {AutocompleteInput, Create, ReferenceInput, required, SimpleForm, TextInput} from 'react-admin';
import PeopleReferenceInput from '../people/PeopleReferenceInput';
import SaveCancelToolbar from "../../modules/components/save-cancel-toolbar";

const searchToFilter = searchText => ({description: searchText ? searchText : -1});
const searchSpecialtyToFilter = searchText => ({name: searchText ? searchText : -1});
const renderSpecialty = (choice) => `${choice.description} - ${choice.descriptionProfessionRef}`;
const HealthcareProfessionalCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<SaveCancelToolbar />}>
            <PeopleReferenceInput source="personId" validate={[required()]} />

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
