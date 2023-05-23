import React from 'react';
import {
    Show,
    TextField,
    ReferenceField,
    SimpleShowLayout,
    Labeled, useRecordContext, usePermissions
} from 'react-admin';
import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";

const SERVICE = 8;

const ServiceField = (props) => {
    const record = useRecordContext(props);
    return record.typeId !== SERVICE ? null : (
        <Labeled label="resources.hierarchicalunits.fields.clinicalSpecialtyId">
            <ReferenceField link={false} {...props}>
                <TextField source="name"/>
            </ReferenceField>
        </Labeled>
    )
}

const UserIsInstitutionalAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin= permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return userAdmin;
}

const HierarchicalUnitShow = props => (
    <Show {...props} hasEdit={UserIsInstitutionalAdmin()}>
        <SimpleShowLayout>
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name"/>
            </ReferenceField>
            <TextField source="alias"/>
            <ReferenceField source="typeId" reference="hierarchicalunittypes" link={ false }>
                <TextField source="description" />
            </ReferenceField>
            <ServiceField {...props} reference="clinicalservices" source="clinicalSpecialtyId"/>
        </SimpleShowLayout>
    </Show>
);

export default HierarchicalUnitShow;
