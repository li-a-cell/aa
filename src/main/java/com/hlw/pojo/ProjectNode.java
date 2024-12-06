package com.hlw.pojo;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projectnode")
public class ProjectNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Integer nodeId;  // 节点 ID

    @Column(name = "project_id", nullable = false)
    private int projectId;  // 关联的项目

    @Column(name = "parent_node_id")
    private Integer  parentNodeId;  // 上级节点 (可为空)

    @Column(name = "node_name", nullable = false)
    private String nodeName;  // 节点名称

    @Column(name = "start_date")
    private LocalDate startDate;  // 开始日期

    @Column(name = "end_date")
    private LocalDate endDate;  // 结束日期

    @Column(name = "node_info")
    private String nodeInfo;  // 节点信息

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NodeStatus status;  // 节点状态


    /**
     * 枚举：节点状态
     */
    public enum NodeStatus {
        未开始, 施工中, 已完成
    }

    // Getter 和 Setter
//    public int getNodeId() {
//        return nodeId;
//    }
//
//    public void setNodeId(int nodeId) {
//        this.nodeId = nodeId;
//    }
//
//    public Project getProject() {
//        return project;
//    }
//
//    public void setProject(Project project) {
//        this.project = project;
//    }
//
//    public Projectnode getParentNode() {
//        return parentNode;
//    }
//
//    public void setParentNode(Projectnode parentNode) {
//        this.parentNode = parentNode;
//    }
//
//    public String getNodeName() {
//        return nodeName;
//    }
//
//    public void setNodeName(String nodeName) {
//        this.nodeName = nodeName;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//
//    public String getNodeInfo() {
//        return nodeInfo;
//    }
//
//    public void setNodeInfo(String nodeInfo) {
//        this.nodeInfo = nodeInfo;
//    }
//
//    public NodeStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(NodeStatus status) {
//        this.status = status;
//    }


}