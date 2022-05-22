import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    TopToolbar,
    ListButton
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";
import RetryReportButton from './RetryReportButton'

const SnvsShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/snvs" label="Listar reportes"/>
                { (data.responseCode !== 200) ? <RetryReportButton record={data}/> : null }
            </TopToolbar>
        )
};
const SnvsShow = props => (
    <Show actions={<SnvsShowActions />} {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="groupEventId" />
            <TextField source="eventId" />
            <TextField source="manualClassificationId" />
            <TextField source="patientId" />
            <TextField source="snomedSctid" />
            <TextField source="snomedPt" />
            <TextField source="status" />
            <TextField source="responseCode" />
            <TextField source="professionalId" />
            <ReferenceField source="institutionId" reference="institutions" link={false}>
                <TextField source="name" />
            </ReferenceField>
            <TextField source="sisaRegisteredId" />
            <SgxDateField source="lastUpdate" />
        </SimpleShowLayout>
    </Show>
);

export default SnvsShow;
