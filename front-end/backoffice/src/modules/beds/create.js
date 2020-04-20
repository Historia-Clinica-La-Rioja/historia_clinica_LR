import React from 'react';
import {
    Create,
    TextInput,
    ReferenceField,
    TextField,
    required
} from 'react-admin';

const BedCreate = props => (
    <Create {...props}>
        <TextInput source="bedNumber" validate={[required()]} />
        <ReferenceField source="roomId" reference="rooms">
            <TextField source="roomNumber" />
        </ReferenceField>
    </Create>
);

export default BedCreate;