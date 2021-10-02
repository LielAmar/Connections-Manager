# Connections Manager
[![Discord](https://img.shields.io/discord/416652224505184276?color=%235865F2&label=Join%20My%20Discord)](https://discord.gg/NzgBrqR)

A centralized library for all of your connections.

## Instalation
You can install Connections Manager using Maven/Gradle:

### Maven
```xml
<repositories>
    <repository>
        <id>lielamar-repo</id>
        <url>https://repo.lielamar.com/repository/maven-public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.lielamar</groupId>
        <artifactId>ConnectionsManager</artifactId>
        <version>${version}</version>
    </dependency>
    
    <dependency>
        <groupId>com.lielamar.ConnectionsManager</groupId>
        <artifactId>common</artifactId>
        <version>${version}</version>
    </dependency>
    
    <dependency>
        <groupId>com.lielamar.ConnectionsManager</groupId>
        <artifactId>MongoDBManager</artifactId>
        <version>${version}</version>
    </dependency>
</dependencies>
```

### Gradle
```xml
repositories {
    lielamar-repo {
        url "https://repo.lielamar.com/repository/maven-public"
    }
}

dependencies {
    compile "com.lielamar:ConnectionsManager:${version}",
    compile "com.lielamar.ConnectionsManager:common:${version}",
    compile "com.lielamar.ConnectionsManager:MongoDBManager:${version}"
}
```

## Usage
Connections Manager comes with 2 built-in integrations for MongoDB and Redis.

### Adding a new Connection Integration
In order to create a new Connection Integration you need to import the `base project` and `common module`.

Create a class and extend `DatabaseConnection` if your connection is to a database, or `Connection` if it's a different type of connection.
In this class you need to implement multiple methods, most notably `getObjectByIdentifier`, `getAllObjects`, `saveObjectByIdentifier` and `deleteObjectByIdentifier`.
Those methods will handle everything related to your connection, and notice that they all use the `T_SerializableObject` type.

### The T_SerializableObject type
`T_SerializableObject` is used to define objects you want to be used in any connection.
It has 2 main methods: `write` & `read`. The former writes a T_SerializableDocument containing all information on the object, and returns it.
The latter receives an existing T_SerializableDocument and updates the local values of the object according to the given document.

Example object:
```java
public class TestPlayer extends T_SerializableObject {

    public String rank;
    public int coins;
    public int xp;

    public TestPlayer() { super(null); }
    public TestPlayer(String identifier) { super(identifier); }

    @Override
    protected void init() {
        this.rank = "Member";
        this.coins = 0;
        this.xp = 0;
    }

    @Override
    public @NotNull DatabaseType getDatabase() { return new DatabaseType("Test"); }

    @Override
    public @NotNull CollectionType getCollection() { return new CollectionType("Test"); }

    @Override
    public @NotNull SystemType getSystem() { return new SystemType("GLOBAL"); }

    @Override
    public @NotNull T_SerializableDocument write() {
        T_SerializableDocument document = new T_SerializableDocument();
        document.put("rank", this.rank);
        document.put("coins", this.coins);
        document.put("xp", this.xp);
        
        return document;
    }

    @Override
    public void read(@Nullable T_SerializableDocument document) {
        if(document == null) return;
        
        this.rank = document.getOrDefault("rank", this.rank);
        this.coins = document.getOrDefault("coins", this.coins);
        this.xp = document.getOrDefault("xp", this.xp);
    }
```

All connections use T_SerializableObject's `read` and `write` methods to save and load data from the connection, but it is not limited to only that.
One usecase can be sending SerializableObjects through Web Sockets in certain requests.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[GPL 3](https://choosealicense.com/licenses/agpl-3.0/)
