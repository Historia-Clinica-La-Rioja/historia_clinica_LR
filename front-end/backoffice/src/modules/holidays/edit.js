import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    DateInput
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const HolidayEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />
            <DateInput source="date" disabled={true} /> 
        </SimpleForm>
    </Edit>
);

export default HolidayEdit;
