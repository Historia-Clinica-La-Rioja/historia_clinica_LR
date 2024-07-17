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
    List
} from 'react-admin';
import { CustomToolbar } from '../../components';

const EmptyTitle = () => <span />;

const ShowGroupPharmacos = (props) => {
    const record = useRecordContext(props);
    return (
        <List
            {...props}
            resource="medicinegroupmedicines"
            filter= {{ 'medicineGroupId': record.medicineGroupId}}
            bulkActionButtons={false}
            exporter={false}
            hasCreate={false}
            perPage={10}
            title={<EmptyTitle />}
            empty={false}
        >
            <Datagrid>
                <TextField label="resources.medicinefinancingstatus.fields.conceptPt" source="conceptPt" />
                <BooleanField label="resources.medicinefinancingstatus.fields.financed" source="financed" />
            </Datagrid>
        </List>
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
            filter= {{ 'medicineGroupId': record.medicineGroupId}}
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
    const group = useGetOne('institutionmedicinegroups', props.id);
    const isDomain = group.data?.isDomain;
    return(
        <Show {...props} hasEdit={true} >
            <SimpleShowLayout toolbar={<CustomToolbar/>} >
                <TextField source="name" disabled/>
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
                <BooleanField source="allDiagnoses" />
                <br/>
                <span>Mensaje para indicaciones</span>
                <TextField  source="message" label=""/>
                <br/>
                <BooleanField source="enabled" />
                <br/>
                {isDomain &&
                <Fragment>
                    <TabbedShowLayout>
                        <Tab label="Fármacos" id="pharmacos">
                            <ShowGroupPharmacos/>
                        </Tab>
                        <Tab label="Problemas/Diagnósticos" id="diagnoses">
                            <ShowGroupProblems/>
                        </Tab>
                    </TabbedShowLayout>
                </Fragment>
                }
            </SimpleShowLayout>
        </Show> 
    );
}


export default InstitutionMedicineGroupShow;