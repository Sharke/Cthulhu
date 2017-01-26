package com.cool.tcp;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;

public class client {

	public interface DataHandler {
		public void DataCallBack(String Type,byte[] Data);
	}
	
    private Boolean IsConnected = false;
    private String _IP = "";
    private int _Port = 0;
    private Socket _Sox;
    private TCPListener Listener;
    public KeepAlive KeepAlive = new KeepAlive();
    private int ListenerWait = 0;
    private BufferedOutputStream _outstream;
    private DataHandler _callback;
    public client(String IP, int Port) {
        _IP = IP;
        _Port = Port;
    }

    public Boolean Connect() {
        try {
            _Sox.close();
            _Sox = null;
            _outstream.close();
            _outstream = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }

        try {
            _Sox = new Socket(_IP, _Port);
            _outstream = new BufferedOutputStream(_Sox.getOutputStream());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        IsConnected = true;
        return true;
    }

    public Boolean Write(byte[] Data) {
        try {
            _outstream.write(Data, 0, Data.length);
            _outstream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean Write(String Data) {
        byte[] barray = Data.getBytes();
        try {
            _outstream.write(barray, 0, barray.length);
            _outstream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean Write(int Integer) {
        try {
            _outstream.write(Integer);
            _outstream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean Write(String Code, int Integer, byte[] Data) {
        try {
            _outstream.write(Code.getBytes());
            _outstream.write(Integer);
            _outstream.write(Data);
            _outstream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public Boolean IsConnected() {
        if (IsConnected) {
            return true;
        }

        return false;
    }

    public void StartListener(int Interval, DataHandler callback) {
        if (IsConnected()) {
            ListenerWait = Interval;
            _callback = callback;
            if (Listener == null) {
                Listener = new TCPListener(this, Interval);
            }

            Listener.Start();
        }
    }

    public void StopListener() {
        if (Listener != null) {
            Listener.Stop();
        }
    }

    public class TCPListener implements Runnable {
        private int _Interval = 0;
        private Boolean _Running = false;
        private Thread _ListenerThread;
        private BufferedInputStream _instream;
        private BufferedReader _inreader;
        private client _conn;
        public TCPListener(client conn, int Interval) {
            _Interval = Interval;
            _conn = conn;
            if (_conn != null) {
                try {
                    _instream = new BufferedInputStream(_conn._Sox.getInputStream());
                    _inreader = new BufferedReader(new InputStreamReader(_instream, StandardCharsets.UTF_8));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        public void Start() {
            _Running = true;
            _ListenerThread = new Thread(this);
            _ListenerThread.start();
        }

        public void Stop() {
            _Running = false;
        }

        @
        Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("Listener Started");
            while (_Running) {
                try {
                    byte[] type = new byte[3];
                    byte[] length = new byte[1];
                    _instream.read(type, 0, 3);
                    String types = new String(type, "UTF-8");
                    System.out.println("Type is " + types);
                    _instream.read(length, 0, 1);
                    System.out.println("Length is " + length[0]);
                    int expected = length[0];
                    byte[] buffer = new byte[expected];
                    _instream.read(buffer, 0, expected);
                    String finalmessage = new String(buffer, "UTF-8");
                    System.out.println("Recieved message " + finalmessage);
                } catch (NumberFormatException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    _conn.IsConnected = false;
                    _Running = false;
                }
                // DO Stuff

                try {
                    Thread.sleep(_Interval);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            System.out.println("Thread Returning;");
            return;
        }


    }

    public class KeepAlive implements Runnable {
        private Thread _keepalivethread;
        private Boolean _enabled = false;
        public int Interval = 100;
        
        public void Enable() {
        	if (!_enabled) {
            _enabled = true;
            _keepalivethread = new Thread(this);
            _keepalivethread.start(); 
        	}
        }

        public void Disable() {
            _enabled = false;
        }

        @
        Override
        public void run() {
            // TODO Auto-generated method stub
            while (!IsConnected) {
                //waiting for initial connection
                try {
                    Thread.sleep(Interval);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            while (_enabled) {
                if (IsConnected()) {
                    //im connected
                } else {
                    if (Connect()) {
                        if (Listener != null) {
                            Listener.Stop();
                            Listener = null;
                            StartListener(ListenerWait,_callback);
                        } else {
                        	StartListener(ListenerWait,_callback);
                        }
                    }
                }

                try {
                    Thread.sleep(Interval);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
