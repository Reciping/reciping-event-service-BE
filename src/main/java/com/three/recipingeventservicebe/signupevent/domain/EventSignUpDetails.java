package com.three.recipingeventservicebe.signupevent.domain;

import com.three.recipingeventservicebe.fefsevent.domain.Reward;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_sign_up_details")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventSignUpDetails {

    @Id
    private String eventId;

    private Reward reward;
    private Instant buttonActivatedAt;
    private Instant createdAt;
    private Instant modifiedAt;
    private boolean isDeleted;

    public void setButtonActivatedAt(Instant activatedAt) {
        this.buttonActivatedAt = activatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

}
