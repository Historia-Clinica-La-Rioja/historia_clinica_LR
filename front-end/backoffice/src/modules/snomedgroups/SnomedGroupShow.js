import {
    BooleanField,
    Datagrid,
    Pagination,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField, useTranslate,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";
import React from "react";

const SnomedGroupShow = props => {

    const translate = useTranslate();
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <TextField source="id"/>
                <TextField source="description"/>
                <TextField source="ecl"/>
                <TextField source="customId"/>
                <ReferenceField source="groupId" reference="snomedgroups" link={true} linkType={"show"} emptyText={translate('resources.snomedgroups.noInfo')} >
                    <TextField source="description"/>
                </ReferenceField>
                <BooleanField source="template"/>
                <ReferenceField source="institutionId" reference="institutions" link={true} linkType={"show"} emptyText={translate('resources.snomedgroups.noInfo')} >
                    <TextField source="name"/>
                </ReferenceField>
                <ReferenceField source="userId" reference="users" link={false} emptyText={translate('resources.snomedgroups.noInfo')} >
                    <TextField source="username"/>
                </ReferenceField>
                <SgxDateField source="lastUpdate"/>
                <ReferenceManyField
                    reference="snomedrelatedgroups"
                    target="groupId"
                    sort={{field: 'orden', order: 'ASC'}}
                    perPage={10}
                    pagination={<Pagination rowsPerPageOptions={[10, 25, 50]}/>}
                    label="resources.snomedgroups.fields.snomedConcepts"
                >
                    <Datagrid>
                        <ReferenceField source="snomedId" reference="snomedconcepts"
                                        label="resources.snomedconcepts.fields.sctid" sortable={false} link="show">
                            <TextField source="sctid"/>
                        </ReferenceField>
                        <ReferenceField source="snomedId" reference="snomedconcepts"
                                        label="resources.snomedconcepts.fields.pt" sortable={false} link={false}>
                            <TextField source="pt"/>
                        </ReferenceField>
                        <TextField source="orden"/>
                        <SgxDateField source="lastUpdate"/>
                    </Datagrid>
                </ReferenceManyField>
            </SimpleShowLayout>
        </Show>
    );
}

export default SnomedGroupShow;
