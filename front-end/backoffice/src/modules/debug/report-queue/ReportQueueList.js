import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    DateField,
    ReferenceField,
} from 'react-admin';

const ReportQueueList = props => (
    <List {...props} sort={{ field: 'createdOn', order: 'DESC' }} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <DateField source="createdOn" showTime />
            <DateField source="generatedOn" showTime />
            <TextField source="generatedError" sortable={false}/>
            <ReferenceField source="fileId"  link="show"  reference="files" sortable={false}>
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default ReportQueueList;
