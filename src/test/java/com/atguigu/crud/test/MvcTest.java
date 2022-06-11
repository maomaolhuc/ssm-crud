package com.atguigu.crud.test;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * 使用Spring测试模块提供的测试请求功能，测试curd请求的正确性
 * Spring4测试的时候，需要servlet3.0的支持
 *
 * @author lfy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "file:src/main/webapp/WEB-INF/dispatcherServlet-servlet.xml"})
public class MvcTest {
    // 传入Springmvc的ioc
    @Autowired
    WebApplicationContext context;
    // 虚拟mvc请求，获取到处理结果。
    MockMvc mockMvc;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testPage() throws Exception {
        //模拟请求拿到返回值
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "5"))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        response.setCharacterEncoding("UTF-8");
        String JsonReturn = response.getContentAsString();
        Msg msg = JSONObject.parseObject(JsonReturn, Msg.class);
        Map<String, Object> extend = msg.getExtend();
        JSONObject pageInfo = (JSONObject) extend.get("pageInfo");
        PageInfo pi = JSONObject.parseObject(String.valueOf(pageInfo), PageInfo.class);
        //请求成功以后，请求域中会有pageInfo；可以取出pageInfo进行验证
//        MockHttpServletRequest request = result.getRequest();
//        PageInfo pi = (PageInfo) request.getAttribute("pageInfo");
        System.out.println("当前页码：" + pi.getPageNum());
        System.out.println("总页码：" + pi.getPages());
        System.out.println("总记录数：" + pi.getTotal());
        System.out.println("在页面需要连续显示的页码");
        int[] nums = pi.getNavigatepageNums();
        for (int i : nums) {
            System.out.print(" " + i);
        }
        System.out.println();
        //获取员工数据
        List<JSONObject> list = pi.getList();
        for (JSONObject jsonObject : list) {
            Employee employee = JSONObject.parseObject(jsonObject.toJSONString(), Employee.class);
            System.out.println("ID：" + employee.getEmpId() + "==>Name:" + employee.getEmpName());
        }

    }

}
