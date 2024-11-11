package telran.employees;

import java.util.Arrays;

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
            response = new Response(ResponseCode.WRONG_DATA, e.getMessage());
        }
        return response;
    }

    private Response addEmployee(String data) {
        Employee employee = Employee.getEmployeeFromJSON(data);
        Response response = null;
        try {
            company.addEmployee(employee);
            response = getOkResponse(employee.toString() + "\nEmployee successfully added to Company");
        } catch (Exception e) {
            response = getWrongDataResponse("Wrong data of Employee");
        }
        return response;
    }

    private Response getDepartmentBudget(String data) {
        Response response = null;
        try {
            String budget = String.valueOf(company.getDepartmentBudget(data));
            response = getOkResponse(budget);
        } catch (Exception e) {
            response = getWrongDataResponse("Such department isn't present in the company");
        }
        return response;
    }

    private Response getDepartments() {
        Response response = null;
        String deps = Arrays.toString(company.getDepartments());
        response = getOkResponse(deps);
        return response;
    }

    private Response getManagersWithMostFactor() {
        Manager[] managers = company.getManagersWithMostFactor();
        JSONArray jsonArray = new JSONArray(Arrays.stream(managers).map(Manager::toString).toArray(String[]::new));
        return getOkResponse(jsonArray.toString());
    }

    private Response getEmployee(String data) {
        Response response = null;
        Employee employee = company.getEmployee(Long.valueOf(data));
        if (employee != null) {
            response = getOkResponse(employee.toString());
        } else {
            response = getWrongDataResponse("Employee with this id is not found in the company");
        }
        return response;
    }

    private Response removeEmployee(String data) {
        Employee employee = company.removeEmployee(Long.valueOf(data));
        Response response = getOkResponse(employee.toString() + "\nEmployee fired");
        return response;
    }

    private Response getOkResponse(String res) {
        return new Response(ResponseCode.OK, res);
    }

    private Response getWrongDataResponse(String res) {
        return new Response(ResponseCode.WRONG_DATA, res);
    }
}
