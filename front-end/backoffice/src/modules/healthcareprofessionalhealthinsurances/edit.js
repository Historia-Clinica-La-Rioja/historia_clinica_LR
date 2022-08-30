import React from 'react';
import {Edit, ReferenceInput, required, SimpleForm} from 'react-admin';
import {AutocompleteInput} from "ra-ui-materialui";
import CustomToolbar from "../components/CustomToolbar";
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const HealthcareProfessionalHealthInsuranceEdit = (props) => (
    <Edit {...props}>
        <SimpleForm toolbar={<CustomToolbar />} redirect="list">
            <ReferenceInput
                label="resources.healthcareprofessionals.license"
                reference="healthcareprofessionals"
                sort={{ field: 'licenseNumber', order: 'ASC' }}
                source="healthcareProfessionalId"
                validate={[required()]}
            >
                <AutocompleteInput optionText="licenseNumber" optionValue="id"/>
            </ReferenceInput>

            <SgxSelectInput 
                source="medicalCoverageId"
                element="medicalcoverages"
                optionText="name"
                alwaysOn
                allowEmpty={false} />

        </SimpleForm>
    </Edit>
);

export default HealthcareProfessionalHealthInsuranceEdit;