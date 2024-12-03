package lsit.Repositories;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import lsit.Models.Clown;

@Primary
@Repository
public class S3ClownRepository implements IntClownRepository{
    final String BUCKET="clowncarinc";
    final String PREFIX="cc.inc/Clowns/";
    final String ACCESS_KEY="GOOG1E35DFGSG6AJPOJAU5GGA3RU2SZYOTWW3FMNH5SFSCAF745Z4BVTVFRKI";
    final String SECRET_KEY="geBCBQhU1bcAH4KlOtwuYfVM0KNSFRvGrFh3eu60";
    final String ENDPOINT_URL="https://storage.googleapis.com";

    S3Client s3client;
    AwsCredentials awsCredentials;

    public S3ClownRepository() {
        awsCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        s3client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .endpointOverride(URI.create(ENDPOINT_URL))
            .region(Region.of("auto"))
            .build();
    }

    public void add(Clown p){
        try{
            p.id = UUID.randomUUID();

            ObjectMapper om = new ObjectMapper();

            String clownJson = om.writeValueAsString(p);
            
            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + p.id.toString())
                .build(),
                RequestBody.fromString(clownJson)
            );
        }
        catch(JsonProcessingException e){}
    }

    public Clown get(UUID id){
        try{
            var objectBytes = s3client.getObject(GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + id.toString())
                .build()
            ).readAllBytes();

            ObjectMapper om = new ObjectMapper();
            Clown p = om.readValue(objectBytes, Clown.class);

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

    public void update(Clown p){
        try{
            Clown x = this.get(p.id);
            if(x == null) return;

            ObjectMapper om = new ObjectMapper();
            String clownJson = om.writeValueAsString(p);
            s3client.putObject(PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(PREFIX + p.id.toString())
                .build(),
                RequestBody.fromString(clownJson)
            );
        }
        catch(JsonProcessingException e){}
    }


    public List<Clown> list(){
        List<Clown> clowns = new ArrayList<Clown>();
        List<S3Object> objects = s3client.listObjects(ListObjectsRequest.builder()
          .bucket(BUCKET)
          .prefix(PREFIX)
          .build()  
        ).contents();

        for(S3Object o : objects){
            Clown p = new Clown();
            //p = this.get(UUID.fromString(o.key().substring(PREFIX.length())));
            p.id = UUID.fromString(o.key().substring(PREFIX.length()));
            clowns.add(p);
        }

        return clowns;
    }
}
