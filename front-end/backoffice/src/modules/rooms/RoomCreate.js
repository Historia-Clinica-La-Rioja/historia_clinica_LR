import React from 'react';
import {AutocompleteInput, Create, DateInput, ReferenceInput, required, SimpleForm, TextInput,} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const redirect = (basePath, id, data) => `/sectors/${data.sectorId}/show`;

const RoomCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" validate={[required()]} />
            <DateInput source="dischargeDate" />

            <ReferenceInput
                source="sectorId"
                reference="sectors"
                sort={{ field: 'description', order: 'ASC' }}
                filterToQuery={searchText => ({description: searchText})}
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default RoomCreate;
