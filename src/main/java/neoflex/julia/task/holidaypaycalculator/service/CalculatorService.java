package neoflex.julia.task.holidaypaycalculator.service;

import neoflex.julia.task.holidaypaycalculator.domain.HolidayInfoDto;
import neoflex.julia.task.holidaypaycalculator.domain.dto.DateDto;
import neoflex.julia.task.holidaypaycalculator.domain.dto.HolidayDto;
import neoflex.julia.task.holidaypaycalculator.domain.dto.JsonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculatorService {
    private final RestTemplate restTemplate;

    private List<LocalDate> datesOfHolidays;

    @Value("${apiKey}")
    private String apiKey;

    @Autowired
    public CalculatorService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostConstruct
    private void instantiateDatesOfBirthdays() {
        datesOfHolidays = getDatesOfHolidays().stream()
                .map(DateDto::getjDate)
                .collect(Collectors.toList());
    }

    public Double calculateHolidayPay(HolidayInfoDto holidayInfoDto) {
        if (!holidayInfoDto.areDatesCorrect()) {
            return Double.NaN;
        }
        holidayInfoDto.checkDaysOfVac();

        if (holidayInfoDto.getSalary() == null) {
            return Double.NaN;
        }

        if (holidayInfoDto.getDaysOfVac() == null
                && (holidayInfoDto.getStartOfVac() == null
                || holidayInfoDto.getEndOfVac() == null)) {
            return Double.NaN;
        }

        int daysOfVac = calculateDaysOfVac(holidayInfoDto);
        double dayPay = holidayInfoDto.getSalary() / 29.3;
        return (Math.round(dayPay * daysOfVac * 100.0) / 100.0);
    }

    private int calculateDaysOfVac(HolidayInfoDto holidayInfoDto) {
        if (holidayInfoDto.getStartOfVac() != null && holidayInfoDto.getEndOfVac() != null) {
            int daysOfVac = holidayInfoDto.getDaysOfVac();

            LocalDate startDate = holidayInfoDto.getStartOfVac().getjDate();
            LocalDate endDate = holidayInfoDto.getEndOfVac().getjDate();

            List<LocalDate> dates = new ArrayList<>();
            while (!startDate.isAfter(endDate)) {
                dates.add(startDate);
                startDate = startDate.plusDays(1);
            }

            for (LocalDate localDate : dates) {
                if (datesOfHolidays.contains(localDate)) {
                    daysOfVac--;
                }
            }
            return daysOfVac;
        } else {
            return holidayInfoDto.getDaysOfVac();
        }
    }

    private List<DateDto> getDatesOfHolidays() {
        String url = "https://calendarific.com/api/v2/holidays?api_key={key}&country=RU&year={year}&type=national";
        JsonDto response = restTemplate.getForObject(url, JsonDto.class, apiKey, 2023);
        List<DateDto> dateDtoList;

        assert response != null;
        List<HolidayDto> holidayDtoList = response.getResponseDto().getHolidayDtos();
        dateDtoList = holidayDtoList.stream()
                .map(HolidayDto::getDateDto)
                .collect(Collectors.toList());

        return dateDtoList;
    }
}
