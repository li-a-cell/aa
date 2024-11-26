package com.hlw.dao;

import com.hlw.dto.EquipmentDetails;
import com.hlw.dto.MaterialUsage;
import com.hlw.dto.ProjectNodeIdDto;
import com.hlw.dto.nodematerial;
import com.hlw.pojo.InspectionTask;
import com.hlw.pojo.ProjectNode;
import com.hlw.pojo.Regulations;
import com.hlw.pojo.TenderTask;
import jakarta.transaction.Transactional;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ManagerMapper {
@Transactional
    // 插入新的项目节点
    @Insert("INSERT INTO projectnode (project_id, parent_node_id, node_name, start_date, end_date, node_info, status) " +
            "SELECT a.project_id, " +
            "       #{parentNodeId}, " +
            "       #{nodeName}, " +
            "       #{startDate}, " +
            "       #{endDate}, " +
            "       #{nodeInfo}, " +
            "       '未开始' " +
            "FROM project AS a " +
            "WHERE a.manager_id = #{managerId}")
    void createProjectNodesByManagerId(
        @Param("managerId") int managerId,
        @Param("parentNodeId") int parentNodeId,
        @Param("nodeName") String nodeName,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("nodeInfo") String nodeInfo);

    // 获取指定状态的父节点为空的顶级项目节点
    @Select("SELECT * FROM projectnode WHERE parent_node_id IS NULL AND status = #{status} AND project_id IN (SELECT project_id FROM project WHERE manager_id=#{managerId} AND project.status IN ('待发布','待招标', '施工中'))")
    List<ProjectNode> getTopLevelNodesByStatus(@Param("managerId") int managerId, @Param("status") ProjectNode.NodeStatus status);
    // 根据父节点 ID 获取其子节点
    @Select("SELECT * FROM projectnode WHERE parent_node_id = #{parentNodeId}")
    List<ProjectNode> getChildNodesByParentNodeId(@Param("parentNodeId") int parentNodeId);
@Transactional
    // 插入新的节点材料配置
    @Insert("INSERT INTO materialnode (node_id, material_id, required_quantity) VALUES (#{nodeId}, #{materialId}, #{requiredQuantity})")
    void addMaterialToNode(@Param("nodeId") int nodeId,
                           @Param("materialId") int materialId,
                           @Param("requiredQuantity") int requiredQuantity);

    // 查询指定材料的当前库存
    @Select("SELECT current_stock_quantity FROM material WHERE material_name = #{materialName}")
    int getCurrentStockQuantityByName(@Param("materialName") String materialName);
@Transactional
     //更新材料数量
    @Update( "UPDATE material SET current_stock_quantity = #{newQuantity} WHERE material_name = #{materialName}")
        void updateMaterial(@Param("materialName") String materialName,@Param("newQuantity") int newQuantity);
// 根据材料名称查询材料ID
    @Select("SELECT material_id FROM material WHERE material_name = #{materialName}")
    int getMaterialIdByName(@Param("materialName") String materialName);
      // 根据设备名称查询设备 ID
    @Select("SELECT equipment_id FROM equipment WHERE equipment_name = #{equipmentName}")
    int getEquipmentIdByName(@Param("equipmentName") String equipmentName);

    // 根据设备 ID 查询设备状态
    @Select("SELECT status FROM equipment WHERE equipment_id = #{equipmentId}")
    String getEquipmentStatusById(@Param("equipmentId") int equipmentId);
@Transactional
    // 更新设备状态
    @Update("UPDATE equipment SET status = #{status} WHERE equipment_id = #{equipmentId}")
    void updateEquipmentStatus(@Param("equipmentId") int equipmentId, @Param("status") String status);
   //设置设备节点
 @Update("UPDATE equipment SET node_id = #{node_id} WHERE equipment_id = #{equipmentId}")
    void updateEquipmentnodeID(@Param("equipmentId") int equipmentId, @Param("node_id") int node_id);
    // 根据节点ID和材料名称获取当前配置数量（用于后续判断等操作）
    @Select("SELECT mn.required_quantity FROM materialnode AS mn ,material AS m  " +
            "WHERE mn.node_id = #{nodeId} AND m.material_name = #{materialName} AND mn.material_id = m.material_id")
    int getCurrentMaterialQuantityForNodeByName(@Param("nodeId") int nodeId,
                                                 @Param("materialName") String materialName);
@Transactional
    // 更新节点对应材料的数量（通过材料名称关联来更新）
    @Update("UPDATE materialnode mn " +
            "JOIN material m ON mn.material_id = m.material_id " +
            "SET mn.required_quantity = #{newQuantity} " +
            "WHERE mn.node_id = #{nodeId} AND m.material_name = #{materialName}")
    void updateMaterialQuantityForNodeByName(@Param("nodeId") int nodeId,
                                              @Param("materialName") String materialName,
                                              @Param("newQuantity") int newQuantity);

//将设备节点置为空
@Update("UPDATE equipment SET node_id = NULL WHERE equipment_id = #{equipmentId}")
void clearEquipmentNodeId(@Param("equipmentId") int equipmentId);


    // 获取指定节点下所有在使用中的设备 ID
    @Select("SELECT equipment_id FROM equipment WHERE status = '使用中' AND node_id = #{nodeId}")
    List<Integer> getInUseEquipmentIdsByNodeId(int nodeId);

       // 更新项目节点状态
@Update("UPDATE projectnode SET status = #{status} WHERE node_id = #{nodeId}")
    void updateProjectNodeStatus(int nodeId, String status);

// 根据节点 ID 获取对应的项目 ID
    @Select("SELECT project_id FROM projectnode WHERE node_id = #{nodeId}")
    int getProjectIdByNodeId(int nodeId);

    // 获取指定项目下所有节点的状态
    @Select("SELECT status FROM projectnode WHERE project_id = #{projectId}")
    List<String> getAllNodeStatusesByProjectId(int projectId);

    // 更新项目状态
    @Update("UPDATE project SET status = #{status} WHERE project_id = #{projectId}")
    void updateProjectStatus(int projectId, String status);

    // 查询指定状态的项目节点数量
    @Select("SELECT COUNT(*) FROM projectnode WHERE status = #{status} AND project_id IN (SELECT project_id FROM project WHERE manager_id = #{managerId} AND status IN ('待发布','待招标', '施工中'))")
    int getNodeCountByStatus(@Param("managerId") int managerId, @Param("status") ProjectNode.NodeStatus status);



   // 获取非 "已完成" 状态的项目节点的开始日期和结束日期
@Select("SELECT start_date, end_date FROM projectnode WHERE status != '已完成' AND project_id IN (SELECT project_id FROM project WHERE manager_id=#{managerId})")
List<ProjectNodeIdDto> getProjectNodeStartEndDate(@Param("managerId") int managerId);



    // 获取当前项目经理未完成的项目对应的所有材料名字及使用数量
    @Select("SELECT m.material_name, SUM(mn.required_quantity) AS total_quantity " +
            "FROM materialnode mn " +
            "JOIN material m ON mn.material_id = m.material_id " +
            "JOIN projectnode pn ON mn.node_id = pn.node_id " +
            "JOIN project p ON pn.project_id = p.project_id " +
            "WHERE p.manager_id = #{managerId} " +
            "AND p.status IN ('待发布', '待招标', '施工中') " +
            "GROUP BY m.material_name")
    List<MaterialUsage> getMaterialsForIncompleteProjects(@Param("managerId") int managerId);

    //查询对应节点的材料

    @Select("SELECT m.material_name AS materialName, " +
        "mn.required_quantity AS requiredQuantity, " +
        "m.material_type AS materialType, " +
        "m.supplier_name AS supplierName, " +
        "m.material_photo AS materialPhoto, " +
        "m.quality_status AS qualityStatus, " +
        "m.remarks AS remarks " +
        "FROM materialnode mn " +
        "JOIN material m ON mn.material_id = m.material_id " +
        "WHERE mn.node_id = #{nodeId}")
List<nodematerial> getMaterialsByNodeId(@Param("nodeId") int nodeId);

    //查询对应节点的设备
@Select("SELECT e.equipment_name AS equipmentName, " +
        "e.equipment_photo AS equipmentPhoto, " +
        "e.status AS status, " +
        "e.equipment_type AS equipmentType, " +
        "e.equipment_model AS equipmentModel, " +
        "e.remarks AS remarks " +
        "FROM equipment e " +
        "WHERE e.node_id = #{nodeId}")
List<EquipmentDetails> getEquipmentDetailsByNodeId(@Param("nodeId") int nodeId);
 // 插入检查任务

    @Insert("INSERT INTO inspectiontask (node_id, inspector_id, status, inspection_type, start_date, due_date) " +
        "SELECT #{nodeId}, " +
        "       #{inspectorId}, " +
        "       #{status}, " +
        "       #{inspectionType}, " +
        "       #{startDate}, " +
        "       #{dueDate} " +
        "FROM DUAL")
    void createInspectionTask(@Param("nodeId") int nodeId,
                          @Param("inspectorId") int inspectorId,
                          @Param("status") String status,
                          @Param("inspectionType") String inspectionType,
                          @Param("startDate") LocalDate startDate,
                          @Param("dueDate") LocalDate dueDate);

    @Update("UPDATE inspectiontask " +
            "SET inspector_id = #{inspectorId}, " +
            "status = #{status}, " +
            "inspection_type = #{inspectionType}, " +
            "start_date = #{startDate}, " +
            "due_date = #{dueDate} " +
            "WHERE inspection_task_id = #{taskId}")
    void updateInspectionTask(@Param("taskId") int taskId,
                              @Param("inspectorId") int inspectorId,
                              @Param("status") String status,
                              @Param("inspectionType") String inspectionType,
                              @Param("startDate") LocalDate startDate,
                              @Param("dueDate") LocalDate dueDate);

    @Select("SELECT * FROM inspectiontask WHERE inspection_task_id = #{taskId}")
    InspectionTask getInspectionTaskById(@Param("taskId") int taskId);
    @Select("SELECT * FROM inspectiontask WHERE inspection_task_id = (SELECT inspection_task_id FROM inspectionrecord WHERE record_name=#{recordName})")
    InspectionTask getInspectionTaskByName(@Param("recordName") String recordName);
    @Select("SELECT * FROM regulations WHERE regulation_name=#{name}")
    Regulations getRegulationsByName(@Param("name") String name);


    // 根据供应商名称查询供应商ID
    @Select("SELECT bidder_id FROM bidder WHERE name = #{supplierName}")
    Integer getSupplierIdByName(@Param("supplierName") String supplierName);

    // 根据施工地名称查询施工地ID
    @Select("SELECT site_id FROM constructionsite WHERE site_name = #{constructionSiteName}")
    Integer getConstructionSiteIdByName(@Param("constructionSiteName") String constructionSiteName);

    // 插入新的项目
    @Insert("INSERT INTO project (manager_id, project_name, project.planned_start_date, project.planned_end_date, description, site_id,budget,status,project_type) " +
            "VALUES (#{managerId}, #{projectName}, #{startDate}, #{endDate}, #{projectDescription},  #{constructionSiteId},#{Budget},#{status},#{project_type} )")
    int createProject(@Param("managerId") int managerId, @Param("projectName") String projectName,
                      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                      @Param("projectDescription") String projectDescription, @Param("constructionSiteId") int constructionSiteId,
                      @Param("Budget") double Budget, @Param("status") String status,
                      @Param("project_type") String project_type );
    // 获取所有 Regulation
     @Select("SELECT regulation_name FROM regulations ")
    List<String> getAllRegulations();
     // 添加投标任务
    @Insert("INSERT INTO tender_task (project_id, tender_task_status,deadline) " +
            "VALUES (#{project_id}, #{tender_task_status}, #{deadline})")
    boolean addTenderTask(@Param("project_id") int project_id, @Param("tender_task_status") TenderTask.TenderTaskStatus tender_task_status,@Param("deadline") LocalDate deadline);

}




