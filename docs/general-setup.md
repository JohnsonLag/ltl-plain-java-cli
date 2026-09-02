## The ltl-plain-java-cli (`ltl`) setup guide.

### Table of contents
[Core dependencies and tested versions](#core-dependencies-and-tested-versions)

[Ways to run `ltl`](#ways-to-run-ltl)

[Option 1: using scripts](#option-1-using-scripts)

[Option 2: using IntelliJ IDEA](#option-2-using-intellij-idea)

### Core dependencies and tested versions.

**Notes**

* First check if any of the dependencies and their required versions are already installed on your system.

  - MySQL: `mysql --version`

  - Java: `java --version`

  - Maven: `mvn --version`

* If an installer (e.g., `.msi` file for Windows, `.dmg` file for macOS) is available for a dependency on your platform, you may find it easiest to use that.

* While installing a dependency, note the installation location, as you will likely need the file paths for later steps in these instructions.

* If a dependency isn’t automatically added to your `PATH`, then you may find it useful to do so manually, although it isn’t necessary.

#### MySQL Community: 8.4.

* Installation guide by platform: https://dev.mysql.com/doc/refman/8.4/en/installing.html

* Downloads page: https://dev.mysql.com/downloads/

* Key steps: configuring the service to run at startup, setting the root account password.

#### Java: 26.

* Installation guide by platform: https://docs.oracle.com/en/java/javase/26/install/overview-jdk-installation.html

* Downloads page: https://www.oracle.com/java/technologies/downloads/

#### Apache Maven: 3.9.16.

* Installation guide by platform: https://maven.apache.org/install.html

* Downloads page: https://maven.apache.org/download.cgi

#### Optional: IntelliJ IDEA: 2026.2.1 (build 262.9437.185)

* Downloads page: https://www.jetbrains.com/idea/download/

#### Optional: `git` or GitHub Desktop

* If you want to clone the repository and be able to pull the latest changes, rather than download a `.zip` copy.

### General setup steps

* Install and configure the core dependencies above.

* Download the `ltl` repository as a `.zip` file from GitHub and unzip it in a directory of your choosing.

* If you won't be using Option 1 below, then simply rename `.env-template.txt` to `.env`. Otherwise, run the `rename-files.bat` file.

* Set up your local `ltl` MySQL database by following the [instructions in the `db` folder](../db/ltl-database-setup-guide.md) of the `ltl` repository.

  - Add the ltl_user credentials for the database (or the credentials for the non-root user you created) to your `.env` file. 

* Select which option below that you prefer to run `ltl`.

### Ways to run `ltl`

#### Option 1: using scripts.

More manual, but a minimal setup.

**One-time setup:**

Windows: fill in the provided batch files (`.bat`).

Linux, macOS: create and use shell scripts (`.sh`).

Fill in the `ltl-compile.bat` file.

```
C:\path\to\mvn clean compile
```

Run the `ltl-compile.bat` file.

Next, fill in the `ltl-run.bat` file with the proper paths on your system. Fill in the correct paths to `java` and the `ltl` folder, and change the `you` in the paths to your username on your computer.

For example, if you downloaded and unzipped this repository to a folder called `tools`, your file would look like this.

```
C:\path\to\java.exe –classpath C:\Users\you\tools\ltl-plain-java-cli\target\classes;C:\Users\you\.m2\repository\org\jsoup\jsoup\1.23.1\jsoup-1.23.1.jar;C:\Users\you\.m2\repository\com\mysql\mysql-connector-j\26.7.0\mysql-connector-j-26.7.0.jar;C:\Users\you\.m2\repository\com\google\protobuf\protobuf-java\4.31.1\protobuf-java-4.31.1.jar org.example.Main
```

**Regular use:**

On the command line, in the project root directory, run `ltl.bat`.

#### Option 2: using IntelliJ IDEA.

Simpler.

**One-time setup:**

* Install IntelliJ IDEA.

* Create a new project and choose the ltl-plain-java-cli directory.

* Open a terminal window and run `mvn clean compile`.

    - If `mvn` is not in your `PATH`, then copy the path to the executable on your system and run `C:\path\to\mvn clean compile`.

**Regular use:**

* In IntelliJ IDEA, choose `Run 'Main'`.