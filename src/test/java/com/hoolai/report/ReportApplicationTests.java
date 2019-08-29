package com.hoolai.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Sets;
import com.hoolai.report.entiy.Retention;
import com.hoolai.report.mapper.RetentionMappper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportApplicationTests {

	@Autowired
	private RetentionMappper retentionMappper;

	@Test
	public void contextLoads() {}

	public List<Retention> getRetentions(){
		List<Retention> retentions = retentionMappper.getAll(Wrappers.<Retention>lambdaQuery().eq(Retention::getDs,"2019-08-28"));
		return retentions;
	}

	public Workbook getWorkbook() throws Exception {
		InputStream is = null;
		Workbook wb = null;
		try {
			is = new FileInputStream("C:\\Users\\Administrator\\Desktop\\test.xlsx");
			wb = WorkbookFactory.create(is);
		}finally {
			if (is != null) {
				is.close();
			}
		}
		return wb;
	}

	@Test
	public void appendExcel() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Workbook workbook = this.getWorkbook();
		Sheet sheet = workbook.getSheetAt(0);
		int rowTotal = sheet.getLastRowNum();
		List<Retention> datas = this.getRetentions();
		Iterator<Retention> it = datas.iterator();

		Row row;
		Cell dateCell;
		String date;
		Cell blankCell;
		Set<Integer> recode = Sets.newHashSet(datas.size());

		while (it.hasNext()){
			boolean flag = false;
			Retention retention = it.next();

			for(int rowNo=1;rowNo <= rowTotal;rowNo++){
				row = sheet.getRow(rowNo);
				dateCell = row.getCell(row.getFirstCellNum());
				date = simpleDateFormat.format(dateCell.getDateCellValue());

				if (retention.getDs().equals(date)){
					blankCell = row.getCell(retention.getDr());
					blankCell.setCellValue(retention.getRetention());
					flag = true;
					recode.add(rowNo);
					break;
				}
			}

//			for (int rowNo=1;rowNo <= rowTotal;rowNo++){
//				if (!recode.contains(rowNo)){
//					row = sheet.getRow(rowNo);
//					Cell cell = row.createCell(row.getLastCellNum()+1);
//					cell.setCellValue(0);
//				}
//			}

			//未找到该日期
			if (!flag){
				System.out.println("未找到该日期");
				Row newRow = sheet.createRow((sheet.getLastRowNum()+1));
				newRow.createCell(0).setCellValue(retention.getDs());
				newRow.createCell(1).setCellValue(retention.getRetention());
			}
		}

		try {
			OutputStream outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\test2.xlsx");
			workbook.write(outputStream);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
	}

}
