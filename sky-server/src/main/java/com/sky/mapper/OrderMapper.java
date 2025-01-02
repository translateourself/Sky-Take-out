package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/31 15:34
 */
@Mapper
public interface OrderMapper {



    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);


    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, Long id);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态，分别查询出接待单，待派送、派送中的订单数量
     * @param status
     * @return
     */
    Integer countStatus(Integer status);

    @Select("select * from orders where status = #{status}  and order_time < #{orderCreateTime} ")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime orderCreateTime);

    Integer countByMap(Map map);
///**
// * Retrieves the top 10 best-selling goods within a specified time range.
// * This method queries the order details and orders tables to calculate the total sales
// * for each product, considering only completed orders (status = 5).
// *
/*  select od.name sum(od.number) number from order_details od, orders o where od.order_id = o.id
    and o.status = 5 and o.order_ time > '2022-10-01' and o.order_time < '2022-10-31' group by od.name
    order by number desc limit 10;
    */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
