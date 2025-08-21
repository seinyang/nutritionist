package sein.nutritionist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
public class Comment {

   @Id @GeneratedValue
   private Long id;

   private String content;

   @ManyToOne(fetch = FetchType.LAZY)
   private User author;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "post_id")
   private Post post;

   private LocalDateTime createAt;

}
