﻿# P4 Plugin
Jenkins plugin for a Perforce Helix Core Server (P4D).

## Contents

* [Release notes](https://github.com/jenkinsci/p4-plugin/blob/master/RELEASE.md)
* [FreeStyle setup guide](docs/SETUP.md)
* [Pipeline setup guide](docs/WORKFLOW.md)
* [Pipeline libraries](docs/LIBRARY.md)
* [MultiBranch guide](docs/MULTI.md)
* [Building Jobs](docs/BUILDINGJOBS.md) 
* [Post Build Actions](docs/POSTBUILD.md) 
* [Advanced scripting](docs/P4GROOVY.md)
* [Notes page](docs/NOTES.md)
* [Jenkins page](https://plugins.jenkins.io/p4)

## Requirements

* Jenkins 1.642.3 or greater.
* Helix Core Server 2016.1 or greater.
* Minimum Perforce Protection of `open` for the Jenkins user.
* Review Build feature requires Helix Swarm 2016.2 or greater.

## Known Limitations
- One Jenkins job per Helix Swarm branch. 

## Install

1. Open Jenkins in a browser; for example http://jenkins_host:8080
2. Browse to 'Manage Jenkins' --> 'Manage Plugins' and select the **Available** tab.
3. Find the 'P4 Plugin' or use the **Filter** if needed
4. Check the box and click the **Install without restart** button

If you are unable to find the plugin, you may need to refresh the 'Update Site'.

1. Select the **Advanced** tab (under 'Manage Plugins')
2. Click the **Check now** button at the bottom of the page.
3. When 'Done' go back to the update centre and try again.

## Building

To build the plugin and run the tests use the following:

	mvn package
  
**Note:** for the tests to run you must have p4d in your PATH, to skip tests use the -DskipTests flag.

Lastest SNAPSHOT builds available [here](https://ci.jenkins.io/blue/organizations/jenkins/Plugins%2Fp4-plugin/branches).

## Manual install

1. Open Jenkins in a browser; e.g. http://jenkins_host:8080
2. Browse to 'Manage Jenkins' --> 'Manage Plugins’ and select the **Advanced** tab.
3. Click the **Browse** button in the 'Upload Plugin' section. 
4. Find the location of the 'p4.hpi' file and click the **Open** button. 
5. Click the **Download now and install after restart** button (this might be different on the newer version). 

