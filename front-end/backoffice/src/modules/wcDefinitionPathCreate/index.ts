import WcDefinitionPathCreate from "./WcDefinitionPathCreate";
import WcDefinitionPathList from "./WcDefinitionPathList";

const wcDefinitionPath = {/* 
    show: RoomShow,*/
    list: WcDefinitionPathList, 
    create: WcDefinitionPathCreate,
    options: {
        submenu: 'more'
    }
};

export default wcDefinitionPath;
