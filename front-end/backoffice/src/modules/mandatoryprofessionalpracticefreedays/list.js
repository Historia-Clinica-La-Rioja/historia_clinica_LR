import {ArrayField, Datagrid, DeleteButton, FunctionField, List, ReferenceField, TextField, SingleFieldList} from 'react-admin';
import renderPerson from "../components/renderperson";
import SubReference from '../components/subreference';
import Chip from '@material-ui/core/Chip';


const MandatoryProfessionalPracticeFreeDaysList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <ReferenceField source="healthcareProfessionalId" reference="healthcareprofessionals" link={false} >
                <SubReference source="personId" reference="person" link={false}>
                    <FunctionField render={renderPerson} />
                </SubReference>
            </ReferenceField>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties" link={false}>
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="mandatoryMedicalPracticeId" reference="mandatorymedicalpractices" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <ArrayField source="days">
                <SingleFieldList>
                    <SimpleChipField />
                </SingleFieldList>
            </ArrayField>
            <DeleteButton />
        </Datagrid>
    </List>
);

const SimpleChipField = ({ record }) => {
    return record ? 
            <Chip
                key={record}
                label={translate(record)}
            />
     : null;
};

const translate = (day) => {
    return ['Domingo','Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'][day];
}

export default MandatoryProfessionalPracticeFreeDaysList;