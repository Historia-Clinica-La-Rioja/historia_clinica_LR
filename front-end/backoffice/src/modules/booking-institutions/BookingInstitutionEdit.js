import React from 'react';
import {
    Edit,
    SimpleForm,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";


const BookingInstitutionEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <SgxSelectInput source="id" element="institutions" optionText="name" alwaysOn allowEmpty={false}/>
        </SimpleForm>
    </Edit>
);

export default BookingInstitutionEdit;
