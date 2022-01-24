import React, { Fragment } from 'react';
import {
    Datagrid,
    DeleteButton,
    Edit,
    FormDataConsumer,
    maxLength, number, Pagination,
    ReferenceInput,
    ReferenceManyField,
    required,
    SelectInput,
    SimpleForm,
    TextField,
    TextInput, useRecordContext,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import SectionTitle from "../components/SectionTitle";
import CreateRelatedButton from "../components/CreateRelatedButton";
import MergeButton from "../../libs/sgx/components/MergeButton";

const PREPAGA = 1;
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

const PrivateHealthInsurancePlanComponent = ({formData}) => {
    return formData.type === PREPAGA ?  (
        <Fragment>
        <SectionTitle label="resources.medicalcoverages.fields.plans"/>
        <CreateRelatedButton
            reference="privatehealthinsuranceplans"
            refFieldName="privateHealthInsuranceId"
            label="resources.privatehealthinsuranceplans.addRelated"/>
        <ReferenceManyField
            addLabel={false}
            reference="privatehealthinsuranceplans"
            target="privateHealthInsuranceId">
            <Datagrid>
                <TextField source="plan"/>
                <DeleteButton redirect="/medicalcoverages"/>
            </Datagrid>
        </ReferenceManyField>
    </Fragment>

    ) : null
}


const MedicalCoverageMergeComponent = (props) => {
    const record = useRecordContext(props);
    return record && record.cuit
        ?
        <Fragment>
            <SectionTitle label="resources.medicalcoverages.fields.merge"/>
            <ReferenceManyField filter={{ type: record.type}}
                                reference="medicalcoveragesmerge"
                                target="referenceId"
                                pagination={<Pagination />}
                                perPage={10}>
                <Datagrid>
                    <TextField  source="name" label="resources.medicalcoverages.fields.name"/>
                    <MergeButton baseMedicalCoverage={record.id}></MergeButton>
                </Datagrid>
            </ReferenceManyField>

        </Fragment>
        : null;
}

const MedicalCoverageEdit = props => (
    <Edit {...props}>
            <SimpleForm toolbar={<CustomToolbar isEdit={true}/>}>
                <TextInput source="name" validate={[required()]}/>
                <TextInput source="cuit" validate={[required(),number()]}/>
                {/*Medical Coverage Type*/}
                <ReferenceInput
                    reference="medicalcoveragetypes"
                    source="type">
                    <SelectInput optionText="value" optionValue="id" validate={[required()]} options={{ disabled: true }}/>
                </ReferenceInput>
                {/*Rnos health inssurance*/}
                <FormDataConsumer>
                    {formDataProps => (<MedicalCoverageRnosField {...formDataProps} source="rnos"/>)}
                </FormDataConsumer>
                {/*Acronym health insurance*/}
                <FormDataConsumer>
                    {formDataProps => (<MedicalCoverageAcronymField {...formDataProps} source="acronym"/>)}
                </FormDataConsumer>

                {/*Plan private health insurance*/}
                <FormDataConsumer>
                    {formDataProps => (<PrivateHealthInsurancePlanComponent {...formDataProps}/>)}
                </FormDataConsumer>
                <MedicalCoverageMergeComponent/>

            </SimpleForm>
    </Edit>
);

export default MedicalCoverageEdit;
