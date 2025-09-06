package io.akhilennu.chatbot.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.akhilennu.chatbot.domain.ResponseData} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResponseDataDTO implements Serializable {

    private Long id;

    private String type;

    @Lob
    private String content;

    private String channelName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponseDataDTO)) {
            return false;
        }

        ResponseDataDTO responseDataDTO = (ResponseDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, responseDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResponseDataDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", channelName='" + getChannelName() + "'" +
            "}";
    }
}
