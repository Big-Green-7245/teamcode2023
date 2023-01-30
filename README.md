<h1 align="center">teamcode 2022-2023</h1>
<p align="center">Codebase for FTC game season 2022-23, POWERPLAY</p>

## Working with Android Studio

### Setting up Android Studio

Simply run the following command in the terminal on macOS/linux to setup Git and the FtcRobotController Android Studio
Project

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Big-Green-7245/teamcode2022_23/main/.assets/setupRepo.sh)"
```

---

**Alternatively, follow the follow guide**

To work with the repository in Android Studio, first clone the newest (currently 7.2) version of FtcRobotController App.
Then, replace the teamcode folder with this repository.

This can be easily done through git. In the terminal, move to a folder where you keep your robotics stuff, and run the
following commands.

```bash
git clone https://github.com/FIRST-Tech-Challenge/FtcRobotController.git
cd FtcRobotController/
sudo rm -r .git
cd TeamCode/src/main/java/org/firstinspires/ftc/teamcode/
rm *
git clone https://github.com/Big-Green-7245/teamcode2022_23.git .
```

### Uploading the code

To upload code to the device, connect to the Control Hub via USB or WiFi-Direct. Select the device and upload.

*Note if connected wirelessly, Android Studio might require cli adb connection first with the following command*

```bash
adb connect 192.168.43.1:5555
```

---

![Connected](.assets/connectedDevice.png)

Once connected, it should look like the above.

### Making sure the code updates

Every TeleOp/Autonomous program should have a programVer variable to keep track of version increments. Sometimes Android
Studio does not update the code right away.

```java
final String programVer="1.0";
```

## Trouble Shooting / Misc

### Sdk Not Found

If you get an error saying that the Android Sdk is not found, try adding `local.properties` in your
project root directory with the following line

```properties
sdk.dir=/Users/yourusernamehere/Library/Android/sdk
```

or pointing to wherever your Android Sdk is located.

### Using Java 17 😎
#### Or other newer versions

To use Java 17, two files need to be edited: `build.common.gradle` and `/FtcRobotController/build.gradle`.  
Locate

```groovy
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

in both files and replace `VERSION_1_7` or `VERSON_1_8` with `VERSION_17`. Then resync gradle through a button that
should appear towards the top right or through the gradle menu on the right (Gradle -> Reload All Gradle Projects).

