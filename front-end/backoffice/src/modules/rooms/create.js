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
import CustomToolbar from "../../modules/components/CustomToolbar";

const RoomCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" validate={[required()]} />
            <DateInput source="dischargeDate" />
            <ReferenceInput
                source="clinicalSpecialtySectorId"
                reference="clinicalspecialtysectors"
                sort={{ field: 'description', order: 'ASC' }}
                filterToQuery={searchText => ({description: searchText})}                
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default RoomCreate;
