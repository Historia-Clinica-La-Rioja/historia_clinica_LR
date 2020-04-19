import React from 'react';
import {
    TextInput,
    ReferenceInput,
    AutocompleteInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';

const ClinicalSpecialtySectorEdit = props => (
    <Edit {...props}>
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
    </Edit>
);

export default ClinicalSpecialtySectorEdit;
