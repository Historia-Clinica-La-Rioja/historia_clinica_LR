import {
    Create,
    SimpleForm,
    TextInput,
    NumberInput,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import React from "react";


const MandatoryMedicalPracticeCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show"  toolbar={<CustomToolbar/>}>
            <NumberInput source="id" />
            <TextInput source="description" />
            <TextInput source="mmpCode" />
            <TextInput source="snomedId" />
        </SimpleForm>
    </Create>
);

export default MandatoryMedicalPracticeCreate;
