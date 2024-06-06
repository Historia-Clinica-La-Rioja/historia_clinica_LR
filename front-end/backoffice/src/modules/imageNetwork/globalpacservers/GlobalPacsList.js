import React from "react";
import {
    List,
    Datagrid,
    TextField,
    usePermissions,
    ReferenceField,
    TopToolbar,
    CreateButton,
} from 'react-admin';
import {makeStyles} from "@material-ui/core/styles";
import { ROOT } from '../../roles';

const ListActions = () => {
    const useStyles = makeStyles({
        button: {
            marginRight: '1%',
        },
        deleteButton: {
            marginLeft: 'auto',
        }
    });
    const classes = useStyles();
    const { permissions } = usePermissions();
    const userIsRoot = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ROOT.role)).length > 0;
    return(
        <TopToolbar>
            <CreateButton
                disabled={!userIsRoot}
                variant="outlined"
                color="primary"
                size="medium"
                className={classes.button}
            />
        </TopToolbar>
    );
};

const GlobalPacsList = props => {
    const { permissions } = usePermissions();
    const userIsRoot = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ROOT.role)).length > 0;
    return (
        <List {...props} actions={<ListActions/>} bulkActionButtons={false} hasCreate={userIsRoot}>
            <Datagrid rowClick="show">-
                <TextField source="name" />
                <TextField source="aetitle" />
                <TextField source="domain" />
                <ReferenceField source="pacServerType" reference="pacservertypes" link={false} >
                    <TextField source="description" />
                </ReferenceField>
                <ReferenceField source="pacServerProtocol" reference="pacserverprotocols" link={false} >
                    <TextField source="description" />
                </ReferenceField>
            </Datagrid>
        </List>
    );
};

export default GlobalPacsList;