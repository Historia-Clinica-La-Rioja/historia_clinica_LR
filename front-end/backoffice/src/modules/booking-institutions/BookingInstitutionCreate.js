import React from 'react';
import {
    Create,
    SimpleForm,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";


const BookingInstitutionCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <SgxSelectInput source="id" element="institutions" optionText="name" alwaysOn allowEmpty={false}/>
        </SimpleForm>
    </Create>
);

export default BookingInstitutionCreate;
