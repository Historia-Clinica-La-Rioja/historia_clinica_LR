
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DeleteButton,
    EditButton,
    Filter,
    TextInput
} from 'react-admin';

const InstitutionalGroupFilter = (props) => (
    <Filter {...props}>
        <TextInput source="name" />
    </Filter>
);

const InstitutionalGroupList = props => (
    <List {...props} filters={<InstitutionalGroupFilter />} >
        <Datagrid rowClick="show">
            <TextField source="name"/>
            <ReferenceField source="typeId" reference="institutionalgrouptypes" link={false}>
                <TextField source="value" />
            </ReferenceField>
            <TextField source="institutions" link={false} sortable={false} />
            <EditButton />
            <DeleteButton redirect={false} />
        </Datagrid>
    </List>
);
export default InstitutionalGroupList;