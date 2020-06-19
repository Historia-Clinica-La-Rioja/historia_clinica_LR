# Mini guía de desarrollo

## Ansible Hello World

Ansible usa python y ssh para ejecutar tareas en otros servidores y configurarlos.
Despues de configurar el inventario se pueden ejecutar pings o comandos básicos desde la consola en los servidores con Ansible.
El modulo `ping` verifica que el server sea conectable con Ansible.
El modulo `shell` puede ejecutar comandos de consola directamente.

```shell
#Esto ejecuta comandos de a uno en todos (all) los hosts
ansible -i ./inventario.yml -m ping all
ansible -i ./inventario.yml -m shell -a ls all
```

Explicación:
* `-i ./inventario.yml` indica de donde leer el inventario.
* `-m ping` ejecuta el modulo ping.
* `-m raw -a ls` ejecuta directamente sobre el shell plano, y pasa como parametro de modulo `ls`, en este caso es lo que se ejecuta en el server remoto. 
* `-m shell -a ls` ejecuta el modulo shell y pasa como parametro de modulo `ls`. Es similar a "raw" pero utilizó Python en el host para ejecutar la consola.
* `all` indica que se ejecute sobre todos los hosts, puede ser tambien sobre uno solo o sobre un grupo o un conjunto.

Login con user y clave
* `--extra-vars "ansible_user=jdottori ansible_password=$PASS"` agregado al final para conectarse con usuario y clave a un server en lugar de par de claves ssh


## Ansible-playbook Hello World

Un playbook es una serie de tareas que se ejecutan secuencialmente, ejemplo de `playbook.yml`

```yml
---
- hosts: all
  tasks:
    - name: Ensure touched-file exists # nombre descriptivo de estado final deseado
      file:  # module
        state: touch # argumentos del modulo
        dest: "/home/guadcore/touched-file" # argumentos del modulo
#- ... otra tarea aca 
```
Este playbook se ejecutará con:
`ansible-playbook -i inventory.yml playbook.yml`

Chequear que en el hosts destino se creo el archivo indicado

Cada tarea puede:
* usar variables para hacer los paths y valores más genéricos
* delimitarse solo a algunos nodos, o algunos grupos

Con parámetros extras desde la CLI se puede:
* limitar los hosts sobre los que se aplica al correr desde la linea de comandos
* cambiar el valor de variables

## Modulos

Unos pocos módulos permiten hacer casi todo. 
Ejemplo, con un modulo de "lineinfile" podes editar un archivo con reges, pero existe módulos de más alto nivel para editar ini's y diferentes tipos de archivos con una potencia aumentada especializada en el tipo de archivo.
Existen miles de modulos en Ansible. 
En versiones nuevas de Ansible se introduce el concepto de Collections, como un paquete de modulos para que puedan importarse selectivamente.

Los módulos más usados son:
* ping 
* raw / shell 
* file: crea / remueve / configura archivos
* lineinfile:
* debug / msg: 
* package / apt / yum: Instalar/remover paquetes del sistema
* group / user: manejo de usuarios y grupos
* get_url: pedidos http a la web
* unarchive: descomprimir
* template: crea un archivo desde un template
* service: control de servicios

### Ejercicio:

Agregar un par de tareas al mismo playbook.
Al menos dos al mismo host/grupo.

## Roles

Permiten modularizar tareas en base a que *rol* toma un servidor en el que se ejecutan esas tareas.
Tienen una estructura de directorios especifica, y adentro son mayormente secuencias de tareas Ansible con su respectivo modulo.

Ejemplo: Transformar a un rol la tarea anterior
1. crear carpeta roles/touched-file/tasks
2. En tasks/main.yml copiar solo la tarea (solo desde `- name: Ensure touched-file exists`), el archivo por buena practica comienza con `---`
3. Modificar el playbook para que en lugar de la tarea aplique el rol

```yml
---
- hosts: all
  roles:
    - touched-files # nombre de la carpeta del rol
```

Esto basicamente hace lo mismo pero modulariza la tarea a un rol. Es normal que la secuencia de tareas sea larga 

En los roles se utilizan multiples carpetas para organizar los archivos que son estandar, las más basicas son:
1. defaults
2. handlers
3. tasks
4. templates
5. vars

### Distribuciones y variables

Es normal en los roles utilizar variantes del rol y de variables para que dicho rol funcione de forma equivalente en diferentes distribuciones de Linux.

### Elegir roles (desde Galaxy)

from: https://www.linuxtechi.com/use-ansible-galaxy-roles-ansible-playbook/

Buscar el rol deseado, por ej para buscar un rol de tomcat
* consola con ansible-galaxy search <<tomcat>>
* buscar en la [webpage]()

Varificar:
* compatibilidad con versión de Ansible
* licencia
* distribución sobre la que funciona
* cumple mis necesidades

Para tener esta info:
* `ansible-galaxy info bennojoy.nt`

## Estructura del proyecto Ansible

Los proyectos deben estructurarse según https://docs.ansible.com/ansible/latest/user_guide/playbooks_best_practices.html. 
A grandes rasgos:
* roles por cada tipo de maquina, en lo posible aplicar SOLID a los roles
* un playbook para cada tipo de equipo que le aplica los roles correspondientes
* un playbook general que llama a los demas playbooks

## Testing con docker

Indicando en el inventario `ansible_connection = docker` se puede conectar a containers docker. 
Esto es útil para desarrollo y testing de roles.
Cuidado que un container docker no es exactamente igual que una VM, por lo que puede haber diferencias. 
También se podria usar VirtualBox para usar VM reales. 

## Testing con Molecule

