package lsit.Repositories;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import lsit.Models.Car;

@Primary
@Repository
public class S3CarRepository implements IntCarRepository{
    
    final String BUCKET="clowncarinc";
    final String PREFIX="cc.inc/Cars/";
    final String ACCESS_KEY="GOOG1E35DFGSG6AJPOJAU5GGA3RU2SZYOTWW3FMNH5SFSCAF745Z4BVTVFRKI";
    final String SECRET_KEY="geBCBQhU1bcAH4KlOtwuYfVM0KNSFRvGrFh3eu60";
    final String ENDPOINT_URL="https://storage.googleapis.com";

    S3Client s3client;
    AwsBasicCredentials awsCredentials;

    public S3CarRepository(){
        awsCredentials = AwsBasicCredentials.create(ACCESS_KEY,SECRET_KEY);
        s3client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .endpointOverride(URI.create(ENDPOINT_URL))
            .region(Region.of("auto"))
            .build();
    }

    public void add(Car p){
        try{
            ObjectMapper om = new ObjectMapper();

            String carJson = om.writeValueAsString(p);

            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX+ p.id.toString())
                .build(),
                RequestBody.fromString(carJson)    
            );
        }
        catch(JsonProcessingException e){}    
    }

    public Car get(UUID id){
        try{
            var objectBytes = s3client.getObject(GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX+ id.toString())
                .build()
            );

            ObjectMapper om = new ObjectMapper();
            Car p = om.readValue(objectBytes, Car.class);
            return p;
        }
        catch(Exception e){
            return null;
        }
    }

    public void remove(UUID id){
        try{
            s3client.deleteObject(DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX+ id.toString())
                .build()
            );
        }
        catch(Exception e){
        }
    }

    public void update(Car p){
        try{
            
            Car x = this.get(p.id);
            if(x == null) return;

            ObjectMapper om = new ObjectMapper();
            String carJson = om.writeValueAsString(p);
            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX+ p.id.toString())
                .build(),
                RequestBody.fromString(carJson)    
            );
        }
        catch(JsonProcessingException e){}
    }

    public List<Car> list(){
        List<Car> cars = new ArrayList<Car>();
        List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
        .bucket(BUCKET)
        .prefix(PREFIX)
        .build()
        ).contents();

        for(S3Object o : objects){
            Car p = new Car();
            p.id = UUID.fromString(o.key().substring(PREFIX.length()));
            cars.add(p);
        }

        return cars;
    }

}
