/*
 * User.java
 * @author bnevins
 *
 * Created on September 2, 2007, 7:11 PM
 *
 */

package com.elf.enterprise.auth;

public class User
{
    public User(String un, String hpw, String fn, 
            String ln, String em, String[] r, int sui)
    {
        userName = un;
        firstName = fn;
        lastName = ln;
        email = em;
        hashedPassword = hpw;
        roles = r;
        signupId = sui;
    }

    public boolean isMember(String role)
    {
        for(String r : roles)
            if(r.equals(role))
                return true;
        
        return false;
    }

    public String   getFirstName()            { return firstName;         }
    public String   getLastName()             { return lastName;          }
    public String   getUserName()             { return userName;          }
    public String   getEmail()                { return email;             }
    public String   getHashedPassword()       { return hashedPassword;    }
    public String[] getRoles()                { return roles;             }
    public int      getSignupId()             { return signupId;          }
    

    public void setFirstName(String s)            { firstName = s; }
    public void setLastName(String s)             { lastName = s;  }
    public void setEmail(String s)                { email = s;     }
    public void setRoles(String[] ss)             { roles = ss; }
    public void setHashedPassword(String s)       { hashedPassword = s; }
    //public void setsignupId(String s)            { hashedPassword = s; }
    

    @Override
    public String toString()
    {
        return  "Username: "        + userName +
                ", First Name: "    + firstName +
                ", Last Name: "     + lastName +
                ", Email: "         + email +
                ", Groups: "        + roles.toString() +
                ", Signup ID: "    + signupId;
    }
    
    private String      userName;
    private String      hashedPassword;
    private String      firstName;
    private String      lastName;
    private String      email;
    private int         signupId;
    private String[]    roles;
}
