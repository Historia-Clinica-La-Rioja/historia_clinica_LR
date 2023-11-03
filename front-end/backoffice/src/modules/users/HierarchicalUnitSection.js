import {
    BooleanField,
    Datagrid,
    EditButton,
    ReferenceField,
    ReferenceManyField,
    TextField
} from "react-admin";
import React, {Fragment} from "react";
import CreateRelatedButton from "../components/CreateRelatedButton";
import SectionTitle from "../components/SectionTitle";

const HierarchicalUnitSection = (props) => {
    return (
        <Fragment>
            <SectionTitle label="resources.users.fields.hierarchicalUnits"/>
            <AssociateHierarchicalUnit {...props}/>
            <ReferenceManyField
                addLabel={false}
                reference="hierarchicalunitstaff"
                target="userId"
            >
                <Datagrid>
                    <ReferenceField source="hierarchicalUnitId" link={false} reference="hierarchicalunits">
                        <TextField source="alias"/>
                    </ReferenceField>
                    <ReferenceField source="hierarchicalUnitId" link={false} reference="hierarchicalunits" label="resources.hierarchicalunits.fields.institutionId">
                        <ReferenceField source="institutionId" link={false} reference="institutions">
                            <TextField source="name"/>
                        </ReferenceField>
                    </ReferenceField>
                    <BooleanField source="responsible"/>
                    <EditButton></EditButton>
                </Datagrid>
            </ReferenceManyField>
        </Fragment>
    )
};

const AssociateHierarchicalUnit = ({ record }) => {
    const customRecord = {personId: record.personId, userId: record.id};
    return ( <CreateRelatedButton
            customRecord={customRecord}
            reference="hierarchicalunitstaff"
            refFieldName="userId"
            label="resources.users.buttons.addHierarchicalUnit"
        />
    );
};

export default HierarchicalUnitSection;
