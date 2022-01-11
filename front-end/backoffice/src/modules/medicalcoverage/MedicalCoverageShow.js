import React, { Fragment } from 'react';
import {
    Datagrid,
    DeleteButton,
    EditButton,
    ListButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField,
    TopToolbar,
    useRecordContext,
    Labeled
} from 'react-admin';
import SectionTitle from "../components/SectionTitle";
import CreateRelatedButton from "../components/CreateRelatedButton";

const PREPAGA = 1;
const OBRA_SOCIAL = 2;

const MedicalCoverageShowActions = ({data}) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/medicalcoverages" label="Listar Coberturas médicas"/>
                <EditButton basePath="/medicalcoverages" record={{id: data.id}}/>
            </TopToolbar>
        )
};

const PrivateHealthInsurancePlanComponent = (props) => {
    const record = useRecordContext(props);
    return record && record.type === PREPAGA
        ?
        <Fragment>
            <SectionTitle label="resources.medicalcoverages.fields.plans"/>
            <CreateRelatedButton
                reference="privatehealthinsuranceplans"
                refFieldName="privateHealthInsuranceId"
                label="resources.privatehealthinsuranceplans.addRelated"
                {...props}/>
            <ReferenceManyField
                addLabel={false}
                reference="privatehealthinsuranceplans"
                target="privateHealthInsuranceId"
                {...props}>
                <Datagrid>
                    <TextField source="plan"/>
                    <DeleteButton redirect="/medicalcoverages"/>
                </Datagrid>
            </ReferenceManyField>
        </Fragment>
        : null;
}

const MedicalCoverageAcronymField = (props) => {
    const record = useRecordContext(props);
    return record && record.type === OBRA_SOCIAL
        ?
        <Labeled label="Acrónimo">
            <TextField source="acronym"/>
        </Labeled>
        : null;
}

const MedicalCoverageRnosField = (props) => {
    const record = useRecordContext(props);
    return record && record.type === OBRA_SOCIAL
        ?
        <Labeled label="Rnos">
            <TextField source="rnos"/>
        </Labeled>
        : null;
}

const MedicalCoverageShow = props => (
    <Show actions={<MedicalCoverageShowActions/>} {...props}>
        <SimpleShowLayout>
            <TextField source="name"/>
            <TextField source="cuit"/>
            <ReferenceField source="type" reference="medicalcoveragetypes" link={false}>
                <TextField source="value"/>
            </ReferenceField>
            <MedicalCoverageRnosField/>
            <MedicalCoverageAcronymField/>
            <PrivateHealthInsurancePlanComponent/>
        </SimpleShowLayout>
    </Show>
);

export default MedicalCoverageShow;
