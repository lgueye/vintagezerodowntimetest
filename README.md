## Description

Project to support a zero-downtime upgrade showcase:

- t0: create boxes
- t1: create production line at revision N
- t2: create staging line from production line (software + schema + data)
- t3: generate production traffic
- t4: start production -> staging traffic duplication
- t5: upgrade staging line (revision N -> revision N + 1)
- t6: stop production -> staging traffic duplication
- t7: upgrade production line (revision N -> revision N + 1)
- t8: test production line at revision N + 1
- t9: drop boxes

Product:

- [frontend]: single page application that displays devices measurements (heart rate, etc.) on graphs
- [backend]: ingests device events, persists them, publishes them to [frontend] via web sockets

Infrastructure:

- [db]: stores append-only events (1 host per env)
- [backends]: ingests, persists and publishes events (2 hosts per env, scalable to N)
- [load-balancer/frontend]: load balances requests to backend hosts, serves frontend files (1 host per env)

## Setup

- Install oracle jdk8+ (system wide)
https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04

- Install maven 3.3.9+ (system wide)
https://archive.apache.org/dist/maven/maven-3

- Install ansible >= 2.2 (system wide)
http://docs.ansible.com/ansible/intro_installation.html#latest-releases-via-apt-ubuntu

- Install nodejs and npm (system wide)
https://www.digitalocean.com/community/tutorials/how-to-install-node-js-on-ubuntu-16-04

- Install requirements
cd infrastructure/vintagezerodowntime/scripts && ./requirements.sh

## Setup your box provider

- Create and provision your provider account (mine is digitalocean https://cloud.digitalocean.com)

- Upload your ssh key to your provider. /!\ : this ssh key will be used to provision the boxes

- Create your API token and set the env var to securely request your provider

## Security

#### Create vault password file to store your passphrase

`touch ~/.vault_pass.txt`
`echo "my_passphrase_change_me" > ~/.vault_pass.txt`
`chmod 700 ~/.vault_pass.txt`

#### Create and encrypt passwords for production env

`rm infrastructure/vintagezerodowntime/group_vars/production/vault.yml`

`touch infrastructure/vintagezerodowntime/group_vars/production/vault.yml`

`echo 'db_user_password: "db_user_password_production_change_me"' >> infrastructure/vintagezerodowntime/group_vars/production/vault.yml`

`echo 'db_admin_password: "db_admin_password_production_change_me"' >> infrastructure/vintagezerodowntime/group_vars/production/vault.yml`

`echo 'monitoring_email_password: "monitoring_email_password_password_production_change_me"' >> infrastructure/vintagezerodowntime/group_vars/production/vault.yml`

`ansible-vault --vault-password-file ~/.vault_pass.txt encrypt infrastructure/vintagezerodowntime/group_vars/production/vault.yml`

#### Create and encrypt passwords for staging env

`rm infrastructure/vintagezerodowntime/group_vars/staging/vault.yml`

`touch infrastructure/vintagezerodowntime/group_vars/staging/vault.yml`
`echo 'db_user_password: "db_user_password_staging_change_me"' >> infrastructure/vintagezerodowntime/group_vars/staging/vault.yml`

`echo 'db_admin_password: "db_admin_password_staging_change_me"' >> infrastructure/vintagezerodowntime/group_vars/staging/vault.yml`

`echo 'monitoring_email_password: "monitoring_email_password_password_staging_change_me"' >> infrastructure/vintagezerodowntime/group_vars/staging/vault.yml`

`ansible-vault --vault-password-file ~/.vault_pass.txt encrypt infrastructure/vintagezerodowntime/group_vars/staging/vault.yml`

## Build

- Build backends

`cd backends && mvn clean install`

## Run check-list

- All softwares installed and running (cf. Setup)
- Ansible requirements installed (cf. Setup)
- Backends already built (required to create/drop boxes, not needed otherwise) 
- Ansible inventory present at `~/digitalocean/vintagezerodowntime` or use `infrastructure/vintagezerodowntime/scripts/01-create-inventory.sh` to create boxes and generate inventory 
- Ansible inventory defines reachable hosts for production and staging env (hosts up and running and ssh key uploaded) 
- Ansible vault file created at `~/.vault_pass.txt`

## Run

- t0: create boxes => `infrastructure/vintagezerodowntime/scripts/01-create-inventory.sh`
- t1: create production line at revision N => `infrastructure/vintagezerodowntime/scripts/02-create-production-line.sh`
- t2: create staging line from production line (software + schema + data) => `infrastructure/vintagezerodowntime/scripts/03-create-staging-line.sh`
- t3: generate production traffic => `infrastructure/vintagezerodowntime/scripts/04-send-traffic-to-production.sh`
- t4: start traffic replay => `infrastructure/vintagezerodowntime/scripts/05-start-traffic-replay.sh`
- t5: upgrade staging line (revision N -> revision N + 1) => `infrastructure/vintagezerodowntime/scripts/06-upgrade-staging-line.sh`
- t6: stop traffic replay => `infrastructure/vintagezerodowntime/scripts/07-stop-traffic-replay.sh`
- t7: upgrade production line (revision N -> revision N + 1) => `infrastructure/vintagezerodowntime/scripts/08-upgrade-production-line.sh`
- t8: drop boxes => `infrastructure/vintagezerodowntime/scripts/09-drop-inventory.sh`
