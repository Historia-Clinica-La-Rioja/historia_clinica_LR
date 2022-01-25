import React from 'react';
import {
    Create,
    FormDataConsumer,
    maxLength, number,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    TextInput
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const OBRA_SOCIAL = 2;

const MedicalCoverageRnosField = ({formData}) => {
    return formData.type !== OBRA_SOCIAL ? null : (
            <TextInput source="rnos" validate={[
                maxLength(10), number()]}/>
    )
}

const MedicalCoverageAcronymField = ({formData}) => {
    return formData.type !== OBRA_SOCIAL ? null : (
            <TextInput source="acronym" validate={[
                maxLength(18)]}/>
    )
}

const MedicalCoverageCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show"  toolbar={<CustomToolbar/>}>
            <TextInput source="name" validate={[required()]}/>
            <TextInput source="cuit" validate={[required(), number()]}/>
            {/*Medical Coverage Type*/}
            <ReferenceInput
                reference="medicalcoveragetypes"
                source="type">
                <SelectInput optionText="value" optionValue="id" validate={[required()]}/>
            </ReferenceInput>

            {/*Rnos health inssurance*/}
            <FormDataConsumer>
                {formDataProps => (<MedicalCoverageRnosField {...formDataProps} source="rnos"/>)}
            </FormDataConsumer>

            {/*Acronym health insurance*/}
            <FormDataConsumer>
                {formDataProps => (<MedicalCoverageAcronymField {...formDataProps} source="acronym"/>)}
            </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default MedicalCoverageCreate;
