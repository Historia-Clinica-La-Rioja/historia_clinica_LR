import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    FunctionField,
    NumberField,
    EditButton,
    DeleteButton
} from 'react-admin';

const renderPracticesList = ({associatedPractices, ...rest}) => {
    if (associatedPractices)
        return associatedPractices.map(p => p.sctid).join(', ')
    return '';
}

const ProcedureTemplateList = props => (
    <List 
        {...props}
        exporter={false}
        bulkActionButtons={false}
        sort={{ field: 'description', order: 'ASC' }}
        filter={{ deleted: false }}
    >
        <Datagrid rowClick="show">
            <NumberField source="id"/>
            <TextField source="description"/>
            <ReferenceField
                source="id" link={false} sortable={false}
                reference="proceduretemplatesnomeds"
                label="resources.proceduretemplates.fields.associatedPractices">
                <FunctionField
                    label="Name"
                    render={record => record && renderPracticesList(record)}
                />
            </ReferenceField>
            <EditButton/>
            <DeleteButton/>
        </Datagrid>
    </List>
);

export default ProcedureTemplateList;