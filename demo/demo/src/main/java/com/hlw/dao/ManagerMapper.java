package com.hlw.dao;

import com.hlw.pojo.ProjectNode;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ManagerMapper {

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
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("nodeInfo") String nodeInfo);

    // 获取项目经理管理的所有项目节点


    // 获取指定状态的父节点为空的顶级项目节点
    @Select("SELECT * FROM projectnode WHERE parent_node_id IS NULL AND status = #{status} AND project_id IN (SELECT project_id FROM project WHERE manager_id=#{managerId})")
    List<ProjectNode> getTopLevelNodesByStatus(@Param("managerId") int managerId, @Param("status") ProjectNode.NodeStatus status);

    // 根据父节点 ID 获取其子节点
    @Select("SELECT * FROM projectnode WHERE parent_node_id = #{parentNodeId}")
    List<ProjectNode> getChildNodesByParentNodeId(@Param("parentNodeId") int parentNodeId);

    // 插入新的节点材料配置
    @Insert("INSERT INTO materialnode (node_id, material_id, required_quantity) VALUES (#{nodeId}, #{materialId}, #{requiredQuantity})")
    void addMaterialToNode(@Param("nodeId") int nodeId,
                           @Param("materialId") int materialId,
                           @Param("requiredQuantity") int requiredQuantity);

    // 查询指定材料的当前库存
    @Select("SELECT current_stock_quantity FROM material WHERE material_name = #{materialName}")
    int getCurrentStockQuantityByName(@Param("materialName") String materialName);
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

    // 更新设备状态
    @Update("UPDATE equipment SET status = #{status} WHERE equipment_id = #{equipmentId}")
    void updateEquipmentStatus(@Param("equipmentId") int equipmentId, @Param("status") String status);
}