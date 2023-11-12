#!/bin/bash

export KOTLIN_VER=$(cat buildSrc/build.gradle.kts | grep 'KOTLIN_VER.*' -o | grep -P -oh '(?<=").*(?=")')

if [[ -z "$KOTLIN_VER" ]]; then
        echo "Cant grep KOTLIN_VER"
        exit 1;
else
        echo "KOTLIN_VER=$KOTLIN_VER"
        echo "KOTLIN_VER=$KOTLIN_VER" >> "$GITHUB_ENV"
fi