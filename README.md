## Java-Networking - Inactive
Java library for network communication.

Efforts have been moved towards the new project, [JStitch](https://github.com/Jibini-Media/jstitch).

One of the most frustrating parts of programming in Java is when you approach communication on a network.  A lot of the systems are confusing, and you really need to know what you are doing.  However, with this library, it should be a lot simpler and easy.  There are two basic goals for this project; goal one is to create a useful and well made API for network communication.  The other is to make the method of communication as efficient and simple as possible.

### Multithreaded Performance
You just dropped a wad of cash on a new processor, but you struggle to use all of its shiny new cores.  Sub-server systems are designed to spread the load of handling connections to clients on several different handlers in different threads.  This allows simultaneous traffic to multiple clients in a more efficient manor.

### Thread Sharing
Using multiple threads for processing data is usually a better way of handling a workload, however the overhead required to manage a multitude of threads may outweigh the gains.  Rather than creating many threads for each individual connection, as other networking libraries do, this library aims to allow connections to be hosted on common threads.

### Server and Client
Separate server classes and client connections added for easy serving to several clients.  Easily handle packets received from the opposite end with custom listeners, and use the input to bring your application to the next level.  If you're in the mood, run several servers or maintain several client connections in one application.

### Local Client Discovery
Heartbeats and listeners allow for two applications running on a local network to detect each other and connect.  Possible use cases include LAN multiplayer games or any application that needs to detect another instance on a local network.  Quickly create a utility that automatically searches your network for running servers for easy connecting.
