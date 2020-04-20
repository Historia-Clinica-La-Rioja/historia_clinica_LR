import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    DateInput
} from 'react-admin';

import OnlySaveToolbar from '../components/only-save-toolbar';

const RoomCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<OnlySaveToolbar />}>
            <TextInput source="roomNumber" validate={[required()]} />
            <TextInput source="description" validate={[required()]} />
            <TextInput source="type" />
            <DateInput source="dischargeDate" />
        </SimpleForm>
    </Create>
);

export default RoomCreate;
