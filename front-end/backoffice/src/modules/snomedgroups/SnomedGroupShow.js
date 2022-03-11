import {
    BooleanField,
    Datagrid,
    DeleteButton,
    Pagination,
    ReferenceField,
    ReferenceManyField,
    SimpleShowLayout,
    TextField,
    useTranslate,
    ShowController,
    ShowView,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";
import React from "react";
import { Link } from 'react-router-dom';
import Button from "@material-ui/core/Button";
import SectionTitle from "../components/SectionTitle";

const CreateRelatedButton = ({ record, reference, label}) => {
    const newRecord = {groupId: record.id, parentGroupId: record.groupId};
    const parentGroupId = record.groupId;
    const translate = useTranslate();
    return (
        <Button
            component={Link}
            to={{
                pathname: `/${reference}/create`,
                state: { record: newRecord },
            }}
            disabled={!parentGroupId}
        >{translate(label)}</Button>
    );
};

const SnomedGroupShow = props => {
    const translate = useTranslate();
    return (
        <ShowController {...props}>
            {controllerProps =>
                <ShowView {...props} {...controllerProps}>
                    <SimpleShowLayout>

                        {/* ID */}
                        <TextField source="id"/>

                        {/* Description */}
                        <TextField source="description"/>

                        {/* ECL */}
                        <TextField source="ecl"/>

                        {/* Custom ID */}
                        <TextField source="customId"/>

                        {/* Group parent */}
                        <ReferenceField source="groupId" reference="snomedgroups" link="show" emptyText={translate('resources.snomedgroups.noInfo')} >
                            <TextField source="description"/>
                        </ReferenceField>

                        {/* Is template */}
                        <BooleanField source="template"/>

                        {/* Institution */}
                        <ReferenceField source="institutionId" reference="institutions" link="show" emptyText={translate('resources.snomedgroups.noInfo')} >
                            <TextField source="name"/>
                        </ReferenceField>

                        {/* User */}
                        <ReferenceField source="userId" reference="users" link={false} emptyText={translate('resources.snomedgroups.noInfo')} >
                            <TextField source="username"/>
                        </ReferenceField>

                        {/* Last update */}
                        <SgxDateField source="lastUpdate"/>

                        {/* Snomed concepts */}
                        <SectionTitle label="resources.snomedgroups.fields.snomedConcepts"/>

                        <CreateRelatedButton
                            reference="snomedrelatedgroups"
                            label="resources.snomedgroups.createRelated"
                        />

                        <ReferenceManyField
                            reference="snomedgroupconcepts"
                            target="groupId"
                            sort={{field: 'conceptPt', order: 'ASC'}}
                            perPage={10}
                            pagination={<Pagination rowsPerPageOptions={[10, 25, 50]}/>}
                            addLabel={false}
                        >
                            <Datagrid>
                                <TextField source="conceptSctid"/>
                                <TextField source="conceptPt"/>
                                <TextField source="orden"/>
                                <SgxDateField source="lastUpdate"/>
                                <DeleteButton redirect={false} disabled={!controllerProps?.record?.groupId} />
                            </Datagrid>
                        </ReferenceManyField>

                    </SimpleShowLayout>
                </ShowView>
            }
        </ShowController>
    );
}

export default SnomedGroupShow;
