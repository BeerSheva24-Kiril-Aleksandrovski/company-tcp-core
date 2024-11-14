package telran.employees;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.json.JSONArray;

import telran.net.Response;
import telran.net.ResponseCode;

public class Methods {

    Company company;

    public Methods(Company company) {
        this.company = company;
    }

    public Response addEmployee(String data) {
        Employee employee = Employee.getEmployeeFromJSON(data);
        company.addEmployee(employee);
        return getOkResponse(employee.toString() + "\nEmployee successfully added to Company");
    }

    public Response getDepartmentBudget(String data) {
        int budget = company.getDepartmentBudget(data);
        return getOkResponse(budget + "");
    }

    public Response getDepartments(String data) {
        String deps = Arrays.toString(company.getDepartments());
        return getOkResponse(deps);
    }

    public Response getManagersWithMostFactor(String data) {
        Manager[] managers = company.getManagersWithMostFactor();
        JSONArray jsonArray = new JSONArray(Arrays.stream(managers).map(Manager::toString).toArray(String[]::new));
        return getOkResponse(jsonArray.toString());
    }

    public Response getEmployee(String data) {
        long id = Long.parseLong(data);
        Employee empl = company.getEmployee(id);
        if (empl == null) {
            throw new NoSuchElementException(String.format("Employee %d not found", id));
        }
        return getOkResponse(empl.toString());
    }

    public Response removeEmployee(String data) {
        Employee employee = company.removeEmployee(Long.valueOf(data));
        return getOkResponse(employee.toString() + "\nEmployee fired");
    }

    public Response getOkResponse(String res) {
        return new Response(ResponseCode.OK, res);
    }
}
