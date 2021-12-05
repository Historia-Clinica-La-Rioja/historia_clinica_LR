import React from 'react';
import {
    Create,
    TextInput,
    SimpleForm,
    required,
    BooleanInput,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const CareLineCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <TextInput source="description" validate={[required()]} />
            <BooleanInput source="consultation" disabled={false} initialValue={false}/>
            <BooleanInput source="procedure" disabled={false} initialValue={false}/>
        </SimpleForm>
    </Create>
);

export default CareLineCreate;
