package model.PostModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;
import lombok.*;
import model.UserModel.UserLocation;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class PostRequest {


    private String text;
    private String image;
    private int likes;
    private String[] tags;
    private String owner;

    public static PostRequest createPost(){
        Faker faker = new Faker(new Locale("en-US"));

        String[] tag = new  String[3];
        for(int i=0; i<tag.length; i++){
            tag[i] = faker.name().firstName();
        }

        PostRequest post = PostRequest.builder()
                .text(faker.lorem().sentence())
                .image(faker.internet().image(faker.number().randomDigitNotZero(),faker.number().randomDigitNotZero(),faker.random().nextBoolean(),faker.number().randomDigitNotZero()+".jpg"))
                .likes(faker.number().numberBetween(1, 100))
                .tags(tag)
                .owner("60d0fe4f5311236168a109f7")
                .build();

        return post;
    }

}
