package telran.employees;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.json.JSONArray;

import telran.net.*;

public class CompanyServerProtocol implements Protocol {

    Company company;

    public CompanyServerProtocol(Company company) {
        this.company = company;
    }

    @Override
    public Response getResponse(Request request) {
        String type = request.requestType();
        String data = request.requestData();
        Response response = null;
        try {
            response = switch (type) {
                case "addEmployee" -> addEmployee(data);
                case "getDepartmentBudget" -> getDepartmentBudget(data);
                case "getDepartments" -> getDepartments();
                case "getEmployee" -> getEmployee(data);
                case "removeEmployee" -> removeEmployee(data);
                case "getManagersWithMostFactor" -> getManagersWithMostFactor();
                default -> getWrongDataResponse(type + " is wrong type");
            };
        } catch (Exception e) {
            response = getWrongDataResponse(e.getMessage());
        }
        return response;
    }

    private Response addEmployee(String data) {
        Employee employee = Employee.getEmployeeFromJSON(data);
        company.addEmployee(employee);
        return getOkResponse(employee.toString() + "\nEmployee successfully added to Company");
    }

    private Response getDepartmentBudget(String data) {
        int budget = company.getDepartmentBudget(data);
        return getOkResponse(budget + "");
    }

    private Response getDepartments() {
        String deps = Arrays.toString(company.getDepartments());
        return getOkResponse(deps);
    }

    private Response getManagersWithMostFactor() {
        Manager[] managers = company.getManagersWithMostFactor();
        JSONArray jsonArray = new JSONArray(Arrays.stream(managers).map(Manager::toString).toArray(String[]::new));
        return getOkResponse(jsonArray.toString());
    }

    private Response getEmployee(String data) {
        long id = Long.parseLong(data);
        Employee empl = company.getEmployee(id);
        if (empl == null) {
            throw new NoSuchElementException(String.format("Employee %d not found", id));
        }
        return getOkResponse(empl.toString());
    }

    private Response removeEmployee(String data) {
        Employee employee = company.removeEmployee(Long.valueOf(data));
        return getOkResponse(employee.toString() + "\nEmployee fired");
    }

    private Response getOkResponse(String res) {
        return new Response(ResponseCode.OK, res);
    }

    private Response getWrongDataResponse(String res) {
        return new Response(ResponseCode.WRONG_DATA, res);
    }
}
