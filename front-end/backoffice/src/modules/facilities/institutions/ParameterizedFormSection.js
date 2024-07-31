import React from 'react';
import {
    TextField,
    EditButton,
    ReferenceManyField,
    Datagrid,
    usePermissions,
    SelectField,
    Pagination,
    useRecordContext,
    NumberField,
    BooleanField,
    List,
    FunctionField
} from 'react-admin';
import CreateRelatedButton from '../../components/CreateRelatedButton';
import SectionTitle from '../../components/SectionTitle';
import { ADMINISTRADOR, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ROOT } from '../../roles';
import ParameterizedFormScopes from '../../masterData/parameterized-form/ParameterizedFormScope';
import { FORM_STATUS_CHOICES, FORM_STATUS_DRAFT, formIsUpdatable } from '../../masterData/parameterized-form/ParameterizedFormStatus';
import UpdateParameterizedFormStatusButton from '../../masterData/parameterized-form/UpdateParameterizedFormStatusButton';
import { Typography } from '@material-ui/core';
import { UpdateFormEnablementInInstitution } from '../../masterData/parameterized-form/UpdateFormEnablementInInstitution';

const UserIsInstitutionalAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin = permissions?.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE);
    return userAdmin;
}

const UserIsAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin = permissions?.hasAnyAssignment(ROOT, ADMINISTRADOR);
    return userAdmin;
}

const ConditionalFormEdit = props => {
    const record = useRecordContext(props);
    return formIsUpdatable(record.statusId) && !UserIsAdmin() ? <CustomEditButton {...props} /> : null;
}

const CreateInstitutionalParameterizedForm = ({ record }) => {
    const customRecord = { institutionId: record.id, isDomain: false };
    return UserIsInstitutionalAdmin() ? (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="parameterizedform"
            refFieldName="institutionId"
            label="resources.parameterizedform.createRelated" />
    ) : null;
};

const CustomEditButton = (props) => {
    const record = useRecordContext(props);
    return <EditButton basePath="/parameterizedform" record={{ id: record.id, institutionId: record.institutionId }} />
}

const ShowInstitutionalParameterizedForm = (props) => {
    const show = (basePath, id, data) => `/parameterizedform/${data.id}/show`;
    const record = useRecordContext(props);
    return (
        <ReferenceManyField
            id='institutionalParameterizedForm'
            addLabel={false}
            reference="institutionalparameterizedform"
            target="institutionId"
            sort={{ field: 'id', order: 'ASC' }}
            filter={{ 'isDomain': false, 'institutionId': record.institutionId }}
        >
            <ReferenceManyField
                id='institutionalParameterizedForm'
                addLabel={false}
                reference="parameterizedform"
                target="institutionId"
                sort={{ field: 'id', order: 'ASC' }}
                filter={{ 'institutionId': record.id }}
                perPage={10}
                pagination={<Pagination />}
            >
                <Datagrid rowClick={show}>
                    <NumberField source="id" />
                    <TextField label="resources.parameterizedform.formName" source="name" />
                    <SelectField label="resources.parameterizedform.status" source="statusId" choices={FORM_STATUS_CHOICES} />
                    <ParameterizedFormScopes label="resources.parameterizedform.scope" />
                    <ConditionalFormEdit />
                    {!UserIsAdmin() && <UpdateParameterizedFormStatusButton {...props} />}
                </Datagrid>
            </ReferenceManyField>
        </ReferenceManyField>
    )
};


const EmptyTitle = () => <span />;

const ShowDomainParameterizedForm = (props) => {
    const record = useRecordContext(props);
    const show = (basePath, id, data) => `/parameterizedform/${data.id}/show`;
    return (
        <List {...props}
            title={<EmptyTitle />}
            resource="parameterizedform"
            hasCreate={false}
            bulkActionButtons={false}
            exporter={false}
            sort={{ field: 'id', order: 'ASC' }}
            filter={{ 'statusId': FORM_STATUS_DRAFT, 'isDomain': true, 'institutionId': record.id }}
            actions={false}
            empty={<p style={{ paddingLeft: 10, marginTop: 0, color: '#8c8c8c' }}>Sin formularios institucionales definidos</p>}
        >
            <Datagrid rowClick={show}>
                <NumberField source="id" />
                <TextField label="resources.parameterizedform.formName" source="name" />
                <SelectField label="resources.parameterizedform.status" source="statusId" choices={FORM_STATUS_CHOICES} />
                <ParameterizedFormScopes label="resources.parameterizedform.scope" />
                <FunctionField
                    label="Habilitado"
                    render={record => {
                        const isEnabled = record && record.isEnabled;
                        return isEnabled ? (
                            <BooleanField record={record} source="isEnabled" label="Habilitado" />
                        ) : (
                            <BooleanField record={{ isEnabled: false }} source="isEnabled" label="Habilitado" />
                        );
                    }}
                />
                {UserIsInstitutionalAdmin() && <UpdateFormEnablementInInstitution institutionId={record.id} {...props} />}
            </Datagrid>
        </List>
    )
};

export const ParameterizedFormSection = (props) => (
    <div>
        <SectionTitle label="resources.institutions.fields.parameterizedForm" />
        <Typography variant="body1" gutterBottom>
            Dominio
        </Typography>
        <ShowDomainParameterizedForm {...props} />
        < br />
        <Typography variant="body1" gutterBottom>
            Institución
        </Typography>
        <CreateInstitutionalParameterizedForm {...props} />
        <ShowInstitutionalParameterizedForm {...props} />
    </div>
);
