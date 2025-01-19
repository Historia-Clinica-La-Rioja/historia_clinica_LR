export const STATUS_CHOICES = [
    { id: 1, name: 'resources.proceduretemplates.statusId.draft'},
    { id: 2, name: 'resources.proceduretemplates.statusId.active'},
    { id: 3, name: 'resources.proceduretemplates.statusId.inactive'}
]

export const procedureTemplateIsUpdateable = (statusId) => {
    return statusId && statusId === 1;
}

export const STATUS_ACTIONS = {
    1: {nextStateActionName: 'resources.proceduretemplates.statusId.activate'},
    2: {nextStateActionName: 'resources.proceduretemplates.statusId.deactivate'},
    3: {nextStateActionName: 'resources.proceduretemplates.statusId.activate'}
}