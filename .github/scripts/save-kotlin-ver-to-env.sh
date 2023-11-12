#!/bin/bash

export KOTLIN_VER=$(cat build.gradle.kts  | sed -n -E 's/val KOTLIN_VER.*=( |)//p' | sed 's/"//g')

if [[ -z "$KOTLIN_VER" ]]; then
        echo "Cant grep KOTLIN_VER"
        exit 1;
else
        echo "KOTLIN_VER=$KOTLIN_VER"
        echo "KOTLIN_VER=$KOTLIN_VER" >> "$GITHUB_ENV"
fi