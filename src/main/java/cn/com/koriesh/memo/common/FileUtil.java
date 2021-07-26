package cn.com.koriesh.memo.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class FileUtil {

    public static final String FILE_READ_ERROR = "读取文件异常";

    public static final String RES_CONTENT = "content";
    public static final String RES_TITLE = "title";

    // **************************************读取端口分布excel

    /**
    * @Author: wangh
    * @Description: 通过 文件流直接读取文件内容 - Excel
    * @Date: 2020/12/8
    * @Param: [fileName, is]
    * @Return: java.util.Map<java.lang.String,java.lang.Object>
    */
    public static Map<String, Object> readExcel(String fileName, InputStream is) throws IOException {
        Map<String, Object> resMap = new HashMap<>();
        List<List<String>> contentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        try{
            if (fileName.toLowerCase().contains("xlsx")) {
                XSSFWorkbook excel = new XSSFWorkbook(is);
                XSSFSheet sheet = excel.getSheetAt(0);
                //取最大列下标(某些列是空，正常读取时会默认跳过，故获取最大的列数，强制读取每一列)
                int maxCellNum = setCellType(sheet);
                //遍历每行,得到指定列
                getCellRows(sheet, maxCellNum, contentList, titleList);
            } else if (fileName.toLowerCase().contains("xls")) {
                HSSFWorkbook excel = new HSSFWorkbook(is);
                HSSFSheet sheet = excel.getSheetAt(0);
                //取最大列下标(某些列是空，正常读取时会默认跳过，故获取最大的列数，强制读取每一列)
                int maxCellNum = setCellType(sheet);
                //遍历每行,得到指定列
                getCellRows(sheet, maxCellNum, contentList, titleList);
            }
            is.close();
        } catch (IOException e){
            log.error(FILE_READ_ERROR);
            throw new IOException(FILE_READ_ERROR);
        }
        resMap.put(RES_TITLE, titleList);
        resMap.put(RES_CONTENT, contentList);
        return resMap;
    }


    /**
     * @Author: wangh
     * @Description: 判断是否是excel
     * @Date: 2020/11/28
     * @Param: [fileName]
     * @Return: boolean
     */
    public static boolean isExcel(String fileName) {
        boolean res = false;
        //文件名非空 并且 文件名和类型的分隔符 . 不能是文件名的最后一位
        if (StringUtils.isNotEmpty(fileName)
                && fileName.lastIndexOf('.') > 0
                && fileName.lastIndexOf('.') < fileName.length() - 1) {

            String nameEnd = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
            String lowNameEnd = nameEnd.toLowerCase();
            if (lowNameEnd.equals("xls") || lowNameEnd.equals("xlsx")) { //只要文件后缀的小写包括 xls   就可以  （xls、xlsx）
                res = true;
            }
        }
        return res;
    }

    /**
     * @Author: wangh
     * @Description:
     * @Date: 2020/12/10
     * @Param: [sheet]
     * @Return: int
     */
    public static int setCellType(XSSFSheet sheet) {
        int maxCellNum = 0;
        for (Row row : sheet) {
            int lastCellNum = row.getLastCellNum();
            if (lastCellNum > maxCellNum) {
                maxCellNum = lastCellNum;
            }
//            for (Cell cell : row) {
//                //不推荐使用的方法,但是取出列有数字的话,要转换一下
//                cell.setCellType(CellType.STRING); //来源cell.CELL_TYPE_STRING，为了通过代码检查修改为静态常量
//            }
        }
        return maxCellNum;
    }

    /**
     * @Author: wangh
     * @Description:
     * @Date: 2020/12/10
     * @Param: [sheet]
     * @Return: int
     */
    public static int setCellType(HSSFSheet sheet) {
        int maxCellNum = 0;
        for (Row row : sheet) {
            int lastCellNum = row.getLastCellNum();
            if (lastCellNum > maxCellNum) {
                maxCellNum = lastCellNum;
            }
//            for (Cell cell : row) {
//                //不推荐使用的方法,但是取出列有数字的话,要转换一下
//                cell.setCellType(CELL_TYPE_STRING); //来源cell.CELL_TYPE_STRING，为了通过代码检查修改为静态常量
//            }
        }
        return maxCellNum;
    }

    /**
     * @Author: wangh
     * @Description:
     * @Date: 2020/12/10
     * @Param: [sheet, maxCellNum, contentList, titleList]
     * @Return: void
     */
    public static void getCellRows(XSSFSheet sheet, int maxCellNum, List<List<String>> contentList, List<String> titleList) {
        for (Row row : sheet) {
            List<String> rows = new ArrayList<>();
            int rowIndex = row.getRowNum();
            // 获取表格内容
            realGetCellRows(maxCellNum, row, rows, titleList, rowIndex);
            // 检查内容判断空
            checkRowsValue(rows, contentList);
        }
    }

    /**
     * @Author: wangh
     * @Description:
     * @Date: 2020/12/10
     * @Param: [sheet, maxCellNum, contentList, titleList]
     * @Return: void
     */
    public static void getCellRows(HSSFSheet sheet, int maxCellNum, List<List<String>> contentList, List<String> titleList) {
        for (Row row : sheet) {
            List<String> rows = new ArrayList<>();
            int rowIndex = row.getRowNum();
            // 获取表格内容
            realGetCellRows(maxCellNum, row, rows, titleList, rowIndex);
            // 检查内容判断空
            checkRowsValue(rows, contentList);
        }
    }

    private static void realGetCellRows(int maxCellNum, Row row, List<String> rows, List<String> titleList, int rowIndex) {
        for (int i = 0; i < maxCellNum; i++) {
            Cell cell = row.getCell(i);
            String celval = "";
            if (cell != null && !cell.toString().isEmpty()) {
                String typeName = cell.getCellType().name();
                if ("STRING".equals(typeName)) {
                    //列的值
                    celval += cell.getStringCellValue();
                } else if ("NUMERIC".equals(typeName)) {
                    celval += (int) cell.getNumericCellValue() + "";
                }
            }

            if (rowIndex == 0) {  //第一行   往往是列名
                titleList.add(celval);
            } else {
                rows.add(celval);
            }
        }
    }

    private static void checkRowsValue(List<String> rows, List<List<String>> contentList) {
        if (!rows.isEmpty()) {
            // 遍历这一行的每一列
            for (String cloumn : rows) {
                // 只要有一列的值不为空  那么这行就有意义   此时把这一行记录下来，退出循环
                if (StringUtils.isNotEmpty(cloumn)) {
                    contentList.add(rows);
                    break;
                }
            }
        }
    }
}

