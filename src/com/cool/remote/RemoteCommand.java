package com.cool.remote;

public class RemoteCommand {
private static String _code = "";
private static byte[] _content;

public void Command (String str, byte[] bytes)
{
	_code = str;
	_content = bytes;
}

public void Execute()
{
	switch(_code)
	{
	case "SCR":
		//screenshot //Done
		break;
	case "SUI":
		//start up info //Done
		break;
	case "FIL":
		//file //Done
		break;
	case "DIR":
		//directory info //Done
		break;
	case "DOS":
		//dos command
		break;
	case "BEP":
		//beep //Done
		break;
	case "INS":
		//install
		break;
	case "UPD":
		//update
		break;
	case "UNI":
		//uninstall
		break;
	case "PRI":
		//print //done
		break;
	case "DIA":
		//dialog //Done
		break;
	case "CHT":
		//chat
		break;
	case "CAM":
		//camera //Done
		break;
	case "WEB":
		//website //Done
		break;
	case "CON":
		//control //Done
		break;
	case "PWR":
		//shutdown //Done
		break;
	case "REB":
		//reboot //Done
		break;
	case "SPE":
		//speak //Done
		break;
	}
}

}
