# chat_room

## Synopsis

Chat_Room was created by Matt Chedister and Adam Pham. It is a simple chat room application using RMI.

## Motivation

This project was created for course CSE 6392 Distributed Systems with Dr. Elmasri.

## Installation

This project was run through the Eclipse IDE on Linux.

## Instructions

1. In a new terminal, type the command "rmiregistry &" and hit enter.
2. In Eclipse, under the 'default' package, run serverMain.
3. Then under the 'client' package, run clientMain.
4. In the first dialog, the "Login" dialog. Click 'Register New User', which will take you
   to the "Register" dialog.
5. In the Register dialog, enter:
    a. Host IP Address: localhost
    b. Username: any
    c. Password: any
   And hit Register. This should automatically log you in and bring you to the ChatRoomList dialog.
   (If you would like to retest the login, please log out and try to log back in through the Login dialog.)
** If you are having a problem with connection in steps 4 or 5 please take the following steps:
	1. In a terminal, type sudo vi /etc/hosts and enter your password
	2. Modify the addresses for BOTH 'localhost' AND your computer name to match the IP address of the wireless network
		a. To find the IP address, type 'ifconfig' in a terminal and take note of the inet address beside 
		   'wlan1' or something similar.
	3. Now restart rmiregistry, server, and client applications and 'localhost' should now work.

6. On the ChatRoomList dialog, you have two options: Create or Join a chatroom. To Join, skip to step 9.
7. To Create a chatroom, click the Create button.
8. In the pop-up window, enter a new chatroom name, and hit OK.
9. To Join a chatroom, double-click the name in the chatroom list.
10. In the Chatroom dialog, type a message into the textfield and hit Send.
11. Click leave or close the window to leave the chatroom.
12. Click Logout to leave the server.

*Note: Remember to kill the rmiregistry process now running in the background using the steps below.
	1. Type ps -ax | grep rmiregistry, make note of the far left number, that's the pid.
	2. Type kill -9 [pid]
