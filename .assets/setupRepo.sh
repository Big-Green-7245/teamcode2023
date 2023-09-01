#!/bin/bash

git clone https://github.com/FIRST-Tech-Challenge/FtcRobotController.git FtcRobotController
cd FtcRobotController/
rm -rf .git
cd TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
rm *
git clone https://github.com/Big-Green-7245/teamcode2023.git .
