import {Datagrid, DeleteButton, List, TextField} from 'react-admin';

const MandatoryMedicalPracticeList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="description" />
            <TextField source="mmpCode" />
            <TextField source="snomedId" />
            <DeleteButton />
        </Datagrid>
    </List>
);

export default MandatoryMedicalPracticeList;