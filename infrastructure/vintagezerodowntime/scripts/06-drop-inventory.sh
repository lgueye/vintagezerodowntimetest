#!/usr/bin/env bash
cd ~/workspace/vintagezerodowntime-test/backends/vintagezerodowntime-boxprovider && java -jar target/vintagezerodowntime-boxprovider-0.0.1.jar --request='{"intent": "drop","provider":"digitalocean","specifications":[{"env":"production"},{"env":"staging"}]}'


