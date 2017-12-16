package com.elf.util.zip;

public class ZipFileException extends Exception
{
    // the constructors are all package scope...
    ZipFileException(Throwable t)
    {
        super(t);
    }
    ZipFileException(String s, Throwable t)
    {
        super(s,t);
    }

    ZipFileException(String s)
    {
        super(s);
    }

    ZipFileException()
    {
    }
}
