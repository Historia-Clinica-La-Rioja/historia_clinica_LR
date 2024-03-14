import React from 'react';
import {
    Datagrid,
    Filter,
    List,
    TextField,
    TextInput, 
    useRecordContext,
} from 'react-admin';
import OutpatientConsultationForwardButton from "./OutpatientConsultationForwardButton";

const CipresEncounterFilter = props =>(
    <Filter {...props}>
        <TextInput source="encounterId" />
        <TextInput source="responseCode"/>
    </Filter>
);

const ShowOutpatientConsultationForwardButton = (props) => {
    const record = useRecordContext(props);
    return record && record.responseCode !== 201 ?
        <OutpatientConsultationForwardButton {...props} /> : null;
};

const CipresEncountersList = props => {
    return (
        <List {...props} bulkActionButtons={false} hasCreate={false} filters={<CipresEncounterFilter/>}>
            <Datagrid rowClick={"show"}>
                <TextField source="encounterId" />

                <TextField source="encounterApiId" />

                <TextField source="status" />

                <TextField  source="responseCode" />

                <TextField  source="date" />

                <ShowOutpatientConsultationForwardButton {...props}/>
            </Datagrid>
        </List>
    );
};

export default CipresEncountersList;
