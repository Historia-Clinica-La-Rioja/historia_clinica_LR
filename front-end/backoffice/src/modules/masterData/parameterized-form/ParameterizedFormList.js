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

const ParameterizedFormList = props => {
    const {permissions} = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    const hasCreate = !userIsAdminInstitutional;
    return (
        <List
            {...props}
            hasCreate={hasCreate}
            bulkActionButtons={false}
            exporter={false}
            sort={{ field: 'id', order: 'ASC' }}
            filter={{ 'isDomain' : true }}
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
        </List>);
};

export default ParameterizedFormList;