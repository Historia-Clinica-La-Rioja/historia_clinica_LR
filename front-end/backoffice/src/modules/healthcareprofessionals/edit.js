import React from 'react';
import {
    TextInput,
    ReferenceInput,
    BooleanInput,
    AutocompleteInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;

const HealthcareProfessionalEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <ReferenceInput
                source="personId"
                reference="people"
                sort={{ field: 'identificationNumber', order: 'ASC' }}
                filterToQuery={searchText => ( searchText ? { identificationNumber: searchText } : '')}
                validate={[required()]}
            >
                <AutocompleteInput optionText={renderPerson} optionValue="id"/>
            </ReferenceInput>
            <TextInput source="licenseNumber" validate={[required()]} />
            <BooleanInput source="isMedicalDoctor" />
        </SimpleForm>
    </Edit>
);

export default HealthcareProfessionalEdit;
