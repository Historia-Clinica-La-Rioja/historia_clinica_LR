import SnomedGroupList from './SnomedGroupList';
import SnomedGroupShow from "./SnomedGroupShow";
import SnomedGroupCreate from "./SnomedGroupCreate";
import SnomedGroupEdit from "./SnomedGroupEdit";

const snomedgroups = {
    list: SnomedGroupList,
    show: SnomedGroupShow,
    create: SnomedGroupCreate,
    edit: SnomedGroupEdit,
    options: {
        submenu: 'masterData'
    }
};

export default snomedgroups;
