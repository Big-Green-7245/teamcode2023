#!/bin/bash

git clone https://github.com/FIRST-Tech-Challenge/FtcRobotController.git FtcRobotController2023
cd FtcRobotController2023/
rm -rf .git
cd TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
rm *
git clone https://github.com/Big-Green-7245/teamcode2022_23.git .
