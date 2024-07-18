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
    NumberField
} from 'react-admin';
import CreateRelatedButton from '../../components/CreateRelatedButton';
import SectionTitle from '../../components/SectionTitle';
import { ADMINISTRADOR, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ROOT } from '../../roles';
import ParameterizedFormScopes from '../../masterData/parameterized-form/ParameterizedFormScope';
import { FORM_STATUS_CHOICES, formIsUpdatable } from '../../masterData/parameterized-form/ParameterizedFormStatus';
import UpdateParameterizedFormStatusButton from '../../masterData/parameterized-form/UpdateParameterizedFormStatusButton';
import { Typography } from '@material-ui/core';

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
    return <EditButton basePath="/parameterizedform" record={{ id: record.parameterizedFormId, institutionId: record.institutionId }} />
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
            perPage={10}
            pagination={<Pagination />}
        >
            <ReferenceManyField
                id='parameterizedForm'
                addLabel={false}
                reference="parameterizedform"
                target="parameterizedFormId"
                sort={{ field: 'id', order: 'ASC' }}
                filter={{'institutionId': record.id}}
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

export const ParameterizedFormSection = (props) => (
    <div>
        <SectionTitle label="resources.institutions.fields.parameterizedForm" />
        <Typography variant="body1" gutterBottom>
            Institución
        </Typography>
        <CreateInstitutionalParameterizedForm {...props} />
        <ShowInstitutionalParameterizedForm {...props} />
    </div>
);
