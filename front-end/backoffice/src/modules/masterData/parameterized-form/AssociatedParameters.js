import React, { Fragment } from 'react';
import {
    TextField,
    useRecordContext,
    Datagrid,
    SelectField,
    ReferenceArrayField,
    SingleFieldList,
    ChipField,
    ReferenceField,
    ReferenceManyField,
    Pagination,
    DeleteButton,
    usePermissions
} from 'react-admin';
import SectionTitle from '../../components/SectionTitle';
import CreateRelatedButton from '../../components/CreateRelatedButton';
import { formIsUpdatable } from './ParameterizedFormStatus';
import { TYPE_CHOICES_IDS } from '../parameters/ParameterTypes';
import UpdateParameterizedFormParameterOrder from './UpdateParameterizedFormParameterOrder';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';

const UserIsInstitutionalAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin = permissions?.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE);
    return userAdmin;
}

const AddParameter = ({ canEdit, ...props }) => {
    const record = useRecordContext(props);
    if (!record) return null;
    return record ? (
        <CreateRelatedButton
            customRecord={{ parameterizedFormId: record.id }}
            record={{ parameterizedFormId: record.id }}
            reference="parameterizedformparameter"
            refFieldName="otroVal"
            label="resources.parameterizedformparameter.button"
        />
    ) : null;

};

const ParameterLoincCode = props => (
    <ReferenceField
        source="loincId"
        reference="loinc-codes"
        link={false}
        sortable={false}
    >
        <TextField source="code" />
    </ReferenceField>
)

const AssociatedParameters = (props) => {
    const record = useRecordContext(props);
    const userIsInstitutionalAdmin = UserIsInstitutionalAdmin();
    const canEdit = formIsUpdatable(record.statusId) && (!userIsInstitutionalAdmin || (userIsInstitutionalAdmin && !record.isDomain));
    return record ? (
        <Fragment>
            <SectionTitle label="resources.parameterizedformparameter.associatedParameters" />
            {canEdit && <AddParameter {...props} />}

            <ReferenceManyField target="parameterizedFormId" link={false} sortable={false}
                reference="parameterizedformparameter"
                label="resources.formparameter.associatedParameters"
                pagination={<Pagination />}
            >
                <Datagrid
                    rowClick={() => { }}
                >
                    <ReferenceField source="parameterId" reference="parameters" label="resources.parameters.fields.description" sortable={false} link={UserIsInstitutionalAdmin() ? '' : 'show'}>
                        <TextField source="description" />
                    </ReferenceField>

                    <ReferenceField source="parameterId" reference="parameters" label="resources.parameters.fields.loincId" sortable={false} link={false}>
                        <ParameterLoincCode/>
                    </ReferenceField>

                    <ReferenceField source="parameterId" reference="parameters" label="resources.parameters.fields.type" sortable={false} link={false}>
                        <SelectField source='typeId' choices={TYPE_CHOICES_IDS} />
                    </ReferenceField>

                    <ReferenceArrayField
                        label="resources.parameters.fields.units"
                        reference="units-of-measure"
                        source="unitsOfMeasureIds"
                        sortable={false}
                    >
                        <SingleFieldList>
                            <ChipField source="code" />
                        </SingleFieldList>
                    </ReferenceArrayField>

                    <TextField source="orderNumber" label="resources.parameterizedformparameter.order" sortable={false} link={false} />

                    {canEdit && <UpdateParameterizedFormParameterOrder />}
                    {canEdit && <DeleteButton redirect={false} />}
                </Datagrid>
            </ReferenceManyField>
        </Fragment>
    ) : null;
}

export default AssociatedParameters;
