import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    NumberField,
    EditButton,
    SelectField,
    useRecordContext
} from 'react-admin';
import UpdateStatusButton from './UpdateStatusButton';
import { STATUS_CHOICES, procedureTemplateIsUpdateable } from './ProcedureTemplateStatus';

const ConditionalEdit = props => {
    const record = useRecordContext(props);
    if (procedureTemplateIsUpdateable(record.statusId))
        return <EditButton {...props}/>;
    return null;
}

const ProcedureTemplateList = props => (
    <List 
        {...props}
        exporter={false}
        bulkActionButtons={false}
        sort={{ field: 'id', order: 'ASC' }}
        filter={{ deleted: false }}
    >
        <Datagrid rowClick="show">
            <NumberField source="id"/>
            <TextField source="description"/>
            <SelectField source="statusId" choices={STATUS_CHOICES}/>
            <ConditionalEdit/>
            <UpdateStatusButton/>
        </Datagrid>
    </List>
);

export default ProcedureTemplateList;