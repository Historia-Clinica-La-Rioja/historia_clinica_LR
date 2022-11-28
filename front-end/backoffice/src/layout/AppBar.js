import * as React from 'react';
import { forwardRef } from 'react';
import { AppBar, UserMenu, MenuItemLink } from 'react-admin';
import HomeIcon from '@material-ui/icons/Home';
import { goToInstitutionsHome } from '../providers/utils/webappLink';

const ConfigurationMenu = forwardRef((props, ref) => (
    <MenuItemLink
        id="access_institutions"
        ref={ref}
        to="/home"
        primaryText="Acceso a Instituciones"
        leftIcon={<HomeIcon />}
        onClick={goToInstitutionsHome} // close the menu on click
    />
));

const CustomUserMenu = (props) => (
    <UserMenu {...props}>
        <ConfigurationMenu />
    </UserMenu>
);

const CustomAppBar = (props) => {
    return (
        <AppBar {...props} userMenu={<CustomUserMenu />}></AppBar>
    );
};

export default CustomAppBar;
