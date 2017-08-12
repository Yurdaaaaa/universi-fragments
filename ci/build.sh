#!/usr/bin/env bash

./gradlew :library:clean :library:check :library:assembleDebug -PpreDexEnable=false