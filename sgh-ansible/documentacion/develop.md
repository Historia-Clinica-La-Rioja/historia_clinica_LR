# Mini guía de desarrollo

## Ansible Hello World

Ansible usa python y ssh para ejecutar tareas en otros servidores y configurarlos.
Despues de configurar el inventario se pueden ejecutar pings o comandos básicos desde la consola en los servidores con Ansible.
El modulo `ping` verifica que el server sea conectable con Ansible.
El modulo `shell` puede ejecutar comandos de consola directamente.

```shell
#Esto ejecuta comandos de a uno en todos (all) los hosts
ansible -i ./hosts-docker.yml -m ping all
ansible -i ./hosts-docker.yml -m shell -a ls all
```

Explicación:
* `-i ./hosts-docker.yml` indica de donde leer el inventario.
* `-m ping` ejecuta el modulo ping.
* `-m raw -a ls` ejecuta directamente sobre el shell plano, y pasa como parametro de modulo `ls`, en este caso es lo que se ejecuta en el server remoto. 
* `-m shell -a ls` ejecuta el modulo shell y pasa como parametro de modulo `ls`. Es similar a "raw" pero utilizó Python en el host para ejecutar la consola.
* `all` indica que se ejecute sobre todos los hosts, puede ser tambien sobre uno solo o sobre un grupo o un conjunto.

Login con user y clave
* `--extra-vars "ansible_user=jdottori ansible_password=$PASS"` agregado al final para conectarse con usuario y clave a un server en lugar de par de claves ssh

## Estructura del proyecto Ansible

Este proyecto está estructurado segun https://docs.ansible.com/ansible/latest/user_guide/playbooks_best_practices.html


## Test de Roles (con Molecule)

Configurar Python 3 como default

sudo apt-get install -y python3 python3-pip  libssl-dev 
```shell
sudo update-alternatives --install /usr/bin/python python /usr/bin/python3.7 1
sudo update-alternatives --install /usr/bin/pip pip /usr/bin/pip3

pip3 install testresources
pip3 install --upgrade --user setuptools
```

Y despues Molecule

```shell
pip install ansible molecule docker paramiko
alias molecule="python3 -m molecule"

```

## Elegir roles (desde Galaxy)

from: https://www.linuxtechi.com/use-ansible-galaxy-roles-ansible-playbook/

Buscar el rol deseado, por ej para buscar un rol de tomcat
* consola con ansible-galaxy search <<tomcat>>
* buscar en la [webpage]()

Before using this we need to figure out couple of things
* is this role compatible with the version of ansible I am running
* what is the license status of this role
* distribución sobre la que funciona
* cumple mis necesidades

Para tener esta info:
* `ansible-galaxy info bennojoy.nt`
