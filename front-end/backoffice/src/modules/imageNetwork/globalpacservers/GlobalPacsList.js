import React from "react";
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    TopToolbar,
    CreateButton,
} from 'react-admin';
import {makeStyles} from "@material-ui/core/styles";

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
    return(
        <TopToolbar>
            <CreateButton
                variant="outlined"
                color="primary"
                size="medium"
                className={classes.button}
            />
        </TopToolbar>
    );
};

const GlobalPacsList = props => {
    return (
        <List {...props} actions={<ListActions/>} bulkActionButtons={false}>
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