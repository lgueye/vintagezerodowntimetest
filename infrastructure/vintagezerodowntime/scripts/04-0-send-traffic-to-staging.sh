#!/usr/bin/env bash
cd ~/workspace/vintagezerodowntime-test/infrastructure && time ansible-playbook --vault-password-file ~/.vault_pass.txt -i ~/.digitalocean/vintagezerodowntime -e "rev=8c4a106 target_env=staging duration=20" vintagezerodowntime/send_traffic.yml

