package neoflex.julia.task.holidaypaycalculator.domain;

import neoflex.julia.task.holidaypaycalculator.domain.dto.DateDto;

public class HolidayInfoDto {
    private Integer salary;
    private Integer daysOfVac;
    private DateDto startOfVac;
    private DateDto endOfVac;

    public HolidayInfoDto setSalary(Integer salary) {
        this.salary = salary;
        return this;
    }

    public HolidayInfoDto setDaysOfVac(Integer daysOfVac) {
        this.daysOfVac = daysOfVac;
        return this;
    }

    public HolidayInfoDto setStartOfVac(DateDto startOfVac) {
        this.startOfVac = startOfVac;
        return this;
    }

    public HolidayInfoDto setEndOfVac(DateDto endOfVac) {
        this.endOfVac = endOfVac;
        return this;
    }

    public Integer getSalary() {
        return salary;
    }

    public Integer getDaysOfVac() {
        return daysOfVac;
    }

    public DateDto getStartOfVac() {
        return startOfVac;
    }

    public DateDto getEndOfVac() {
        return endOfVac;
    }

    public void checkDaysOfVac() {
        if (this.endOfVac != null && this.startOfVac != null) {
            long actual = this.endOfVac.getjDate().toEpochDay() - this.startOfVac.getjDate().toEpochDay() + 1;
            if (this.daysOfVac != actual) {
                this.daysOfVac = (int) actual;
            }
        }
    }

    public boolean areDatesCorrect() {
        if (this.endOfVac != null && this.startOfVac != null) {
            return this.getStartOfVac().getjDate().isBefore(this.getEndOfVac().getjDate())
                    || this.getStartOfVac().getjDate().equals(this.getEndOfVac().getjDate());
        } else {
            return true;
        }
    }
}