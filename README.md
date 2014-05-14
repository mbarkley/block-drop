Errai BlockDrop Demo
=====================

This demo app allows users to play a tetris-like game online with eachother. In the lobby players select other players and invite them to games, or join pre-existing games. In game rooms, opponents boards can be viewed by selecting them from the score menu. When a player scores a line while having an opponent selected, the opponent will gain a new line at the bottom of their board!

BlockDrop uses Errai Messaging and Errai CDI for Client-Server communication. Errai UI and Data Binding are used for menus in the lobby and game rooms. Errai Navigation is used to navigate to and from the lobby.

This demo is designed to work with a full Java EE 6 server such as JBoss EAP 6, JBoss AS 7, or Wildfly 8. Although it should be possible to craft a deployment of this demo to a simpler web server, it's much simpler to deploy to an EE 6 capable app server.

Prerequisites
-------------

 * Maven 3 (run `mvn --version` on the command line to check)
 * An unzipped copy of JBoss AS 7, EAP 6, or Wildfly 8.

Build and deploy (production mode)
----------------

To build a .war file and deploy it to the local running JBoss EAP 6 or AS 7 instance:

    % mvn clean package
    % cp target/demo-1.0-SNAPSHOT.war $JBOSS_HOME/standalone/deployments/demo.war
    % $JBOSS_HOME/bin/standalone.sh

Once the above commands have completed, you should be able to access the app at the following URL:

    http://localhost:8080/demo

Code and Refresh (development mode)
----------------

Note: The performance in Dev Mode is slower than production. For most applications this is not much of a burder, but the impact for this game is quite noticeable.

Using GWT DevMode, it is possible to instantly view changes to client code by simply refreshing the browser window. You should be able to start the demo in dev mode with this single command:

    % mvn clean gwt:run

When the GWT Dev Mode window opens, press "Launch Default Browser" to start the app.

To debug in development mode, set up two remote debuggers: one on port 8000 for the client-side code, and one on port 8001 for the server-side code. Then:
* Run `mvn clean gwt:debug`
* Start your server remote debugger
* Start your client remote debugger
* Press "Launch Default Browser"

Troubleshooting
---------------

Here are some resources that may help if you encounter difficulties:
* [Forum](https://community.jboss.org/en/errai)
* IRC : #errai @ freenode
