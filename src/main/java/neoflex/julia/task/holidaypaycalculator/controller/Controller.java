package neoflex.julia.task.holidaypaycalculator.controller;

import neoflex.julia.task.holidaypaycalculator.domain.HolidayInfoDto;
import neoflex.julia.task.holidaypaycalculator.domain.dto.DateDto;
import neoflex.julia.task.holidaypaycalculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private final CalculatorService calculatorService;

    @Autowired
    public Controller(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/calculate")
    public ResponseEntity<Double> getHolidayPay(@RequestParam("salary") Integer salary,
                                                @RequestParam("vac") Integer daysOfVacation,
                                                @RequestParam(value = "start", required = false) String startDate,
                                                @RequestParam(value = "end", required = false) String endDate) {
        HolidayInfoDto holidayInfoDto = new HolidayInfoDto()
                .setSalary(salary)
                .setDaysOfVac(daysOfVacation);

        if (startDate != null) {
            holidayInfoDto.setStartOfVac(new DateDto().setIsoDate(startDate));
        }
        if (endDate != null) {
            holidayInfoDto.setEndOfVac(new DateDto().setIsoDate(endDate));
        }

        Double holidayPay = calculatorService.calculateHolidayPay(holidayInfoDto);

        if (!holidayPay.isNaN()) {
            return new ResponseEntity<>(holidayPay, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(holidayPay, HttpStatus.BAD_REQUEST);
        }
    }
}
