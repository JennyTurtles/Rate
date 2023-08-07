package org.sys.rate.controller.admin;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.mapper.IndicatorMapper;
import org.sys.rate.model.Indicator;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.IndicatorService;
import org.sys.rate.model.TreeNode;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/indicator")
public class IndicatorController {
    @Autowired
    private IndicatorService indicatorService;
    @Resource
    private IndicatorMapper indicatorMapper;

    // 根据成果类型和具体的名字进行查询，返回包含indicator的全部信息
    // 算了，写在一起吧，虽然冗余了代码
    @GetMapping("/getProductByTypeName")
    public RespBean getProductByTypeName(@RequestParam("indicatorType") String indicatorType, @RequestParam("fullName") String fullName) {
        try {
            List<T> product = indicatorService.getProductByTypeName(indicatorType, fullName);
            return RespBean.ok("200", product);
        } catch (Exception e) {
            return RespBean.error("error when getProductByTypeName");
        }
    }


    // 根据成果类型和部分具体的名字进行查询，对每种类型获取不同的内容
    @GetMapping("/getProductNamesByTypeName")
    public RespBean getProductNamesByTypeName(@RequestParam("indicatorType") String indicatorType, @RequestParam("name") String name) {
        try {
            List<String> productNamesByTypeName = indicatorService.getProductNamesByTypeName(indicatorType, name);
            return RespBean.ok("200", productNamesByTypeName);
        } catch (Exception e) {
            return RespBean.error("error when getProductNamesByTypeName");
        }
    }

    // 根据indicator_id返回中间表中所有的年份并进行去重和排序
    @GetMapping("/getAllYear/{indicatorId}/{indicatorType}")
    public RespBean getAllYearById(@PathVariable Integer indicatorId, @PathVariable String indicatorType){
        try {
            ArrayList<Integer> yearList = indicatorService.getAllYearById(indicatorId, indicatorType);
            return RespBean.ok("getAllYear", yearList);
        } catch (Exception e) {
            return RespBean.error("getAllYear wrong!");
        }
    }

    // 从fromYear和indicator_id获取所有的publication_id列表1，从toYear和indicator_id获取所有的publication_id列表2，
    // list1中要首先去除list2的内容，然后再加入list2
    @PostMapping("clone/{fromYear}/{toYear}/{indicatorId}/{indicatorType}")
    public RespBean clone(@PathVariable Integer fromYear, @PathVariable Integer toYear, @PathVariable Long indicatorId, @PathVariable String indicatorType){
        try {
            indicatorService.clone(fromYear, toYear, indicatorId, indicatorType);
            return RespBean.ok("clone success!");
        } catch (Exception e) {
            return RespBean.error("clone wrong!");
        }
    }


    // 上限为7层，每层最多99个节点，因为order字段最大长度为20。
    // 依赖order字段确定父子关系并保持有序，仅使用id和parent不能保持有序。
    // 首先遍历一次，将order字符串按照'.'进行分割，分割为list，建立map（orderList:Indicator)。通过递归确定父子关系。时间复杂度O(n)
    // 前提：(每次增加和删除操作后可以保证)order是连续的且从1开始。
    @GetMapping("/getScoreById")
    public RespBean getScoreById(Integer indicatorId) {
        Integer res = -1;
        try {
            res = indicatorMapper.selectScoreById(indicatorId);
        } catch (Exception e) {
            return RespBean.error("error", null);
        }
        return RespBean.ok("ok", res);
    }

