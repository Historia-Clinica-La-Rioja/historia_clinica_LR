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
import SaveCancelToolbar from "../../modules/components/save-cancel-toolbar";

const RoomCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<SaveCancelToolbar />}>
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
