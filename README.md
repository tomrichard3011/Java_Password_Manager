# Team15Project

A password manager that obfuscates passwords in memory and can save them to an encrypted file.
This program can also generate cryptographically secure passwords using the computers CSPRNG.

This program is also dependant on third party libraries.
They've already been included in our code, however you can find them here:
https://github.com/mklemm/base-n-codec-java
https://github.com/NovaCrypto/SecureString


Note:
If you are running this program on linux, you need to have XClip installed for the copy password function to work.

Known bugs:
Occasionally, the password generator will create a password with an invalid ASCII character.
This is likely a result of the hash byte data being directly encoded using base94 without checking for invalid byte values.
