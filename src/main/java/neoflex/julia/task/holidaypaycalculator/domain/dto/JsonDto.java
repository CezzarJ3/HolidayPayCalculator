package neoflex.julia.task.holidaypaycalculator.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDto {
    @JsonProperty("response")
    private ResponseDto responseDto;

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }
}
