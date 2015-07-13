package core.TestVee2.utils;

import core.TestVee2.Page;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExcelUtils extends Page

{
    private static Workbook workbook = null;
    private static final String   resPass  = "Pass";
    private static final String   resFail  = "Fail";

    /**
     * Read Excel Content, by rows, and return an array having as elements
     * arrays with values from each column
     * @param inputFilePath
     * @param sheetName
     * @return
     */
    public static ArrayList<ArrayList<Cell>> getDataFromExcelSheet(String inputFilePath, String sheetName)
    {

        try
        {
            ArrayList<ArrayList<Cell>> sheetData;
            try (FileInputStream inputFile = new FileInputStream(inputFilePath)) {
                workbook = WorkbookFactory.create(inputFile);
                Sheet sheet = workbook.getSheet(sheetName);
                /* Object for values from entire sheet */
                sheetData = new ArrayList<>();
                /* Iterate through cells */
                Iterator<Row> rows = sheet.rowIterator();
                while (rows.hasNext())
                {
                    
                    Row row = rows.next();
                    
                    Iterator<Cell> cells = row.cellIterator();
                    
                    /* Object for values from a row */
                    ArrayList<Cell> rowData = new ArrayList<>();
                    
                    while (cells.hasNext())
                    {
                        
                        Cell cell = cells.next();
                        
                        rowData.add(cell);
                        
                    }
                    
                    sheetData.add(rowData);
                    
                }
            }
            return sheetData;
        }
        catch (IOException | InvalidFormatException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Returns a specific cell content
     * @param inputFilePath
     * @param sheetName
     * @param rowColumnFilter - identifies filter cell by row and column
     * separated by "|" (e.g.: testcase1row|testnamecolumn)
     * @param columnFilter
     * @return
     */
    public static Cell getCellDataFromExcelSheet(String inputFilePath, String sheetName, String rowColumnFilter, String columnFilter)
    {
        FileInputStream inputFile = null;
        try
        {
            try
            {
                inputFile = new FileInputStream(inputFilePath);
                workbook = WorkbookFactory.create(inputFile);
                Sheet sheet = workbook.getSheet(sheetName);

                int columnFIdx = getColumnIndex(sheet, columnFilter);
                int rowIdx = getRowIndex(sheet, rowColumnFilter);

                return sheet.getRow(rowIdx).getCell(columnFIdx);
            }
            finally
            {
                inputFile.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Returns an array of cells representing a specific column
     * @param inputFilePath
     * @param sheetName
     * @param columnFilter
     * @return
     */
    public static ArrayList<Cell> getColumnDataFromExcelSheet(String inputFilePath, String sheetName, String columnFilter)
    {
        FileInputStream inputFile = null;
        try
        {
            try
            {
                inputFile = new FileInputStream(inputFilePath);

                workbook = WorkbookFactory.create(inputFile);

                Sheet sheet = workbook.getSheet(sheetName);

                /* Object for values from a column */
                ArrayList<Cell> columnData = new ArrayList<Cell>();

                int columnFIdx = getColumnIndex(sheet, columnFilter);

                Iterator<Row> rows = sheet.rowIterator();

                while (rows.hasNext())
                {
                    Row row = rows.next();

                    Cell cell = row.getCell(columnFIdx);

                    columnData.add(cell);

                }
                return columnData;
            }
            finally
            {
                inputFile.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Returns an array of cells representing the row filtered by a specific
     * cell value
     * @param inputFilePath - path to excel
     * @param sheetName - excel sheet name
     * @param rowFilter - row filter cell value
     * @param columnFilter - column name for filter cell identification
     * @return - array of cells
     */
    public static ArrayList<Cell> getRowDataFromExcelSheet(String inputFilePath, String sheetName, String rowcolumnFilter)
    {
        try
        {
            FileInputStream inputFile = null;
            try
            {
                inputFile = new FileInputStream(inputFilePath);

                workbook = WorkbookFactory.create(inputFile);

                Sheet sheet = workbook.getSheet(sheetName);

                /* Object for values from a row */
                ArrayList<Cell> rowData = new ArrayList<Cell>();

                Row row = sheet.getRow(getRowIndex(sheet, rowcolumnFilter));

                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext())
                {
                    Cell cell = cells.next();

                    rowData.add(cell);
                }
                return rowData;
            }
            finally
            {
                inputFile.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns row index based on sheet name and row|column filter
     * @param sheet
     * @param rowColumnFilter
     * @return - If no match found, returns -1
     */
    private static int getRowIndex(Sheet sheet, String rowColumnFilter)
    {
        // rowColumnFilter break down
        String[] rowFilter = rowColumnFilter.split("[|]");
        int rowColumnFIdx = getColumnIndex(sheet, rowFilter[1]);

        Iterator<Row> rows = sheet.rowIterator();

        while (rows.hasNext())
        {
            Row row = rows.next();

            Cell cellF = row.getCell(rowColumnFIdx);

            if (!(cellF == null) && (cellF.toString().equals(rowFilter[0])))
            {
                return row.getRowNum();
            }
        }

        return -1;
    }

    /**
     * Returns row index based on excel file path, sheet name and row|column
     * filter
     * @param inputFilePath
     * @param sheetName
     * @param rowColumnFilter
     * @return - If no match found, returns -1
     */
    private static int getRowIndex(String inputFilePath, String sheetName, String rowColumnFilter)
    {

        FileInputStream inputFile = null;
        try
        {
            try
            {
                inputFile = new FileInputStream(inputFilePath);

                workbook = WorkbookFactory.create(inputFile);

                Sheet sheet = workbook.getSheet(sheetName);

                return getRowIndex(sheet, rowColumnFilter);
            }
            finally
            {
                inputFile.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Returns column index based on provided sheet name and cell value.
     * @param sheet - sheet object
     * @param columnFilter - column name
     * @return - If no match found, returns -1
     */
    private static int getColumnIndex(Sheet sheet, String columnFilter)
    {
        Iterator<Row> rows = sheet.rowIterator();

        while (rows.hasNext())
        {
            Row row = rows.next();

            Iterator<Cell> cells = row.cellIterator();

            while (cells.hasNext())
            {
                Cell cell = cells.next();

                if (!(cell == null) && (cell.toString().equals(columnFilter)))
                {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }

    /**
     * Returns cell value or the cashed value if cell contains formula
     * @param cell
     * @return empty string if cell is null
     */
    public static String getCellValue(Cell cell)
    {
        if (cell == null)
        {
            return "";
        }
        String cellValue = "";
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
        {
            switch (cell.getCachedFormulaResultType())
            {
                case Cell.CELL_TYPE_NUMERIC:
                    cellValue = NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    cellValue = cell.getRichStringCellValue().toString();
                    break;
            }
        }
        else
            cellValue = cell.toString();
        return cellValue;
    }

    /**
     * Prints data from a sheet structure (list of lists of cells) to console
     * @param dataHolder
     */
    public static void printCellGroupDataToConsole(ArrayList<ArrayList<Cell>> dataHolder)
    {
        for (ArrayList<Cell> cellList : dataHolder)
        {
            System.out.print("|");
            for (Cell cell : cellList)
            {

                String stringCellValue = getCellValue(cell);

                System.out.print(stringCellValue + "|");
            }
            System.out.println();
        }

    }

    /**
     * Prints data from a list of cells to console
     * @param dataHolder
     */
    public static void printCellDataToConsole(ArrayList<Cell> dataHolder)
    {
        for (Cell cell : dataHolder)
        {
            System.out.print("|");
            String stringCellValue = getCellValue(cell);
            System.out.print(stringCellValue + "|");
        }
        System.out.println();

    }

    /**
     * Writes data to an Excel file. NOTE: the file is written from scratch
     * @param outputFilePath
     * @param sheetName
     * @param dataHolder
     * @return Returns true if file is created successfully else false
     */
    public boolean writeToExcelFile(String outputFilePath, String sheetName, ArrayList<String[]> dataHolder)
    {
        try
        {
            FileOutputStream outputFile = new FileOutputStream(outputFilePath);
            Workbook workbook = new HSSFWorkbook();

            Map<String, CellStyle> styles = createStyles(workbook);

            Sheet sheet = workbook.createSheet(sheetName);
            sheet.setDefaultRowHeight((short) 300);

            for (int indexRows = 0; indexRows < dataHolder.size(); indexRows++)
            {
                Row row = sheet.createRow(indexRows);

                for (int indexCell = 0; indexCell < dataHolder.get(0).length; indexCell++)
                {
                    Cell cell = row.createCell(indexCell);

                    cell.setCellValue(dataHolder.get(indexRows)[indexCell]);

                    // Format sheet/cells
                    sheet.autoSizeColumn(indexCell);

                    // Format header row
                    if (indexRows == 0)
                    {
                        cell.setCellStyle(styles.get("header"));
                    }
                    else
                    {
                        cell.setCellStyle(styles.get("cell_h"));
                        // Particular cases - to be removed
                        // ------
                        if (dataHolder.get(indexRows)[indexCell].equals(resFail))
                        {
                            cell.setCellStyle(styles.get("text_red"));
                        }
                        if (indexCell == 1 || indexCell == 2)
                        {
                            cell.setCellStyle(styles.get("cell_left"));
                        }
                        // ------
                    }
                }

            }

            workbook.write(outputFile);
            outputFile.close();
            return true;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Writes specific cell into an existing excel file (NOTE: file is
     * overridden if base and output path are the same)
     * @param baseFilePath
     * @param outputFilePath
     * @param sheetName
     * @param rowColumnFilter
     * @param columnFilter
     * @param cellValue
     * @return
     */
    public static boolean writeStringCellToExcelFile(String baseFilePath, String outputFilePath, String sheetName, String rowColumnFilter, String columnFilter, String cellValue)
    {
        try
        {
            Sheet sheet = getExcelSheet(baseFilePath, sheetName);

            return writeStringCellToExcelFile(baseFilePath, outputFilePath, sheetName, getRowIndex(sheet, rowColumnFilter), columnFilter, cellValue);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes specific cell into an existing excel file (NOTE: file is
     * overridden if base and output path are the same)
     * @param baseFilePath
     * @param outputFilePath
     * @param sheetName
     * @param rowFilter
     * @param columnFilter
     * @param cellValue
     * @return
     */
    public static boolean writeStringCellToExcelFile(String baseFilePath, String outputFilePath, String sheetName, int rowFilter, String columnFilter, String cellValue)
    {
        try
        {
            Sheet sheet = getExcelSheet(baseFilePath, sheetName);

            int colIdx = getColumnIndex(sheet, columnFilter);

            Row row = sheet.getRow(rowFilter);

            Cell cell = row.getCell(colIdx);
            if (cell == null)
                cell = row.createCell(colIdx);

            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(cellValue);

            saveExcel(outputFilePath);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a column of cells into an existing excel file (NOTE: file is
     * overridden if base and output path are the same)
     * @param baseFilePath
     * @param outputFilePath
     * @param sheetName
     * @param rowFilter
     * @param columnFilter
     * @param cellValues
     * @return
     */
    public static boolean writeColumnToExcelFile(String baseFilePath, String outputFilePath, String sheetName, int rowFilter, String columnFilter, ArrayList<String> cellValues)
    {
        try
        {

            Sheet sheet = getExcelSheet(baseFilePath, sheetName);

            int colIdx = getColumnIndex(sheet, columnFilter);

            for (int i = 0; i < cellValues.size(); i++)
            {
                Row row = sheet.getRow(rowFilter + i);

                Cell cell = row.getCell(colIdx);
                if (cell == null)
                    cell = row.createCell(colIdx);

                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(cellValues.get(i));
            }

            saveExcel(outputFilePath);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a column to a specified sheet based on column name
     * @param sheet
     * @param rowFilter
     * @param columnFilter
     * @param cellValues - ArrayList<String>
     * @return
     */
    public static boolean writeColumnToExcelFile(Sheet sheet, int rowFilter, String columnFilter, ArrayList<String> cellValues)
    {
        try
        {

            if (cellValues.size() > (sheet.getLastRowNum() - (rowFilter - 1)))
            {
                throw new IllegalArgumentException("Provided number of result rows exceeds file existent rows");
            }

            int colIdx = getColumnIndex(sheet, columnFilter);

            for (int i = 0; i < cellValues.size(); i++)
            {
                Row row = sheet.getRow(rowFilter + i);

                Cell cell = row.getCell(colIdx);
                if (cell == null)
                    cell = row.createCell(colIdx);

                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(cellValues.get(i));
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes a column to a specified sheet based on column index
     * @param sheet
     * @param rowFilter
     * @param colIdx
     * @param cellValues - ArrayList<String>
     * @return
     */
    public static boolean writeColumnToExcelFile(Sheet sheet, int rowFilter, int colIdx, ArrayList<String> cellValues)
    {
        try
        {

            if (cellValues.size() > (sheet.getLastRowNum() - (rowFilter - 1)))
            {
                throw new IllegalArgumentException("Provided number of result rows exceeds file existent rows");
            }

            for (int i = 0; i < cellValues.size(); i++)
            {
                Row row = sheet.getRow(rowFilter + i);

                Cell cell = row.getCell(colIdx);
                if (cell == null)
                    cell = row.createCell(colIdx);

                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(cellValues.get(i));
            }

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Appends a set of columns into an existing excel file (NOTE: file is
     * overridden if base and output path are the same)
     * @param baseFilePath
     * @param outputFilePath
     * @param sheetName
     * @param rowFilter - 0 based
     * @param columnNames - "|" separated column names (e.g. col1|col2|col3)
     * @param cellValues - ArrayList<String[]>
     * @return
     */
    public static boolean writeColumnsToExcelFile(String baseFilePath, String outputFilePath, String sheetName, int rowFilter, String columnNames, ArrayList<String[]> cellValues)
    {
        try
        {
            int tmpcolIdx = -1;
            ArrayList<String> tmpValues = new ArrayList<String>();

            Sheet sheet = getExcelSheet(baseFilePath, sheetName);

            String[] colNames = columnNames.split("[|]");

            if (colNames.length != cellValues.get(0).length)
            {
                throw new IllegalArgumentException("Provided column names must match the number of column values");
            }

            for (int i = 0; i < colNames.length; i++)
            {
                tmpValues.clear();
                for (int j = 0; j < cellValues.size(); j++)
                {
                    tmpValues.add(cellValues.get(j)[i]);
                }

                tmpcolIdx = apendColumn(sheet, rowFilter, colNames[i]);
                writeColumnToExcelFile(sheet, rowFilter + 1, tmpcolIdx, tmpValues);
            }

            saveExcel(outputFilePath);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates result cells style according to result values (Green for Pass,
     * Red for Fail)(NOTE: file is overridden if base and output path are the
     * same)
     * @param baseFilePath
     * @param outputFilePath
     * @param sheetName
     * @param rowFilter
     * @param columnFilter
     * @param cellValues
     * @return
     */
    public static boolean updateResultCellsStyle(String baseFilePath, String outputFilePath, String sheetName, int rowFilter, String columnFilter, ArrayList<String> cellValues)
    {
        try
        {

            Sheet sheet = getExcelSheet(baseFilePath, sheetName);

            Map<String, CellStyle> styles = createStyles(workbook);

            int colIdx = getColumnIndex(sheet, columnFilter);

            for (int i = 0; i < cellValues.size(); i++)
            {
                Row row = sheet.getRow(rowFilter + i);

                Cell cell = row.getCell(colIdx);

                if (cell.getStringCellValue().equals(resPass))
                {
                    cell.setCellStyle(styles.get("text_green"));
                }
                else if (cell.getStringCellValue().equals(resFail))
                {
                    cell.setCellStyle(styles.get("text_red"));
                }

            }

            saveExcel(outputFilePath);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Appends a column at the end of data (NOTE: file is overridden if base and
     * output path are the same)
     * @param baseFilePath
     * @param outputFilePath
     * @param sheetName
     * @param rowFilter
     * @param colName
     * @return
     */
    public static boolean appendColumn(String baseFilePath, String outputFilePath, String sheetName, int rowFilter, String colName)
    {
        try
        {
            Sheet sheet = getExcelSheet(baseFilePath, sheetName);

            if (apendColumn(sheet, rowFilter, colName) > -1)
            {
                saveExcel(outputFilePath);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Appends a column at the end of data (NOTE: file is overridden if base and
     * output path are the same)
     * @param sheet
     * @param rowFilter
     * @param colName
     * @return
     */
    public static int apendColumn(Sheet sheet, int rowFilter, String colName)
    {
        int colIdx = -1;
        try
        {
            Row row = sheet.getRow(rowFilter);
            colIdx = row.getLastCellNum();

            Cell cell = row.getCell(colIdx);
            if (cell != null)
            {
                throw new IllegalArgumentException("Column at index " + Integer.toString(colIdx) + " allready exists and has value = [" + cell.toString() + "]");
            }

            cell = row.createCell(colIdx);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(colName);

            return colIdx;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Compares test results based on expected and actual columns, writes result
     * (Pass/Fail) into specific column, updating also cell style
     * @param inputFilePath
     * @param sheetName
     * @param expColumn
     * @param actColumn
     * @param resultColumn
     * @return
     */
    public static boolean compareResults(String inputFilePath, String sheetName, String expColumn, String actColumn, String resultColumn)
    {
        try
        {

            ArrayList<Cell> expCells = getColumnDataFromExcelSheet(inputFilePath, sheetName, expColumn);
            ArrayList<Cell> actCells = getColumnDataFromExcelSheet(inputFilePath, sheetName, actColumn);
            ArrayList<String> resultCells = new ArrayList<String>();

            // get list start index
            int listStartIdx = 0;

            for (int i = 0; i < expCells.size(); i++)
            {
                if (getCellValue(expCells.get(i)).equals(expColumn))
                {
                    listStartIdx = i + 1;
                    break;
                }
            }

            // compare expCells and actCells
            for (int i = listStartIdx; i < expCells.size(); i++)
            {

                String expCellValue = getCellValue(expCells.get(i));
                String actCellValue = getCellValue(actCells.get(i));

                if (expCellValue.equals(actCellValue))
                {
                    resultCells.add(resPass);
                }
                else
                {
                    resultCells.add(resFail);
                }

            }

            // get row start index
            int rowStartIdx = getRowIndex(inputFilePath, sheetName, expColumn + "|" + expColumn);

            writeColumnToExcelFile(inputFilePath, inputFilePath, sheetName, rowStartIdx + 1, resultColumn, resultCells);
            updateResultCellsStyle(inputFilePath, inputFilePath, sheetName, rowStartIdx + 1, resultColumn, resultCells);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Creates border style
     * @param wb
     * @return
     */
    private static CellStyle createBorderedStyle(Workbook wb)
    {
        CellStyle borderStyle = wb.createCellStyle();
        borderStyle.setBorderRight(CellStyle.BORDER_DOUBLE);
        borderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderBottom(CellStyle.BORDER_DOUBLE);
        borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderLeft(CellStyle.BORDER_DOUBLE);
        borderStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderTop(CellStyle.BORDER_DOUBLE);
        borderStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return borderStyle;
    }

    /**
     * Creates a line style (top&bottom single lines, left&right dashed lines)
     * creates more `continuous` lines
     * @author ccotoi
     * @param wb workbook
     * @return cell style
     */
    private static CellStyle createLineStyle(Workbook wb)
    {
        CellStyle borderStyle = wb.createCellStyle();
        borderStyle.setBorderRight(CellStyle.BORDER_HAIR);
        borderStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderBottom(CellStyle.BORDER_HAIR);
        borderStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        borderStyle.setBorderLeft(CellStyle.BORDER_DASHED);
        borderStyle.setLeftBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        borderStyle.setBorderTop(CellStyle.BORDER_DASHED);
        borderStyle.setTopBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());
        return borderStyle;
    }

    /**
     * Create a library of cell styles
     * @param wb
     * @return
     */
    private static Map<String, CellStyle> createStyles(Workbook wb)
    {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        DataFormat df = wb.createDataFormat();

        CellStyle style;

        Font headerFont = wb.createFont();
        headerFont.setFontName("Calibri");
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("header_date", style);

        Font font1 = wb.createFont();
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font1);
        styles.put("cell_b", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font1);
        styles.put("cell_b_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_b_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_g", style);

        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_bg", style);

        Font lineFont = wb.createFont();
        lineFont.setFontName("Calibri");
        lineFont.setFontHeightInPoints((short) 12);
        lineFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(lineFont);
        style.setWrapText(true);
        styles.put("cell_h", style);

        style = createBorderedStyle(wb);
        style.setFont(lineFont);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        styles.put("cell_left", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        styles.put("cell_normal_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_normal_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setIndention((short) 1);
        style.setWrapText(true);
        styles.put("cell_indented", style);

        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cell_blue", style);

        style = createBorderedStyle(wb);
        style.setFont(lineFont);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cell_red", style);

        style = createBorderedStyle(wb);
        Font redFont = wb.createFont();
        redFont.setFontName("Calibri");
        redFont.setFontHeightInPoints((short) 12);
        redFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        redFont.setColor(IndexedColors.CORAL.getIndex());
        style.setFont(redFont);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("text_red", style);

        style = createBorderedStyle(wb);
        Font greenFont = wb.createFont();
        greenFont.setFontName("Calibri");
        greenFont.setFontHeightInPoints((short) 12);
        greenFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        greenFont.setColor(IndexedColors.GREEN.getIndex());
        style.setFont(greenFont);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("text_green", style);

        style = createLineStyle(wb);
        styles.put("line_simple", style);

        return styles;
    }

    /**
     * Loads data from the provided excel, returning a sheet based on name
     * @param baseFilePath
     * @param sheetName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws InvalidFormatException
     */
    private static Sheet getExcelSheet(String baseFilePath, String sheetName) throws FileNotFoundException, IOException, InvalidFormatException
    {
        InputStream fileIn = new FileInputStream(baseFilePath);
        workbook = WorkbookFactory.create(fileIn);
        Sheet sheet = workbook.getSheet(sheetName);
        return sheet;
    }

    /**
     * Writes excel data to disk, closing the file
     * @param outputFilePath
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void saveExcel(String outputFilePath) throws FileNotFoundException, IOException
    {
        FileOutputStream fileOut = new FileOutputStream(outputFilePath);
        workbook.write(fileOut);
        fileOut.close();
    }

}
