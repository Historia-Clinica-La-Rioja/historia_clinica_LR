# Test con Docker

Para testear la configuración con Docker

```
docker-compose up --build -d 
ansible -i ./hosts-docker.yml -m ping all
ansible-playbook -i ./hosts-docker.yml debug.yml
ansible-playbook -i ./hosts-docker.yml webservers.yml
ansible-playbook -i ./hosts-docker.yml postgres.yml
ansible-playbook -i ./hosts-docker.yml sga-completo.yml
```

Esto realiza los siguientes pasos:

* levanta 8 contenedores docker con una imagen de (`centos` con `systemd`)
* test ping de ansible
* ejecuta el playbook para configurar todos los nodos de SAI

Para probar los contenedores:
* `docker-compose ps`
* `docker-compose exec CONTAINER bash`

Las IPs están estáticas para que sea más similar a producción. Asi que se pueden usar esos IPs para acceder via Web.
