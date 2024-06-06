import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import {
    SgxDateInput,
    CustomToolbar,
} from '../../components';

const HolidayCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />} >
            <TextInput source="description" validate={[required()]} />
            <SgxDateInput source="date" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default HolidayCreate;