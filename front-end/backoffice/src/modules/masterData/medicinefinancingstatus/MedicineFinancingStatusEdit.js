import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    BooleanInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';


const MedicineEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar/>} >
            <TextInput source="conceptPt" disabled />
            <BooleanInput source="financed" label="Financiado por dominio"/>
        </SimpleForm>
    </Edit>
);

export default MedicineEdit;