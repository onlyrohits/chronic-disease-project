package org.smartloli.kafka.eagle.web.service;

import com.aliyun.hitsdb.client.value.response.QueryResult;
import com.iss.bigdata.health.elasticsearch.entity.Condition;
import com.iss.bigdata.health.elasticsearch.entity.UserBasic;
import com.iss.bigdata.health.elasticsearch.service.ElasticSearchServiceImpl;
import com.iss.bigdata.health.patternrecognition.entity.*;
import com.iss.bigdata.health.patternrecognition.service.OfflineLearningTask;
import la.matrix.DenseMatrix;
import la.matrix.Matrix;
import org.smartloli.kafka.eagle.web.dao.OffLineLearningDao;
import org.smartloli.kafka.eagle.web.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by weidaping on 2018/5/2.
 */
@Service("offLineLearning")
public class OffLineLearningService {

    @Autowired
    private OffLineLearningDao offLineLearningDao;

    /***
     *
     * 重置疾病表的数据
     */
    public void resetDisease(){
        offLineLearningDao.truncateDisease("ke_disease");
        List<String> diseaseList = new ArrayList<>(getAllDisease());
        System.out.println(diseaseList);
        offLineLearningDao.insertAllDisease(diseaseList);
    }

    /***
     *
     * 重置疾病表的数据
     */
    public List<DiseaseDB> getAllDiseaseDB(){
        List<DiseaseDB> diseaseDBS = offLineLearningDao.getAllDisease();
        return diseaseDBS;
    }

    /***
     * 获取所有疾病
     * @return
     */
    public Set<String> getAllDisease(){
        Set<String> allDisease = new HashSet<>();
        List<Condition> conditionList = new ElasticSearchServiceImpl().searchCondition(new ArrayList<>());
        if (conditionList != null && conditionList.size() > 0) {
            for (Condition condition: conditionList) {
                allDisease.add(condition.getDescription());
            }
        }
        return allDisease;
    }


    /***
     * 根据传入的用户id查询所有用户的度量值信息，并封装成离线学习的用户数据
     * @param start
     * @param end
     * @param metrics
     * @param userIds
     * @return
     */
    public OffLineUserData getMetricByUserIds(Long start, Long end, List<String> metrics, List<String> userIds){
        OffLineUserData offLineUserData = new OffLineUserData();
        int[] dataLengthArr = null;
        String[] metricName = null;
        TSSequence tsSequence = new TSSequence();
        List<List<Double>> dataPoint = new ArrayList<>();
        if (userIds != null && userIds.size() > 0) {
            dataLengthArr = new int[userIds.size()];
            for (int u = 0; u < userIds.size(); u++) {
                List<QueryResult> queryResults = new ElasticSearchServiceImpl().searchMetric(start, end, metrics, userIds.get(u));
                if (queryResults != null && queryResults.size() > 0) {
                    metricName = new String[queryResults.size()];
                    for (int i = 0; i < queryResults.size(); i++) {
                        QueryResult queryResult = queryResults.get(i);
                        metricName[i] = queryResult.getMetric();
                        List<Double> doubles = new ArrayList<>();
                        dataLengthArr[u] = queryResult.getDps().size();
                        for (long key : queryResult.getDps().keySet()) {
                            doubles.add(queryResult.getDps().get(key).doubleValue());
                        }
                        dataPoint.add(doubles);
                    }
                }
            }
        }
        Matrix data = new DenseMatrix(list2Array(list2list(dataPoint, metrics.size())));
        tsSequence.setData(data);
        offLineUserData.setMetricName(metricName)
                .setDataLengthArr(dataLengthArr)
                .setTsSequence(tsSequence);
        return offLineUserData;
    }


