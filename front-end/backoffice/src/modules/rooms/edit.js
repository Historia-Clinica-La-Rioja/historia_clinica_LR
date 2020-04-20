import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    DateInput
} from 'react-admin';
import OnlySaveToolbar from "../components/only-save-toolbar";

const BedEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<OnlySaveToolbar />}>
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" />
            <DateInput source="dischargeDate" />
        </SimpleForm>
    </Edit>
);

export default BedEdit;
