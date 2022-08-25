import React from 'react';
import {
    AutocompleteInput,
    Create,
    FormDataConsumer,
    ReferenceInput,
    required,
    SimpleForm
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const renderPerson = (choice) => choice ? `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}` : '';

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
                label="resources.healthcareprofessionalhealthinsurances.fields.personId"
                source="healthcareProfessionalId"
                reference="healthcareprofessionals"
                sort={{ field: 'firstName', order: 'ASC' }}
                validate={[required()]}
                filterToQuery={searchText => ({firstName: searchText})}
            >
                <AutocompleteInput optionText={renderPerson} optionValue="id" />
            </ReferenceInput>

            <FormDataConsumer>
                {formDataProps => ( <HealthcareProfessionalHealthInsurance {...formDataProps} source="medicalCoverageId" />)}
            </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalHealthInsuranceCreate;
