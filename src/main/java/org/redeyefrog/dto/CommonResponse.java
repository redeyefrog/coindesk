package org.redeyefrog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CommonResponse {

    @JsonProperty("RESULT_DESC")
    private String resultDesc;

}
