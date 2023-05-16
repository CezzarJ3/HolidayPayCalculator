package neoflex.julia.task.holidaypaycalculator.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayDto {
    private String name;

    @JsonProperty("date")
    private DateDto dateDto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateDto getDateDto() {
        return dateDto;
    }

    public void setDateDto(DateDto dateDto) {
        this.dateDto = dateDto;
    }
}
