package com.cool.util;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import com.cool.json.*;
import java.io.File;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
public class functions {
	
	public static class Device {
		
		public static void MessageBox(String Title, String Message, String Type) {
			
			if (Type == "Error")
			{
			JOptionPane.showMessageDialog(null, Message, Title, JOptionPane.ERROR_MESSAGE);
			}
			
			else if (Type == "Warning") {
				JOptionPane.showMessageDialog(null, Message, Title, JOptionPane.WARNING_MESSAGE);
			}
			
			else if (Type == "Info") {
				JOptionPane.showMessageDialog(null, Message, Title, JOptionPane.INFORMATION_MESSAGE);
			}
			
			else if (Type == "Question") {
				JOptionPane.showMessageDialog(null, Message, Title, JOptionPane.QUESTION_MESSAGE);
			}
			
			else if (Type == "Plain") {
				JOptionPane.showMessageDialog(null, Message, Title, JOptionPane.PLAIN_MESSAGE);
			}
		}
		
		public static byte[] Screenshot(){
			try {
				BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", stream);
                return stream.toByteArray();
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public static void Shutdown() {
		    String shutdownCommand = "";
		    String operatingSystem = java.lang.System.getProperty("os.name");

		    if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
		        shutdownCommand = "shutdown -h now";
		    }
		    else if ("Windows".equals(operatingSystem)) {
		        shutdownCommand = "shutdown.exe -s -t 0";
		    }
		    else {
		        //throw new RuntimeException("Unsupported operating system.");
		    }

		    try {
				Runtime.getRuntime().exec(shutdownCommand);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    java.lang.System.exit(0);
		}
		
		public static void Reboot() {
		    String shutdownCommand = "";
		    String operatingSystem = java.lang.System.getProperty("os.name");

		    if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
		        shutdownCommand = "reboot";
		    }
		    else if ("Windows".equals(operatingSystem)) {
		        shutdownCommand = "shutdown.exe -r -t 0";
		    }
		    else {
		        //throw new RuntimeException("Unsupported operating system.");
		    }

		    try {
				Runtime.getRuntime().exec(shutdownCommand);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    java.lang.System.exit(0);
		}
		
		public static void ClickMouse(int X, int Y) {
	        Robot robot;
			try {
				robot = new Robot();
		        robot.mouseMove(X, Y);
		        robot.mousePress(InputEvent.BUTTON1_MASK);
		        robot.mouseRelease(InputEvent.BUTTON1_MASK);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	        }
		
		public static String ListDrives(){
			File[] Drives = File.listRoots();
			String str = "";
			for (File localDrive : Drives) {
				str += "Drive::" + localDrive.getAbsolutePath() + "\n";
			}
			return str;
		}
		
		public static int CoreCount() {
			return Runtime.getRuntime().availableProcessors();
		}
		
		public static String ListFiles(String Path){
			File Folder = new File(Path);
			File[] FileList = Folder.listFiles();
			String strdir = "";
			String strfi = "";
			for (int i = 0; i < FileList.length; i++) {
				if (FileList[i].isDirectory()) {
					strdir += "Dir::" + FileList[i].getName() + "\n";
				} else {
					strfi += "File::" + FileList[i].getName() + "\n";
				}
			}
			return strdir + strfi;
		}
		
		public static void Beep(double freq, final double millis) {
		    try {
		        final Clip clip = AudioSystem.getClip();
		        AudioFormat af = clip.getFormat();

		        if (af.getSampleSizeInBits() != 16) {
		            System.err.println("Weird sample size.  Dunno what to do with it.");
		            return;
		        }

		        //System.out.println("format " + af);

		        int bytesPerFrame = af.getFrameSize();
		        double fps = 11025;
		        int frames = (int)(fps * (millis / 1000));
		        frames *= 4; // No idea why it wasn't lasting as long as it should.

		        byte[] data = new byte[frames * bytesPerFrame];

		        double freqFactor = (Math.PI / 2) * freq / fps;
		        double ampFactor = (1 << af.getSampleSizeInBits()) - 1;

		        for (int frame = 0; frame < frames; frame++) {
		            short sample = (short)(0.5 * ampFactor * Math.sin(frame * freqFactor));
		            data[(frame * bytesPerFrame) + 0] = (byte)((sample >> (1 * 8)) & 0xFF);
		            data[(frame * bytesPerFrame) + 1] = (byte)((sample >> (0 * 8)) & 0xFF);
		            data[(frame * bytesPerFrame) + 2] = (byte)((sample >> (1 * 8)) & 0xFF);
		            data[(frame * bytesPerFrame) + 3] = (byte)((sample >> (0 * 8)) & 0xFF);
		        }
		        clip.open(af, data, 0, data.length);

		        // This is so Clip releases its data line when done.  Otherwise at 32 clips it breaks.
		        clip.addLineListener(new LineListener() {                
		            @Override
		            public void update(LineEvent event) {
		                if (event.getType() == Type.START) {
		                    Timer t = new Timer((int)millis + 1, new ActionListener() {
		                        @Override
		                        public void actionPerformed(ActionEvent e) {
		                            clip.close();
		                        }
		                    });
		                    t.setRepeats(false);
		                    t.start();
		                }
		            }
		        });
		        clip.start();
		    } catch (Exception ex) {
		        //System.err.println(ex);
		    }
		}
		
		public static class DeviceInfo
		{
			public static String IP;
			public static String OS;
			public static String Resolution;
			public static String ComputerName;
			public static String UserName;
			public static String Language;
			public static String TimeZone;
			public static String Architecture;
			public static String Country;
			public static String CountryCode;
			public static String HomeDirectory;
			public static String Region;
			public static String City;
			public static String Continent;
			public static String Latitude;
			public static String Longtitude;
			
			public DeviceInfo() {
				GeoLocate();
				OS();
				Resolution();
				UserName();
				ComputerName();
				Language();
				TimeZone();
				Architecture();
				HomeDirectory();
			}
			
			private static void IP() {
				//Backup Method To Retrieve IP If GeoPlugin.net Goes Down
				try {
					URL whatismyip;
					whatismyip = new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
					IP = in.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			
			private static void GeoLocate() {				
				try {
					URL whatismyip;
					whatismyip = new URL("http://www.geoplugin.net/json.gp");
					BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
					char[] buf = new char[8142];
					while (in.read(buf) != -1);
					JsonObject object = Json.parse((new String(buf)).trim()).asObject();
					IP = object.get("geoplugin_request").asString();
					City = object.get("geoplugin_city").asString();
					Region = object.get("geoplugin_regionName").asString();
					Country = object.get("geoplugin_countryName").asString();
					Continent = object.get("geoplugin_continentCode").asString();
					Latitude = object.get("geoplugin_latitude").asString();
					Longtitude = object.get("geoplugin_longitude").asString();
					CountryCode = object.get("geoplugin_countryCode").asString();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					IP();
					Country();
				}			
			}
			
			private static void OS() {
				OS = java.lang.System.getProperty("os.name");
			}
			
			private static void Resolution() {
				GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				int width = gd.getDisplayMode().getWidth();
				int height = gd.getDisplayMode().getHeight();
				Resolution = String.valueOf(width) + "x" + String.valueOf(height);
			}
			private static void UserName() {
				UserName = java.lang.System.getProperty("user.name");
			}
			
			private static void ComputerName() {
				String hostname = "Unknown";

				try
				{
				    InetAddress addr;
				    addr = InetAddress.getLocalHost();
				    hostname = addr.getHostName();
				}
				catch (UnknownHostException ex)
				{
				    
				}
				
				ComputerName = hostname;
			}
			
			private static void Language() {
				Language = java.lang.System.getProperty("user.language");
			}
			
			private static void TimeZone() {
				TimeZone = java.lang.System.getProperty("user.timezone");
			}
			
			private static void Architecture() {
				Architecture = java.lang.System.getProperty("sun.arch.data.model");
			}
			
			private static void HomeDirectory() {
				HomeDirectory = java.lang.System.getProperty("user.home");
			}
			
			private static void Country() {
				//Backup Method To Retrieve Country If GeoPlugin.net Goes Down
				Country = java.lang.System.getProperty("user.country");
			}
		}
	}
	
	public static class AV {
		
		public static void TextToSpeech (String str) {
			String voicename = "kevin16";
			VoiceManager voiceManager = VoiceManager.getInstance();
		    Voice voice = voiceManager.getVoice(voicename);
		    if (voice != null) {
		    	voice.allocate();
		    	voice.speak(str);
		    	voice.deallocate();
		    }
		}
	}

	public static class Net {
		
		public static void Visit(String WebURI) {
			if (Desktop.isDesktopSupported()) 
			{
			  try {
				Desktop.getDesktop().browse(URI.create(WebURI));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	}

	public static class Printing {
		
		public static void Print (String str) {
			JTextPane jtp = new JTextPane();
			jtp.setBackground(Color.white);
			jtp.setText(str);
			boolean show = false;
			try {
			    jtp.print(null, null, show, null, null, show);
			} catch (java.awt.print.PrinterException ex) {
			    ex.printStackTrace();
			}
		}
	}
}
