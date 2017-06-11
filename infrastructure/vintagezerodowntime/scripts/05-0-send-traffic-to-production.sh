#!/usr/bin/env bash
cd ~/workspace/vintagezerodowntime-test/infrastructure && time ansible-playbook --vault-password-file ~/.vault_pass.txt -i ~/.digitalocean/vintagezerodowntime -e "rev=a3da546 target_env=production duration=20" vintagezerodowntime/send_traffic.yml

