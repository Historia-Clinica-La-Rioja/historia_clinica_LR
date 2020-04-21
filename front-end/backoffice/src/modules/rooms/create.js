import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    DateInput,
    ReferenceInput,
    AutocompleteInput,
    required,
} from 'react-admin';

const RoomCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
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
    </Create>
);

export default RoomCreate;
