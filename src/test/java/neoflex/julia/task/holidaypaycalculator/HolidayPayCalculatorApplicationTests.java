package neoflex.julia.task.holidaypaycalculator;

import neoflex.julia.task.holidaypaycalculator.domain.HolidayInfoDto;
import neoflex.julia.task.holidaypaycalculator.domain.dto.DateDto;
import neoflex.julia.task.holidaypaycalculator.domain.dto.HolidayDto;
import neoflex.julia.task.holidaypaycalculator.domain.dto.JsonDto;
import neoflex.julia.task.holidaypaycalculator.service.CalculatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class HolidayPayCalculatorApplicationTests {
    private TestRestTemplate restTemplate;

    @Autowired
    private CalculatorService calculatorService;
    @Value("${apiKey}")
    private String apiKey;

    @Test
    void contextLoads() {

    }

    @BeforeEach
    void beforeEach() {
        restTemplate = new TestRestTemplate();
    }

    @Test
    @DisplayName("Тестирование API для получения официальных праздников")
    public void testApi() {
        String url = "https://calendarific.com/api/v2/holidays?api_key={key}&country=RU&year={year}&type=national";
        JsonDto response = restTemplate.getForObject(url, JsonDto.class, apiKey, 2023);
        Assertions.assertNotNull(response);

        List<HolidayDto> holidayDtos = response.getResponseDto().getHolidayDtos();
        Assertions.assertNotNull(holidayDtos);

        List<DateDto> dateDtoList;
        dateDtoList = holidayDtos.stream()
                .map(HolidayDto::getDateDto)
                .collect(Collectors.toList());
        Assertions.assertNotNull(dateDtoList);

        Assertions.assertEquals(17, dateDtoList.size());
    }

    @Test
    @DisplayName("Вычисление отпусных без дат")
    public void testCalculatorWithoutDates() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(10);
        double expected = 3412.97;
        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных в даты без праздников")
    public void testCalculatorWithDatesNoHolidays() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(10)
                .setStartOfVac(new DateDto().setIsoDate("2023-05-16"))
                .setEndOfVac(new DateDto().setIsoDate("2023-05-25"));

        double expected = 3412.97;
        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных в даты с тремя праздниками")
    public void testCalculatorWithDatesTheDaysHolidays() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(10)
                .setStartOfVac(new DateDto().setIsoDate("2023-04-30"))
                .setEndOfVac(new DateDto().setIsoDate("2023-05-09"));

        double expected = 2389.08;
        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных в даты праздников")
    public void testCalculatorWithDatesOnlyHolidays() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(3)
                .setStartOfVac(new DateDto().setIsoDate("2023-01-01"))
                .setEndOfVac(new DateDto().setIsoDate("2023-01-03"));

        double expected = 0;
        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных без переданной зарплаты")
    public void testCalculatorWithoutSalary() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setDaysOfVac(3);

        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(Double.NaN, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных без количества дней и даты окончания")
    public void testCalculatorWithStartDateWithoutVac() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setStartOfVac(new DateDto().setIsoDate("2023-01-01"));

        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(Double.NaN, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных без количества дней и даты начала")
    public void testCalculatorWithEndDateWithoutVac() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setEndOfVac(new DateDto().setIsoDate("2023-01-01"));

        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(Double.NaN, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных без даты конца по количеству дней")
    public void testCalculatorWithStartDateWithVac() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(10)
                .setStartOfVac(new DateDto().setIsoDate("2023-04-30"));

        double expected = 3412.97;
        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных без даты начала по количеству дней")
    public void testCalculatorWithEndDateWithVac() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(10)
                .setEndOfVac(new DateDto().setIsoDate("2023-05-10"));

        double expected = 3412.97;
        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Тест рассчёта количества дней между датами")
    public void testLocalDate() {
        LocalDate dateS = LocalDate.parse("2023-05-16");
        LocalDate dateE = LocalDate.parse("2023-05-25");

        long expected = 10;
        long actual = dateE.toEpochDay() - dateS.toEpochDay() + 1;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Проверка метода проверки количества дней")
    public void testCheckOfDaysOfVac() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(3)
                .setStartOfVac(new DateDto().setIsoDate("2023-04-30"))
                .setEndOfVac(new DateDto().setIsoDate("2023-05-09"));
        holidayInfoDto.checkDaysOfVac();

        int expected = 10;
        int actual = holidayInfoDto.getDaysOfVac();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Вычисление отпускных в даты с неверным количеством переданных дней")
    public void testCalculatorWithDatesWithWrongVac() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setDaysOfVac(3)
                .setStartOfVac(new DateDto().setIsoDate("2023-04-30"))
                .setEndOfVac(new DateDto().setIsoDate("2023-05-09"));
        holidayInfoDto.checkDaysOfVac();

        double expected = 2389.08;
        double actual = calculatorService.calculateHolidayPay(holidayInfoDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Проверка дат - неверные даты")
    public void testCheckDatesOfHoliday() {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(10_000)
                .setStartOfVac(new DateDto().setIsoDate("2023-04-30"))
                .setEndOfVac(new DateDto().setIsoDate("2023-04-09"));

        boolean actual = holidayInfoDto.areDatesCorrect();

        Assertions.assertFalse(actual);
    }
}
