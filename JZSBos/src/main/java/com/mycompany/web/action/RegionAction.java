package com.mycompany.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mycompany.domain.Region;
import com.mycompany.service.RegionService;
import com.mycompany.utils.PinYin4jUtils;
import com.mycompany.utils.UUIDUtils;
import com.mycompany.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/region")
@Results({
	@Result(name = "list",location = "../base/region.jsp")
})
public class RegionAction extends BaseAction<Region>{

	private static final long serialVersionUID = 6612158569570359561L;
	
	@Autowired
	private RegionService regionService;
	
	//接收上传的文件
	private File myFile;
	public void setMyFile(File myFile) {
		this.myFile = myFile;
	}
	
	/**
	* 批量导入
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Action("importXls")
	public String importXls() throws Exception{
		String flag = "1";
		//使用ＰＯＩ解析Ｅｘｃｅｌ文件
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(myFile));
			//获得第一个sheet页
			HSSFSheet sheet = workbook.getSheetAt(0);
			List<Region> list = new ArrayList<Region>();
			for(Row row: sheet){
				int rowNum = row.getRowNum();
				if(rowNum == 0){
					//第一行，标题行，忽略
					continue;
				}
				
				String id = row.getCell(0).getStringCellValue();
				String province = row.getCell(1).getStringCellValue();
				String city = row.getCell(2).getStringCellValue();
				String district = row.getCell(3).getStringCellValue();
				String postcode = row.getCell(4).getStringCellValue();
				Region region = new Region(id, province, city, district, postcode, null, null, null);
				
				city = city.substring(0,city.length() - 1);
				String[] stringToPingyin = PinYin4jUtils.stringToPinyin(city);
				//数组转字符串
				String citycode = StringUtils.join(stringToPingyin,"");
				
				//简码---->>HBSJZCA
				province = province.substring(0, province.length() - 1);
				district = district.substring(0, district.length() - 1);
				String info = province + city + district;////河北石家庄长安
				String[] headByString = PinYin4jUtils.getHeadByString(info);
				String shortcode = StringUtils.join(headByString,"");
				
				region.setCitycode(citycode);
				region.setShortcode(shortcode);
				list.add(region);
			}
			regionService.saveBatch(list);
		} catch (Exception e) {
			flag = "0";
		}
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(flag);
		return NONE;
	}
	
	@Action(value="pageQuery",results={@Result(type="json",
			params={"root","pageBean","excludeProperties","rows.*\\.subareas"})})
	public String pageQuery(){
		regionService.pageQuery(pageBean);
		return SUCCESS;
	}
	
	@Action("add")
	public String add(){
		model.setId(UUIDUtils.uuidString());
		regionService.save(model);
		return "list";
	}
	
	//接收ids参数
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Action("delete")
	public String delete(){
		regionService.deleteByIds(ids);
		return "list";
	}
	
	@Action("edit")
	public String edit(){
		regionService.update(model);
		return "list";
	}
	
	//模糊查询条件
	private String q;
	public void setQ(String q) {
		this.q = q;
	}
	List<Region> regionList = null;
	public List<Region> getRegionList() {
		return regionList;
	}
	
	@Action(value="listajax",results={@Result(type="json",
			params={"root","regionList","excludeProperties",".*\\.subareas"})})
	public String listajax(){
		if(StringUtils.isNotBlank(q)){
			regionList = regionService.findByQ(q);
		}else{
			regionList = regionService.findAll();
		}
		return SUCCESS;
	}
	
}
