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

const OBRA_SOCIAL = 2;
const ART = 3;

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

const MedicalCoverageMergeComponent = (props) => {
    const record = useRecordContext(props);
    return record && record.cuit && record.enabled && record.type!== ART
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

const MedicalCoveragePlans = (props) => {
    const record = useRecordContext(props);
    return record && record.type !== ART
        ?
        <Fragment>
            <SectionTitle label="resources.medicalcoverages.fields.plans"/>
            <CreateRelatedButton
                reference="medicalcoverageplans"
                refFieldName="medicalCoverageId"
                label="resources.medicalcoverageplans.addRelated"
                {...props}/>
            <ReferenceManyField
                addLabel={false}
                reference="medicalcoverageplans"
                target="medicalCoverageId"
                {...props}>
                <Datagrid>
                    <TextField source="plan"/>
                    <DeleteButton redirect="/medicalcoverages"/>
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

                {/*Plan medical coverage*/}
                <FormDataConsumer>
                    {formDataProps => (<MedicalCoveragePlans {...formDataProps}/>)}
                </FormDataConsumer>
                <div className="height-30" />
                <MedicalCoverageMergeComponent  />

            </SimpleForm>
    </Edit>
);

export default MedicalCoverageEdit;
