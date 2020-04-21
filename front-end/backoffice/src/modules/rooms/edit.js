import React from 'react';
import {
    TextInput,
    AutocompleteInput,
    ReferenceInput,
    Edit,
    SimpleForm,
    required,
    DateInput
} from 'react-admin';

const BedEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show">
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" validate={[required()]} />
            <DateInput source="dischargeDate" />
            <ReferenceInput
                source="clinicalSpecialtySectorId"
                reference="clinicalspecialtysectors"
                sort={{ field: 'description', order: 'ASC' }}
            >
                <AutocompleteInput optionText="description" optionValue="id"/>
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);

export default BedEdit;
