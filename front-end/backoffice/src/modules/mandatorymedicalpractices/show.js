import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';


const MandatoryMedicalPracticeShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <TextField source="mmpCode" />
            <TextField source="snomedId" />
        </SimpleShowLayout>
    </Show>
);

export default MandatoryMedicalPracticeShow;