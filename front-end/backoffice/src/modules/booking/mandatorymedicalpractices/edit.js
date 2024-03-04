import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    NumberInput,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';


const MandatoryMedicalPracticeEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show"  toolbar={<CustomToolbar/>}>
            <NumberInput source="id" />
            <TextInput source="description" />
            <TextInput source="mmpCode" />
            <TextInput source="snomedId" />
        </SimpleForm>
    </Edit>
);

export default MandatoryMedicalPracticeEdit;
