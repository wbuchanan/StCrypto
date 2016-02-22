cap prog drop crypto

prog def crypto

    version 13

    syntax [, File(string asis) ]

    javacall org.paces.Stata.Cryptography.Crypto stcrypto, args(`file')

end


