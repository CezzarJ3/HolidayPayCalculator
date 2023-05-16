package neoflex.julia.task.holidaypaycalculator.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DateDto {
    @JsonProperty("iso")
    private String isoDate;

    @JsonIgnore
    private LocalDate jDate;

    public String getIsoDate() {
        return isoDate;
    }

    public DateDto setIsoDate(String isoDate) {
        this.isoDate = isoDate;
        jDate = LocalDate.parse(isoDate);
        return this;
    }

    public LocalDate getjDate() {
        return jDate;
    }
}
