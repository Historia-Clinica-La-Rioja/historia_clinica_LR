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

const MedicalCoveragePlanField = ({formData}) => {
    return formData.type !== 1 ? null : (
                <TextInput source="plan" validate={[
                    maxLength(10)]}/>
        )
}

const MedicalCoverageRnosField = ({formData}) => {
    return formData.type !== 2 ? null : (
            <TextInput source="rnos" validate={[
                maxLength(10), number()]}/>
    )
}

const MedicalCoverageAcronymField = ({formData}) => {
    return formData.type !== 2 ? null : (
            <TextInput source="acronym" validate={[
                maxLength(18)]}/>
    )
}

const MedicalCoverageCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show"  toolbar={<CustomToolbar/>}>
            <TextInput source="name" validate={[required()]}/>
            {/*Medical Coverage Type*/}
            <ReferenceInput
                reference="medicalcoveragetypes"
                source="type">
                <SelectInput optionText="value" optionValue="id" validate={[required()]}/>
            </ReferenceInput>


            {/*Plan private health insurance*/}
            <FormDataConsumer>
                {formDataProps => (<MedicalCoveragePlanField {...formDataProps} source="plan"/>)}
            </FormDataConsumer>

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