    @GetMapping
    public RespBean getAll() {
        List<Indicator> data = indicatorService.getAll();
        int maxId = 0;
        Map<List<Integer>, TreeNode> map = new HashMap<>();
        for (Indicator indicator : data) {
            if (indicator.getId() > maxId)
                maxId = indicator.getId();
            //将order转换为int型的list，并加入map
            String tmp[] = indicator.getOrder().split("\\.");
            List<Integer> key = new ArrayList<>();
            for (String numString : tmp)
                key.add(Integer.parseInt(numString));
            TreeNode Node = new TreeNode(indicator.getId(), indicator.getOrder() + " " + indicator.getName(), indicator.getType(), indicator.getOrder(), indicator.getScore(), indicator.getRankN());
            map.put(key, Node);
        }
        List<Integer> empty = new ArrayList<>();
        List<TreeNode> res = getChildrenStructure(map, empty);
        List<Object> idAndRes = new ArrayList<>();
        idAndRes.add(maxId + 1);//返回最大的id
        idAndRes.add(res);
        return RespBean.ok("getAll success!", idAndRes); //异常处理待添加
    }

    @PostMapping
    public RespBean save(@RequestBody Indicator indicator) {
        boolean flag = indicatorService.save(indicator);
        if (flag)
            return RespBean.ok("save success!");
        else
            return RespBean.error("save fail!");
    }

    @GetMapping("/getIndicatorId/{order}")
    public RespBean getIndicatorId(@PathVariable String order) {
        Integer indicatorId = indicatorService.getIndicatorId(order);
        return RespBean.ok("200", indicatorId);

    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable Integer id) {
        String order = indicatorService.selectOrder(id);
        Map<List<Integer>, TreeNode> map = getMap();
        adjustOrder(order, map, -1);
        boolean flag = indicatorService.delete(id);

        if (flag)
            return RespBean.ok("delete success!");
        else
            return RespBean.error("delete fail!");
    }

    @PutMapping
    public RespBean update(@RequestBody Indicator indicator) {
        boolean flag = true;
        if (indicator.getScore() == -1) {
//            List<Integer> orderList = stringToList(indicator.getOrder());
//            if (orderList.size() == 2) //在第二层，需要一起修改子节点的type
//            {
//                Map<List<Integer>,TreeNode> map= getMap();
//                List<TreeNode> children = new ArrayList<>();
//                getChildren(map,orderList,children);
//                for (TreeNode child : children)
//                {
//                    Indicator updateObj = new Indicator(child.getId(),"",indicator.getName(),"", 0, 0);
//                    boolean f = indicatorService.updateType(updateObj);
//                    if (!f)
//                        flag = false;
//                }
//                boolean f = indicatorService.updateType(new Indicator(indicator.getID(),"",indicator.getName(),"", 0, 0));
//                flag = f && flag;
//            }
            flag = indicatorService.update(indicator) && flag;
        } else //修改的是根节点
        {
            Map<List<Integer>, TreeNode> map = getMap();
            List<Integer> orderList = stringToList(indicatorService.selectOrder(indicator.getId()));
            List<TreeNode> children = new ArrayList<>();
            getChildren(map, orderList, children);
            //修改所有子节点的score
            for (TreeNode child : children) {
                Indicator updateObj = new Indicator(child.getId(), child.getLabel(), "", "", indicator.getScore(), 0);
                boolean f = indicatorService.updateScoreName(updateObj);
                if (!f)
                    flag = false;
            }
            boolean f = indicatorService.updateScoreName(indicator);
            if (!f)
                flag = false;
        }
        if (flag)
            return RespBean.ok("update success!");
        else
            return RespBean.error("update fail!");
    }

