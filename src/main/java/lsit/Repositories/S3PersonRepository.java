package lsit.Repositories;

import java.net.URI;
import java.util.*;

import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lsit.Models.Person;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;



@Primary
@Repository
public class S3PersonRepository implements IntPersonRepository {
    final String BUCKET="lsit-example-bucket";
    final String PREFIX="cc.inc/Person/";
    final String ACCESS_KEY="GOOG1EM4BODFHSWVPEFXA3FWS3TEG6CIL5RZ7WHQ3QN66MMUF5VKVAPAXYZCH";
    final String SECRET_KEY="v/8XBIN27gtNjdRcK5ffIUcioMVyJur5UG7hPy24";
    final String ENDPOINT_URL="https://storage.googleapis.com";

    S3Client s3client;
    AwsCredentials awsCredentials;
    
    public S3PersonRepository(){
        awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        s3client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .endpointOverride(URI.create(ENDPOINT_URL))
            .region(Region.of("auto"))
            .build();
    }

    public void add(Person p){
        try{
            p.id = UUID.randomUUID();

            ObjectMapper om = new ObjectMapper();

            String personJson = om.writeValueAsString(p);
            
            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + p.id.toString())
                .build(),
                RequestBody.fromString(personJson)
            );
        }
        catch(JsonProcessingException e){}
    }

    public Person get(UUID id){
        try{
            var objectBytes = s3client.getObject(GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + id.toString())
                .build()
            ).readAllBytes();

            ObjectMapper om = new ObjectMapper();
            Person p = om.readValue(objectBytes, Person.class);

            return p;
        }catch(Exception e){
            return null;
        }
    }

    public void remove(UUID id){
        s3client.deleteObject(DeleteObjectRequest.builder()
            .bucket(BUCKET)
            .key(PREFIX + id.toString())
            .build()
        );  
    }

    public void update(Person p){
        try{
            Person x = this.get(p.id);
            if(x == null) return;

            ObjectMapper om = new ObjectMapper();
            String petJson = om.writeValueAsString(p);
            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + p.id.toString())
                .build(),
                RequestBody.fromString(petJson)
            );
        }
        catch(JsonProcessingException e){}
    }

    public List<Person> list(){
        List<Person> person = new ArrayList<Person>();
        List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
          .bucket(BUCKET)
          .prefix(PREFIX)
          .build()  
        ).contents();

        for(S3Object o : objects){
            Person p = new Person();
            //p = this.get(UUID.fromString(o.key().substring(PREFIX.length())));
            p.id = UUID.fromString(o.key().substring(PREFIX.length()));
            person.add(p);
        }

        return person;
    }
}