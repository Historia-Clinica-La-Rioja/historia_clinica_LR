# Configuración de tareas programadas

En el backend existen tareas programadas (scheduled jobs), las cuales son implementadas con crons de Spring, que permite que sean configurables y se ejecuten automáticamente a determinado horario y/o con determinada frecuencia. Por defecto se encuentra habilitada la ejecución de tareas programadas, pero se pueden deshabilitar seteando la propiedad `scheduledjobs.enabled=false`.

Cada tarea programada existente se debe poder habilitar/deshabilitar por propiedad.
> Ejemplo: `scheduledjobs.some-job.enabled=true`

Para configurar el horario y/o frecuencia de ejecución, cada tarea debe tener 6 parámetros configurados también por propiedades: hora, minuto, segundo, día del mes, mes y día de la semana. Cada parámetro debe seguir sus pautas según cómo se definen las [expresiones cron](https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm).

> Ejemplo 1: configurar ejecución para las 3 AM todos los días.
```
scheduledjobs.some-job.seconds=0
scheduledjobs.some-job.minutes=0
scheduledjobs.some-job.hours=3
scheduledjobs.some-job.dayofmonth=*
scheduledjobs.some-job.month=*
scheduledjobs.some-job.dayofweek=*
```

> Ejemplo 2: configurar ejecución cada 3 hs, a los 0 minutos y 0 segundos, sólo días martes y jueves.
```
scheduledjobs.some-job.seconds=0
scheduledjobs.some-job.minutes=0
scheduledjobs.some-job.hours=*/3
scheduledjobs.some-job.dayofmonth=*
scheduledjobs.some-job.month=*
scheduledjobs.some-job.dayofweek=TUE,THU
```