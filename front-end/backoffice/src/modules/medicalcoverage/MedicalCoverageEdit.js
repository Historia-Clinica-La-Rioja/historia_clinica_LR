import React from 'react';
import {
    Edit,
    FormDataConsumer,
    maxLength,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    TextInput,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const MedicalCoveragePlanField = ({formData}) => {
    return formData.type !== 1 ? null : (
        <TextInput source="plan" validate={[
            maxLength(10)]}/>
    )
}

const MedicalCoverageRnosField = ({formData}) => {
    return formData.type !== 2 ? null : (
            <TextInput source="rnos" validate={[
                maxLength(10)]}/>
    )
}

const MedicalCoverageAcronymField = ({formData}) => {
    return formData.type !== 2 ? null : (
            <TextInput source="acronym" validate={[
                maxLength(18)]}/>
    )
}

const MedicalCoverageEdit = props => (
    <Edit {...props}>
        <SimpleForm toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="name" validate={[required()]}/>
            {/*Medical Coverage Type*/}
            <ReferenceInput
                reference="medicalcoveragetypes"
                source="type">
                <SelectInput optionText="value" optionValue="id" validate={[required()]} options={{ disabled: true }}/>
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
    </Edit>
);

export default MedicalCoverageEdit;
