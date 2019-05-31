package com.adinnet.common;

import com.adinnet.repository.Course;
import com.adinnet.repository.Menu;
import com.adinnet.repository.SysRoleMenu;
import com.alibaba.druid.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangren
 * @Description: 树
 * @create 2018-09-14 13:15
 **/
public class MenuTree {

    private List<Menu> nodes;

    private List<SysRoleMenu> checknodes;


    /**
     * 创建一个新的实例 Tree.
     *
     * @param nodes   将树的所有节点都初始化进来。
     */
    public MenuTree(List<Menu> nodes, List<SysRoleMenu> checknodes){
        this.nodes = nodes;
        this.checknodes = checknodes;
    }


    /**
     * buildTree
     * 描述:  创建树
     * @return List<Map<String,Object>>
     * @exception
     * @since  1.0.0
     */
    public  List<Map<String, Object>> buildTree(){
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        for(Menu node : nodes) {
            //这里判断父节点，需要自己更改判断
            if ( 0 == node.getParentId().intValue() ) {
                //System.out.println("node"+node.getSysPermissionName());
                Map<String, Object> map = buildTreeChildsMap(node);
                list.add(map);
            }
        }
        return list;
    }
    public static List<Map<String, Object>> buildTree( List<Course> courseList){
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        for(Course node : courseList) {
            //这里判断父节点，需要自己更改判断
                //System.out.println("node"+node.getSysPermissionName());
                Map<String, Object> map = buildTreeChildsMap(node);
                list.add(map);
        }
        return list;
    }
    private static Map<String, Object> buildTreeChildsMap(Course childNode){
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("checked", false);
        stateMap.put("disabled", false);
        stateMap.put("expanded", false);
        stateMap.put("selected", false);
        map.put("id", childNode.getId());
        map.put("text", childNode.getTitle());
        map.put("url", null);
        map.put("state", stateMap);
        return map;
    }
    /**
     * buildChilds
     * 描述:  创建树下的节点。
     * @param node
     * @return List<Map<String,Object>>
     * @exception
     * @since  1.0.0
     */
    private List<Map<String, Object>> buildTreeChilds(Menu node){
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        List<Menu> childNodes = getChilds(node);
        for(Menu childNode : childNodes){
            //System.out.println("childNode"+childNode.getSysPermissionName());
            Map<String, Object> map = buildTreeChildsMap(childNode);
            list.add(map);
        }
        return list;
    }

    /**
     * buildChildMap
     * 描述:生成Map节点
     * @param childNode
     * @return Map<String, Object>
     */
    private Map<String, Object> buildTreeChildsMap(Menu childNode){
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> stateMap = new HashMap<>();
        stateMap.put("checked", false);
        for(SysRoleMenu checknode : checknodes){
            if(checknode.getMenuId().equals(childNode.getMenuId())){
                stateMap.put("checked", true);
            }
        }
        stateMap.put("disabled", false);
        stateMap.put("expanded", false);
        stateMap.put("selected", false);
        map.put("id", childNode.getMenuId());
        map.put("text", childNode.getMenuName());
        map.put("url", childNode.getMenuUrl());
        map.put("state", stateMap);
        List<Map<String, Object>> childs = buildTreeChilds(childNode);
        if(childs.isEmpty() || childs.size() == 0){
            //map.put("state","open");
        }else{
            map.put("nodes", childs);
        }
        return map;
    }


    /**
     * getChilds
     * 描述:  获取子节点
     * @param parentNode
     * @return List<Resource>
     * @exception
     * @since  1.0.0
     */
    public List<Menu> getChilds(Menu parentNode) {
        List<Menu> childNodes = new ArrayList<Menu>();
        for(Menu node : nodes){
            //System.out.println(node.getParentId()+"-------"+parentNode.getId());
            if (node.getParentId().equals(parentNode.getMenuId())) {
                childNodes.add(node);
            }
        }
        return childNodes;
    }

    /**
     * buildTree
     * 描述:  创建树
     * @return List<Map<String,Object>>
     * @exception
     * @since  1.0.0
     */
    public List<Menu> buildTreeGrid(){
        List<Menu> list = new ArrayList<Menu>();
        for(Menu node : nodes) {
            //这里判断父节点，需要自己更改判断
            if (0 == node.getParentId().intValue()) {
                List<Menu> childs = buildTreeGridChilds(node);
                node.setChildren(childs);
                list.add(node);
            }
        }
        return list;
    }

    /**
     * buildChilds
     * 描述:  创建树下的节点。
     * @param node
     * @return List<Map<String,Object>>
     * @exception
     * @since  1.0.0
     */
    private List<Menu> buildTreeGridChilds(Menu node){
        List<Menu> list = new ArrayList<Menu>();
        List<Menu> childNodes = getChilds(node);
        for(Menu childNode : childNodes){
            //System.out.println("childNode"+childNode.getSysPermissionName());
            List<Menu> childs = buildTreeGridChilds(childNode);
            childNode.setChildren(childs);
            list.add(childNode);
        }
        return list;
    }

}
