# Setup Ansible

Este documento incluye información útil para el desarrollo de Ansible.

## Instalar Ansible y Molecule con Docker

Algunos paquetes dan problemas por querer instalarse en usuario, etc. 
Por eso se recomienda usar `environments`de Python. 

Consideraciones:
* Validar que se esté usando Pyhton 3
   * python3 --version
   * pip3 --version

```shell
sudo apt update
sudo apt install -y python3-pip
pip3 install "ansible<2.10" --upgrade
pip3 install "molecule<2.23" --upgrade

# algunos de estos modulos estan en : https://docs.ansible.com/ansible/latest/scenario_guides/guide_docker.html
pip3 install docker --upgrade
pip3 install docker-py --upgrade
pip3 install docker-pycreds
```

Para el uso de Docker en Windows desactivar el soporte para WSL2.

## Configurar servidores

En los VPS se puede configurar un usuario Ansible que no requiera clave para `sudo`.
Se recomienda usar un usuario `sudoer` sin password [con estas instrucciones](https://phpraxis.wordpress.com/2016/09/27/enable-sudo-without-password-in-ubuntudebian/)

En el server:
```shell
sudo useradd ansible
sudo usermod -aG sudo ansible
sudo visudo
#ansible     ALL=(ALL) NOPASSWD:ALL
```

En el cliente:
```shell
#generar
ssh-keygen -t ed25519 -f ./keys/id_ed25519_ansible
cp keys/* ~/.ssh/
#COPY PUBLIC KEY
ssh-copy-id -i keys/id_ed25519_ansible.pub ansible@<<servidor>>
```

A partir de ahora, con esa clave te conectas al equipo directamente como ansible! `ssh ansible@<<servidor>>`

Opciones:
* Agregar hosts en `sudo nano /etc/ansible/hosts` (revisar path segun instalación), con formato `USER@HOST`
* Utilizar archivo de inventario para dicho server


### Agregar fingerprint ssh

Requiere tener agregada la finger-print-ssh de los host destinos.

### Usar passwords

Si se desea usar ssh con passwords en lugar de claves es necesario instalar en el cliente sshpass

```shell
sudo apt install -y sshpass
```
