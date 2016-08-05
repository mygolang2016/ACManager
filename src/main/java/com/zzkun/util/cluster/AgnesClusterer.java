package com.zzkun.util.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Agnes聚类算法实现
 * Created by Administrator on 2016/8/4.
 */
public class AgnesClusterer {

    private static final Logger logger = LoggerFactory.getLogger(AgnesClusterer.class);

    private List<List<Double>> list = null;
    private List<Double> dis = null;

    /**
     * 构造函数
     * @param val 需要进行聚类的数组元数据
     */
    public AgnesClusterer(double[] val) {
        list = new ArrayList<>();
        dis = new ArrayList<>();
        double[] copyVal = Arrays.copyOf(val, val.length);
        Arrays.sort(copyVal);
        for(int i = 0; i < copyVal.length; ++i) {
            list.add(new ArrayList<>(Collections.singletonList(copyVal[i])));
            if(i < val.length-1)
                dis.add(Math.abs(copyVal[i+1] - copyVal[i]));
        }
    }

    private void updateDis(int pos) {
        List<Double> disList = new ArrayList<>();
        for (Double a : list.get(pos))
            for (Double b : list.get(pos + 1))
                disList.add(Math.abs(a - b));
        Collections.sort(disList);
        double ans = disList.get(disList.size()/2);
        dis.set(pos, ans);
    }

    public void clusterOne() {
        if(list.size() <= 1)
            return;
        double minDis = Double.MAX_VALUE;
        int minPos = 0;
        for(int i = 0; i < dis.size(); ++i) {
            if(dis.get(i) < minDis) {
                minDis = dis.get(i);
                minPos = i;
            }
        }
        logger.info("当前最小距离：{}", minDis);
        list.get(minPos).addAll(list.get(minPos+1));
        list.remove(minPos+1);
        dis.remove(minPos);
        if(minPos > 0) updateDis(minPos-1);
        if(minPos < list.size()-1) updateDis(minPos);
        logger.info("单次聚类完成，剩余类数：{}", list.size());
        logger.info("当前结果：{}", list);
    }

    public void cluster() {
        while(list.size() > 1)
            clusterOne();
    }
}
