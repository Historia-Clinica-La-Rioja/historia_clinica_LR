import React from 'react';
import {
    Edit,
    TextInput,
    ReferenceField,
    TextField,
    required
} from 'react-admin';

const BedEdit = props => (
    <Edit {...props}>
        <TextInput source="bedNumber" validate={[required()]} />
        <ReferenceField source="roomId" reference="rooms">
            <TextField source="roomNumber" />
        </ReferenceField>
    </Edit>
);

export default BedEdit;