    public List<List<Double>> list2list(List<List<Double>> dataPoint, int m){
        List<List<Double>> listList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Double> doubleList = new ArrayList<>();
            for (int j = i; j < dataPoint.size(); j = j + m) {
                doubleList.addAll(dataPoint.get(j));
            }
            listList.add(doubleList);
        }
        return listList;
    }

    /***
     * list转数组
     *
     * @param in
     * @return
     */
    public double[][] list2Array(List<List<Double>> in){
        double[][] out = new double[in.get(0).size()][in.size()];
        if (in != null && in.size() > 0){
            for (int i = 0; i < in.size(); i++) {
                List<Double> doubles = in.get(i);
                if (doubles != null && doubles.size() > 0) {
                    for (int j = 0; j < doubles.size(); j++) {
                        out[j][i] = doubles.get(j);
                    }
                }
            }
        }
        return out;
    }


    /***
     * 根据条件筛选用户
     * @param ageStart
     * @param ageEnd
     * @param gender
     * @param conditions
     * @return
     */
    public List<PatientInfo> searchPatientByConditions(String ageStart, String ageEnd, String gender, List<String> conditions){
        List<PatientInfo> patientInfos = new ArrayList<>();
        ArrayList<Condition> conditionArrayList = new ElasticSearchServiceImpl().searchCondition(conditions);
        System.out.println(conditionArrayList.size());
        ArrayList<UserBasic> userBasicArrayList = new ElasticSearchServiceImpl().searchUserByConditions(ageStart, ageEnd, gender);
        System.out.println(userBasicArrayList.size());
        for (Condition condition : conditionArrayList) {
            for (UserBasic userBasic : userBasicArrayList) {
                if (condition.getUser_id().equals(userBasic.getUser_id())) {
                    PatientInfo patientInfo = new PatientInfo();
                    Date birthday = userBasic.getBirthdate();
                    Date deathDay = userBasic.getDeathdate();
                    int birth = LocalDateTime.ofInstant(birthday.toInstant(), ZoneId.systemDefault()).getYear();
                    if (deathDay == null || deathDay.equals("null")) {
                        int now = LocalDate.now().getYear();
                        patientInfo.setAge(now - birth);
                    } else {
                        int death = LocalDateTime.ofInstant(deathDay.toInstant(), ZoneId.systemDefault()).getYear();
                        patientInfo.setAge(death - birth);
                    }
                    patientInfo.setGender(userBasic.getGender())
                            .setUserId(userBasic.getUser_id())
                            .setName(userBasic.getName())
                            .setRace(userBasic.getRace())
                            .setDisease(condition.getDescription());
                    patientInfos.add(patientInfo);
                }
            }
        }
        return patientInfos;
    }


    /***
     * 开始调用机器学习
     * @param learningConfigure
     * @param userIds
     */
    public void learning(LearningConfigure learningConfigure, List<String> userIds){

        SAXAnalysisWindow tmin = new SAXAnalysisWindow(learningConfigure.getSlidingWindowSize(),
                                                        learningConfigure.getPaaSize(),
                                                        learningConfigure.getAlphabetSize());
        OffLineUserData offLineUserData = getMetricByUserIds(Long.valueOf(learningConfigure.getDateBegin()),
                                                            Long.valueOf(learningConfigure.getDateEnd()),
                                                            string2ArrayList(learningConfigure.getMetric(), ",metrics,"),
                                                            userIds);
        OfflineLearningTask task = new OfflineLearningTask(offLineUserData.getTsSequence(),
                                                            offLineUserData.getDataLengthArr(),
                                                            tmin,
                                                            learningConfigure.getAnalysisWindowStartSize(),
                                                            learningConfigure.getFrequencyThreshold(),
                                                            learningConfigure.getrThreshold(),
                                                            learningConfigure.getK(),
                                                            offLineUserData.getMetricName());
        MiningTaskManager miningTaskManager = new MiningTaskManager();
        miningTaskManager.submit(learningConfigure.getConfigureId(), task);
        offLineLearningDao.insertConfigure(learningConfigure);
    }


    /***
     * 字符串根据符号切割，转成list
     * @param in
     * @param symbol
     * @return
     */
    public ArrayList<String> string2ArrayList(String in, String symbol){
        ArrayList<String> out = new ArrayList<>();
        if (in.indexOf(symbol) > 0) {
            String [] strings = in.split(symbol);
            for (int i = 0; i < strings.length; i++) {
                out.add(strings[i]);
            }
        } else {
            out.add(in);
        }
        return out;
    }

    public void saveLearningResult(){
        List<SymbolicPatternDB> symbolicPatternDBS = new ArrayList<>();
        List<PatternDetail> patternDetails = new ArrayList<>();
        MiningTaskManager miningTaskManager = new MiningTaskManager();
        if (!MiningTaskManager.miningTaskMap.isEmpty()) {
            for (String taskId: MiningTaskManager.miningTaskMap.keySet()) {
                if (miningTaskManager.isDone(taskId)) {
                    List<PatternResult> symbolicPatterns = miningTaskManager.getSymbolicPatterns(taskId);
                    int i = 0;
                    for (PatternResult symbolicPattern : symbolicPatterns) {
                        SymbolicPatternDB symbolicPatternDB = new SymbolicPatternDB();
                        String symbolicPatternId = UUID.randomUUID().toString();
                        symbolicPatternDB.setLengths(symbolicPattern.getLength())
                                .setId(symbolicPatternId)
                                .setConfigureId(taskId)
                                .setPatternOrder(i)
                                .setAlias(String.valueOf(i));
                        symbolicPatternDBS.add(symbolicPatternDB);
                        for (MeasureResult measure : symbolicPattern.getMeasureResults()) {
                            PatternDetail patternDetail = new PatternDetail();
                            patternDetail.setId(UUID.randomUUID().toString())
                                    .setSymbolicPatternId(symbolicPatternId)
                                    .setMeasureName(measure.getName())
                                    .setMeasureValue(measure.getStrValue())
                                    .setDatas(measure.getCenter().toString());
                            patternDetails.add(patternDetail);
                        }
                        i++;
                    }
                    offLineLearningDao.updateIsDone("已完成", taskId);
                    miningTaskManager.deleteMiningTask(taskId);
                }
            }
        }
        try {
            offLineLearningDao.insertAllPatternDetail(patternDetails);
            offLineLearningDao.insertAllSymbolicPattern(symbolicPatternDBS);
        } catch (Exception e) {}
    }


    public List<LearningConfigure> getAllConfigure() {
        return offLineLearningDao.getAllConfigure();
    }


    public void stopLearning(String configureId){
        new MiningTaskManager().cancel(configureId);
        offLineLearningDao.updateIsDone("已停止", configureId);
    }

//    @Transactional
    public void deleteConfigure(String configureId){
        List<String> patternIds = offLineLearningDao.getPatternIdByConfigureId(configureId);
        System.out.println("======" + patternIds);
        offLineLearningDao.deleteConfigureById(configureId);
        if (patternIds != null && patternIds.size() > 0) {
            offLineLearningDao.deletePatternByConfigureId(configureId);
            offLineLearningDao.deleteDetailByPatternId(patternIds);
        }
    }


    public List<Pattern> showResult(String configureId){
        List<Pattern> patterns = offLineLearningDao.getPatternByConfigureId(configureId);
        List<PatternDetail> patternDetails = offLineLearningDao.getAllDetail();
        for (Pattern pattern : patterns) {
            List<PatternDetail> patternDetailsTo = new ArrayList<>();
            for (PatternDetail patternDetail : patternDetails) {
                if (pattern.getId().equals(patternDetail.getSymbolicPatternId())) {
                    patternDetailsTo.add(patternDetail);
                }
            }
            pattern.setPatternDetail(patternDetailsTo);
        }
        System.out.println("===========" + patterns);
        return patterns;
    }

    public PatternDetail getDetailById(String id){
        return offLineLearningDao.getDetailById(id);
    }

}
