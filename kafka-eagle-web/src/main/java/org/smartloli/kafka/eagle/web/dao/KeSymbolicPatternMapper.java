package org.smartloli.kafka.eagle.web.dao;

import org.smartloli.kafka.eagle.web.pojo.KeSymbolicPattern;

import java.util.List;

public interface KeSymbolicPatternMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ke_symbolic_pattern
     *
     * @mbggenerated Wed May 09 12:07:25 CST 2018
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ke_symbolic_pattern
     *
     * @mbggenerated Wed May 09 12:07:25 CST 2018
     */
    int insert(KeSymbolicPattern record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ke_symbolic_pattern
     *
     * @mbggenerated Wed May 09 12:07:25 CST 2018
     */
    int insertSelective(KeSymbolicPattern record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ke_symbolic_pattern
     *
     * @mbggenerated Wed May 09 12:07:25 CST 2018
     */
    KeSymbolicPattern selectByPrimaryKey(String id);

    List<KeSymbolicPattern> selectByConfigId(String configureId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ke_symbolic_pattern
     *
     * @mbggenerated Wed May 09 12:07:25 CST 2018
     */
    int updateByPrimaryKeySelective(KeSymbolicPattern record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ke_symbolic_pattern
     *
     * @mbggenerated Wed May 09 12:07:25 CST 2018
     */
    int updateByPrimaryKey(KeSymbolicPattern record);
}
