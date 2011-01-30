package com.dds.partitioning;

import java.util.List;
import java.util.Set;
import com.dds.cluster.Node;

public interface RoutingStrategy {

    public String getType();
    
    public List<Node> routeRequest(byte[] key);
    
    public List<Integer> getPartitionList(byte[] key);
    
    public List<Integer> getReplicatingPartitionList(int partitionId);
    
    public Set<Node> getNodes();

}