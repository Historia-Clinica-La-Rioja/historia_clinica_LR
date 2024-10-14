import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    BooleanInput,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const redirect = (basePath, id, data) => `/institutions/${data.institutionId}/show`

const InstitutionMedicineFinancingStatusEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>} >
            <TextInput source="conceptPt" disabled />
            <BooleanInput source="financedByDomain" label="Financiado por dominio" disabled />
            <BooleanInput source="financedByInstitution" label="Financiado por institucion"/>
        </SimpleForm>
    </Edit>
);

export default InstitutionMedicineFinancingStatusEdit;