#!/usr/bin/env bash
cd ~/workspace/vintagezerodowntime-test/infrastructure && time ansible-playbook --vault-password-file ~/.vault_pass.txt -i ~/.digitalocean/vintagezerodowntime -e "rev=9783303 target_env=staging clean_db=false" vintagezerodowntime/upgrade_line.yml
