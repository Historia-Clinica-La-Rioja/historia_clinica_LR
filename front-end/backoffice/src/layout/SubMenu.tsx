import * as React from 'react';
import { Fragment, ReactElement } from 'react';
import { useSelector } from 'react-redux';
import {
    List,
    MenuItem,
    ListItemIcon,
    Typography,
    Collapse,
    Tooltip,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import ExpandMore from '@material-ui/icons/ExpandMore';
import ListIcon from '@material-ui/icons/List';
import {
    useTranslate, ReduxState, ResourceDefinition, MenuItemLink,
    MenuItemLinkProps,
} from 'react-admin';

const useStyles = makeStyles(theme => ({
    icon: { minWidth: theme.spacing(5) },
    sidebarIsOpen: {
        '& a': {
            transition: 'padding-left 195ms cubic-bezier(0.4, 0, 0.6, 1) 0ms',
            paddingLeft: theme.spacing(4),
        },
    },
    sidebarIsClosed: {
        '& a': {
            transition: 'padding-left 195ms cubic-bezier(0.4, 0, 0.6, 1) 0ms',
            paddingLeft: theme.spacing(2),
        },
    },
}));

const menuItemProps = ({} as MenuItemLinkProps);

interface Props {
    dense: boolean;
    handleToggle: () => void;
    icon: ReactElement;
    isOpen: boolean;
    name: string;
    resources: ResourceDefinition[];
}

const SubMenu = (props: Props) => {
    const { handleToggle, isOpen, name, icon, resources, dense } = props;
    const translate = useTranslate();
    const classes = useStyles();
    const sidebarIsOpen = useSelector<ReduxState, boolean>(
        state => state.admin.ui.sidebarOpen
    );

    if (!resources?.length) {
        return <Fragment></Fragment>;
    }
    const header = (
        <MenuItem id={name} dense={dense} button onClick={handleToggle} disabled={resources.length === 0}>
            <ListItemIcon className={classes.icon}>
                {isOpen ? <ExpandMore /> : icon}
            </ListItemIcon>
            <Typography variant="inherit" color="textSecondary">
                {translate(name)}
            </Typography>
        </MenuItem>
    );

    return (
        <Fragment>
            {sidebarIsOpen || isOpen ? (
                header
            ) : (
                <Tooltip title={translate(name)} placement="right">
                    {header}
                </Tooltip>
            )}
            <Collapse in={isOpen} timeout="auto" unmountOnExit>
                <List
                    dense={dense}
                    component="div"
                    disablePadding
                    className={
                        sidebarIsOpen
                            ? classes.sidebarIsOpen
                            : classes.sidebarIsClosed
                    }
                >
                    {resources.filter(resource => resource.hasList).map(resource => (
                        <MenuItemLink id={name}
                            {...menuItemProps}
                            to={{
                                pathname: `/${resource.name}`,
                                state: { _scrollToTop: true },
                            }}
                            primaryText={translate(`resources.${resource.name}.name`, {
                                smart_count: 2,
                            })}
                            leftIcon={resource.icon? <resource.icon /> : <ListIcon />}
                            dense={dense}
                            key={resource.name}
                        />
                    ))}
                </List>
            </Collapse>
        </Fragment>
    );
};

export default SubMenu;
