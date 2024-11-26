package lsit.Repositories.Services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import lsit.Models.Car;
import lsit.Models.Clown;

@Primary
@Repository
public class S3ClownService implements IntClownService {
    final String BUCKET = "lsit-example-bucket";
    final String PREFIX = "cc.inc/Clown/";
    final String ACCESS_KEY = "GOOG1EM4BODFHSWVPEFXA3FWS3TEG6CIL5RZ7WHQ3QN66MMUF5VKVAPAXYZCH";
    final String SECRET_KEY = "v/8XBIN27gtNjdRcK5ffIUcioMVyJur5UG7hPy24";
    final String ENDPOINT_URL = "https://storage.googleapis.com";

    S3Client s3client;
    AwsBasicCredentials awsCredentials;
    
    public S3ClownService(){
        awsCredentials = AwsBasicCredentials.create(ACCESS_KEY,SECRET_KEY);
        s3client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .endpointOverride(URI.create(ENDPOINT_URL))
            .build();
    }
    
    public Clown get(UUID id){
        try{
        var objectBytes = s3client.getObject(GetObjectRequest.builder()
        .bucket(BUCKET)
        .key(PREFIX+ id.toString())
        .build()
        );

        ObjectMapper om = new ObjectMapper();
        Clown clown = om.readValue(objectBytes, Clown.class);
        return clown;
        }
        catch(Exception e){
            return null;
        }
    }
    
    public List<Clown> list(){
        List<Clown> clowns = new ArrayList<Clown>();
        List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
        .bucket(BUCKET)
        .prefix(PREFIX)
        .build()
        ).contents();

        for(S3Object o : objects){
            Clown clown = new Clown();
            clown.id = UUID.fromString(ACCESS_KEY);
            clowns.add(clown);
        }

        return clowns;
    }

   
    @Override
    public void add(Clown p) {
        try{
            ObjectMapper om = new ObjectMapper();

            String clownJson = om.writeValueAsString(p);

            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX+ p.id.toString())
                .build(),
                RequestBody.fromString(clownJson)    
            );
        }
        catch(JsonProcessingException e){}  
    }

    @Override
    public void update(Clown p) {
        try{
            
            Clown x = this.get(p.id);
            if(x == null) return;

            ObjectMapper om = new ObjectMapper();
            String clownJson = om.writeValueAsString(p);
            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX+ p.id.toString())
                .build(),
                RequestBody.fromString(clownJson)    
            );
        }
        catch(JsonProcessingException e){}
    }

    @Override
    public void remove(UUID id) {
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

    
    




}
