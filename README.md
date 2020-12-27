# Webserver_with_GUI
An easy to use Webserver with GUI

This is a web server, written in java,
which has an easy to use GUI.
The webserver whithout GUI and config files is by SSaurel,
from his tutorial, but I have modifyed 
this code much.

The Java version, which is compiled,
is the version 14, but you can compile
the sourcecode yourself.

# Sourcecompiling and running
1. Download the sources.zip and unpack it into a folder.
2. run "javac Webserver.java ServerMain.java".
3. Ceate a file named manifest.txt and write ths into it:

Main-Class: ServerMain
Clath-Path: .

4. Now you can open your commandline and run:
"jar cfm <the name of the Jar file>.jar manifest.txt *.class"
 
 5. You can now execute it with java -jar <the name of the Jar file>.jar.
