package ${packPath}admin.controller#if($subPackPath != '').${subPackPath}#end;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.origin.eurybia.jdbc.plugin.Pager;
import com.origin.eurybia.jdbc.query.Query;
import com.origin.eurybia.jdbc.query.SortOperator;
import com.origin.eurybia.jdbc.query.WhereOperator;
import com.origin.eurybia.utils.ReflectionUtils;
import com.origin.eurybia.utils.StringUtils;

/**
 * ${tableNote}管理
 * @author ${author}
 * @date ${systemDate}
 */
@Controller
@Config(menuElId = "${lowerName}")
@RequestMapping(value = "${lowerName}")
public class ${className}Controller extends BaseController {

    @Autowired(required = false)
    private ${className}Service ${lowerName}Service;

    /**
     * ${tableNote}视图
     *
     * @return
     * @throws Exception
     */
    @Auth(verifyAuthority=true,authorityType=AuthEnum.AuthorityEnum.BROWSER)
    @RequestMapping(value = "/index")
    public ModelAndView indexView() throws Exception {

        Map<String, Object> map = new HashMap<>();

        return RenderView("${lowerName}/${lowerName}View", map);
    }

    /**
     * 获取单条记录
     * @param model
     * @return
     */
    @Auth(verifyAuthority=true,authorityType=AuthEnum.AuthorityEnum.BROWSER)
    @RequestMapping(value = "/data", produces = "text/html;charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public ResultMsg dataOne(@RequestBody ${className}Model model) {

        Query query = new Query();
        //查询条件
        WhereOperator whereOperator = getQueryTerm(${className}Entity.class, model);
        query.addWhereOperator(whereOperator);
        ${className}Entity ${lowerName}Entity = ${lowerName}Service.queryOne(query);
        return new ResultMsg(true, ${lowerName}Entity, "");
    }

    /**
     * 普通查询不分页
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Auth(verifyAuthority=true,authorityType=AuthEnum.AuthorityEnum.BROWSER)
    @RequestMapping(value = "/dataList", produces = "text/html;charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public List<${className}Entity> dataList(${className}Model model) throws Exception {

        Query query = new Query();
        //查询条件
        WhereOperator whereOperator = getQueryTerm(${className}Entity.class, model);
        query.addWhereOperator(whereOperator);
        //排序
        SortOperator sortOperator = getSortTerm(${className}Entity.class,model);
        query.addSortOperator(sortOperator);

        return ${lowerName}Service.queryList(query);
    }

    /**
     * 普通查询分页
     *
     * @param model
     * @return
     * @throws Exception
     */
    @Auth(verifyAuthority=true,authorityType=AuthEnum.AuthorityEnum.BROWSER)
    @RequestMapping(value = "pageList", produces = "text/html;charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> dataPageList(@RequestBody ${className}Model model) throws Exception {

        Pager<${className}Entity> pager = new Pager<>();
        pager.setPageId(model.getPage());
        pager.setPageSize(model.getRows());

        //查询条件
        WhereOperator whereOperator = getQueryTerm(${className}Entity.class, model);
        pager.addWhereOperator(whereOperator);
        //排序
        SortOperator sortOperator = getSortTerm(${className}Entity.class,model);
        pager.addSortOperator(sortOperator);

        ${lowerName}Service.queryPage(pager);

        //设置页面数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("total", pager.getRowCount());
        jsonMap.put("rows", pager.getResults());
        return jsonMap;
    }

    /**
     * 详情界面
     *
     * @return
     * @throws Exception
     */
    @Auth(verifyAuthority=true,authorityType=AuthEnum.AuthorityEnum.BROWSER)
    @RequestMapping(value = "/detail")
    public ModelAndView detailView(@RequestParam Integer id) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return RenderView("${lowerName}/${lowerName}Detail", map);
    }


    /**
     * 增加记录
     *
     * @return
     * @throws Exception
     */
    @Auth(verifyAuthority = true, authorityType=AuthEnum.AuthorityEnum.ADD)
    @RequestMapping("/add")
    public ModelAndView add() throws Exception {

        Map<String, Object> map = new HashMap<>();

        return RenderView("${lowerName}/${lowerName}Add", map);
    }

    /**
     * 修改记录
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Auth(verifyAuthority = true, authorityType = AuthEnum.AuthorityEnum.EDIT)
    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam Integer id) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);

        return RenderView("${lowerName}/${lowerName}Edit", map);
    }

    /**
     * 新增、编辑保存
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @Auth(verifyAuthority = true, authorityType = AuthEnum.AuthorityEnum.SAVE)
    @RequestMapping(value = "save", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public ResultMsg save(@RequestBody ${className}Entity entity) throws Exception {

        if (entity.get${tableKey}() == null) {
            ${lowerName}Service.save(entity);
        } else {
            ${lowerName}Service.updateSelective(entity);
        }
        return new ResultMsg(true, "数据保存成功");

    }

    /**
     * 删除记录
     *
     * @param ids
     * @return
     */
    @Auth(verifyAuthority = true, authorityType = AuthEnum.AuthorityEnum.DEL)
    @RequestMapping(value = "remove", produces = "text/html;charset=UTF-8", method = {RequestMethod.POST})
    @ResponseBody
    public ResultMsg remove(@RequestBody Integer[] ids) {

        ${lowerName}Service.deleteBatch(ids);
        return new ResultMsg(true);
    }
}
