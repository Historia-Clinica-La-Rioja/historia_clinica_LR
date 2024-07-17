import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    NumberField,
    EditButton,
    SelectField,
    useRecordContext,
    Filter,
    BooleanInput,
    TextInput,
    usePermissions,
} from 'react-admin';
import { FORM_STATUS_CHOICES, formIsUpdatable } from './ParameterizedFormStatus';
import UpdateParameterizedFormStatusButton from './UpdateParameterizedFormStatusButton';
import ParameterizedFormScopes from './ParameterizedFormScope';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

const ConditionalFormEdit = props => {
    const record = useRecordContext(props);
    return formIsUpdatable(record.statusId) && !UserIsInstitutionalAdmin() ? <EditButton {...props}/> : null;
}

const UserIsInstitutionalAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin = permissions?.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE);
    return userAdmin;
}

const StatusFilters = (props) => {
    return(
    <Filter {...props}>
        <BooleanInput
            source="excludeInactive"
            label="resources.parameterizedform.excludeInactive"
            alwaysOn/>
        <TextInput source="name" label="resources.parameterizedform.formName" />
    </Filter>);
}

const ParameterizedFormList = props => (
    <List
        {...props}
        hasCreate={true}
        bulkActionButtons={false}
        exporter={false}
        sort={{ field: 'id', order: 'ASC' }}
        filter={{ deleted: false }}
        filters={<StatusFilters />}
        filterDefaultValues={{excludeInactive: true}}
    >
        <Datagrid rowClick="show">
            <NumberField source="id" />
            <TextField label="resources.parameterizedform.formName" source="name" />
            <SelectField label="resources.parameterizedform.status" source="statusId" choices={FORM_STATUS_CHOICES} />
            <ParameterizedFormScopes label="resources.parameterizedform.scope"/>
            <ConditionalFormEdit />
            {!UserIsInstitutionalAdmin() && <UpdateParameterizedFormStatusButton /> }
        </Datagrid>
    </List>
);

export default ParameterizedFormList;