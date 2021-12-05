import CareLineList from './CareLineList';
import CareLineCreate from "./CareLineCreate";
import CareLineShow from "./CareLineShow";
import CareLineEdit from "./CareLineEdit";

const careLines = {
    show: CareLineShow,
    list: CareLineList,
    create: CareLineCreate,
    edit: CareLineEdit
};

export default careLines;
