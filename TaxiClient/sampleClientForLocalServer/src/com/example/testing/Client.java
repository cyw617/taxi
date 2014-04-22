package com.example.testing;

public class Client
{
	Client(String ClientID, String lon, String lat, String info)
	{
		this.ClientID = ClientID;
		this.lon = lon;
		this.lat = lat;
		this.info = info;
		
	
	}
	
	public String toString()
	{	
		return ClientID+" "+lon+" "+lat+" "+info+"\n";
	}

	
	
	String ClientID;
	String lon;
	String lat;
	String info;

}