    //难点。需要完成的操作：1.修改目标的记录以及目标子节点的记录 2.修改目标记录关联的其他记录（2.1 原层级标签坐标-1；2.2 新层级标签坐标+1）
    @PostMapping("/insert")
    public RespBean insert(@RequestBody Indicator indicator) //对象内的order为新位置的order,其他字段也为新数据
    {
        String oldOrder = indicatorService.selectOrder(indicator.getId()); //通过id获取老的order
        String newOlder = indicator.getOrder();
        if (oldOrder.equals(newOlder))
            return RespBean.ok("insert success");
        List<Integer> oldOrderList = stringToList(oldOrder);
        List<Integer> newOrderList = stringToList(newOlder);
        Map<List<Integer>, TreeNode> map = getMap(); //使用同一个map,避免多次创建,修改表的操作不会相互干扰

        boolean isSameParent = false; //如：新老order都在1.1下 即1.1.x 如为true需要特殊处理
        if (oldOrderList.size() == newOrderList.size()) {
            int i = 0;
            for (; i < newOrderList.size() - 1; i++) {
                if (oldOrderList.get(i) != newOrderList.get(i))
                    break;
            }
            if (i == newOrderList.size() - 1)
                isSameParent = true;
        }

        //完成操作1
        indicatorService.updateAllField(indicator);
        List<TreeNode> children = new ArrayList<>();
        getChildren(map, oldOrderList, children);
        for (TreeNode child : children) {
            String newOrderChild = child.getOrder().replaceFirst(oldOrder, newOlder);
            List<Integer> newOrderListChild = stringToList(newOrderChild);
            String type = indicator.getType();
            int score = indicator.getScore();
            if (newOrderListChild.size() == 2) //如果子节点变成二级标签，则type与name相同
                type = child.getLabel();
            if (oldOrderList.size() == 1 && newOrderListChild.size() > 2) //当父节点拖动到一级目录时，大于二级节点的type需要与二级目录一致
            {
                List<Integer> tmp = stringToList(child.getOrder());
                List<Integer> parent = new ArrayList<>();
                parent.add(tmp.get(0));
                parent.add(tmp.get(1));
                type = map.get(parent).getType();
            }
            Indicator updateObj = new Indicator(child.getId(), child.getLabel(), type, newOrderChild, score, indicator.getId());
            indicatorService.updateAllField(updateObj);
        }
        //完成操作2
        if (isSameParent) //在同一标签下
        {
            int oldOrderNum = oldOrderList.get(oldOrderList.size() - 1);
            int newOrderNum = newOrderList.get(newOrderList.size() - 1);
            int level = oldOrderList.size();
            if (oldOrderNum < newOrderNum) //从前往后移动
            {
                for (int i = 0; i < newOrderNum - oldOrderNum; i++) {
                    List<Integer> key = new ArrayList<>();
                    key.addAll(oldOrderList);
                    key.set(level - 1, oldOrderNum + 1 + i);
                    TreeNode node = map.get(key);
                    List<TreeNode> childrenNode = new ArrayList<>();
                    getChildren(map, key, childrenNode);
                    key.set(level - 1, oldOrderNum + i);
                    String orderString = listToString(key);
                    Indicator updateObj = new Indicator(node.getId(), null, null, orderString, 0, 0);
                    indicatorService.updateOrder(updateObj);
                    adjustOrderChild(childrenNode, level, -1);
                }
                //1.1 1.2 1.3 1.4   1.2<-1.4
            } else //从后往前移动
            {
                for (int i = 0; i < oldOrderNum - newOrderNum; i++) {
                    List<Integer> key = new ArrayList<>();
                    key.addAll(oldOrderList);
                    key.set(level - 1, oldOrderNum - 1 - i); //找到1.3,+1
                    TreeNode node = map.get(key);
                    List<TreeNode> childrenNode = new ArrayList<>();
                    getChildren(map, key, childrenNode);
                    key.set(level - 1, oldOrderNum - i);
                    String orderString = listToString(key);
                    Indicator updateObj = new Indicator(node.getId(), null, null, orderString, 0, 0);
                    indicatorService.updateOrder(updateObj);
                    adjustOrderChild(childrenNode, level, 1);
                }
            }
        } else {
            adjustOrder(oldOrder, map, -1);
            adjustOrder(newOlder, map, 1);
        }
        return RespBean.ok("");
    }

    private List<TreeNode> getChildrenStructure(Map<List<Integer>, TreeNode> map, List<Integer> now) //保留父子结构
    {
        List<TreeNode> res = new ArrayList<>();
        for (int i = 0; i < 99; i++) //至多99个子节点
        {
            List<Integer> next = new ArrayList<>();
            next.addAll(now); //可能影响性能，深拷贝
            next.add(i + 1);
            TreeNode node = map.get(next); //1.1
            if (node != null) {
                List<TreeNode> children = getChildrenStructure(map, next);
                node.setChildren(children);
                res.add(node);
            } else
                break;
        }
        return res;
    }

