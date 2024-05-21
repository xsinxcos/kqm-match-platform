package com.chaos.recommend;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.post.domain.entity.PostUser;
import com.chaos.post.service.PostUserService;
import com.chaos.recommend.bo.RecommendBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 基于用户的协同过滤算法模型
 * @author: xsinxcos
 * @create: 2024-03-21 22:00
 **/
@Component
@Slf4j
public class RecommendDataModel extends AbstractModel {
    private static Recommender recommender;

    private final PostUserService postUserService;

    private static UserNeighborhood userNeighborhood;

    private static final int FAVORITE_STATUS = 1;

    private static final float FAVORITE_STATUS_VALUE = 4.0F;

    private static final int USER_POST_MATCH_STATUS = 0;

    private static final float USER_POST_MATCH_STATUS_VALUE = 5.0F;

    public RecommendDataModel(PostUserService postUserService) {
        this.postUserService = postUserService;
        //初始化主动调用初始化模型方法，后续由定时任务进行调用
        init();
    }

    /**
     * 构筑模型，定时构筑模型
     */
    @Scheduled(cron = "0 0 00 * * ? ")
    public void init() {
        try {
            File recommendTempFile = File.createTempFile("temp", ".csv");
            List<RecommendBo> dataInDatabase = getDataInDatabase();
            setFileRecommendCsv(dataInDatabase, recommendTempFile);
            setRecommender(recommendTempFile);
        } catch (IOException e) {
            log.error("临时文件创建失败");
        }
    }

    /**
     * 获取相关推荐
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<Long> getRecommendByUserid(int pageSize, int pageNum, long userId) throws TasteException {
        int beginIndex = (pageNum - 1) * pageSize;
        int endIndex = pageNum * pageSize;

        int length = userNeighborhood.getUserNeighborhood(userId).length;

        if (beginIndex > length) return new ArrayList<>();

        List<RecommendedItem> recommendedItemList = recommender.recommend(userId, 100);
        recommendedItemList = recommendedItemList.subList(beginIndex, Math.min(endIndex + 1, recommendedItemList.size()));

        return recommendedItemList.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
    }

    /**
     * 重新构筑推荐模型
     */
    public void setRecommender(File file) {
        try {
            //构筑模型
            DataModel dataModel = new FileDataModel(file);
            //计算相似度，相似度算法有很多种，欧几里得、皮尔逊等等。
            UserSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
            //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
            userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
            //构建推荐器，协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
            recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);

            log.info("推荐模型构筑成功");
        } catch (IOException | TasteException e) {
            log.error("推荐模型构筑失败");
        }
    }


    /**
     * 将从数据库获得的数据写入训练集
     *
     * @param recommendBos
     */
    public void setFileRecommendCsv(List<RecommendBo> recommendBos, File file) {
        //排序
        recommendBos.sort((o1, o2) -> Math.toIntExact(o1.getFirstElement() - o2.getFirstElement()));
        try {
            // 以覆盖的形式打开文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            // 使用 OutputStreamWriter 将文件输出流转换为 Writer
            Writer writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            // 构造写入文件的内容
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < recommendBos.size(); i++) {
                RecommendBo recommendBo = recommendBos.get(i);

                if (i > 0 && !Objects.equals(recommendBos.get(i - 1).getFirstElement(), recommendBo.getFirstElement())) {
                    sb.append("\n");
                }

                sb.append(recommendBo.getFirstElement())
                        .append(",")
                        .append(recommendBo.getSecondElement())
                        .append(",")
                        .append(recommendBo.getValue())
                        .append("\n");
            }
            // 写入文件
            writer.write(sb.toString());
            // 关闭 Writer 和文件输出流
            IoUtil.close(writer);
            IoUtil.close(fileOutputStream);

            log.info("训练集写入成功");
        } catch (IOException e) {
            log.error("训练集写入失败");
        }
    }

    /**
     * 从数据库获取训练集数据
     *
     * @return
     */
    private List<RecommendBo> getDataInDatabase() {
        List<RecommendBo> recommendBos = new ArrayList<>();
        LambdaQueryWrapper<PostUser> wrapper = new LambdaQueryWrapper<>();
        postUserService.list(wrapper).forEach(o -> {
            switch (o.getStatus()) {
                case USER_POST_MATCH_STATUS:
                    recommendBos.add(new RecommendBo(o.getUserId(), o.getPostId(), USER_POST_MATCH_STATUS_VALUE));
                    break;
                case FAVORITE_STATUS:
                    recommendBos.add(new RecommendBo(o.getUserId(), o.getPostId(), FAVORITE_STATUS_VALUE));
                    break;
                default:
                    break;
            }
        });
        return recommendBos;
    }
}
