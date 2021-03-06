package com.elf.network;
import java.net.*;
import java.io.*;

/** A shorthand way to create BufferedReaders and
 *  PrintWriters associated with a Socket.
 *  <P>
 *  Taken from Core Servlets and JSP,
 *  http://www.apl.jhu.edu/~hall/csajsp/.
 *  1999 Marty Hall; may be freely used or adapted.
 */

public class SocketUtil {
  /** Make a BufferedReader to get incoming data. */
  
  public static BufferedReader getReader(Socket s)
      throws IOException {
    return(new BufferedReader(
	     new InputStreamReader(s.getInputStream())));
  }

  /** Make a PrintWriter to send outgoing data.
   *  This PrintWriter will automatically flush stream
   *  when println is called.
   */
  
  public static PrintWriter getWriter(Socket s)
      throws IOException {
    // 2nd argument of true means autoflush
    return(new PrintWriter(s.getOutputStream(), true));
  }
}
