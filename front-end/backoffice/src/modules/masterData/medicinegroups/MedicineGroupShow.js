import React, { Fragment } from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    BooleanField,
    TopToolbar,
    ListButton,
    EditButton,
    TabbedShowLayout,
    Tab,
    ReferenceManyField,
    Datagrid,
    DeleteButton,
    Pagination,
    useRecordContext
 }from 'react-admin';
import { CreateRelatedButton } from '../../components';

const MedicineGroupShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/medicinegroups" label="Listar grupos de fármacos"/>
                <EditButton basePath="/medicinegroups" record={{ id: data.id }} /> 
            </TopToolbar>
        )
};

const AddPharmacoToGroup = (props) => {
    const record = useRecordContext(props);
    const customRecord = {medicineGroupId: record.id};
    return (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="medicinegroupmedicines"
            refFieldName="medicineGroupId"
            label="resources.medicinegroups.fields.addpharmaco"/>
    );
}

const AddProblemToGroup = (props) => {
    const record = useRecordContext(props);
    const customRecord = {medicineGroupId: record.id};
    const allDiagnoses = record && record.allDiagnoses;
    return ( allDiagnoses ? null :
        <CreateRelatedButton
            customRecord={customRecord}
            reference="medicinegroupproblems"
            refFieldName="medicineGroupId"
            label="resources.medicinegroups.fields.addproblem"/>
    );
}

const ShowPharmacos = (props) => {
    return (
        <ReferenceManyField
            id='medicinegroupmedicines'
            addLabel={false}
            reference='medicinegroupmedicines'
            target='medicineGroupId'
            pagination={<Pagination/>}
        >
            <Datagrid>
                <TextField label="resources.medicinefinancingstatus.fields.conceptPt" source="conceptPt" />
                <BooleanField label="resources.medicinefinancingstatus.fields.financed" source="financed" />
                <DeleteButton redirect={false}/>
            </Datagrid>
        </ReferenceManyField>
    );
}

const ShowProblems = (props) => {
    const record = useRecordContext(props);
    const allDiagnoses = record && record.allDiagnoses;
    return allDiagnoses ? <p>Se incluyen todos los problemas/diagnósticos</p> :
    (
        <ReferenceManyField
            id='medicinegroupproblems'
            addLabel={false}
            target="medicineGroupId"
            reference='medicinegroupproblems'
            pagination={<Pagination/>}
        >
            <Datagrid>
                <TextField label="resources.medicinegroupproblems.fields.conceptPt" source="conceptPt"/>
                <DeleteButton redirect={false}/>
            </Datagrid>
        </ReferenceManyField>
    );
}

const MedicineGroupShow = props => {
    return(
            <Show {...props} actions={<MedicineGroupShowActions/>}>
                <SimpleShowLayout>
                    <TextField source="name"/>
                    <br/>
                    <span>Cobertura pública exclusiva</span>
                    <BooleanField source="requiresAudit"/>
                    <br/>
                    <span>Ámbito</span>
                    <BooleanField source="outpatient"/>
                    <BooleanField source="emergencyCare"/>
                    <BooleanField source="internment"/>
                    <br/>
                    <span>Diagnósticos y problemas</span>
                    <BooleanField label="Incluir todos" source="allDiagnoses" />
                    <br/>
                    <span>Mensaje para indicaciones</span>
                    <TextField source="message" label=""/>
                    <Fragment>
                        <TabbedShowLayout>
                            <Tab label="Fármacos" id="pharmacos">
                                <AddPharmacoToGroup/>
                                <ShowPharmacos/>
                            </Tab>
                            <Tab label="Problemas/Diagnósticos" id="diagnoses">
                                <AddProblemToGroup/>
                                <ShowProblems/>
                            </Tab>
                        </TabbedShowLayout>
                    </Fragment>
                </SimpleShowLayout>
            </Show>
    );
};

export default MedicineGroupShow;