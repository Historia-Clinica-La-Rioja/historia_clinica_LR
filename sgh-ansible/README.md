# Ansible SGH

## Instalación de Ansible

Este documento incluye referencias varias para Ansible.

* [Instalar Ansible](documentacion/setup-ansible.md)
* [Mini tutorial de desarrollo](documentacion/develop.md)
* [Testing: Levantar un ambiente con contenedores docker para desarrollo](documentacion/test.md)
* [Molecule: para TDD de roles](Docker-test.md)

## Instalación de SGH con Ansible

Para la instalación de SGH mediante los playbooks Ansible provistos aqui se deben seguir los siguientes pasos:

Prerequisitos:
* El host local debe ser Linux 
* Tener una BBDD Postgres instalada con acceso de admin o equivalente para crear el usuario
* Tener 4 VPS segun documento de despliegue
* Tener instalado Maven en host local 

A grandes rasgos los pasos a realizar para la instalación son:
0. Instalar Ansible y Maven en el host local
1. Configurar un inventario con los VPS a utilizar indicando el grupo de cada uno. Se utiliza Debian 10 (Buster) para los VPS base. 
Se proveen archivos de inventarios de template para un ambiente de alta disponibilidad (HA - High Availability) y otro para un único nodo, en ambos casos con una base de datos PostgreSQL 11+ externa y ya instalada.
Se supone que la base de datos está ya instalada y configurada correctamente para aceptar conexiones remotas.
   1.1 Copiar el template `hosts-template.yml` a un archivo llamado `hosts-deploy.yml`
   1.2 Editar nombres y grupos de hosts o arquitectura según URL o dirección IP de cada uno. 

2. Configurar variables necesarias
3. Ejecutar los playbooks indicados

## Groups y host especiales

Existen los siguientes grupos que deben ser configurados en el inventory con las variables de cada uno:

* `web_load_balancers`
* `backservers`
* `frontservers`
* `databases` y nodo especial `postgres_master`
   * El nodo especial `postgres_master` es el nodo de la base de datos
   * database_su_name: nombre de usuario de sysadmin de la bbdd (usado solo para crear el usuario y bbdd de aplicacion)
   * database_su_password: clave del usuario anterior
Si el paso automatizado de crear la base de datos prefiere realizarse externamente a estos playbooks, omitir el plabook `3-create-database.yml`

En los pasos siguientes se supone que el inventario está almacenado en `hosts-deploy.yml`.

## Instalar solo webservers

```
ansible-playbook -i hosts-deploy.yml 1-frontservers.yml
ansible-playbook -i hosts-deploy.yml 2-backservers.yml
ansible-playbook -i hosts-deploy.yml 3-sghservers.yml
ansible-playbook -i hosts-deploy.yml 4-lb_web.yml
ansible-playbook -i hosts-deploy.yml 5-deploy.yml
```

Los pasos 1 a 4 se realizan una unica vez. Con siguientes versiones solo es necesario ejecutar el paso 5.

```
ansible-playbook -i hosts-deploy.yml 5-deploy.yml
```

## Adminsitrar el proceso SGH

El servidor SGH se instala como servicio de Tomcat y el mismo puede ser controlado con `systemd`, lo que permite utilizar los siguientes comandos para manejarlo.

```shel
# iniciar, parar y ver el estado del servicio
systemctl start tomcat
systemctl stop tomcat
systemctl status tomcat
# ver logs y seguir viendolos (follow)
sudo journalctl -u tomcat.service -f
```
