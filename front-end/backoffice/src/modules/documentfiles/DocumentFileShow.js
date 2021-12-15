import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    ReferenceField,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";
import CreateRelatedButton from "../components/CreateRelatedButton";

const DocumentFileShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="filename" />
            <ReferenceField source="typeId" reference="documenttypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <SgxDateField source="createdOn" showTime/>
        </SimpleShowLayout>
    </Show>
);

export default DocumentFileShow;
