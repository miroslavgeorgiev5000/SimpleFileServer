# Simple File Server

Allows you to host files easily over HTTP. 
The purpose of this project is to have a minimalistic HTTP hosting service in java which
can be easily modified to provide on demand APIs, or to handle dynamic webapps where 
middleware such as apache doesn't suit the needs.

Usage:
1. execute compile.bat
2. execute run.bat
3. open your browser, type localhost and hit enter
4. your browser should display the contents of the root_dir folder

- by default, it runs on port 80. If you want it to be accessible over the internet, make sure port 80 is forwarded on your network.
- to change the folder from which files are hosted, edit the root_dir.txt file. By default, it contains a local path to the root_dir folder with the two dummy files. 
