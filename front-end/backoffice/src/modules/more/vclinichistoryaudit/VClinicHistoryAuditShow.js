import {
    Show,
    TextField,
    SimpleShowLayout,
    SelectField,
    useRedirect,
    TopToolbar,
    Button,
} from 'react-admin';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import {makeStyles} from '@material-ui/core/styles';
import {
    SgxDateField,
} from '../../components';

const ClinicHistoryAuditActions = ({ data }) => {
    const useStyles = makeStyles({
        button: {
            marginRight: '',
        },
    });
    const classes = useStyles();
    const redirect = useRedirect();
    const goBack = () => {
        redirect('/vclinichistoryaudit');
    }
    return (!data) ? <TopToolbar/> :
        (
            <TopToolbar>
                <Button
                    variant="outlined"
                    color="primary"
                    size="medium"
                    className={classes.button}
                    label="sgh.components.customtoolbar.backButton"
                    onClick={goBack}>
                    <ArrowBackIcon />
                </Button>
            </TopToolbar>
        )
};


const ClinicHistoryAuditShow = (props) => (
        <Show actions={<ClinicHistoryAuditActions />} {...props} >
            <SimpleShowLayout>
                <TextField source="firstName" />
                <TextField source="lastName" />
                <TextField source="description" />
                <TextField source="identificationNumber" />
                <TextField source="username" />
                <SgxDateField source="date"/>
                <SelectField source="reasonId" choices={[
                        { id: 1, name: 'resources.vclinichistoryaudit.medicalEmergency' },
                        { id: 2, name: 'resources.vclinichistoryaudit.professinalConsultation' },
                        { id: 3, name: 'resources.vclinichistoryaudit.patientConsultation' },
                        { id: 4, name: 'resources.vclinichistoryaudit.audit' },
                    ]} />
                <TextField source="institutionName" />
                <TextField source="observations" />
            </SimpleShowLayout>
        </Show>
);


export default ClinicHistoryAuditShow;