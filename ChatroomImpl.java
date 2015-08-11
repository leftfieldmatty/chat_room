import iface.Chatroom;
import iface.ClientCallbackInterface;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//public class ChatroomImpl
//this class keeps track of all users and rooms, 
//which users are online and in which room.
public class ChatroomImpl extends UnicastRemoteObject implements Chatroom
{
	private List currentClients; // list of all client users
	private List currentRooms;   // list of all chatrooms
	private Iterator userIterator; // iterator that's used to go through the client users
	private Iterator roomIterator; // iterator that's used to go through the rooms
	
	//public ChatroomImpl
	//constructor, reads in the initial XML files and sets up the 
	//client and room lists
	public ChatroomImpl() throws RemoteException
	{
		try
        {
            Registry registry = LocateRegistry.createRegistry(4446);
            registry.rebind("ChatroomTest", this);
            
            initializeClientList();
            initializeRoomList();
        }
        catch (Exception e)
        {
            System.out.println("****SERVER  ChatroomImpl err: " + e.getMessage());
            e.printStackTrace();
        } 
		printAll();
	}
	
	//initializeClientList
	//reads in the clients.xml file and constructs the currentClients list
	public void initializeClientList()
	{
		try 
		{ 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
		
			builder = factory.newDocumentBuilder();

			Document chatDoc = null;
			chatDoc = (Document) builder.parse(new File ("clients.xml"));

			currentClients = new ArrayList();
			chatDoc.getDocumentElement().normalize();
			NodeList userList = chatDoc.getElementsByTagName("user");
			for (int counter = 0; counter < userList.getLength(); ++counter)
			{
				Node node = userList.item(counter);
				Element element = (Element) node;
        	
				NodeList nameNodes = element.getElementsByTagName("name").item(0).getChildNodes();
				Node nameNode = (Node) nameNodes.item(0);
        	
				NodeList pwNodes = element.getElementsByTagName("password").item(0).getChildNodes();
				Node pwNode = (Node) pwNodes.item(0);
				
				ClientUser tempClient = new ClientUser(nameNode.getNodeValue(), pwNode.getNodeValue());
				currentClients.add(tempClient);
			}
		}
		catch (ParserConfigurationException e1) 
		{
			e1.printStackTrace();
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//initializeRoomList
	//reads in the clients.xml file and constructs the currentRooms list
	public void initializeRoomList()
	{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
		try 
		{
			builder = factory.newDocumentBuilder();

			Document chatDoc;
			chatDoc = (Document) builder.parse(new File ("rooms.xml"));

			currentRooms = new ArrayList();
			chatDoc.getDocumentElement().normalize();
			NodeList roomList = chatDoc.getElementsByTagName("room");
			for (int counter = 0; counter < roomList.getLength(); ++counter)
			{
				Node node = roomList.item(counter);
				Element element = (Element) node;
        	
				NodeList nameNodes = element.getElementsByTagName("name").item(0).getChildNodes();
				Node nameNode = (Node) nameNodes.item(0);
        	
				ClientRoom tempRoom = new ClientRoom(nameNode.getNodeValue());
				currentRooms.add(tempRoom);  	
			}
		}
		catch (ParserConfigurationException e1) 
		{
			e1.printStackTrace();
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	//addUser
	//takes in a username and password and saves it to the CurrentClients list
	//also calls everyone's doAddUserCB function
	public int addUser(String userName, String password)
	{   
	   ClientUser newUser;
	   try {
			newUser = new ClientUser(userName, password);
	
		   userIterator = currentClients.iterator();
		   while(userIterator.hasNext())
		   {
			   
			   ClientUser cUser = (ClientUser)userIterator.next();
			   if (cUser.getName().equals(userName))
			   {
				   return -1;
			   }
		   }
		   newUser.setOnline(true);
		   currentClients.add(newUser);
		   writeUsersXML();
		} 
		catch (RemoteException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printAll();
		return 1;
	}
	
	//addUser
	//takes in a room name and saves it to the CurrentRooms list
	//also calls everyone's doAddRoomCB function
	public int addRoom(String roomName) throws RemoteException
	{
		
		ClientRoom newRoom = new ClientRoom(roomName);
		   roomIterator = currentRooms.iterator();
		   while(roomIterator.hasNext())
		   {
			   ClientRoom cRoom = (ClientRoom)roomIterator.next();
			   if (cRoom.getName().equals(roomName))
			   {
				   return -1;
			   }
		   }
		
		currentRooms.add(newRoom);
		writeRoomsXML();
		
		userIterator = currentClients.iterator();
		   while(userIterator.hasNext())
		   {
			   ClientUser cUser = (ClientUser)userIterator.next();
			   if (cUser.getOnline())
			   {
				   cUser.doAddRoomCB(roomName);
			   }
		   }
		   return 1;
	}
	
	//removeUser
	//takes in a userName to remove, and removes it from the currentClients list
	//also calls userRoomLeave on all rooms, and doRemoveUserCB for all active clients
	public void removeUser(String userName) throws RemoteException
	{
		//remove individual user
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
			ClientUser cUser = (ClientUser)userIterator.next();
			if (cUser.getName().equals(userName))
			{
				roomIterator = currentRooms.iterator();
				while (roomIterator.hasNext())
				{
					ClientRoom tempRoom = (ClientRoom)roomIterator.next();
					userRoomLeave(userName, tempRoom.getName() );
				}
				userIterator.remove();
				break;
			}
		}
		
		//announce via CBs that the user is gone
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
			ClientUser cUser = (ClientUser)userIterator.next();
			if (cUser.getOnline())
			{
				   cUser.doRemoveUserCB(userName);
			}
		}
		writeUsersXML();
	}
	
	//removeRoom
	//removes the inputed roomName from the currentRooms list
	//also calls all clients' doRemoveRoomCB functions
	public void removeRoom(String roomName) throws RemoteException
	{
		//go through the subject room and tell everyone to get out
		roomIterator = currentRooms.iterator();
		while(roomIterator.hasNext())
		{
			ClientRoom cRoom = (ClientRoom)roomIterator.next();
			if (cRoom.getName().equals(roomName))
			{
				List roomClients = cRoom.getUsers();
				if(roomClients != null)
				{
					userIterator = roomClients.iterator();
					while(userIterator.hasNext())
					{
						ClientUser cUser = (ClientUser)userIterator.next();
						if(cUser.getOnline())
						{
							cUser.doLeaveRoomCB(cUser.getName(),roomName);
						}
						
					}
				}
				roomIterator.remove();
			}//end if match found
		}
		
		//go through all active users and call the remove room CB
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
			ClientUser cUser = (ClientUser)userIterator.next();
			if (cUser.getOnline())
			{
				   cUser.doRemoveRoomCB(roomName);
			}
		}
		
		writeRoomsXML();
	}
	
	//message
	//takes in the message, the target room, and the user who sent the message
	//this function calls the messageCB for all clients in the room, except for the originating client
	public void message(String incomingMsg, String roomName, String userName) throws RemoteException
	{
		System.out.println("****SERVER  inside message, incomingMsg is " + incomingMsg + " roomName is " + roomName + " userName is " + userName);
		roomIterator = currentRooms.iterator(); 
		while(roomIterator.hasNext())
		{
			ClientRoom cRoom = (ClientRoom)roomIterator.next();
			if (cRoom.getName().equals(roomName))
			{
				List roomClients = cRoom.getUsers();
				System.out.println(roomClients);
				if(roomClients != null)
				{
					Iterator clientIterator = roomClients.iterator();
					while(clientIterator.hasNext())
					{
						String clientName = (String) clientIterator.next();
						userIterator = currentClients.iterator();
						while(userIterator.hasNext())
						{
							ClientUser cUser = (ClientUser) userIterator.next();						
							
							if((cUser.getName().equals(clientName)) && !(cUser.getName().equals(userName)))
							{
								System.out.println("****SERVER  sending doMessageCB with msg to " + incomingMsg + " to user " + cUser.getName() + " to room " + cRoom.getName());
								cUser.doMessageCB(incomingMsg, cRoom.getName());
							}
							else
							{
								System.out.println("Not sending message to " + cUser.getName());
							}
						}
					}
				}
			}
		}
	}
	
	//userRoomJoin
	//takes in a user to join to a room
	//adds the user to the room's list, and calls all users' doJoinRoomCB functions
	public void userRoomJoin(String userName, String roomName) throws RemoteException
	{
		System.out.println("****SERVER  insdie userRoomJoin, userName is " + userName + "roomName is " + roomName);
		boolean userExists = false;
		roomIterator = currentRooms.iterator();
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
			ClientUser cUser = (ClientUser)userIterator.next();
			if(cUser.getName().equals(userName))
			{
				userExists = true;
				break;
			}
		}
		if(userExists)
		{
			while(roomIterator.hasNext())
			{
				ClientRoom cRoom = (ClientRoom)roomIterator.next();
				if (cRoom.getName().equals(roomName))
				{
					cRoom.addUser(userName);
					userIterator = currentClients.iterator();
					while(userIterator.hasNext())
					{
						ClientUser cUser = (ClientUser) userIterator.next();
						if(cUser.getOnline())
						{
							cUser.doJoinRoomCB(userName, roomName);
							cUser.doUserJoinRoomCB(userName, roomName);
						}
					}
				}
			}
		}
		printAll();
	}
	
	//userRoomLeave
	//takes a user and a room, and removes the user from the room
	//calls all users' doLeaveRoomCB functions
	public void userRoomLeave(String userName, String roomName) throws RemoteException
	{
		boolean userExists = false;
		boolean userInRoom = false;
		ClientRoom tempRoom = new ClientRoom("junk");
		ClientUser tempUser = new ClientUser("junk1", "junk2");
		roomIterator = currentRooms.iterator();
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
			ClientUser cUser = (ClientUser)userIterator.next();
			if(cUser.getName().equals(userName))
			{
				userExists = true;
				tempUser = cUser;
				break;
			}
		}
		
		while(roomIterator.hasNext())
		{
			ClientRoom cRoom = (ClientRoom)roomIterator.next();
			if (cRoom.getName().equals(roomName))
			{
				List roomClients = cRoom.getUsers();
				if (roomClients != null)
				{
					userIterator = roomClients.iterator();
					while(userIterator.hasNext())
					{
						String tempUser1 = (String) userIterator.next();
						if(tempUser1.equals(userName))
						{
							userInRoom = true;
							tempRoom = cRoom;
						}
					}
				}
			}
		}
		
		if(userExists && userInRoom)
		{
			tempRoom.removeUser(tempUser.getName());
			
			userIterator = currentClients.iterator();
			while(userIterator.hasNext())
			{
				ClientUser cUser = (ClientUser) userIterator.next();
				if(cUser.getOnline())
				{
					cUser.doLeaveRoomCB(userName, roomName);
				}
			}
		}
		printAll();
	}
	
