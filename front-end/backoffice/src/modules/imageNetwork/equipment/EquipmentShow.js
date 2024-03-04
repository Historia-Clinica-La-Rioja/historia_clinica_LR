import React from 'react';
import {
    Show,
    TextField,
    TopToolbar,
    EditButton,
    Button,
    ReferenceField,
    SimpleForm,
    Labeled,
    BooleanField,
    useRedirect
} from 'react-admin';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import { makeStyles } from '@material-ui/core/styles';
import SectionTitle from '../../components/SectionTitle';

const EquipmentActions = ({ data }) => {
    const useStyles = makeStyles({
        button: {
            marginRight: '1%',
        },
        deleteButton: {
            marginLeft: 'auto',
        }
    });
    const classes = useStyles();
    const redirect = useRedirect();
    const goBack = () => {
        redirect('/equipment');
    }
    return (!data || !data.id) ? <TopToolbar/> :
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
                <EditButton
                    basePath="/equipment"
                    record={{id:data.id }}
                    variant="outlined"
                    color="primary"
                    size="medium"
                    className={classes.button}
                />
            </TopToolbar>
        )
};

const RenderModality = (data ) => {
    const modality = data.record;
    return  (<>{modality.acronym} ({modality.description})</>);
};

const EquipmentShow = (props) => (
    <Show actions={<EquipmentActions />} {...props}>
        <SimpleForm>
            <SectionTitle label="resources.equipment.detailLabel"/>
            <TextField source="name" />
            <TextField source="aeTitle" />
            <ReferenceField link={false}  source="sectorId" reference="sectors">
                <TextField source="description"/>
            </ReferenceField>
            <ReferenceField link={false} source="pacServerId"  reference="pacserversimagelvl">
                <TextField  source="name" />
            </ReferenceField>
            <ReferenceField link={false}source="orchestratorId" reference="orchestrator">
                <TextField  source="name" />
            </ReferenceField>
            <ReferenceField link={false}source="modalityId" reference="modality">
                <RenderModality/>
            </ReferenceField>
            <Labeled label="resources.equipment.fields.createId">
                <BooleanField source="createId" />
            </Labeled>
        </SimpleForm>
    </Show>
);


export default EquipmentShow;
