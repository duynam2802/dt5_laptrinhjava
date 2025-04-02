package ut.edu.childgrowth.models;

import jakarta.persistence.*;

//ĐÁNH GIÁ

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @Column(name = "userId", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Integer rating;

    private String comment;

    public Feedback() {
    }

    public Feedback(Long userId, Integer rating, String comment) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    public void submitFeedback(Integer rating, String comment) {
//        this.rating = rating;
//        this.comment = comment;
//    }
//
//    public String getFeedback() {
//        return "Feedback ID: " + feedbackId + ", Rating: " + rating + ", Comment: " + comment;
//    }
}