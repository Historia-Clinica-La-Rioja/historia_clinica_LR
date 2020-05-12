import React from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    SelectInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';

const searchToFilter = searchText => ({description: searchText ? searchText : -1});
const renderSpecialty = (choice) => `${choice.description} - ${choice.descriptionProfessionRef}`;
const HealthcareProfessionalSpecialtyCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />

            <ReferenceInput
                source="healthcareProfessionalId"
                reference="healthcareprofessionals"
                label="resources.healthcareprofessionalspecialties.fields.healthcareProfessionalId"
                sort={{ field: 'licenseNumber', order: 'ASC' }}
            >
                <SelectInput optionText="licenseNumber" optionValue="id" validate={[required()]} options={{ disabled: true }} />
            </ReferenceInput>

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

export default HealthcareProfessionalSpecialtyCreate;
