# Team15Project

A password manager that obfuscates passwords in memory and can save them to an encrypted file.<br>
This program can also generate cryptographically secure passwords using the computers CSPRNG.<br>
<br>
This program is also dependant on third party libraries.<br> 
All code within the edu.sjsu.Team15 package is our code, including the EntryPoint.java, Main.java and JUnit4Tests/.<br>
They've already been included in our code, however you can find the third party libraries here:<br>
Package:<br>
[net.codesup.utilities.basen](https://github.com/mklemm/base-n-codec-java)<br>
[io.github.novacrypto](https://github.com/NovaCrypto/SecureString)<br>


Note:<br>
1.If you are running this program on linux, you need to have XClip installed for the copy password function to work.<br>
2.If you decide to run the JUnit tests, XMLTests will generate extra files.<br>
<br>
Known bugs:<br>
Occasionally, the password generator will create a password with an invalid ASCII character.<br>
This is likely a result of the hash byte data being directly encoded using base94 without checking for invalid byte values.<br>
