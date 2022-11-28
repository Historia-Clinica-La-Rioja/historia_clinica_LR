import * as React from 'react';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { makeStyles } from '@material-ui/core/styles';
import classnames from 'classnames';
import {
    DashboardMenuItem,
    MenuProps,
    ReduxState,
    getResources,
    ResourceDefinition,
} from 'react-admin';

import SubMenu from './SubMenu';
import BugReportIcon from '@material-ui/icons/BugReport';
import GroupIcon from '@material-ui/icons/Group';
import MenuOpenIcon from '@material-ui/icons/MenuOpen';
import TuneIcon from '@material-ui/icons/Tune';
import LocalHospitalIcon from '@material-ui/icons/LocalHospital';
import AssignmentIcon from '@material-ui/icons/Assignment';

type MenuName = 'staff' | 'facilities' | 'debug' | 'masterData' | 'booking' | 'more';

const submenu = (submenu: string) => (resource: ResourceDefinition): boolean => 
    (!!resource.hasList && (resource.options?.submenu || '') === submenu);

const Menu = ({ dense = false }: MenuProps) => {
    const [state, setState] = useState({
        staff: false,
        facilities: false,
        debug: false,
        masterData: false,
        booking: false,
        more: false,
    });

    const open = useSelector((state: ReduxState) => state.admin.ui.sidebarOpen);
    const classes = useStyles();

    const handleToggle = (menu: MenuName) => {
        setState(state => ({ ...state, [menu]: !state[menu] }));
    };

    let resources = useSelector(getResources) || [];

    return (
        <div className={classnames(classes.root, {
                [classes.open]: open,
                [classes.closed]: !open,
            })}
        >
            {' '}
            <DashboardMenuItem />
            <SubMenu
                handleToggle={() => handleToggle('staff')}
                isOpen={state.staff}
                name="app.menu.staff"
                icon={<GroupIcon />}
                dense={dense}
                resources={resources.filter(submenu('staff'))}
            />
            <SubMenu
                handleToggle={() => handleToggle('facilities')}
                isOpen={state.facilities}
                name="app.menu.facilities"
                icon={<LocalHospitalIcon />}
                dense={dense}
                resources={resources.filter(submenu('facilities'))}
            />
            <SubMenu
                handleToggle={() => handleToggle('debug')}
                isOpen={state.debug}
                name="app.menu.debug"
                icon={<BugReportIcon />}
                dense={dense}
                resources={resources.filter(submenu('debug'))}
            />
            <SubMenu
                handleToggle={() => handleToggle('booking')}
                isOpen={state.booking}
                name="app.menu.booking"
                icon={<AssignmentIcon />}
                dense={dense}
                resources={resources.filter(submenu('booking'))}
            />
            <SubMenu
                handleToggle={() => handleToggle('masterData')}
                isOpen={state.masterData}
                name="app.menu.masterData"
                icon={<TuneIcon  />}
                dense={dense}
                resources={resources.filter(submenu('masterData'))}
            />
            <SubMenu
                handleToggle={() => handleToggle('more')}
                isOpen={state.more}
                name="app.menu.more"
                icon={<MenuOpenIcon />}
                dense={dense}
                resources={resources.filter(submenu('more'))}
            />
        </div>
    );
};

const useStyles = makeStyles(theme => ({
    root: {
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1),
        transition: theme.transitions.create('width', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    open: {
        width: 250,
    },
    closed: {
        width: 55,
    },
}));

export default Menu;