	//verifyUser
	//takes in a username and a password, and checks it against the XML file
	//returns 1 if successful, 2 for an incorrect password, and -1 for user not found
	public int verifyUser(String userName, String password)
	{
		userIterator = currentClients.iterator();
		int returnVal = -1;
		
		while(userIterator.hasNext())
		{
		  ClientUser cUser = (ClientUser) userIterator.next();
		  if (cUser.getName().equals(userName))
		  {
			  if(cUser.getPW().equals(password))
			  {
				  cUser.setOnline(true);
				  returnVal = 1;
				  break;
			  }
			  else
			  {
				  returnVal = 2;
				  break;
			  }
		  }
		}
		printAll();
		return returnVal;
	}
	
	//signOutUser
	//called when a client logs off
	public void signOutUser(String userName) throws RemoteException
	{
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
			ClientUser cUser = (ClientUser)userIterator.next();
			if (cUser.getName().equals(userName))
			{
				roomIterator = currentRooms.iterator();
				while (roomIterator.hasNext())
				{
					ClientRoom tempRoom = (ClientRoom)roomIterator.next();
					userRoomLeave(userName, tempRoom.getName() );
				}
				cUser.setOnline(false);
				break;
			}
		}
		printAll();
	}
	
	//getInitialClientsAndRooms
	//is called by a user who just signed in
	//will call that users callbacks to populate the rooms and other users
	public void getInitialClientsAndRooms(String userName)
	{ 
		try {
			ClientUser cUser = null;
			userIterator = currentClients.iterator();
			while(userIterator.hasNext())
			{
			  cUser = (ClientUser) userIterator.next();
			  if(cUser.getName().equals(userName))
			  { 
				  break;
			  }
			}
			if(cUser != null)
			{
				roomIterator = currentRooms.iterator();
				while(roomIterator.hasNext())
				{
					ClientRoom cRoom = (ClientRoom) roomIterator.next();
					cUser.doAddRoomCB(cRoom.getName());
				}
			}
		}
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
	}
	
	//requestRoomUsers
	//gets all users in a classroom and broadcasts that info out
	public void requestRoomUsers(String userName, String roomName)
	{
		ClientUser cUser = null;
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
		  cUser = (ClientUser) userIterator.next();
		  if(cUser.getName().equals(userName))
		  { 
			  break;
		  }
		}
		if(cUser != null)
		{
			roomIterator = currentRooms.iterator();
			while(roomIterator.hasNext())
			{
				ClientRoom tempRoom = (ClientRoom)roomIterator.next();
				if(tempRoom.getName().equals(roomName))
				{
					userIterator = tempRoom.getUsers().iterator();
					while(userIterator.hasNext())
					{
						try {
							cUser.doUserJoinRoomCB((String)userIterator.next(),tempRoom.getName());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						
				}
			}
		}
	}
	
	//registerCBs
	//takes a userName and a callback interface object and registers them together
	public void registerCBs(String userName, ClientCallbackInterface CBI) throws RemoteException//messageCB messageCBObj, addUserCB addUserCBObj, removeUserCB removeUserCBObj, addRoomCB addRoomCBObj, removeRoomCB removeRoomCBObj, joinRoomCB joinRoomCBObj, leaveRoomCB leaveRoomCBObj)
	{
		userIterator = currentClients.iterator();
		while(userIterator.hasNext())
		{
		  ClientUser cUser = (ClientUser) userIterator.next();
		  if(cUser.getName().equals(userName))
		  {
			cUser.registerForClientCB(CBI);
			break;
		  }
		}
	}
	
	//writeUsersXML
	//this fucntion writes the current set of users to the clients.xml file
	public void writeUsersXML()
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Clients");
			doc.appendChild(rootElement);
			
			userIterator = currentClients.iterator();
			while(userIterator.hasNext())
			{
			  ClientUser cUser = (ClientUser) userIterator.next();
				Element client = doc.createElement("user");
				rootElement.appendChild(client);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(cUser.getName()));
				client.appendChild(name);
				
				Element pw = doc.createElement("password");
				pw.appendChild(doc.createTextNode(cUser.getPW()));
				client.appendChild(pw);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("clients.xml"));
	 
			transformer.transform(source, result);

			printAll();
			
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} 
		catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
		} 
		catch (TransformerException e) 
		{
			e.printStackTrace();
		}	
	}
	
	//writeRoomsXML
	//this function writes all the current rooms to the rooms.xml file
	public void writeRoomsXML()
	{
		try
		{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Rooms");
			doc.appendChild(rootElement);
			
			roomIterator = currentRooms.iterator();
			while(roomIterator.hasNext())
			{
			  ClientRoom cRoom = (ClientRoom) roomIterator.next();
				Element room = doc.createElement("room");
				rootElement.appendChild(room);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(cRoom.getName()));
				room.appendChild(name);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("rooms.xml"));
	 
			transformer.transform(source, result);

			printAll();
		
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} 
		catch (TransformerConfigurationException e) 
		{
			e.printStackTrace();
		} 
		catch (TransformerException e) 
		{
			e.printStackTrace();
		}
	}
	
	//printAll
	//this function is used for debugging and prints all users and rooms to the standard out
	public void printAll()
	{
		System.out.println("###############################");
		userIterator = currentClients.iterator();
		System.out.println("CLIENTS ");
		while(userIterator.hasNext())
		{
			ClientUser cUser = (ClientUser)userIterator.next();
			System.out.println("NAME is " + cUser.getName());
			System.out.println("PASSWORD is " + cUser.getPW());
			if (cUser.getOnline() == true)
			{
				System.out.println("User is online");
			}
			else
			{
				System.out.println("User is offline");
			}
		}
		System.out.println("\n ROOMS");
		roomIterator = currentRooms.iterator();
		while(roomIterator.hasNext())
		{
			ClientRoom cRoom = (ClientRoom)roomIterator.next();
			System.out.println("NAME is " + cRoom.getName());
			List roomClients = cRoom.getUsers();
			//System.out.println(roomClients);
			if(roomClients != null)
			{
				Iterator subIterator = roomClients.iterator();
				System.out.println("USERS IN THIS ROOM ARE");
				while(subIterator.hasNext())
				{
					System.out.println("USER " + (String)subIterator.next());
				}
			}
			else
			{
				System.out.println("NO USERS IN THIS ROOM");
			}
		}
		
		System.out.println("###############################");
	}
	
}
