import {React, Fragment} from 'react';
import {
    SimpleShowLayout,
    Show,
    TextField,
    BooleanField,
    TabbedShowLayout,
    Tab,
    Datagrid,
    useRecordContext,
    useGetOne,
    List,
} from 'react-admin';
import { CustomToolbar } from '../../components';
import {CreateRelatedButton} from '../../components';

const EmptyTitle = () => <span />;

const ShowGroupPharmacos = (props) => {
    const record = useRecordContext(props);
    return (
        <List
            {...props}
            resource="medicinegroupmedicines"
            filter= {{ medicineGroupId: record.medicineGroupId, institutionId: record.institutionId }}
            bulkActionButtons={false}
            exporter={false}
            hasCreate={false}
            perPage={10}
            title={<EmptyTitle />}
            empty={false}
        >
            <Datagrid>
                <TextField label="resources.institutionmedicinesfinancingstatus.fields.conceptPt" source="conceptPt" />
                <BooleanField label="resources.institutionmedicinesfinancingstatus.fields.financedByDomain" source="financed" />
                <BooleanField label="resources.institutionmedicinesfinancingstatus.fields.financedByInstitution" source="financedByInstitution" />
            </Datagrid>
        </List>
    );
}

const AddPharmacoToGroup = (props) => {
    const record = useRecordContext(props);
    const customRecord = {medicineGroupId: record.medicineGroupId, institutionId: record.institutionId};
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
    const customRecord = {medicineGroupId: record.medicineGroupId};
    const allDiagnoses = record && record.allDiagnoses;
    return ( allDiagnoses ? null :
        <CreateRelatedButton
            customRecord={customRecord}
            reference="medicinegroupproblems"
            refFieldName="medicineGroupId"
            label="resources.medicinegroups.fields.addproblem"/>
    );
}

const ShowGroupProblems = (props) => {
    const record = useRecordContext(props);
    const allDiagnoses = record && record.allDiagnoses;
    return allDiagnoses ? <p>Se incluyen todos los problemas/diagnósticos</p> :
    (
        <List
            {...props}
            resource="medicinegroupproblems"
            filter= {{ medicineGroupId: record.medicineGroupId, institutionId: record.institutionId }}
            bulkActionButtons={false}
            exporter={false}
            hasCreate={false}
            perPage={10}
            title={<EmptyTitle />}
            empty={false}
        >
            <Datagrid>
                <TextField label="resources.medicinegroupproblems.fields.conceptPt" source="conceptPt"/>
            </Datagrid>
        </List>
    );
}

const InstitutionMedicineGroupShow = props => {

    const { data: record } = useGetOne('institutionmedicinegroups', props.id);

    const isDomain = record?.isDomain;
    return(
        <Show {...props} hasEdit={true} >
            <SimpleShowLayout toolbar={<CustomToolbar/>} >
                <TextField source="name" disabled/>
                <br/>
                <span>Cobertura pública exclusiva</span>
                <BooleanField source="requiresAudit"/>
                {record?.requiredDocumentation && <TextField source="requiredDocumentation"/>}
                <br/>
                <span>Ámbito</span>
                <BooleanField source="outpatient"/>
                <BooleanField source="emergencyCare"/>
                <BooleanField source="internment"/>
                <br/>
                <span>Diagnósticos y problemas</span>
                <BooleanField source="allDiagnoses" />
                <br/>
                <span>Mensaje para indicaciones</span>
                <TextField source="message" label=""/>
                <br/>
                <BooleanField source="enabled" />
                <br/>
                <Fragment>
                    <TabbedShowLayout>
                        <Tab label="Fármacos" id="pharmacos">
                            {!isDomain && <AddPharmacoToGroup/>}  
                            <ShowGroupPharmacos/>
                        </Tab>
                        <Tab label="Problemas/Diagnósticos" id="diagnoses">
                            {!isDomain && <AddProblemToGroup/>}
                            <ShowGroupProblems/>
                        </Tab>
                    </TabbedShowLayout>
                </Fragment>
            </SimpleShowLayout>
        </Show> 
    );
}


export default InstitutionMedicineGroupShow;