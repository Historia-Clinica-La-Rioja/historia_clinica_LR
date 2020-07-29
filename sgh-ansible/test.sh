#!/bin/sh

docker-compose down
docker-compose up -d
ansible-playbook -i hosts-template-single-node.yml 1-frontservers.yml \
                                                   2-backservers.yml\
                                                   3-create-database.yml\
                                                   4-lb_web.yml\
                                                   5-deploy.yml
