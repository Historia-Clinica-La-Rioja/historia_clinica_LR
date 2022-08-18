import React from 'react';
import {
    TextInput,
    DateInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const HolidayCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />} >
            <TextInput source="description" validate={[required()]} />
            <DateInput source="date" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default HolidayCreate;