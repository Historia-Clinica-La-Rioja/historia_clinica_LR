import React, {Fragment} from 'react';
import {
    Datagrid,
    DeleteButton,
    ReferenceField,
    ReferenceManyField,
    TextField,
} from 'react-admin';
import SectionTitle from '../../components/SectionTitle';

const InstitutionalGroupSection = (props) => (
    <Fragment>
        <SectionTitle label="resources.users.fields.institutionalGroups"/>
        <ReferenceManyField
            addLabel={false}
            reference="institutionalgroupusers"
            target="userId"
        >
            <Datagrid rowClick="show">
                <ReferenceField source="institutionalGroupId"  link={false}  reference="institutionalgroups" label="resources.institutionalgroups.fields.name">
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="institutionalGroupId"  link={false}  reference="institutionalgroups" label="resources.institutionalgroups.fields.typeId">
                    <ReferenceField source="typeId" reference="institutionalgrouptypes" link={false}>
                        <TextField source="value" />
                    </ReferenceField>
                </ReferenceField>
                <ReferenceField source="institutionalGroupId"  link={false}  reference="institutionalgroups" label="resources.institutionalgroups.fields.institutions">
                    <TextField source="institutions" label='Instituciones'/>
                </ReferenceField>
                <DeleteButton redirect={false} />
            </Datagrid>
        </ReferenceManyField>
    </Fragment>
);

export default InstitutionalGroupSection;