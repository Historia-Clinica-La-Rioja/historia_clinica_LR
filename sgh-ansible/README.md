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

0. Instalar Ansible y Maven en el host local.
1. Configurar un inventario con los VPS a utilizar indicando el grupo de cada uno. Se utiliza Debian 10 (Buster) para los VPS base. 
2. Configurar variables necesarias.
3. Generar los archivos binarios. 
4. Ejecutar los playbooks indicados.

## Groups y host especiales

Los playbooks estan desarrollados para funcionar sobre `Debian 10 Buster`.
Existen los siguientes grupos que deben ser configurados en el inventory con las variables de cada uno:

* `web_load_balancers`
* `backservers`
* `frontservers`
* `databases`
* nodo especial `postgres_master`
   * El nodo especial `postgres_master` es el nodo de la base de datos, debe ser una BBDD ya existente
   * este nodo debe poseer instalado soporte para el locale "en-us.UTF-8"
   * este nodo debe permitir conexiones desde los nodos clientes, para eso en general es necesario editar estos dos archivos que suelen estar ubicados en `/var/lib/postgresql/data`
      * `listen_adres='*'` en `postgres.conf` 
      * `host all all all md5` en pb_hba.conf
   * database_su_name: nombre de usuario de sysadmin de la bbdd (usado solo para crear el usuario y bbdd de aplicacion)
   * database_su_password: clave del usuario anterior


Se proveen archivos de inventarios de template para un ambiente de alta disponibilidad (HA - High Availability) y otro para un único nodo, en ambos casos con una base de datos PostgreSQL 11+ externa y ya instalada.
Se supone que la base de datos está ya instalada y configurada correctamente para aceptar conexiones remotas.
   1.1 Copiar el template `hosts-template.yml` a un archivo llamado `hosts-deploy.yml` localizado en un directorio externo al proyecto.
   1.2 Editar nombres y grupos de hosts o arquitectura según URL o dirección IP de cada uno. 

En el nodo `localhost` es necesario tener Maven 3 instalado para realizar la migracion de la BBDD.
Se prevee en una versión futura quitar este requerimiento.

En los pasos siguientes se asumirá que el inventario está almacenado en `${PATH}/hosts-deploy.yml`.

## Configuración de variables necesarias

La tabla siguiente expone el conjunto de variables que deben configurarse de manera obligatoria en el archivo de inventario `hosts-deploy.yml`.

| variable     | descripción | Efecto |
| ------------ | ----------- | ------- |
| token_secret | Palabra utilizada para encriptar el token de seguridad | No inicia la aplicación |
| recaptcha_secret_key | Clave secreta para el sitio otorgada por [Google reCaptcha](https://www.google.com/recaptcha/admin/create) | Login no habilitado |
| recaptcha_site_key | Clave pública para el sitio otorgada por [Google reCaptcha](https://www.google.com/recaptcha/admin/create) | Login no habilitado |
| renaper_usuario | Nombre de usuario para login en servicio Renaper | No funciona servicio Renaper | 
| renaper_clave | Clave para login en servicio Renaper | No funciona servicio Renaper | 
| federar_issuer | URI del dominio (registrada previamente ante la DNGISS)  | No funciona servicio Federar. Sin acceso a la Red Nacional de interoperabilidad |
| federar_subject | Nombre del dominio | No funciona servicio Federar. Sin acceso a la Red Nacional de interoperabilidad |
| federar_sign_key | Palabra secreta, única y cifrada la cual será asignada por la DNGISS para autenticación | No funciona servicio Federar |
| snowstorm_id | Identificador para autenticación | No funciona servicio snowstorm para búsqueda de conceptos Snomed |
| snowstorm_key | Clave para autenticación | No funciona servicio snowstorm para búsqueda de conceptos Snomed |
 
**Variables sugeridas a ser modificadas**: 

Las variables expuestas en la tabla siguiente podrán ser modificadas en el archivo `/roles/sgh-back/defaults/main.yml`. Si bien, estas variables han sido configuradas con valores por defecto, se recomienda configurarlas con valores propios. 

| variable     | descripción | Efecto |
| ------------ | ----------- | ------- |
| admin_password | Contraseña para usuario root | Configura la aplicación por defecto con contraseña `admin123`|
| document_root_directory | Directorio de almacenamiento para documentos | No inicia la aplicación |

## Almacenamiento de documentos
Para despliegues multi nodos se recomienda configurar un NFS cuyo directorio deberá configurarse en la variable `{{ document_root_directory }}`.  

## Instalar todo

```
../scripts/build-ansible.sh

ansible-playbook -i ${PATH}/hosts-deploy.yml 1-frontservers.yml
ansible-playbook -i ${PATH}/hosts-deploy.yml 2-backservers.yml
ansible-playbook -i ${PATH}/hosts-deploy.yml 3-create-database.yml
ansible-playbook -i ${PATH}/hosts-deploy.yml 4-lb_web.yml
ansible-playbook -i ${PATH}/hosts-deploy.yml 5-deploy.yml
```

Los pasos 1 a 5 se realizan una unica vez. 
Si el paso automatizado de crear la base de datos prefiere realizarse externamente a estos playbooks, omitir el plabook `3-create-database.yml`, de todas formas debe indicarse el `postgres_master` porque su IP es necesaria para configurar el backend.

Con siguientes deploys — *actualización de versiones* — solo es necesario ejecutar el playbook 5.

```shell
git fetch --tags
git checkout ${TAG}

../scripts/build-ansible.sh

ansible-playbook -i hosts-deploy.yml 5-deploy.yml
```

## Startup 
Al iniciar la aplicación, se podrá operar con el usuario administrador `admin@example.com` y contraseña configurada en la variable de ambiente `admin.password`, cuyo valor por defecto es `admin123`. Tras el login, la aplicación accederá al módulo de Backoffice.    

## Problemas conocidos y solución de errores

* Se ha reportado que en los nodos de Load Balancer ha sido necesario  "sudo apt install policykit-1"


## Adminsitrar el proceso SGH

El servidor SGH se instala como servicio de Tomcat y el mismo puede ser controlado con `systemd`, lo que permite utilizar los siguientes comandos para manejarlo.

```shell
# iniciar, parar y ver el estado del servicio
systemctl start tomcat
systemctl stop tomcat
systemctl status tomcat
# ver logs y seguir viendolos (follow)
sudo journalctl -u tomcat.service -f
```
