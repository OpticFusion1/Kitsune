# Kitsune
Kitsune is a program that allows one to easily reverse engineer .jar and .class files.<br>
Kitsune currently requires Java 17 to run and currently only has 2 tools, this however will change over time.

| Command | Description | Usage |
| --- | --- | --- |
| strings | Prints every string that is in the jar or class file. | strings <<file_name>|all <directory_path>> [--normalize] [--removeDuplicates] [--showSHA1Hash] 
| analyze | Analyzes the jar or class file and returns a variety of useful information. | analyze <<file_name> \| all <directory_path>>

Strings Arguments<br>
normalize: Makes the string lowercase and trims any spaces<br>
removeDuplicates: Every string only logs once<br>
showSHA1Hash: Logs the string's SHA-1 hash. More hashes will be supported in the future.
