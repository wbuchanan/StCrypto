# Stata Cryptography Plugin

Started building the plugin in response to a [Statalist posting](http://www.statalist.org/forums/forum/general-stata-discussion/general/1326747-storing-api-keys-in-stata) regarding how to store API keys securely for use in Stata.  Additionally, this would also make it easier and safer to use the [Stata Email](https://github.com/wbuchanan/StataEmail) plugin with a file containing the user's email credentials securely.  

# Examples
 
## Example 1. Encrypting a File

```
// Load a freely available data set
import delimited using                                                       ///   
"https://raw.githubusercontent.com/emorisse/FBI-Hate-Crime-Statistics/master/2013/table13.csv", ///   
delim(",") case(l) varn(1) clear

// Save the dataset to disk
qui: save hateCrimeDataFBI2013.dta, replace

// You can pass the filepath to the program directly or you can set it in the 
// GUI

// Passes the file name directly.  
crypto, f("`c(pwd)'/hateCrimeDataFBI2013.dta")

// Starts the GUI without prepopulating the filename
crypto

```

When the GUI pops up there are two password fields.  A valid password 
satisfies the conditions below:

|-----------------------|-------------------------------|
| Character Class       | Minimum number of occurrences |
| Lower Case Alphabetic | 1                             |
| Upper Case Alphabetic | 1                             |
| Numeric Character     | 1                             |
| Non-Alphanumeric      | 1                             |

If an invalid password is attempted the GUI with throw an error telling you 
which character classes were found (true) and not found (false).  You can 
then choose to try entering a valid password again.  Additionally, if the 
characters in both password fields are not identical the GUI will pop up a 
new alert letting you know that the passwords did not match.  

When you click on the encrypt button a message will display that will let you 
know whether or not the file was successfully encrypted/decrypted.  

# Important Notes
__Keep in mind that this is a very early release with limited capabilities.  I 
wouldn't recommend using it in a production environment at the moment because 
too many of the parameters are specified in advance.__  Additional work will 
expose some of the settings to end users (e.g., encryption algorithms, key 
lengths, etc...), but requires more work to get the available options on the 
user's system (in addition to the valid combinations of algorithms, modes, 
padding, key lengths, etc...).  While the program can be launched interactively 
from Stata, the goal is to make a CLI for the program as well, to allow users 
to distribute the application and/or point others to the application so they 
can decrypt files/data being sent to them.
  
  