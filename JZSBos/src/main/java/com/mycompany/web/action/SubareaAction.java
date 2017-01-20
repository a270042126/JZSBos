package com.mycompany.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Controller;

import com.mycompany.domain.Decidedzone;
import com.mycompany.domain.Region;
import com.mycompany.domain.Subarea;
import com.mycompany.service.RegionService;
import com.mycompany.service.SubareaService;
import com.mycompany.utils.FileUtils;
import com.mycompany.web.action.base.BaseAction;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@Namespace("/subarea")
@Results({
	@Result(name = "list",location = "../base/subarea.jsp")
})
public class SubareaAction extends BaseAction<Subarea>{

	private static final long serialVersionUID = 3529269278616490581L;
	
	@Autowired
	private SubareaService subareaService;
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
			List<Subarea> list = new ArrayList<Subarea>();
			for(Row row: sheet){
				int rowNum = row.getRowNum();
				if(rowNum == 0){
					continue;
				}
				
				String id = row.getCell(0).getStringCellValue();
				if(!StringUtils.isNotBlank(id)){
					continue;
				}
				
				String regionId = row.getCell(1).getStringCellValue();
				Region region = regionService.findById(regionId);
				String addresskey = row.getCell(2).getStringCellValue();
				String startnum = row.getCell(3).getStringCellValue();
				String endnum = row.getCell(4).getStringCellValue();
				String single = row.getCell(5).getStringCellValue();
				String position = row.getCell(6).getStringCellValue();
				
				Subarea subarea = new Subarea(id,null,region,addresskey,startnum,endnum,single,position);
				list.add(subarea);
			}
			subareaService.saveOrUpdateBatch(list);
		} catch (Exception e) {
			flag = "0";
		}
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(flag);
		return NONE;
	}
	
	@Action(value="pageQuery",results={@Result(type="json",
			params={"root","pageBean","excludeProperties","detachedCriteria,rows.*\\.decidedzone,rows.*\\.subareas"})})
	public String pageQuery(){
		// 在查询之前，封装条件
		DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();
		String addresskey = model.getAddresskey();
		Region region = model.getRegion();
		Decidedzone decidedzone = model.getDecidedzone();
		
		if(StringUtils.isNotBlank(addresskey)){
			// 按照地址关键字模糊查询
			detachedCriteria.add(Restrictions.like("addresskey", "%" + addresskey + "%"));
		}
		
		if(region != null){
			// 创建别名，用于多表关联查询
			detachedCriteria.createAlias("region", "r");
			String province = region.getProvince();
			String city = region.getCity();
			String district = region.getDistrict();
			
			if(StringUtils.isNotBlank(province)){
				// 按照省进行模糊查询
				detachedCriteria.add(Restrictions.like("r.province", "%"
						+ province + "%"));
			}
			
			if(StringUtils.isNotBlank(city)){
				// 按照城市进行模糊查询
				detachedCriteria.add(Restrictions.like("r.city", "%" + city
						+ "%"));
			}
			
			if (StringUtils.isNotBlank(district)) {
				// 按照区域进行模糊查询
				detachedCriteria.add(Restrictions.like("r.district", "%"
						+ district + "%"));
			}
		}
		
		if(decidedzone != null){
			String decidezoneid = decidedzone.getId();
			detachedCriteria.createAlias("decidedzone", "d");
			detachedCriteria.add(Restrictions.eq("d.id", decidezoneid));
		}
		
		subareaService.pageQuery(pageBean);
		return SUCCESS;
	}
	
	/**
	 * 使用POI写入Excel文件，提供下载
	 * @throws IOException 
	 */
	@Action("exportXls")
	public String exportXls() throws IOException{
		List<Subarea> list = subareaService.findAll();
		// 在内存中创建一个Excel文件，通过输出流写到客户端提供下载
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建一个sheet页
		HSSFSheet sheet = workbook.createSheet("分区数据");
		// 创建标题行
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("分区编号");
		headRow.createCell(1).setCellValue("区域编号");
		headRow.createCell(2).setCellValue("地址关键字");
		headRow.createCell(3).setCellValue("起始号");
		headRow.createCell(4).setCellValue("结束号");
		headRow.createCell(5).setCellValue("单双号");
		headRow.createCell(6).setCellValue("位置信息");
		headRow.createCell(7).setCellValue("省市区");
		
		for(Subarea subarea: list){
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(subarea.getId());
			dataRow.createCell(1).setCellValue(subarea.getRegion().getId());
			dataRow.createCell(2).setCellValue(subarea.getAddresskey());
			dataRow.createCell(3).setCellValue(subarea.getStartnum());
			dataRow.createCell(4).setCellValue(subarea.getEndnum());
			dataRow.createCell(5).setCellValue(subarea.getSingle());
			dataRow.createCell(6).setCellValue(subarea.getPosition());
			Region region = subarea.getRegion();
			dataRow.createCell(7).setCellValue(region.getProvince()+region.getCity()+region.getDistrict());
		}
		
		String filename = "分区数据.xls";
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		String agent = request.getHeader("User-Agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		//一个流两个头
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
		ServletOutputStream outputStream = response.getOutputStream();
		ServletContext servletContext = (ServletContext) ActionContext.getContext().get(ServletActionContext.SERVLET_CONTEXT);
		String contentType = servletContext.getMimeType(filename);
		response.setContentType(contentType);
		response.setHeader("content-disposition", "attchment;filename="+filename);
		workbook.write(outputStream);
		return NONE;
	}

	@Action("add")
	public String add(){
		subareaService.save(model);
		return "list";
	}
	
	@Action("edit")
	public String edit(){
		
		Subarea subarea = subareaService.findById(model.getId());
		
		if(model.getRegion() != null && model.getRegion().getId() != null){
			subarea.setRegion(model.getRegion());
		}
		
		subarea.setAddresskey(model.getAddresskey());
		subarea.setStartnum(model.getStartnum());
		subarea.setEndnum(model.getEndnum());
		subarea.setSingle(model.getSingle());
		subarea.setPosition(model.getPosition());
		
		subareaService.update(subarea);
		
		return "list";
	}
	
	//接收ids参数
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	@Action("delete")
	public String delete(){
		subareaService.deleteByIds(ids);
		return "list";
	}
	
	List<Subarea> subareaList = null;
	public List<Subarea> getSubareaList() {
		return subareaList;
	}
	
	private Integer association = 0;
	public void setAssociation(Integer association) {
		this.association = association;
	}
	
	@Action(value="listajax",results={@Result(type="json",
			params={"root","subareaList","excludeProperties",".*\\.decidedzone,.*\\.region"})})
	public String listajax(){
		subareaList = subareaService.findListByAssociation(association);
		return SUCCESS;
	}
}
