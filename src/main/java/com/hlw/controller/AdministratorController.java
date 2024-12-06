package com.hlw.controller;

import com.hlw.pojo.Equipment;
import com.hlw.pojo.Material;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Result;
import com.hlw.service.AdministratorService;
import com.hlw.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import com.hlw.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AdministratorController 类用于处理管理员相关的 HTTP 请求。
 */
@RestController
@RequestMapping("/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    // 提取员工ID解析逻辑
    private int parseEmployeeId(Object employeeIdObj) {
        try {
            return Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Employee ID format");
        }
    }

    // 将字符串转换为整数，若转换失败则抛出异常
    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " should be a valid number");
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);


    /**
     * 获取待招标项目数量
     *
     * @param request HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含待招标项目数量的 Result 对象。
     */
    @GetMapping("/biddingNum")
    public Result getBiddingsNum(HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        // 获取请求中的 employeeId
        Object employeeIdObj = request.getAttribute("employeeId");

        if (employeeIdObj == null) {
            logger.warn("employee_id is missing in the request");
            return Result.error("employee_id is missing in the request");
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
            if (employeeId <= 0) {
                logger.warn("Employee ID must be a positive integer");
                return Result.error("Employee ID must be a positive integer");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid Employee ID format", e);
            return Result.error("Invalid Employee ID format");
        } catch (Exception e) {
            logger.error("An unexpected error occurred: " + e.getMessage(), e);
            return Result.error("An unexpected error occurred: " + e.getMessage());
        }

        try {
            int biddingsNum = administratorService.getBiddingsNum();
            logger.info("Successfully fetched biddings num: {}", biddingsNum);
            return Result.success(biddingsNum);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while fetching biddings num: " + e.getMessage(), e);
            return Result.error("An unexpected error occurred while fetching biddings num: " + e.getMessage());
        }
    }

    /**
     * 获取员工数量
     *
     * @param request HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含员工数量的 Result 对象。
     */
    @GetMapping("/employee")
    public Result getEmployeeNum(HttpServletRequest request) {
        try {
            // 获取 employeeId, 若不存在则返回错误信息
            Object employeeIdObj = request.getAttribute("employeeId");
            if (employeeIdObj == null) {
                return Result.error("employeeId is missing in the request");
            }

            // 解析 employeeId，如果格式不对返回错误信息
            int employeeId = parseEmployeeId(employeeIdObj);

            // 返回员工数量
            return Result.success(administratorService.getEmployeeNum());
        } catch (Exception e) {
            return Result.error("Failed to get employee number due to internal error");
        }
    }

    /**
     * 获取新增员工数量
     *
     * @param employee 包含年份和月份信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含新增员工数量的 Result 对象。
     */
    @PostMapping("/newEmployee")
    public Result getNewEmployeeNum(@RequestBody String employee, HttpServletRequest request) {
        try {
            // 获取 employeeId, 若不存在则返回错误信息
            Object employeeIdObj = request.getAttribute("employeeId");
            if (employeeIdObj == null) {
                return Result.error("employeeId is missing in the request");
            }

            // 解析 employeeId，若格式不正确则返回错误
            int employeeId = parseEmployeeId(employeeIdObj);

            // 获取 year 和 month，若为空则返回错误
            String year = JsonUtils.getValueFromJson(employee, "year");
            String month = JsonUtils.getValueFromJson(employee, "month");
            if (year == null || month == null) {
                return Result.error("Year or month is missing in the request");
            }

            // 将 year 和 month 转换为整数，并验证其有效性
            int yearInt = parseInt(year, "Year");
            int monthInt = parseInt(month, "Month");

            // 获取新员工数量
            int num = administratorService.getNewEmployeeNum(yearInt, monthInt);

            // 返回结果
            return Result.success(num);
        } catch (Exception e) {
            return Result.error("Failed to get new employee number due to internal error");
        }
    }

    /**
     * 更新项目信息
     *
     * @param projects 包含项目信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回表示更新操作结果的 Result 对象。
     */
    @PostMapping("/updateProjects")
    public Result updateProjects(@RequestBody String projects, HttpServletRequest request) {
        Object employeeIdObj = request.getAttribute("employeeId");
        if (employeeIdObj == null) {
            return Result.error("employeeId is missing in the request");
        }
        int employeeId;
        try {
            employeeId = Integer.parseInt(employeeIdObj.toString());
        } catch (NumberFormatException e) {
            return Result.error("Invalid Employee ID format");
        }
        JsonUtils jsonUtils = new JsonUtils();
        String projectId = JsonUtils.getValueFromJson(projects, "projectId");
        String projectName = JsonUtils.getValueFromJson(projects, "projectName");
        String mangerName = JsonUtils.getValueFromJson(projects, "managerName");
        String startDate = JsonUtils.getValueFromJson(projects, "plannedStartDate");
        String endDate = JsonUtils.getValueFromJson(projects, "plannedEndDate");
        String budget = JsonUtils.getValueFromJson(projects, "budget");
        String status = JsonUtils.getValueFromJson(projects, "status");
        String description = JsonUtils.getValueFromJson(projects, "description");
        String projectType = JsonUtils.getValueFromJson(projects, "projectType");
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 解析字符串为 LocalDate
        LocalDate localstartDate = LocalDate.parse(startDate, formatter);
        LocalDate localendDate = LocalDate.parse(endDate, formatter);
        administratorService.updateProjects(Integer.parseInt(projectId), projectName, mangerName, localstartDate, localendDate, Double.parseDouble(budget), status, description, projectType);
        return Result.success();
    }

    /**
     * 删除项目信息
     *
     * @param project 包含项目 ID 信息的 JSON 字符串。
     * @param request HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回表示删除操作结果的 Result 对象。
     */
    @PostMapping("/deleteProject")
    public Result deleteProject(@RequestBody String project, HttpServletRequest request) {
        try {
            // 提取并验证 employeeId
            Object employeeIdObj = request.getAttribute("employeeId");
            if (employeeIdObj == null) {
                logger.error("Missing employeeId in the request");
                return Result.error("employeeId is missing in the request");
            }
            int employeeId = Integer.parseInt(employeeIdObj.toString());

            // 获取项目ID
            String projectId = JsonUtils.getValueFromJson(project, "projectId");
            if (projectId == null || projectId.isEmpty()) {
                logger.error("Missing projectId in the request");
                return Result.error("projectId is missing in the request");
            }

            // 删除项目
            administratorService.deleteProject(Integer.parseInt(projectId));

            logger.info("Project with ID {} deleted successfully by employee {}", projectId, employeeId);
            return Result.success("Project deleted successfully");
        } catch (NumberFormatException e) {
            logger.error("Invalid employeeId or projectId format", e);
            return Result.error("Invalid employeeId or projectId format");
        } catch (Exception e) {
            logger.error("Error occurred while deleting project", e);
            return Result.error("Error occurred while deleting project: " + e.getMessage());
        }
    }


    /**
     * 获取新员工数量
     *
     * @param employee 包含年份和月份信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回包含新员工数量的 Result 对象。
     */
    @PostMapping("/newTender")
    public Result getNewTenderNum(@RequestBody String employee, HttpServletRequest request) {
        try {
            // 获取 employeeId
            Object employeeIdObj = request.getAttribute("employeeId");
            if (employeeIdObj == null) {
                logger.error("Missing employeeId in the request");
                return Result.error("employeeId is missing in the request");
            }
            int employeeId = Integer.parseInt(employeeIdObj.toString());

            // 提取 year 和 month
            String year = JsonUtils.getValueFromJson(employee, "year");
            String month = JsonUtils.getValueFromJson(employee, "month");

            if (year == null || month == null) {
                logger.error("Missing year or month in the request body");
                return Result.error("year or month is missing in the request");
            }

            // 转换 year 和 month 为整数，并验证其有效性
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);

            if (yearInt <= 0 || monthInt <= 0 || monthInt > 12) {
                logger.error("Invalid year or month values: year = {}, month = {}", yearInt, monthInt);
                return Result.error("Invalid year or month values");
            }

            // 获取新增招标数量
            int num = administratorService.getNewTenderNum(yearInt, monthInt);

            // 返回结果
            return Result.success(num);
        } catch (NumberFormatException e) {
            logger.error("Invalid employeeId or year/month format", e);
            return Result.error("Invalid employeeId or year/month format");
        } catch (Exception e) {
            logger.error("Error occurred while fetching new tender number", e);
            return Result.error("Error occurred while fetching new tender number: " + e.getMessage());
        }
    }


    /**
     * 更新员工信息
     *
     * @param employeeId 需要更新的员工 ID。
     * @param updatedEmployee 更新后的员工信息。
     * @return 返回表示更新操作结果的 Result 对象。
     */
    @PutMapping("/updateEmployee/{employeeId}")
    public Result updateEmployee(@PathVariable("employeeId") int employeeId, @RequestBody User updatedEmployee) {
        // 检查传入的 updatedEmployee 是否有效
        if (updatedEmployee == null) {
            logger.error("Invalid input: updatedEmployee is null");
            return Result.error("Invalid input: updatedEmployee is null");
        }

        // 日志记录：记录更新请求
        logger.info("Received update request for employeeId: {}", employeeId);

        try {
            // 调用服务层进行更新操作
            boolean success = administratorService.updateEmployeeInfo(employeeId, updatedEmployee);

            if (success) {
                // 成功更新员工信息
                logger.info("Employee with ID {} updated successfully", employeeId);
                return Result.success("Employee updated successfully");
            } else {
                // 更新失败
                logger.warn("Failed to update employee with ID {}", employeeId);
                return Result.error("Failed to update employee");
            }
        } catch (Exception e) {
            // 捕获所有异常并记录日志
            logger.error("Error occurred while updating employee with ID {}: {}", employeeId, e.getMessage());
            return Result.error("Error occurred while updating employee: " + e.getMessage());
        }
    }


    /**
     * 删除员工信息
     *
     * @param employeeId 需要删除的员工 ID。
     * @return 返回表示删除操作结果的 Result 对象。
     */
    @DeleteMapping("/deleteEmployee/{employeeId}")
    public Result deleteEmployee(@PathVariable("employeeId") int employeeId) {
        // 日志记录：记录删除请求
        logger.info("Received delete request for employeeId: {}", employeeId);

        // 参数验证：检查 employeeId 是否有效（正整数）
        if (employeeId <= 0) {
            logger.error("Invalid employeeId: {}", employeeId);
            return Result.error("Invalid employeeId provided");
        }

        try {
            // 调用服务层删除员工
            boolean success = administratorService.deleteEmployeeById(employeeId);

            if (success) {
                // 删除成功
                logger.info("Employee with ID {} deleted successfully", employeeId);
                return Result.success("Employee deleted successfully");
            } else {
                // 删除失败
                logger.warn("Failed to delete employee with ID {}", employeeId);
                return Result.error("Failed to delete employee");
            }
        } catch (Exception e) {
            // 捕获异常并记录日志
            logger.error("Error occurred while deleting employee with ID {}: {}", employeeId, e.getMessage());
            return Result.error("Error occurred while deleting employee: " + e.getMessage());
        }
    }


    /**
     * 添加新员工
     *
     * @param newEmployee 新员工的信息。
     * @return 返回表示添加操作结果的 Result 对象。
     */
    @PostMapping("/createEmployee")
    public Result addEmployee(@RequestBody User newEmployee) {
        // 日志记录：记录新员工添加请求
        logger.info("Received request to add new employee: {}", newEmployee);

        // 输入校验：确保必要字段不为空
        if (newEmployee.getName() == null || newEmployee.getName().isEmpty()) {
            logger.error("Employee name is missing");
            return Result.error("Employee name is required");
        }
        if (newEmployee.getEmployeeId() == 0) {
            logger.error("Employee ID is invalid");
            return Result.error("Invalid Employee ID");
        }

        try {
            // 调用服务层添加员工
            boolean success = administratorService.addEmployee(newEmployee);

            if (success) {
                // 员工添加成功
                logger.info("Employee {} added successfully", newEmployee.getEmployeeId());
                return Result.success("Employee added successfully");
            } else {
                // 员工添加失败
                logger.warn("Failed to add employee: {}", newEmployee.getEmployeeId());
                return Result.error("Failed to add employee");
            }
        } catch (Exception e) {
            // 捕获异常并记录日志
            logger.error("Error occurred while adding employee: {}. Error: {}", newEmployee.getEmployeeId(), e.getMessage());
            return Result.error("Error occurred while adding employee: " + e.getMessage());
        }
    }


    /**
     * 获取所有员工信息
     *
     * @return 返回包含所有员工信息的 Result 对象。
     */
    @GetMapping("/employees")
    public Result getAllEmployees() {
        // 日志记录：记录获取员工列表的请求
        logger.info("Received request to get all employees");

        try {
            // 获取所有员工列表
            List<User> employees = administratorService.getAllEmployees();

            // 判断员工列表是否为空
            if (employees != null && !employees.isEmpty()) {
                // 返回成功的员工列表
                logger.info("Successfully retrieved {} employees", employees.size());
                return Result.success(employees);
            } else {
                // 员工列表为空，返回错误提示
                logger.warn("No employees found");
                return Result.error("No employees found");
            }
        } catch (Exception e) {
            // 捕获异常并记录日志
            logger.error("Error occurred while fetching employees: {}", e.getMessage());
            return Result.error("Error occurred while fetching employees: " + e.getMessage());
        }
    }


    /**
     * 添加材料入库
     *
     * @param material 包含材料信息的 JSON 字符串。
     * @param request  HTTP 请求对象，用于获取请求属性中的 employeeId。
     * @return 返回表示添加操作结果的 Result 对象。
     */
    @PostMapping("/materialStorage")
    public Result addMaterialStorage(@RequestBody String material, HttpServletRequest request) {
        // 日志记录：记录收到的请求
        logger.info("Received request to add material storage");

        try {
            // 从 JSON 中提取物料信息
            String materialName = JsonUtils.getValueFromJson(material, "materialName");
            String quantity = JsonUtils.getValueFromJson(material, "quantity");
            String entryDate = JsonUtils.getValueFromJson(material, "entryDate");
            String supplierName = JsonUtils.getValueFromJson(material, "supplierName");
            String price = JsonUtils.getValueFromJson(material, "price");
            String remarks = JsonUtils.getValueFromJson(material, "remarks");

            // 验证输入参数是否有效
            if (materialName == null || quantity == null || entryDate == null || supplierName == null || price == null) {
                logger.warn("Missing required material parameters");
                return Result.error("Missing required material parameters");
            }

            // 解析数量和价格
            int quantityParsed;
            int priceParsed;
            try {
                quantityParsed = Integer.parseInt(quantity);
                priceParsed = Integer.parseInt(price);
            } catch (NumberFormatException e) {
                logger.error("Invalid number format for quantity or price: {}", e.getMessage());
                return Result.error("Invalid number format for quantity or price");
            }

            // 解析入库日期
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(entryDate, formatter);
            } catch (Exception e) {
                logger.error("Invalid date format for entryDate: {}", e.getMessage());
                return Result.error("Invalid date format for entryDate, expected yyyy-MM-dd");
            }

            // 调用服务层方法添加物料存储
            return administratorService.addMaterialStorage(materialName, quantityParsed, localDate, supplierName, priceParsed, remarks);


        } catch (Exception e) {
            // 捕获整个请求过程中的异常并记录日志
            logger.error("Error occurred while adding material storage: {}", e.getMessage());
            return Result.error("Error occurred while processing the request");
        }
    }


    /**
     * 获取父节点
     *
     * @param projectId 项目 ID。
     * @return 返回包含父节点信息的 Result 对象。
     */
    @GetMapping("/topLevelNodes/{projectId}")
    public Result getParentNodeByProjectId(@PathVariable int projectId) {
        logger.info("Received request to get parent node for project ID: {}", projectId);

        try {
            // 从服务层获取父节点
            ProjectNode parentNode = administratorService.getParentNodeByProjectId(projectId);

            if (parentNode == null) {
                logger.warn("No parent node found for project ID: {}", projectId);
                return Result.error("No parent node found for the given project ID.");
            }

            // 返回成功响应
            logger.info("Successfully retrieved parent node for project ID: {}", projectId);
            return Result.success(parentNode);

        } catch (Exception e) {
            // 捕获任何异常并记录日志
            logger.error("Error occurred while retrieving parent node for project ID {}: {}", projectId, e.getMessage());
            return Result.error("Error occurred while processing the request.");
        }
    }


    /**
     * 获取所有材料
     *
     * @return 返回包含所有材料信息的列表。
     */
    @GetMapping("/getAllMaterial")
    public Result getAllMaterials() {
        logger.info("Received request to get all materials");

        try {
            // 获取所有物料数据
            List<Material> materials = administratorService.getAllMaterials();

            if (materials == null || materials.isEmpty()) {
                logger.warn("No materials found in the database");
                return Result.error("No materials found");
            }

            // 返回成功响应
            logger.info("Successfully retrieved all materials");
            return Result.success(materials);

        } catch (Exception e) {
            // 捕获异常并记录日志
            logger.error("Error occurred while retrieving all materials: {}", e.getMessage());
            return Result.error("Error occurred while processing the request");
        }
    }


    //获取项目及对应节点数量
    @PostMapping("/nodes/count")
    public Result getNodeCountByStatus(@RequestBody String project) {
        try {
            // 解析请求中的参数
            JsonUtils jsonUtils = new JsonUtils();
            String projectIdStr = jsonUtils.getValueFromJson(project, "project_id");
            String statusStr = jsonUtils.getValueFromJson(project, "status");

            // 校验 project_id 是否为空或无效
            if (projectIdStr == null || projectIdStr.isEmpty()) {
                return Result.error("Project ID is missing or invalid");
            }

            // 校验 status 是否有效
            ProjectNode.NodeStatus status;
            try {
                status = ProjectNode.NodeStatus.valueOf(statusStr);
            } catch (IllegalArgumentException e) {
                return Result.error("Invalid status value provided");
            }

            // 获取节点数量
            int projectId = Integer.parseInt(projectIdStr);
            int count = administratorService.getNodeCountByStatus(projectId, status);

            // 返回成功响应
            return Result.success(count);
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error occurred while retrieving node count: {}", e.getMessage());
            return Result.error("Error occurred while processing the request");
        }
    }

    // 添加设备
    @PostMapping("/createequipment")
    public Result addEquipment(@RequestBody Equipment newEquipment) {
        if (newEquipment == null) {
            return Result.error("设备信息不能为空");
        }

        // 参数校验：例如检查设备名称、设备类型等是否为空
        if (newEquipment.getEquipmentName() == null || newEquipment.getEquipmentName().isEmpty()) {
            return Result.error("设备名称不能为空");
        }

        if (newEquipment.getEquipmentType() == null || newEquipment.getEquipmentType().isEmpty()) {
            return Result.error("设备类型不能为空");
        }

        try {
            boolean success = administratorService.addEquipment(newEquipment);
            if (success) {
                return Result.success("设备添加成功");
            } else {
                return Result.error("设备添加失败，可能是由于重复数据或其他原因");
            }
        } catch (Exception e) {
            // 记录异常日志
            logger.error("Error occurred while adding equipment: {}", e.getMessage());
            return Result.error("设备添加失败，系统错误");
        }
    }

}
