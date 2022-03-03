import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";

const SnomedGroupList = props => (
    <List {...props} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="description" />
            <TextField source="customId" />
            <SgxDateField source="lastUpdate" />
        </Datagrid>
    </List>
);

export default SnomedGroupList;