    private void getChildren(Map<List<Integer>, TreeNode> map, List<Integer> now, List<TreeNode> res) //平铺所有children,使用res传回
    {
        for (int i = 0; i < 99; i++) //至多99个子节点
        {
            List<Integer> next = new ArrayList<>();
            next.addAll(now); //可能影响性能，深拷贝
            next.add(i + 1);
            TreeNode node = map.get(next);
            if (node != null) {
                getChildren(map, next, res);
                res.add(node);
            } else
                break;
        }
    }

    private Map<List<Integer>, TreeNode> getMap() //key:list形式的order value:数据库内的行
    {
        List<Indicator> data = indicatorService.getAll();
        Map<List<Integer>, TreeNode> map = new HashMap<>();
        for (Indicator indicator : data) {
            List<Integer> key = stringToList(indicator.getOrder());
            TreeNode Node = new TreeNode(indicator.getId(), indicator.getName(), indicator.getType(), indicator.getOrder(), indicator.getScore(), indicator.getRankN());
            map.put(key, Node);
        }
        return map;
    }

    //使order-1, 对象：被删除节点的兄弟节点以及兄弟节点的子节点
    void adjustOrder(String order, Map<List<Integer>, TreeNode> map, int minusOrPlus) //minusOrPlus:-1,后面节点-1；1：后面节点+1
    {
        List<Integer> orderTargetList = stringToList(order); //将需要处理的order字符串转换为list //1.1
        //寻找编号更大的兄弟节点
        int level = orderTargetList.size(); //修改所有index=level-1位置的值，即：1.1最后的1
        for (int i = 0; i < 99; i++) {
            List<Integer> key = new ArrayList<>();
            key.addAll(orderTargetList);
            if (minusOrPlus == -1)
                key.set(key.size() - 1, key.get(key.size() - 1) + 1 + i); //1.2及以后的全部-1
            else if (minusOrPlus == 1)
                key.set(key.size() - 1, key.get(key.size() - 1) + i); //1.1及以后的全部+1
            TreeNode node = map.get(key);
            if (node == null)
                break;
            else {
                //处理兄弟节点 修改order列表再转换为string
                List<Integer> orderList = new ArrayList<>();
                orderList.addAll(key);
                orderList.set(level - 1, orderList.get(level - 1) + minusOrPlus);
                String orderString = listToString(orderList);
                Indicator updateObj = new Indicator(node.getId(), null, null, orderString, 0, 0); //只需要修改order,故其他属性可以为空
                indicatorService.updateOrder(updateObj);
                //处理兄弟节点的所有的子节点
                List<TreeNode> children = new ArrayList<>();
                getChildren(map, key, children);
                adjustOrderChild(children, level, minusOrPlus);
            }
        }
    }

    void adjustOrderChild(List<TreeNode> children, int level, int minusOrPlus) {
        for (TreeNode treeNode : children) {
            List<Integer> chileOrderList = stringToList(treeNode.getOrder());
            chileOrderList.set(level - 1, chileOrderList.get(level - 1) + minusOrPlus);
            String childOrderString = listToString(chileOrderList);
            Indicator updateChildObj = new Indicator(treeNode.getId(), null, null, childOrderString, 0, 0);
            indicatorService.updateOrder(updateChildObj);
        }
    }

    List<Integer> stringToList(String s) {
        String tmp[] = s.split("\\.");
        List<Integer> orderList = new ArrayList<>();
        for (String numString : tmp)
            orderList.add(Integer.parseInt(numString));
        return orderList;
    }

    String listToString(List<Integer> key) {
        String res = "";
        for (int i = 0; i < key.size(); i++) {
            if (i != 0)
                res += ".";
            res += key.get(i);
        }
        return res;
    }


}
