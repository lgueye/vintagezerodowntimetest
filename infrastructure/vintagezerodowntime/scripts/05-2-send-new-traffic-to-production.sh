#!/usr/bin/env bash
cd ~/workspace/vintagezerodowntime-test/infrastructure && time ansible-playbook --vault-password-file ~/.vault_pass.txt -i ~/.digitalocean/vintagezerodowntime -e "rev=3e19643 target_env=production duration=5" vintagezerodowntime/send_traffic.yml

