import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput,
    NumberField,
    FunctionField
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";

const RestClientMeasureFilter = (props) => (
    <Filter {...props}>
        <TextInput source="host" />
    </Filter>
);

const RestClientMeasuresList = props => (
    <List {...props} filters={<RestClientMeasureFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="show" style={{tableLayout: 'fixed'}}>
            <TextField source="host"/>
            <TextField source="path"/>
            <TextField source="method" />
            <NumberField source="responseCode" />
            <FunctionField label="resources.rest-client-measures.fields.time" render={record => `${record.responseTimeInMillis/1000}`} />
            <SgxDateField source="requestDate" showTime/>
        </Datagrid>
    </List>
);

export default RestClientMeasuresList;

