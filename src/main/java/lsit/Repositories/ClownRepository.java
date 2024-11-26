package lsit.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

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

@Repository
public class ClownRepository implements IntClownRepository{
    static HashMap<UUID, Clown> clowns = new HashMap<>();

    public void add(Clown p){
        clowns.put(p.id, p);
    }

    public Clown get(UUID id){
        return clowns.get(id);
    }

    public void remove(UUID id){
        clowns.remove(id);
    }

    public void update(Clown p){
        Clown x = clowns.get(p.id);
        x.kind = p.kind;
        x.name = p.name;
    }

    public List<Clown> list(){
        return new ArrayList<>(clowns.values());
    }
}
