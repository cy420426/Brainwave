package com.chat.core.commons.sharding;

import java.util.*;

/**
 * @classDesc: 功能描述:(一致性hash)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class ThreadConsistentHash {
    /**
     * 物理节点
     */
    List<String> realNodes = new ArrayList<>();

    /**
     * 虚拟节点的数量（每个真实节点虚拟出来的数量总和大概在1000-2000最佳）
     * 默认100
     */
    private int virNodeNum = 1500;

    /**
     * 定义一个存放hash的TreeMap
     */
    private final SortedMap<Integer, String> hashTree = new TreeMap<>();

    /**
     * 创建类初始化指定每个物理节点的虚拟节点数量
     */
    public ThreadConsistentHash(int virNodeNum) {
        this.virNodeNum = virNodeNum;
    }

    KetamaHash ketamaHash = new KetamaHash();

    /**
     * 添加一个节点
     */
    public void addNode(String node) {
        //物理节点列表添加一个
        realNodes.add(node);
        //开始虚拟物理节点
        String virNode = null;
        for (int i = 0; i < virNodeNum; i++) {
            //虚拟节点的名字（用于hash）
            virNode = node + "vir/*/" + i;
            //keTaMa计算hash值
            int virHash = Math.abs(ketamaHash.getHash(virNode));
            //int virHash = getHashCode(virNode);
            //保存到hash树
            hashTree.put(virHash, node);
        }
    }

    /**
     * 当前节点数量
     */
    public int size() {
        return realNodes.size();
    }

    /**
     * 删除一个节点
     */
    public void removeNode(String node) {
        realNodes.remove(node);
        hashTree.entrySet().removeIf(next -> next.getValue().equals(node));
    }

    /**
     * 路由节点（获取node）
     */
    public String getNode(String key) {
        int virHash = Math.abs(ketamaHash.getHash(key));
        // int virHash = getHashCode(key);
        // 获取大于等于该key的子map（hash环顺时针）
        SortedMap<Integer, String> sortedMap = hashTree.tailMap(virHash);
        if (sortedMap.isEmpty()) {
            return hashTree.get(hashTree.firstKey());
        } else {
            return sortedMap.get(sortedMap.firstKey());
        }
    }

}
