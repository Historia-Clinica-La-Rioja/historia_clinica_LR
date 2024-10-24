import React from 'react';
import { Link } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import {
    Datagrid,
    DeleteButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField,
    usePermissions,
    useTranslate
} from 'react-admin';
import SectionTitle from '../../components/SectionTitle';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

const CreateRelatedButton = ({record, reference, label, disabled}) => {
    const newRecord = {careLineInstitutionId: record.id, institutionId: record.institutionId, careLineId: record.careLineId};
    const translate = useTranslate();
    return (
        <Button
            component={Link}
            to={{
                pathname: `/${reference}/create`,
                state: { record: newRecord },
            }}
            disabled={disabled}
        >{translate(label)}</Button>
    );
};

const CareLineinstitutionShow = props => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.hasAnyAssignment(...BASIC_BO_ROLES);
    return(
        <Show {...props}>
            <SimpleShowLayout>

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>

                <ReferenceField source="careLineId" reference="carelines">
                    <TextField source="description" />
                </ReferenceField>

                <SectionTitle label="resources.carelineinstitution.fields.specialtys"/>
                <CreateRelatedButton
                    reference="carelineinstitutionspecialty"
                    label="resources.carelineinstitution.fields.newspecialty"
                    disabled={userIsRootOrAdmin}
                />

                <ReferenceManyField
                    addLabel={false}
                    reference="carelineinstitutionspecialty"
                    target="careLineInstitutionId"
                >
                    <Datagrid>
                        <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                            <TextField source="name"/>
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={userIsRootOrAdmin} />
                    </Datagrid>
                </ReferenceManyField>

                <SectionTitle label="resources.carelineinstitution.fields.practices"/>
                <CreateRelatedButton
                    reference="carelineinstitutionpractice"
                    label="resources.carelineinstitution.fields.newpractice"
                    disabled={userIsRootOrAdmin}
                />

                <ReferenceManyField
                    addLabel={false}
                    reference="carelineinstitutionpractice"
                    target="careLineInstitutionId"
                >
                    <Datagrid>
                        <ReferenceField source="snomedRelatedGroupId" reference="practicesinstitution" link={false}>
                            <TextField source="description" />
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={userIsRootOrAdmin} />
                    </Datagrid>
                </ReferenceManyField>

            </SimpleShowLayout>
        </Show>
    );
};

export default CareLineinstitutionShow;
