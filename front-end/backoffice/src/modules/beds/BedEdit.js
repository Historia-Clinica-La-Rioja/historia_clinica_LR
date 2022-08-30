import React from 'react';
import {
    BooleanInput,
    Datagrid,
    Edit,
    Pagination,
    ReferenceManyField,
    required,
    SimpleForm,
    TextInput
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";
import CustomToolbar from "../components/CustomToolbar";
import SgxDateField from "../../dateComponents/sgxDateField";

const BedEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="bedNumber" validate={[required()]} />

            <SgxSelectInput source="roomId" element="rooms" optionText="description" alwaysOn allowEmpty={false}/>

            <BooleanInput source="enabled" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="available" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="free" validate={[required()]} disabled={false} initialValue={true}/>

            <ReferenceManyField
                addLabel={true}
                label="resources.beds.fields.internmentepisodes"
                reference="internmentepisodes"
                target="bedId"
                sort={{ field: 'entryDate', order: 'DESC' }}
                filter={{ statusId: 1 }}
                pagination={<Pagination />}
            >
                <Datagrid>
                    <SgxDateField source="entryDate" />
                </Datagrid>
            </ReferenceManyField>

        </SimpleForm>
    </Edit>
);

export default BedEdit;
