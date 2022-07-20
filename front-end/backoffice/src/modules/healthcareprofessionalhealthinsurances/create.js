import React from 'react';
import {Create, FormDataConsumer, ReferenceInput, required, SimpleForm} from 'react-admin';
import {AutocompleteInput} from "ra-ui-materialui";
import CustomToolbar from "../components/CustomToolbar";
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const HealthcareProfessionalHealthInsurance = ({ formData, ...rest }) => {
    return !formData.healthcareProfessionalId ? null :  (

        <SgxSelectInput {...rest}
            element="medicalcoverages"
            optionText="name"
            alwaysOn
            allowEmpty={false} />
    );
};

const HealthcareProfessionalHealthInsuranceCreate = (props) => (
    <Create {...props}>
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

            <FormDataConsumer>
                {formDataProps => ( <HealthcareProfessionalHealthInsurance {...formDataProps} source="medicalCoverageId" />)}
            </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalHealthInsuranceCreate;