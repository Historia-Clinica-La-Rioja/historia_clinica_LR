import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField, NumberField, FunctionField
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";


const InstitutionShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="uri"/>
            <TextField source="host"/>
            <TextField source="path"/>
            <TextField source="method" />
            <NumberField source="responseCode" />
            <NumberField source="responseTimeInMillis" />
            <FunctionField label="resources.rest-client-measures.fields.time" render={record => `${record.responseTimeInMillis/1000}`} />
            <SgxDateField source="requestDate" showTime/>
        </SimpleShowLayout>
    </Show>
);

export default InstitutionShow;
