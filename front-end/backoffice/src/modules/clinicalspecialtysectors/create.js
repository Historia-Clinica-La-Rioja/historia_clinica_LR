import React from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';

const ClinicalSpecialtySectorCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <ReferenceInput
                source="sectorId"
                reference="sectors"
                sort={{ field: 'description', order: 'ASC' }}
            >
                <AutocompleteInput optionText="description" optionValue="id" validate={[required()]} />
            </ReferenceInput>
            <ReferenceInput
                source="clinicalSpecialtyId"
                reference="clinicalspecialties"
                sort={{ field: 'name', order: 'ASC' }}
            >
                <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default ClinicalSpecialtySectorCreate;
