import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
} from 'react-admin';
import {
    SgxDateInput,
    CustomToolbar,
} from '../../components';

const HolidayEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />
            <SgxDateInput source="date" disabled={true} /> 
        </SimpleForm>
    </Edit>
);

export default HolidayEdit;
