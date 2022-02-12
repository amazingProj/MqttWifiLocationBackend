package Model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

class DBHandler implements RestAPI {
    public DBHandler(){
        ConnectionString connectionString = new ConnectionString("mongodb+srv://Asaf:<password>@indoornavigtiondb.pb6oq.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("test");

    }

    @Override
    public void update(){}

    @Override
    public void delete(){}

    @Override
    public void get() {

    }

    @Override
    public void create() {

    }

}