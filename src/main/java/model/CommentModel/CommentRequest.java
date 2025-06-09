package model.CommentModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;
import lombok.*;

import java.util.Locale;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class CommentRequest {

    private String message;
    private String owner;
    private String post;

    public static CommentRequest createComment(){
        Faker faker = new Faker(new Locale("en-US"));

        int minLength = 2;
        int maxLength = 500;

        StringBuilder text = new StringBuilder();

        // Keep appending random sentences until we reach or exceed maxLength
        while (text.length() < maxLength) {
            text.append(faker.lorem().sentence()).append(" ");
        }

        // Trim to a random length between min and max
        int finalLength = faker.number().numberBetween(minLength, maxLength);
        String result = text.substring(0, finalLength).trim();
        System.out.println("Rezultat: " + result);


        CommentRequest comment = CommentRequest.builder()
                .message(result)
                .owner("60d0fe4f5311236168a109f7")
                .post("60d21b2c67d0d8992e610c37")
                .build();

        return comment;
    }

}
